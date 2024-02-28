from fastapi import FastAPI, HTTPException
from wordfreq import zipf_frequency

app = FastAPI()

@app.get("/zipf_frequency")
def get_zipf_frequency(word: str, lang_code: str) -> float :
    try:
        frequency = zipf_frequency(word, lang_code)
        # return frequency
        return frequency
    except LookupError:
        raise HTTPException(status_code=404, detail="Language not supported")

if __name__ == "__main__":
    #print(zipf_frequency("dumm", "de"))
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)