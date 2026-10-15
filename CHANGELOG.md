# История версий Sketchware Pro RU

Полная история выпусков форка **Sketchware Pro RU** от RasNikGal.

Репозиторий: https://github.com/rasnikgal-pixel/Sketchware-Pro

---

## 7.8.2 — 16.10.2026 (versionCode 180) — ТЕКУЩИЙ

### 🐛 Белый экран — финально исправлено

- Собранные приложения больше не зависают на белом экране при запуске
- Отключена автоматическая блокирующая инициализация логката
- Логкат запускается через 2 секунды после старта

---

## 7.8.1 — 16.10.2026 (versionCode 179)

### 🐛 Исправлены вылеты приложений

- **Белый экран** при запуске собранного приложения — исправлено
- Ошибка при использовании **блоков Таймер** — исправлено
- Вылет при работе с **батареей** на Android 14+ — исправлено
- Вылет при работе с **сетью** на Android 12+ — исправлено
- Вылет при показе **уведомлений** — исправлено
- Вылет при работе с **файлами, SMS, контактами, камерой** — исправлено

### 📋 Разрешения

- Приложения теперь **автоматически запрашивают** нужные разрешения
- **Уведомления** работают на Android 13+

### 📝 Прочее

- Исправлены блоки Java-кода (`menuAddMenuItem`, `EditTextSingleLine`)
- Исправлена работа с **датой** и **JSON**

---

## 7.8.0 — 15.10.2026 (versionCode 178)

### 🧩 180+ новых блоков в 19 файлах

- **SecurityBlocks (27):** SMS, Контакты, Звонки, NFC, Биометрия, Обои, Будильники, Wakelock, Аккаунты, PhoneState, Packages, Wi-Fi
- **UtilityBlocks (32):** Уведомления, Clipboard, Батарея, Система, JSON, Вибрация, Share, Prefs, Base64, MD5/SHA-256, Дата
- **NetworkBlocks (4):** URL host, Network/Wi-Fi/Mobile state
- **BitmapBlocks (8):** FromFile, Resize, Rotate, Crop, SaveToFile, GetWidth, GetHeight, ToGray
- **FileBlocks (15):** ReadText, WriteText, AppendText, Delete, Exists, Size, ListDir, CopyTo, MoveTo, Mkdirs, GetName, GetExtension
- **IntentBlocks (12):** OpenApp, OpenSettings, ShareFile, SendSms, PickImage, PickFile, TakePicture, InstallApk, UninstallApp
- **StringBlocks (12):** Upper, Lower, Trim, Replace, Split, Length, Contains, StartsWith, EndsWith, IndexOf, Reverse
- **ListBlocks (6):** Size, IsEmpty, Contains, IndexOf, Sort, Reverse
- **MathBlocks (8):** Clamp, Lerp, Distance2D, DegToRad, RadToDeg, Sign, Log2, RandomFloat
- **ColorBlocks (5):** FromRgb, FromArgb, Red, Green, Blue
- **SystemInfoBlocks (8):** SDK, AppVersion, PackageName, AppName, Timezone, Locale, IsTablet
- **CryptoBlocks (5):** Sha1, Sha512, RandomUuid, RandomInt, RandomString
- **DateTimeBlocks (6):** Year, Month, Day, Hour, Minute, Second
- **SqliteBlocks (5):** Open, Exec, Query, GetCount, Close
- **UiUtilsBlocks (5):** DpToPx, PxToDp, GetStatusBarHeight, GetScreenDensity, GetScreenDpi
- **LocaleBlocks (4):** FormatNumber, FormatCurrency, FormatPercent, SetLocale
- **NotificationExtBlocks (4):** ChannelCreate, AreNotificationsEnabled, ShowWithChannel, OpenSettings
- **BatteryBlocks (3):** Temperature, Voltage, Health
- **RegexBlocks (8):** Matches, Find, FindAll, ReplaceAll, ReplaceFirst, Split, Group, IsValid

### 🔒 Полная автоматизация разрешений Android (4 уровня)

- **38 разрешений** в справочнике (title + tag + description), локализация на 5 языков
- **Уровень 1:** Компонент → разрешение
- **Уровень 2:** Блок → разрешение
- **Уровень 3:** Манифест (`<uses-permission>`)
- **Уровень 4:** Runtime-запрос (`requestPermissions`)

### 💬 UX

- Диалог при создании компонента: «📋 Будет добавлено разрешение: 🔒 Камера [Опасное]» + описание
- Значок ⚠ для блоков с отсутствующими компонентами
- Настройка `block-dependency-hints` (вкл. по умолчанию)

### 🔄 Апдейтер — фикс

- Автопроверка при каждом запуске
- Убрана ложная проверка `isDownloadUrlAvailable`
- Убраны настройки периода из ConfigActivity
- Ручные проверки: ConfigActivity + ProgramInfoActivity

---

## 7.7.0 — 15.10.2026 (versionCode 177)

### 📝 SORA-редактор

- Свернуть блоки (fold) + подсветка парных скобок
- Форматирование кода
- Поиск и замена в файле и по всему проекту
- Go to Symbol (класс/метод)
- Экспорт файла через SAF
- Предпросмотр layout-XML

### 🌍 Мультиязычность

- Changelog на языке пользователя (multilingual с fallback)
- Мастер языка — экспорт мастер-файла для переводчика
- `i18n_check.py` — проверка полноты локализации
- Исправлены пропущенные ключи в zh/hi/uk
- Удалены дубликаты `common_word_*`

### 🛠 Инфраструктура

- `check_links.py` — проверка ссылок
- `generate_readme.py` — проверка синхронности README

### 🔄 Апдейтер

