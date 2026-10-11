#!/usr/bin/env python3
"""
Добавляет новый строковый ресурс сразу во все 5 языковых файлов.
Автоматически экранирует апострофы и % (кроме %1$s, %2$d).

Использование:
  python3 add_string.py --key my_key --ru "Привет" --en "Hello" \
      --zh "你好" --hi "नमस्ते" --uk "Привіт"
  python3 add_string.py --file strings_common.xml --key ...

Если перевод не указан для какого-то языка — используется значение RU.
"""
import argparse
import os
import re
import sys

LANGS = [
    ('values', 'ru'),
    ('values-en', 'en'),
    ('values-zh', 'zh'),
    ('values-hi', 'hi'),
    ('values-uk', 'uk'),
]

def escape_apostrophes(text):
    result = []
    i = 0
    while i < len(text):
        c = text[i]
        if c == "'" and (i == 0 or text[i-1] != '\\'):
            result.append("\\'")
        else:
            result.append(c)
        i += 1
    return ''.join(result)

def escape_percent(text):
    # Экранируем % если это не %1$s, %2$d, %s, %d
    return re.sub(r'%(?![0-9s])', r'\\%', text)

def prepare(text):
    if not text:
        return text
    return escape_percent(escape_apostrophes(text))

def has_key(path, key):
    s = open(path, encoding='utf-8').read()
    return f'name="{key}"' in s

def add_to_file(path, key, value):
    if has_key(path, key):
        return False
    s = open(path, encoding='utf-8').read()
    add = f'    <string name="{key}">{prepare(value)}</string>\n'
    s = s.replace('</resources>', add + '</resources>')
    open(path, 'w', encoding='utf-8').write(s)
    return True

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--file', default='strings.xml',
                    help='Имя файла (strings.xml / strings_common.xml)')
    ap.add_argument('--key', required=True)
    ap.add_argument('--ru', required=True)
    ap.add_argument('--en')
    ap.add_argument('--zh')
    ap.add_argument('--hi')
    ap.add_argument('--uk')
    args = ap.parse_args()

    root = 'app/src/main/res'
    added = []
    skipped = []

    for folder, code in LANGS:
        path = os.path.join(root, folder, args.file)
        if not os.path.exists(path):
            print(f'ПРОПУСК (нет файла): {path}')
            continue
        val = getattr(args, code, None) or args.ru
        if add_to_file(path, args.key, val):
            added.append(folder)
        else:
            skipped.append(folder)

    print(f'Добавлено: {added}')
    print(f'Уже было: {skipped}')

if __name__ == '__main__':
    main()
