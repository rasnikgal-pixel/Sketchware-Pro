#!/usr/bin/env python3
"""Проверка полноты локализации.
Использование: python3 i18n_check.py
"""
import os
import re
import sys

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "app", "src", "main", "res")
LOCALES = ["values", "values-en", "values-zh", "values-hi", "values-uk"]

SKIP = ("abc_", "mtrl_", "material_", "androidx_", "bottomsheet_", "appbar_", "call_notification_", "character_counter_", "common_google_play_services_", "status_bar_", "expand_button_", "fab_transformation_", "m3_", "com.google.", "ga_", "gcm_", "google_", "project_id", "password_toggle_", "path_password_", "preference_", "searchbar_", "searchview_", "side_sheet_", "nav_rail_", "summary_collapsed_", "v7_preference_", "exposed_dropdown_", "hide_bottom_", "item_view_role_", "clear_text_end_")

def read_keys(path):
    if not os.path.isfile(path):
        return set()
    s = open(path, encoding="utf-8").read()
    keys = set(re.findall(r'<string name="([^"]+)"', s))
    return {k for k in keys if not k.startswith(SKIP)}

def main():
    keys_by_loc = {}
    for loc in LOCALES:
        keys_by_loc[loc] = read_keys(os.path.join(ROOT, loc, "strings.xml"))
    base = keys_by_loc["values"]
    missing_total = 0
    extra_total = 0
    for loc in LOCALES:
        if loc == "values":
            continue
        miss = sorted(base - keys_by_loc[loc])
        extra = sorted(keys_by_loc[loc] - base)
        print(f"=== {loc} ===")
        print(f"  всего: {len(keys_by_loc[loc])}, нет {len(miss)}, лишних {len(extra)}")
        if miss:
            print("  --- отсутствуют ---")
            for k in miss[:50]:
                print(f"    {k}")
            if len(miss) > 50:
                print(f"    ... и ещё {len(miss)-50}")
            missing_total += len(miss)
        if extra:
            extra_total += len(extra)
            print("  --- лишние (есть только здесь) ---")
            for k in extra[:20]:
                print(f"    {k}")
        print()
    if extra_total > 0:
        print(f"ℹ️  Лишних ключей (есть только в некоторых локалях): {extra_total}")
    if missing_total == 0:
        print("✅ Локализация полная.")
        return 0
    print(f"⚠️  Всего отсутствует ключей: {missing_total}")
    return 1

if __name__ == "__main__":
    sys.exit(main())
