#!/usr/bin/env python3
"""Генератор мастер-файла для нового языка.
Читает app/src/main/res/values/strings.xml + strings_common.xml,
создаёт strings_master.xml с ключами и русскими значениями.
"""
import os
import re
import sys

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)),
                    "app", "src", "main", "res", "values")

def read_strings(path):
    if not os.path.isfile(path):
        return []
    s = open(path, encoding="utf-8").read()
    items = re.findall(r'<string name="([^"]+)"[^>]*>(.*?)</string>',
                       s, re.DOTALL)
    return items

SKIP = ("abc_", "mtrl_", "material_", "androidx_", "bottomsheet_", "appbar_", "call_notification_", "character_counter_", "common_google_play_services_", "status_bar_", "expand_button_", "fab_transformation_", "m3_", "com.google.", "ga_", "gcm_", "google_", "project_id", "password_toggle_", "path_password_", "preference_", "searchbar_", "searchview_", "side_sheet_", "nav_rail_", "summary_collapsed_", "v7_preference_", "exposed_dropdown_", "hide_bottom_", "item_view_role_", "clear_text_end_", "bottom_sheet_behavior", "title_activity_")

def main():
    strings_xml = os.path.join(ROOT, "strings.xml")
    common_xml = os.path.join(ROOT, "strings_common.xml")
    items = read_strings(strings_xml) + read_strings(common_xml)
    # Уникальные по ключу — побеждает первое вхождение
    seen = {}
    for k, v in items:
        if k not in seen:
            seen[k] = v.strip()
    keys = sorted(k for k in seen.keys() if not k.startswith(SKIP))
    out = []
    out.append('<?xml version="1.0" encoding="utf-8"?>')
    out.append('<resources>')
    out.append('    <!-- Мастер-файл для нового языка. -->')
    out.append('    <!-- Значения — из русской локали (эталон). -->')
    out.append('    <!-- Замените значения на перевод, сохраните как strings.xml в values-XX. -->')
    out.append('')
    for k in keys:
        v = seen[k]
        v = v.replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;')
        out.append(f'    <string name="{k}">{v}</string>')
    out.append('</resources>')
    print('\n'.join(out))
    print(f'# Итого: {len(keys)} ключей', file=sys.stderr)

if __name__ == "__main__":
    main()
