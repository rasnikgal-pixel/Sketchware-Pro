#!/usr/bin/env python3
"""
Вынос конкатенированных русских строк: "текст" + var [+ "текст2" + var2...]
Все переменные становятся %1$s, %2$s, ...
"""
import re, sys, os
from xml.sax.saxutils import escape

def slugify(path):
    name = os.path.basename(path).replace('.java', '')
    return re.sub(r'(?<!^)(?=[A-Z])', '_', name).lower()

def parse_concat(expr):
    """
    Парсит выражение вида:
      "текст" + var1 + "текст2" + var2 + ...
    Возвращает (format_string, args_list) или None.
    """
    # Разбиваем по + на верхнем уровне (не внутри скобок/кавычек)
    parts = []
    depth = 0
    in_str = False
    cur = ''
    i = 0
    while i < len(expr):
        c = expr[i]
        if c == '"' and (i == 0 or expr[i-1] != '\\'):
            in_str = not in_str
            cur += c
        elif not in_str and c == '(':
            depth += 1
            cur += c
        elif not in_str and c == ')':
            depth -= 1
            cur += c
        elif not in_str and depth == 0 and c == '+':
            parts.append(cur.strip())
            cur = ''
        else:
            cur += c
        i += 1
    if cur.strip():
        parts.append(cur.strip())

    if len(parts) < 2:
        return None

    fmt = ''
    args = []
    for p in parts:
        if p.startswith('"') and p.endswith('"'):
            # Литеральная строка — берём без кавычек, с эскейпом обратно
            inner = p[1:-1]
            fmt += inner
        else:
            # Переменная/выражение
            args.append(p)
            fmt += f'%{len(args)}$s'

    # Должна быть хотя бы одна русская буква в fmt
    if not re.search(r'[А-Яа-яЁё]', fmt):
        return None

    return fmt, args

def find_concat_strings(source):
    """
    Ищет в источнике конкатенации, начинающиеся с русской строки.
    Возвращает список (start, end, fmt, args).
    """
    results = []
    # Ищем все "..." с русскими буквами
    for m in re.finditer(r'"([^"\\]*(?:\\.[^"\\]*)*)"', source):
        text = m.group(1)
        if not re.search(r'[А-Яа-яЁё]', text):
            continue
        # Проверяем, что следующий непробельный символ — +
        after_pos = m.end()
        while after_pos < len(source) and source[after_pos] in ' \t':
            after_pos += 1
        if after_pos >= len(source) or source[after_pos] != '+':
            continue

        # Захватываем всё выражение справа от кавычки до ; или ) на верхнем уровне
        # Простой подход: тянем до ближайшего ; или \n на верхнем уровне
        expr_start = m.start()
        # Найдём границу выражения
        depth = 0
        in_str = False
        j = after_pos
        while j < len(source):
            c = source[j]
            if c == '"' and (j == 0 or source[j-1] != '\\'):
                in_str = not in_str
            elif not in_str:
                if c == '(':
                    depth += 1
                elif c == ')':
                    if depth == 0:
                        break
                    depth -= 1
                elif c == ';' and depth == 0:
                    break
                elif c == '\n' and depth == 0:
                    break
            j += 1
        expr = source[expr_start:j].strip()
        parsed = parse_concat(expr)
        if parsed:
            results.append((expr_start, j, parsed[0], parsed[1], expr))
    return results

def ensure_string_in_xml(xml_path, key, value):
    txt = open(xml_path, encoding='utf-8').read()
    if f'name="{key}"' in txt:
        return False
    escaped = escape(value)
    add = f'    <string name="{key}">{escaped}</string>\n'
    txt = txt.replace('</resources>', add + '</resources>')
    open(xml_path, 'w', encoding='utf-8').write(txt)
    return True

def ensure_Helper_import(source):
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

def process(java_path, dry=False):
    source = open(java_path, encoding='utf-8').read()
    hits = find_concat_strings(source)

    if not hits:
        print(f'Нет конкатенаций в {java_path}')
        return

    slug = slugify(java_path)
    ru_xml = 'app/src/main/res/values/strings.xml'
    en_xml = 'app/src/main/res/values-en/strings.xml'

    if dry:
        for i, (s, e, fmt, args, expr) in enumerate(hits, 1):
            print(f'  {i}: {fmt} | args={args}')
        return

    # Заменяем с конца
    new_source = source
    hits_sorted = sorted(hits, key=lambda x: -x[0])
    total = len(hits_sorted)
    for idx, (s, e, fmt, args, expr) in enumerate(hits_sorted, 1):
        real_idx = total - idx + 1
        key = f'auto_concat_{slug}_{real_idx:03d}'
        ensure_string_in_xml(ru_xml, key, fmt)
        ensure_string_in_xml(en_xml, key, f'[TODO] {fmt}')
        args_str = ', '.join(args)
        replacement = f'Helper.getResString(R.string.{key}, {args_str})'
        new_source = new_source[:s] + replacement + new_source[e:]

    new_source = ensure_Helper_import(new_source)
    open(java_path, 'w', encoding='utf-8').write(new_source)
    print(f'=== {java_path}: вынесено {total} конкатенаций ===')

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print('Использование: python3 i18n_extract_concat.py <file.java> [--dry-run]')
        sys.exit(1)
    dry = '--dry-run' in sys.argv
    process(sys.argv[1], dry=dry)
