from pyglossary.glossary_v2 import Glossary
import mediawiki_langcodes
import fastapi
import psycopg
Glossary.init()

AUTHOR = "Vuizur"
DB_USER = "postgres"
DB_PASSWORD = "silver"
DB_NAME = "wikt"

conn = psycopg.connect(
    host="localhost",
    dbname=DB_NAME,
    user=DB_USER,
    password=DB_PASSWORD
)

cursor = conn.cursor()

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

def create_dict(source_lang_code: str, target_lang_code: str):
    glossary = Glossary()
    source_lang, target_lang = get_name(source_lang_code, target_lang_code)
    print(f"Creating dictionary from {source_lang} to {target_lang}")

create_dict("en", "cs")

# FastAPI app allowing two languages to be selected and a dictionary to be created using path parameters
app = fastapi.FastAPI()

@app.get("/create_dictionary/{source_lang_code}/{target_lang_code}")
def create_dictionary(source_lang_code: str, target_lang_code: str):
    create_dict(source_lang_code, target_lang_code)
    return {"message": "Dictionary created successfully!"}
