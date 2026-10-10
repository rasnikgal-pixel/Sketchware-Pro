#!/usr/bin/env python3
"""
Оборачивает тернарники с Helper.getResString в (CharSequence) — чтобы
компилятор не путал перегрузки setText/setTitle/Toast.makeText.
"""
import re, os, sys

# Паттерн: X ? Helper.getResString(R.string.a) : Helper.getResString(R.string.b)
# Где X — короткое выражение (без ? и :)
# Не трогаем, если уже есть (CharSequence) перед тернарником
TERNA_PATTERN = re.compile(
    r'(?<!\(CharSequence\) )([A-Za-z_][\w\.]*)\s*\?\s*'
    r'(Helper\.getResString\(R\.string\.[\w]+\))\s*:\s*'
    r'(Helper\.getResString\(R\.string\.[\w]+\))'
)

def fix_file(path):
    src = open(path, encoding='utf-8').read()
    new = TERNA_PATTERN.sub(
        lambda m: f'(CharSequence) ({m.group(1)} ? {m.group(2)} : {m.group(3)})',
        src)
    if new == src:
        return False
    open(path, 'w', encoding='utf-8').write(new)
    return True

def main():
    roots = sys.argv[1:] or ['app/src/main/java']
    total = 0
    for root in roots:
        for dirpath, _, files in os.walk(root):
            for f in files:
                if not f.endswith('.java'): continue
                p = os.path.join(dirpath, f)
                if fix_file(p):
                    print('исправлен:', p)
                    total += 1
    print(f'Готово. Изменено файлов: {total}')

if __name__ == '__main__':
    main()
