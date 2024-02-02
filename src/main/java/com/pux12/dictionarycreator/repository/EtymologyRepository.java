package com.pux12.dictionarycreator.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pux12.dictionarycreator.model.entity.Etymology;

@Repository
public interface EtymologyRepository extends JpaRepository<Etymology, Long> {
  // @EntityGraph(attributePaths = { "senses", "translations", "forms" })
  // List<Etymology> findByWord(String word);
  // JPA generated queries are simply too slow, so we use native queries instead
  @Query(value = """
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
            'code', t.code,
            'lang', t.lang,
            'sense', t.sense,
            'word', t.word
          )))
          FROM "translation" t
          WHERE t.etymology_id = e.id
        )
      )))
      FROM etymology e where e.word = :word
            """, nativeQuery = true)
  String findByWrd(@Param("word") String word);

  @Query(value = """
      select t2.word from "translation" t1
      join "translation" t2 on t2.etymology_id = t1.etymology_id and t1.sense = t2.sense
      where t2.lang_code = :targetLangCode and t1.lang_code = :sourceLangCode and t1.word = :word
        """, nativeQuery = true)
  List<String> findTranslation(@Param("sourceLangCode") String sourceLangCode,
      @Param("targetLangCode") String targetLangCode, @Param("word") String word);

  @Query(value = """
        SELECT * from etymology e where e.word = :word limit 1;
      """, nativeQuery = true)
  Etymology testRandom(@Param("word") String word);

  // find by source wiktionary code, paginated
  Page<Etymology> findBySourceWiktionaryCode(String source_wiktionary_code, Pageable pageable);

}
