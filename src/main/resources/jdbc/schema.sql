-- Etym table with the rows: id, pos, lang_code, lang, wiktionary_code, JSON senses
CREATE TABLE IF NOT EXISTS etym (
    id SERIAL PRIMARY KEY,
    word TEXT,
    pos TEXT,
    lang_code TEXT,
    lang TEXT,
    wiktionary_code TEXT,
    senses JSON,
    translations JSON
);