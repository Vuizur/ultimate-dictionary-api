from pyglossary.glossary_v2 import Glossary
import mediawiki_langcodes
import fastapi
import psycopg
import os
from uuid import uuid4

AUTHOR = "Vuizur"
DB_USER = "postgres"
DB_PASSWORD = "silver"
DB_NAME = "wikt"
DICTIONARY_BASE_FOLDER = "dictionaries/"

Glossary.init()

# create dictionary base folder if it doesn't exist
if not os.path.exists(DICTIONARY_BASE_FOLDER):
    os.makedirs(DICTIONARY_BASE_FOLDER)

conn = psycopg.connect(
    host="localhost", dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD
)

cursor = conn.cursor()

GET_FULL_ENTRIES = """
select json_agg(json_strip_nulls(json_build_object(
        'word', e.word,
        'canonical_forms', (select json_agg(distinct f.form)
        from form f 
        join form_tags ft on f.id = ft.form_id
        where f.etymology_id = e.id and ft.tags = 'canonical'
        ),
        'forms', (SELECT json_agg(distinct f.form
        )
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
      from etymology e where e.source_wiktionary_code = %s and e.lang_code = %s AND e.random_number IS NOT NULL
      """
        # random_number is null for entries that only have inflection glosses

def get_name(source_l_code: str, target_l_code: str) -> tuple[str, str]:
    source_lang = mediawiki_langcodes.code_to_name(source_l_code, target_l_code)
    if source_lang == "":
        source_lang = mediawiki_langcodes.code_to_name(source_l_code, "en")
        target_lang = mediawiki_langcodes.code_to_name(target_l_code, "en")
    else:
        target_lang = mediawiki_langcodes.code_to_name(target_l_code, target_l_code)

    source_lang = source_lang.capitalize()
    target_lang = target_lang.capitalize()
    return source_lang, target_lang


def create_entry_html(
    canonical_form: str | None,
    ipas: list[str] | None,
    pos: str | None,
    senses: list[dict[str, list[str]]],
    etymology: str | None,
) -> str:
    html = f"<b>{canonical_form}</b>"

    if ipas is not None:
        html += f"<br>{', '.join(ipas)}"
    if pos is not None:
        html += f"<br><i>{pos}</i>"
    # Add senses in ordered list
    html += "<ol>"
    for sense in senses:
        if "glosses" not in sense:
            continue
        glosses = sense["glosses"]
        html += "<li>"
        html += f"{', '.join(glosses)}"
        if "examples" in sense:
            # Add first example in cursive
            html += f"<br><i>{sense['examples'][0]}</i>"
        html += "</li>"
    html += "</ol>"
    if etymology is not None:
        html += f"<br>{etymology}"
    return html


def create_dict(source_lang_code: str, target_lang_code: str) -> str:
    glos = Glossary()
    source_lang, target_lang = get_name(source_lang_code, target_lang_code)
    print(f"Creating dictionary from {source_lang} to {target_lang}")
    cursor.execute(
        GET_FULL_ENTRIES,
        (source_lang_code, target_lang_code),
    )

    entries = cursor.fetchone()[0]
    for entry in entries:
        if "canonical_forms" in entry:
            word: str = entry["canonical_forms"][0]
        else:
            word: str = entry["word"]
        html = create_entry_html(
            word,
            entry.get("ipas"),
            entry.get("pos"),
            entry["senses"],
            entry.get("etymology"),
        )
        word_list: list[str] = [word]
        if word != entry["word"]:
            word_list.append(entry["word"])
        # Append forms to word list
        if "forms" in entry:
            word_list.extend(entry["forms"])
        glos.addEntry(glos.newEntry(word_list, html, "h"))

    glos.setInfo("title", f"{source_lang} - {target_lang} (Wiktionary)")
    glos.setInfo("author", AUTHOR)
    file_path = (
        f"{DICTIONARY_BASE_FOLDER}wiktionary_{source_lang_code}_{target_lang_code}"
        + str(uuid4())
    )
    glos.write(file_path + ".ifo", format="Stardict")
    return file_path


create_dict("en", "cs")
quit()

# FastAPI app allowing two languages to be selected and a dictionary to be created using path parameters
app = fastapi.FastAPI()


@app.get("/create_dictionary/{source_lang_code}/{target_lang_code}")
def create_dictionary(source_lang_code: str, target_lang_code: str):
    file_path = create_dict(source_lang_code, target_lang_code)
    return {
        "file_path": file_path
    }  # Spring boot will then serve the file to the user and delete it afterwards
