from wordfreq import zipf_frequency
import psycopg


AUTHOR = "Vuizur"
DB_USER = "postgres"
DB_PASSWORD = "silver"
DB_NAME = "wikt"

BATCH_SIZE = 10000

conn = psycopg.connect(
    host="localhost", dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD
)
cur = conn.cursor()

cur.execute("SELECT id, lang_code, word FROM etymology")

i = 1
while True:
    print(f"Batch {i}")
    rows = cur.fetchmany(BATCH_SIZE)

    if not rows:
        break

    for row in rows:
        id, lang_code, word = row
        frequency = zipf_frequency(word, lang_code)

        cur.execute(
            "UPDATE etymology SET frequency = %s WHERE id = %s", (frequency, id)
        )

    conn.commit()

cur.close()
conn.close()
