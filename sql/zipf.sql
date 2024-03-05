CREATE OR REPLACE FUNCTION zipf_freq(word text, language text) 
RETURNS float4 
AS $$
from wordfreq import zipf_frequency
return zipf_frequency(word, language)
$$ LANGUAGE plpython3u;