#!/usr/bin/env python3
"""
Вынос русских строк из layout XML: android:text/hint/title/contentDescription.
"""
import re, sys, os
from xml.sax.saxutils import escape

ATTRS = ['text', 'hint', 'title', 'contentDescription']

def slugify(path):
    name = os.path.basename(path).replace('.xml', '')
    s = re.sub(r'(?<!^)(?=[A-Z])', '_', name).lower()
    s = re.sub(r'[^a-z0-9_]', '_', s)
    return s

def ensure_string_in_xml(xml_path, key, value):
    txt = open(xml_path, encoding='utf-8').read()
    if f'name="{key}"' in txt:
        return False
    escaped = escape(value)
    add = f'    <string name="{key}">{escaped}</string>\n'
    txt = txt.replace('</resources>', add + '</resources>')
    open(xml_path, 'w', encoding='utf-8').write(txt)
    return True

def process(layout_path, dry=False):
    src = open(layout_path, encoding='utf-8').read()
    slug = slugify(layout_path)
    ru_xml = 'app/src/main/res/values/strings.xml'
    en_xml = 'app/src/main/res/values-en/strings.xml'

    # Собираем все совпадения
    matches = []
    for attr in ATTRS:
        pattern = re.compile(r'android:' + attr + r'="([^"]*[А-Яа-яЁё][^"]*)"')
        for m in pattern.finditer(src):
            matches.append((m.start(), m.end(), attr, m.group(1)))

    if not matches:
        print(f'Нет русских атрибутов в {layout_path}')
        return

    if dry:
        for i, (s, e, attr, text) in enumerate(matches, 1):
            print(f'  {i}: {attr}="{text}"')
        return

    # Заменяем с конца
    new_src = src
    matches_sorted = sorted(matches, key=lambda x: -x[0])
    total = len(matches_sorted)
    for idx, (s, e, attr, text) in enumerate(matches_sorted, 1):
        real_idx = total - idx + 1
        key = f'auto_layout_{slug}_{real_idx:03d}'
        ensure_string_in_xml(ru_xml, key, text)
        ensure_string_in_xml(en_xml, key, f'[TODO] {text}')
        replacement = f'android:{attr}="@string/{key}"'
        new_src = new_src[:s] + replacement + new_src[e:]

    open(layout_path, 'w', encoding='utf-8').write(new_src)
    print(f'=== {layout_path}: вынесено {total} строк ===')

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print('Использование: python3 i18n_extract_layout.py <file.xml> [--dry-run]')
        sys.exit(1)
    dry = '--dry-run' in sys.argv
    process(sys.argv[1], dry=dry)