- `skipVersion` в памяти (A1) — «Позже» скрывает до перезапуска

---

## 7.6.1 — 14.10.2026 (versionCode 176)

- Фикс кнопки «Проверить обновления»

---

## 7.6.0 — 14.10.2026 (versionCode 176)

### 🛠 Инструменты

- Экспорт проекта в `.swb`
- Импорт `.swb` с превью
- Экран резервных копий
- Восстановление из бэкапа
- Форма отправки запроса на SkwProME@gmail.com

### ⚡ Дизайнер

- Тумблер «Предпросмотр как основная кнопка»
- Кнопка «Собрать APK»
- Предпросмотр layout

### 🐛 Фиксы

- FilePicker через скрытый фрагмент
- Защита `getDip` от `density=0` (Android 16)
- MaterialColors в фоне → DebugLogger

---

## 7.5.0 — 12.10.2026 (versionCode 175)

### 📊 Граф проекта

- Визуальный граф (Canvas + зум + фильтры)
- 5 раскладок: tree, horizontal, radial, grid, force-directed
- Переменные, компоненты, функции
- Переход к редактору события
- Поиск, фильтр по Activity
- Экспорт PNG

### 📝 SORA-редактор

- Редактор с вкладками
- Java/Kotlin/XML подсветка через TextMate
- Кнопка «Текстовый редактор SORA» в главном меню

### 👤 Мой код

- Редактор пользовательских Java-классов

### 🛠 Инструменты

- Перенос SORA и Графа в Инструменты
- Журнал отладки

---

## 7.4.0 — 11.10.2026 (versionCode 174)

### 🌍 Мультиязычность — 5 языков

- 🇷🇺 Русский, 🇬🇧 English, 🇨🇳 中文, 🇮🇳 हिन्दी, 🇺🇦 Українська
- Fallback английского

### 📝 Вынос строк

- 443 строки в uk/zh/hi
- 117 строк из 60 layout-файлов
- 19 строк из 13 menu-файлов
- Многострочные конкатенации, тернарники

### 🛠 Инфраструктура

- `add_string.py` — скрипт добавления строк

---

## 7.3.1 — 10.10.2026 (versionCode 155)

### 🎨 Дизайнер

- Жесты в палитре (одинарный, двойной, долгий тап)
- Двойной тап на вкладках палитры
- Двойной тап на заголовке категории
- Секция «Жесты в палитре» в настройках
- Быстрый вызов настроек из Drawer

---

## 7.3.0 — 09.10.2026 (versionCode 154)

### 📚 Встроенная справка

- 217 якорей из structure.json
- Кнопка «?» в логике, дизайнере, свойствах
- Онлайн-справка с FAB
- Пункт «Справка» в Drawer

### 📤 Экспорт `.skproj`

- Компоненты, MoreBlocks, переменные, ресурсы
- FileProvider
- Пункты в меню проекта

### 💾 AutoBackup

- AutoBackupManager — фоновый автобэкап

### 🛠 Инструменты

- Экран «Инструменты» в Drawer

### 🎨 Дизайнер

- Строка поиска виджетов
- Контекстное меню виджета
- Защита от NPE
- Умный диалог сохранения

---

## 7.2.1 — 07.10.2026 (versionCode 153)

### 🔍 Проверка логики блоков

- Проверка всего экрана
- Проверка всего проекта перед сборкой
- Тумблер «Проверка логики перед сборкой»
- Кнопка «Проверить логику» рядом с поиском
- Подсветка проблемных блоков

### 📋 Правила

- Пустой параметр
- forever без break
- Несуществующий компонент
- Пустое/тривиальное условие
- Недостижимые блоки

---

## 7.2.0 — 06.10.2026 (versionCode 152)

### 🎨 Конструктор

- «Сборки» → «Конструктор»
- Готовые + Мои наборы

### 🛠 Управление

- Двойной тап → сохранение как сборка
- Экспорт/импорт конструктора
- BlockChainSerializer

---

## 7.1.1 — 05.10.2026 (versionCode 151)

### 🔧 Фиксы

- Удалены ссылки на удалённые ресурсы
- Восстановлена закрывающая скобка
- Удалён AboutActivity

### 🔄 UpdateChecker

- Фикс проверки при запуске
- jsDelivr fallback
- minVersion enforce
- DebugLogger
- default period = always

### 🎨 Иконки

- 200+ иконок в 12 категориях
- Показ screenIcon

---

## 7.1.0 — 21.09.2026 (versionCode 150)

### 📦 Сборки блоков

- Новая категория «Сборки»
- 10 готовых шаблонов

### 🤖 SmartDrop

- Автопредложение создать/подключить компонент

### 🖼 Шаблоны экранов

- 200 макетов в 20 категориях

### 🎨 5 тем

- Фиолетовая тёмная, AMOLED, Синяя, Зелёная, Золотая

### 🔍 Улучшения редактора

- Поиск блоков по всем палитрам
- Избранные блоки
- Умный поиск
- Сохранение скролла

---

## 📊 Общая статистика

- **12 релизов** за ~24 дня
- **180+ блоков** (только в 7.8.0)
- **38 разрешений** Android
- **5 языков** интерфейса
- **200+ иконок**
- **200 шаблонов экранов**
- **51+ сборка** блоков
- **10 проверок логики**
- **2 редактора** (SORA + Мой код)
- **Граф проекта** с 5 раскладками
- **Экспорт** `.skproj`, `.swb`, резервные копии
- **Автобэкап**
- **Встроенная справка** (217 якорей)
- **5 цветных тем**

---

**Автор форка:** RasNikGal
**Лицензия:** свободная, не публиковать в Google Play, указывать авторство
**Для 4PDA:** автор RasNikGal
