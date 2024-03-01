from typing import Any
from wordfreq import zipf_frequency, available_languages
import psycopg
from tqdm import tqdm


def check_all_langs(cur: Any, langs: set[str]):
    for lang in langs:
        print(lang)
        cur.execute("SELECT * FROM etymology WHERE lang_code = %s LIMIT 1", (lang,))
        if not cur.fetchone():
            print(f"Language code {lang} does not exist in the database")


AUTHOR = "Vuizur"
DB_USER = "postgres"
DB_PASSWORD = "silver"
DB_NAME = "wikt"

BATCH_SIZE = 20_000

langs = set([l for l, _ in available_languages().items()])

conn = psycopg.connect(
    host="localhost", dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD
)
#conn2 = psycopg.connect(
#    host="localhost", dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD
#)


def simple_solution():
    print("Updating rows...")
    i = 1
    pbar = tqdm(total=20_000_000)
    with conn.cursor(name="wordfreq_cursor") as cur, conn.cursor() as upd_cur:
        cur.itersize = 20000
        cur.execute("SELECT id, lang_code, word FROM etymology")
        rows = cur.fetchmany(cur.itersize)
        while len(rows) > 0:
            for row in rows:
                id, lang_code, word = row
                if lang_code in langs:
                    frequency = zipf_frequency(word, lang_code)
                    upd_cur.execute(
                        "UPDATE etymology SET frequency = %s WHERE id = %s", (frequency, id)
                    )
                pbar.update(1)

                if i % BATCH_SIZE == 0:
                    conn.commit()
                i += 1
    conn.commit()

def temp_table_solution():
    with conn.cursor(name="wordfreq_cursor") as cur, conn.cursor() as ins_cur:
        ins_cur.execute("CREATE TEMP TABLE temp_frequency(id INTEGER NOT NULL, frequency FLOAT4) ON COMMIT DROP")
        cur.itersize = 20_000
        cur.execute("SELECT id, lang_code, word FROM etymology LIMIT 3000")
        i = 1 
        pbar = tqdm(total=20_000_000)
        with ins_cur.copy("COPY temp_frequency (id, frequency) FROM STDIN") as copy:
            for row in cur:
                id, lang_code, word = row
                if lang_code in langs:
                    frequency = zipf_frequency(word, lang_code)
                    copy.write_row((id, frequency))
                pbar.update(1)
            ins_cur.execute("UPDATE etymology e SET e.frequency = t.frequency FROM temp_frequency t WHERE e.id = t.id")
            i += 1
        conn.commit()

temp_table_solution()

conn.close()
