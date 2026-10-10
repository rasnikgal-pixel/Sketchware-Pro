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
    """Возвращает список (start_pos, end_pos, text) для UI-строк."""
    results = []
    # Простое сканирование: ищем "русский текст"
    # Но пропускаем совпадения внутри комментариев и в строках, содержащих %-конкатенацию
    for m in re.finditer(r'"([^"\\]*(?:\\.[^"\\]*)*)"', source):
        text = m.group(1)
        if not re.search(r'[А-Яа-яЁё]', text):
            continue
        # Пропускаем строки-комментарии: если перед кавычкой стоит // или /* или *
        before = source[max(0, m.start()-80):m.start()]
        # Обрезаем по последней //
        line_start = before.rfind('\n')
        if line_start >= 0:
            line = before[line_start+1:]
        else:
            line = before
        stripped = line.lstrip()
        if stripped.startswith('//') or stripped.startswith('*') or stripped.startswith('/*'):
            continue
        # Проверяем, что следующая непустая часть после закрывающей кавычки — это
        # , ) ; + — то есть в контексте вызова метода
        after = source[m.end():m.end()+40]
        # Если идёт конкатенация — пропускаем
        if re.match(r'\s*\+', after):
            continue
        results.append((m.start(), m.end(), text))
    return results

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
        replacement = f'getString(R.string.{key})'
        new_source = new_source[:s] + replacement + new_source[e:]

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
