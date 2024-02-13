import json
with open("D:/ProgrammingStuff/kaikki/raw-wiktextract-data.json", "r", encoding="utf-8") as f, \
    open("multiple_form_ofs.txt", "w", encoding="utf-8") as f2:
    i = 0
    for line in f:
        obj = json.loads(line)
        if "senses" in obj:
            for sense in obj["senses"]:
                if "form_of" in sense and len(sense["form_of"]) != 1:
                    f2.write(obj["word"])
                    f2.write(str(sense))
                    f2.write("\n")
        i += 1
        if i > 20000:
            quit()