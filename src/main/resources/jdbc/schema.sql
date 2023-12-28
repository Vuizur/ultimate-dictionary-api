-- Etym table with the rows: id, pos, lang_code, lang, wiktionary_code, JSON senses
CREATE TABLE IF NOT EXISTS etym (
    id SERIAL PRIMARY KEY,
    pos VARCHAR(10),
    lang_code VARCHAR(10),
    lang VARCHAR(100),
    wiktionary_code VARCHAR(100),
    senses JSON
);