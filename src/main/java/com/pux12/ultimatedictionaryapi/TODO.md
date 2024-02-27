* Endpoint that allows creation of a dictionary
* Create a Translation response class
    - entries that have a form that fits
* Create DTO for translation endpoint
* Search for cyrillic translations from latin languages in Russian wiktionary, maybe open bug if large problem
* Synonyms
* Related words
* Collation for Translations
* Add word frequencies (Problem: we need Python code/server and wordfreq package)
* Set up powerful server for DB creation
* Maybe cluster by lang code/source wiktionary code for faster creation of dictionaries.
    - Then run analyze
* Create wordfreq server
* Run pganalyze for create dictionary query
* Explore creating a general JSON schema for all languages and then autogenerating the correct Spring Boot model (does something like this even exist?)
* Post API to Wikitionary grease pit and ask for feedback.
* Fix randomness by using a common table expression to select a random row and update its random_number and then query for the relevant data