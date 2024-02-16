package com.pux12.ultimatedictionaryapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pux12.ultimatedictionaryapi.model.entity.Etymology;

@Repository
public interface EtymologyRepository extends JpaRepository<Etymology, Long> {

  public static String AGGREGATE_JSON_ENTRIES = """
    SELECT json_agg(json_strip_nulls(json_build_object(
      'id', e.id,
      'etymology', e.etymology,
      'lang_code', e.lang_code,
      'pos', e.pos,
      'source_wiktionary_code', e.source_wiktionary_code,
      'word', e.word,
      'forms', (
        SELECT json_agg(json_strip_nulls(json_build_object(
          'id', f.id,
          'form', f.form,
          'tags', ft.tags
        )))
        FROM form f
        LEFT JOIN form_tags ft ON f.id = ft.form_id
        WHERE f.etymology_id = e.id
      ),
      'senses', (
        SELECT json_agg(json_strip_nulls(json_build_object(
          'id', s.id,
          'examples', se.examples,
          'glosses', sg.glosses
        )))
        FROM sense s
        LEFT JOIN sense_examples se ON s.id = se.sense_id
        LEFT JOIN sense_glosses sg ON s.id = sg.sense_id
        WHERE s.etymology_id = e.id
      ),
      'translations', (
        SELECT json_agg(json_strip_nulls(json_build_object(
          'id', t.id,
          'code', t.lang_code,
          'lang', t.lang,
          'sense', t.sense,
          'word', t.word
        )))
        FROM "translation" t
        WHERE t.etymology_id = e.id
      )
    )))
    FROM etymology e """;


  // JPA generated queries are simply too slow, so we use native queries instead
  //@Query(value = AGGREGATE_JSON_ENTRIES + "where e.word = :word collate no_case_no_diac", nativeQuery = true)
  //String findByWrd(@Param("word") String word);

  @Query(value = """
      select
        json_agg(distinct t2.word)
      from
        "translation" t1
      join "translation" t2 on
        t2.etymology_id = t1.etymology_id
        and t1.sense is not distinct from t2.sense
      left join translation_sense_ids tsi1 on
        tsi1.translation_id = t1.id
      left join translation_sense_ids tsi2 on
        tsi2.translation_id = t2.id
      where
        t2.lang_code = :targetLangCode
        and t1.lang_code = :sourceLangCode
        and t1.word = :word
        and tsi1.sense_ids is not distinct from tsi2.sense_ids
            """, nativeQuery = true)
  String findContextLessTranslation(@Param("sourceLangCode") String sourceLangCode,
      @Param("targetLangCode") String targetLangCode, @Param("word") String word);

  @Query(value = """
      select json_agg(json_build_object(
        'word', word,
        'pos', pos,
        'ipas', ipa,
        'translation', translation
      )) as result from (
        select e.word, e.pos, array_agg(distinct s.ipa) as ipa, array_agg(distinct t.word) as translation from etymology e
        join "translation" t on e.id = t.etymology_id
        left join sound s on s.etymology_id = e.id
        where e.word = :word collate no_case_no_diac
        and e.lang_code = :sourceLangCode
        and t.lang_code = :targetLangCode
        group by e.id, e.word, e.pos
      ) as subquery

              """, nativeQuery = true)
  String findTranslationWithPosAndIPA(@Param("sourceLangCode") String sourceLangCode,
      @Param("targetLangCode") String targetLangCode, @Param("word") String word);

  @Query(value = """
      select json_agg(json_strip_nulls(json_build_object(
        'word', e.word,
        'canonical_forms', (select json_agg(distinct f.form)
        from form f 
        join form_tags ft on f.id = ft.form_id
        where f.etymology_id = e.id and ft.tags = 'canonical'
        ),
        'ipas', (select json_agg(
          so.ipa)
          from sound so
          where so.etymology_id = e.id),
        'etymology', e.etymology,
        'pos', e.pos,
        'senses', (
            SELECT json_agg(json_strip_nulls(json_build_object(
              'glosses', (select json_agg(sg.glosses) from sense_glosses sg where sg.sense_id = s.id),
              'examples', (select json_agg(se.examples) from sense_examples se where se.sense_id = s.id)
            )))
            FROM sense s where s.etymology_id = e.id
           )
      )))
      from etymology e where e.word = :word collate no_case_no_diac and e.source_wiktionary_code = :targetLangCode and e.lang_code = :sourceLangCode
        """, nativeQuery = true)
  String findProperWiktionaryEntries(@Param("sourceLangCode") String sourceLangCode,
      @Param("targetLangCode") String targetLangCode, @Param("word") String word);

  /* @Query(value = AGGREGATE_JSON_ENTRIES + "where e.lang_code = :langCode and e.source_wiktionary_code = :wiktionaryLangCode and random_number < :randNum order by ")
  String getRandomWord(@Param("langCode") String langCode, @Param("wiktionaryLangCode") String wiktionaryLangCode, @Param("randNum") float randNum);

 */
  // find by source wiktionary code, paginated. (Only testing..)
  Page<Etymology> findBySourceWiktionaryCode(String source_wiktionary_code, Pageable pageable);

}
