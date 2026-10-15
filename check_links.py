#!/usr/bin/env python3
"""Проверка битых ссылок в README-*.md.
Использование: python3 check_links.py
"""
import os
import re
import sys
import urllib.request
import urllib.error

ROOT = os.path.dirname(os.path.abspath(__file__))
FILES = ["README.md", "README-en.md", "README-zh.md", "README-hi.md", "README-uk.md", "FIXES.md"]
URL_RE = re.compile(r'https?://[^\s\)\]<>"]+')

SKIP_DOMAINS = (
    "4pda.to",
    "img.shields.io",
    "/pull/new/",
)

def collect_urls(path):
    if not os.path.isfile(path):
        return []
    s = open(path, encoding="utf-8", errors="ignore").read()
    return list(set(URL_RE.findall(s)))

def check_url(url, timeout=10):
    try:
        req = urllib.request.Request(url, method="HEAD")
        req.add_header("User-Agent", "Mozilla/5.0")
        with urllib.request.urlopen(req, timeout=timeout) as r:
            return r.status
    except urllib.error.HTTPError as e:
        return e.code
    except Exception:
        return -1

def main():
    total = 0
    broken = 0
    for f in FILES:
        urls = collect_urls(os.path.join(ROOT, f))
        if not urls:
            continue
        print(f"=== {f} ({len(urls)} ссылок) ===")
        for u in sorted(urls):
            if any(d in u for d in SKIP_DOMAINS):
                continue
            total += 1
            code = check_url(u)
            mark = "OK" if 200 <= code < 400 else f"FAIL {code}"
            print(f"  [{mark}] {u}")
            if not (200 <= code < 400):
                broken += 1
    print(f"\nВсего: {total}, битых: {broken}")
    return 0 if broken == 0 else 1

if __name__ == "__main__":
    sys.exit(main())
