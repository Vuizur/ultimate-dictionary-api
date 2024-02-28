# Test requests per second for
# @app.get("/zipf_frequency")
# def get_zipf_frequency(word: str, lang_code: str) -> float :

import requests
import time
from wordfreq import zipf_frequency

# localhost:8000
#url = "http://localhost:8000/zipf_frequency"
url = "http://127.0.0.1:8000/zipf_frequency?word=Haus&lang_code=de"
def test_requests_per_second():
    session = requests.Session()
    for i in range(1000):
        response = session.get(url)
words = ["Haus"] * 1000

# test batched
def test_requests_per_second_batch():
    url = "http://127.0.0.1:8000/zipf_frequency"
    session = requests.Session()
    # construct list with 1000 times the word "Haus"
    for i in range(1000):
        response = session.post(url, json={"words": words, "lang_code": "de"})

def test_zipf_frequency():
    for i in range(1000):
        zf = zipf_frequency("Haus", "de")


# Simulate 100000 requests
start = time.time()
test_requests_per_second()
#test_zipf_frequency()
end = time.time()
print("Time: ", end - start)
print("Requests per second: ", 1000 / (end - start))


