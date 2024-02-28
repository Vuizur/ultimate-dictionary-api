from fastapi import FastAPI, HTTPException
from wordfreq import zipf_frequency

app = FastAPI()

@app.get("/zipf_frequency")
async def get_zipf_frequency(word: str, lang_code: str) -> float :
    try:
        frequency = zipf_frequency(word, lang_code)
        # return frequency
        return frequency
    except Exception:
        raise HTTPException(status_code=404, detail="Language not supported")

# batched zipf_frequency that allows sending a list of words
@app.post("/zipf_frequency_batch")
async def get_zipf_frequency_batch(words: list[str], lang_code: str) -> list[float] :
    try:
        frequencies = [zipf_frequency(word, lang_code) for word in words]
        return frequencies
    except Exception:
        raise HTTPException(status_code=404, detail="Language not supported")


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)