#!/usr/bin/env python3
"""
Обновляет update.json после релиза:
- releaseDate  = сегодня (UTC)
- apkSize      = размер APK в байтах
- sha256       = хеш APK (lowercase hex)
- versionCode  = берётся из app/build.gradle (versionCode)
- versionName  = берётся из app/build.gradle (versionName)
- downloadUrl  = строится из тега релиза

changelog НЕ трогается — пишется вручную.
"""
import hashlib
import json
import os
import re
import sys
from datetime import datetime, timezone
from pathlib import Path


def read_gradle_version():
    """Читает versionCode / versionName из app/build.gradle."""
    gradle = Path("app/build.gradle")
    if not gradle.exists():
        print("WARN: app/build.gradle not found, keeping version from update.json", file=sys.stderr)
        return None, None

    text = gradle.read_text(encoding="utf-8")
    vcode = re.search(r"versionCode\s+(\d+)", text)
    vname = re.search(r'versionName\s+"([^"]+)"', text)
    vname_str = vname.group(1).lstrip("v") if vname else None
    return (
        int(vcode.group(1)) if vcode else None,
        vname_str,
    )


def sha256_of(path: Path) -> str:
    h = hashlib.sha256()
    with path.open("rb") as f:
        for chunk in iter(lambda: f.read(1 << 20), b""):
            h.update(chunk)
    return h.hexdigest()


def main():
    apk_path = Path(sys.argv[1]) if len(sys.argv) > 1 else Path("apk/app-release.apk")
    if not apk_path.exists():
        print(f"ERROR: APK not found: {apk_path}", file=sys.stderr)
        return 1

    apk_size = apk_path.stat().st_size
    apk_sha = sha256_of(apk_path)
    print(f"APK size: {apk_size} bytes")
    print(f"APK sha256: {apk_sha}")

    update_path = Path("update.json")
    if not update_path.exists():
        print("ERROR: update.json not found", file=sys.stderr)
        return 1

    data = json.loads(update_path.read_text(encoding="utf-8"))

    # Обновить versionCode/versionName из gradle (если получилось)
    vcode, vname = read_gradle_version()
    if vcode is not None:
        data["versionCode"] = vcode
    if vname is not None:
        data["versionName"] = vname

    # releaseDate = сегодня UTC
    data["releaseDate"] = datetime.now(timezone.utc).strftime("%Y-%m-%d")

    # downloadUrl из тега (по умолчанию 7.1.0, можно переопределить env RELEASE_TAG)
    tag = os.environ.get("RELEASE_TAG", data.get("versionName", "7.1.0"))
    repo = os.environ.get("GITHUB_REPOSITORY", "rasnikgal-pixel/Sketchware-Pro")
    data["downloadUrl"] = f"https://github.com/{repo}/releases/download/{tag}/app-release.apk"

    # метрики APK
    data["apkSize"] = apk_size
    data["sha256"] = apk_sha

    # дефолты для новых полей
    data.setdefault("channel", "stable")
    data.setdefault("minVersion", 0)
    data.setdefault("required", False)

    update_path.write_text(
        json.dumps(data, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )
    print("update.json updated:")
    print(f"  versionCode = {data['versionCode']}")
    print(f"  versionName = {data['versionName']}")
    print(f"  releaseDate = {data['releaseDate']}")
    print(f"  apkSize     = {data['apkSize']}")
    print(f"  sha256      = {data['sha256']}")
    print(f"  downloadUrl = {data['downloadUrl']}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
