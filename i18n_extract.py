#!/usr/bin/env python3
"""
Вынос русских хардкод-строк из Java в strings.xml.
Использование: python3 i18n_extract.py <путь_к_java_файлу> [--dry-run]
"""
import sys, re, os, xml.etree.ElementTree as ET
from xml.sax.saxutils import escape

# Методы, где первая строка-аргумент — это UI-текст
UI_METHODS = [
    'toast', 'setTitle', 'setMessage', 'setText', 'setHint',
    'setPositiveButton', 'setNegativeButton', 'setNeutralButton',
    'setSingleChoiceItems', 'setItems', 'show',
    'makeText', 'setError', 'setContentDescription',
    'setCustomTitle', 'setSubtitle',
]

def slugify(path):
    # Из пути делаем префикс: app/src/main/java/com/besome/sketch/editor/ViewEditor.java
    # → view_editor
    name = os.path.basename(path).replace('.java', '')
    # CamelCase → snake_case
    s = re.sub(r'(?<!^)(?=[A-Z])', '_', name).lower()
    return s



def find_ui_strings(source):
    """Посимвольный сканер: text blocks, комментарии, обычные строки."""
    results = []
    n = len(source)
    i = 0
    DQ = chr(34)

    while i < n:
        c = source[i]

        # text block: три кавычки подряд
        if source[i:i+3] == DQ + DQ + DQ:
            end = source.find(DQ + DQ + DQ, i + 3)
            if end < 0:
                break
            i = end + 3
            continue

        # строчный комментарий
        if source[i:i+2] == '//':
            nl = source.find(chr(10), i)
            if nl < 0:
                break
            i = nl + 1
            continue

        # блочный комментарий
        if source[i:i+2] == '/*':
            end = source.find('*/', i + 2)
            if end < 0:
                break
            i = end + 2
            continue

        # обычная строка
        if c == DQ:
            j = i + 1
            buf = []
            while j < n:
                ch = source[j]
                if ch == chr(92) and j + 1 < n:
                    buf.append(source[j:j+2])
                    j += 2
                    continue
                if ch == DQ:
                    break
                buf.append(ch)
                j += 1
            text = ''.join(buf)
            if re.search(r'[А-Яа-яЁё]', text):
                before = source[max(0, i-30):i]
                if not re.search(r'\bcase\s*$', before):
                    after = source[j+1:j+1+20] if j + 1 < n else ''
                    if not re.match(r'\s*\+', after):
                        results.append((i, j+1, text))
            i = j + 1
            continue

        i += 1

    return results

def ensure_Helper_import(source):
    """Добавляет import Helper и import R, если их нет."""
    m = re.search(r'^\s*package\s+[\w\.]+\s*;\s*\n', source, re.MULTILINE)
    if not m:
        return source
    insert_pos = m.end()
    adds = ''
    if 'import mod.hey.studios.util.Helper;' not in source:
        adds += 'import mod.hey.studios.util.Helper;\n'
    if 'import pro.sketchware.R;' not in source:
        adds += 'import pro.sketchware.R;\n'
    if adds:
        return source[:insert_pos] + '\n' + adds + source[insert_pos:]
    return source


def ensure_string_in_xml(xml_path, key, value, add_todo=None):
    """Добавляет <string name="key">value</string> перед </resources>."""
    txt = open(xml_path, encoding='utf-8').read()
    if f'name="{key}"' in txt:
        return False
    if add_todo:
        val = add_todo
    else:
        val = value
    escaped = escape(val)
    add = f'    <string name="{key}">{escaped}</string>\n'
    txt = txt.replace('</resources>', add + '</resources>')
    open(xml_path, 'w', encoding='utf-8').write(txt)
    return True

def process(java_path, dry=False):
    source = open(java_path, encoding='utf-8').read()
    hits = find_ui_strings(source)

    if not hits:
        print(f'Нет UI-строк в {java_path}')
        return

    slug = slugify(java_path)
    ru_xml = 'app/src/main/res/values/strings.xml'
    en_xml = 'app/src/main/res/values-en/strings.xml'

    # Заменяем строки с конца, чтобы индексы не сбивались
    new_source = source
    changes = []
    for idx, (s, e, text) in enumerate(reversed(hits), 1):
        real_idx = len(hits) - idx + 1
        key = f'auto_{slug}_{real_idx:03d}'
        changes.append((key, text))

    if dry:
        print(f'=== {java_path} — найдено {len(hits)} строк ===')
        for key, text in reversed(changes):
            print(f'  {key}: {text}')
        return

    # Добавляем в strings.xml (сначала все ключи, потом замена в Java)
    for key, text in reversed(changes):
        ensure_string_in_xml(ru_xml, key, text)
        ensure_string_in_xml(en_xml, key, f'[TODO] {text}', add_todo=None)

    # Заменяем в Java (с конца)
    for idx, (s, e, text) in enumerate(reversed(hits), 1):
        real_idx = len(hits) - idx + 1
        key = f'auto_{slug}_{real_idx:03d}'
        replacement = f'Helper.getResString(R.string.{key})'
        new_source = new_source[:s] + replacement + new_source[e:]

    new_source = ensure_Helper_import(new_source)
    open(java_path, 'w', encoding='utf-8').write(new_source)
    print(f'=== {java_path}: вынесено {len(hits)} строк ===')
    for key, text in reversed(changes):
        print(f'  {key}: {text}')

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print('Использование: python3 i18n_extract.py <file.java> [--dry-run]')
        sys.exit(1)
    dry = '--dry-run' in sys.argv
    process(sys.argv[1], dry=dry)
