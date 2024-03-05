UPDATE etymology
SET frequency = zipf_freq(word, lang_code)
WHERE id IN (SELECT id FROM etymology LIMIT 1000);