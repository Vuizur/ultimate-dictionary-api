# Ultimate Dictionary API

A Spring Boot app that exposes a dictionary API for definitions and translations for a wide variety of languages. It supports translations for each language pair.

### API

The main API is `/translation/{source_lang}/{target_lang}/{word}`. I currently have an instance running on http://116.202.96.240:8080/ (I can't guarantee 100 percent uptime though).

It returns a JSON object in the form that contains 3 keys:
* **entries**, which contains a list of all entry taken from Wiktionary, such as IPA, part of speech, definitions, examples, ...
* **posTranslations**, which contains IPA + part of speech, a number of translations, but no detailed definitions or examples
* **translations**, a list of unstructured translations taken from the translation boxes of all Wiktionaries

For example, a request to [`/translation/es/en/estimar`](http://116.202.96.240:8080/translation/es/en/estimar) will return

```json
{
    "entries": [
        {
            "word": "estimar",
            "ipas": [
                "/estiˈmaɾ/",
                "[es.t̪iˈmaɾ]"
            ],
            "etymology": "Borrowed from Latin aestimāre.",
            "pos": "verb",
            "senses": [
                {
                    "glosses": [
                        "to esteem"
                    ]
                },
                {
                    "glosses": [
                        "to estimate"
                    ]
                },
                {
                    "glosses": [
                        "to think, to believe"
                    ]
                }
            ]
        }
    ],
    "translations": [
        "accuse",
        "appraise",
        "appreciate",
        "assay",
        "assess",
        "enjoy",
        "esteem",
        "estimate",
        "evaluate",
        "fancy",
        "gauge",
        "have a high regard for",
        "hold in (high) esteem",
        "honour",
        "judge",
        "like",
        "love",
        "prize",
        "rate",
        "respect",
        "tax",
        "think highly of",
        "think well of",
        "value"
    ]
}
```

### Statistics 
* 18 620 000 dictionary entries with all sort of metadata
* 7 282 354 translations
* 6 283 different supported languages

### Future work
I don't know how much time I will have, but there are a few things that I haven't implemented.
* Lookup for inflected forms - my VPS currently does not have enough disk space. (Maybe one could also exclude languages with too many inflections, or I need to rework my deployment.)
* Getting random words: This is more difficult than it appears, because all standard solutions will be too slow. So we will have to use clever indexing and possibly associate each word with a random number in the DB
* Integrating word frequency data from wordfreq

### Data source
The database is populated with extracted Wiktionary data from 6 Wiktionaries, powered by [Wiktextract](https://github.com/tatuylonen/wiktextract).

### Caveats
Data may be incorrect. This is mainly caused by some nonstandard formatting on Wiktionary that wiktextract doesn't parse correctly. Feel free to report (and fix) possible problems over there. If the problem does not lie with wiktextract, often the translation tables in some Wiktionary editions may be wrong. You can also open an issue here (especially if it is a big problem), I might be able to find the source (no promises).

### Contributing
* Feel free to report bugs, send pull requests and request new features
