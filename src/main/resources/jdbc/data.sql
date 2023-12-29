--CREATE TABLE IF NOT EXISTS etym (
--    id SERIAL PRIMARY KEY,
--    pos VARCHAR(10),
--    lang_code VARCHAR(10),
--    lang VARCHAR(100),
--    wiktionary_code VARCHAR(100),
--    senses JSON
--);

-- Insert test data
INSERT INTO etym (pos, lang_code, lang, wiktionary_code, senses)
VALUES ('noun', 'en', 'English', 'en:dog', '[{"gloss": "a dog", "tags": ["en"], "examples": ["He has a dog."]}]');
INSERT INTO etym (pos, lang_code, lang, wiktionary_code, senses)
VALUES ('noun', 'en', 'English', 'en:cat', '[{"gloss": "a cat", "tags": ["en"], "examples": ["She has a cat."]}]');
INSERT INTO etym (pos, lang_code, lang, wiktionary_code, senses)
VALUES ('noun', 'en', 'English', 'en:bird', '[{"gloss": "a bird", "tags": ["en"], "examples": ["He has a bird."]}]');
INSERT INTO etym (pos, lang_code, lang, wiktionary_code, senses)
VALUES ('noun', 'en', 'English', 'en:fish', '[{"gloss": "a fish", "tags": ["en"], "examples": ["He has a fish."]}]');
INSERT INTO etym (pos, lang_code, lang, wiktionary_code, senses)
VALUES ('noun', 'en', 'English', 'en:horse', '[{"gloss": "a horse", "tags": ["en"], "examples": ["He has a horse."]}]');
INSERT INTO etym (pos, lang_code, lang, wiktionary_code, senses)
VALUES ('noun', 'en', 'English', 'en:cow', '[{"gloss": "a cow", "tags": ["en"], "examples": ["He has a cow."]}]');
INSERT INTO etym (pos, lang_code, lang, wiktionary_code, senses)
VALUES ('noun', 'en', 'English', 'en:goat', '[{"gloss": "a goat", "tags": ["en"], "examples": ["He has a goat."]}]');
INSERT INTO etym (pos, lang_code, lang, wiktionary_code, senses)
VALUES ('noun', 'en', 'English', 'en:pig', '[{"gloss": "a pig", "tags": ["en"], "examples": ["He has a pig."]}]');