select json_agg(json_strip_nulls(json_build_object(
        'word', e.word,
        'canonical_forms', (select json_agg(distinct f.form)
        from form f 
        join form_tags ft on f.id = ft.form_id
        where f.etymology_id = e.id and ft.tags = 'canonical'
        ),
        'forms', (SELECT json_agg(distinct f.form
        )))
        FROM form f
        LEFT JOIN form_tags ft ON f.id = ft.form_id
        WHERE f.etymology_id = e.id and ft.tags <> 'canonical'
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
      from etymology e where e.source_wiktionary_code = :targetLangCode and e.lang_code = :sourceLangCode