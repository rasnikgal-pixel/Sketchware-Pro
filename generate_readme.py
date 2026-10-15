#!/usr/bin/env python3
"""Утилита работы с README.
Режим: check — сравнить структуру README.md и README-en.md

"""
import os
import re
import sys

ROOT = os.path.dirname(os.path.abspath(__file__))

def read(path):
    if not os.path.isfile(path):
        return ""
    return open(path, encoding="utf-8").read()

def sections(text):
    return [line.strip() for line in text.splitlines()
            if line.startswith("## ") or line.startswith("### ")]

def normalize(s):
    # Убираем ведущие #, пробелы, эмодзи и регистр — для сравнения структуры
    s = re.sub(r'^#+\s*', '', s)
    s = re.sub(r'[^\w\s]', '', s, flags=re.UNICODE)
    return s.strip().lower()

def extract_emoji(s):
    import re as _re
    m = _re.search(r'[🌀-🫿☀-➿]', s)
    return m.group(0) if m else '?'

def do_check():
    ru = [s for s in sections(read(os.path.join(ROOT, "README.md"))) if s.startswith("## ")]
    en = [s for s in sections(read(os.path.join(ROOT, "README-en.md"))) if s.startswith("## ")]
    ru_e = [extract_emoji(s) for s in ru]
    en_e = [extract_emoji(s) for s in en]
    print(f"README.md:    {len(ru)} h2-секций")
    print(f"README-en.md: {len(en)} h2-секций")
    print()
    print("Эмодзи (порядок):")
    print("  ru:", " ".join(ru_e))
    print("  en:", " ".join(en_e))
    print()
    diff = False
    for i, (a, b) in enumerate(zip(ru, en), 1):
        if ru_e[i-1] != en_e[i-1]:
            print(f"  ⚠️  #{i}: ru={ru_e[i-1]}  en={en_e[i-1]}")
            print(f"      ru: {a}")
            print(f"      en: {b}")
            diff = True
    if len(ru) != len(en):
        diff = True
        if len(ru) > len(en):
            print(f"  ⚠️  В README.md на {len(ru)-len(en)} секций больше:")
            for s in ru[len(en):]:
                print(f"      {s}")
        else:
            print(f"  ⚠️  В README-en.md на {len(en)-len(ru)} секций больше:")
            for s in en[len(ru):]:
                print(f"      {s}")
    if not diff:
        print("✅ Структура README совпадает (по эмодзи и порядку).")
        return 0
    return 1

def main():
    if len(sys.argv) < 2:
        print("Использование: python3 generate_readme.py [check|gen-short]")
        return 1
    cmd = sys.argv[1]
    if cmd == "check":
        return do_check()
    print(f"Неизвестная команда: {cmd}")
    return 1

if __name__ == "__main__":
    sys.exit(main())
