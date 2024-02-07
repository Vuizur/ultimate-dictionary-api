# Python script to show codepoints of a string
import unicodedata

def show_codepoints(s):
    for c in s:
        print(f'{c} U+{ord(c):04X} {unicodedata.name(c)}')

if __name__ == '__main__':
    STRING = "дендрохронологиями"
    STRING2 = "дѐндрохроноло́гиями"

    print(f'Codepoints of "{STRING}"')
    show_codepoints(STRING)
    print(f'Codepoints of "{STRING2}"')
    show_codepoints(STRING2)