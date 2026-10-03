# Исправления и улучшения в форке RasNikGal

Этот форк содержит **полную русскую локализацию** интерфейса Sketchware Pro, а также ряд исправлений багов из официального репозитория и дополнительные улучшения.

Все изменения собраны ниже, сгруппированы по категориям.

---

## 🐛 Исправленные баги (из официального Sketchware Pro)

### [#1812](https://github.com/Sketchware-Pro/Sketchware-Pro/issues/1812) — ConcurrentModificationException при выходе из пустой секции компонентов
**Проблема:** при выходе из пустой секции Events/Component приложение зависало, затем падало с `ConcurrentModificationException` в `a.a.a.eC.k()`.
**Решение:** `UnsavedChangesSaver` запускается только при наличии реальных несохранённых данных (через проверку `jC.*`).
**Файлы:** `com/besome/sketch/design/DesignActivity.java`
**PR:** [#10](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/10)

### [#1716](https://github.com/Sketchware-Pro/Sketchware-Pro/issues/1716) — Кнопка Run перестаёт работать после первой сборки
**Проблема:** после успешной сборки кнопка «Запустить» перестаёт реагировать на нажатия.
**Решение:** 
- `cancelBuild()` больше не выключает кнопку полностью, а показывает «Отмена...».
- `onPostExecute()` сбрасывает `currentBuildTask` только если это актуальный таск (race guard).
- Защита от двойного тапа: пока предыдущая сборка не завершилась, новый таск не стартует.
**Файлы:** `com/besome/sketch/design/DesignActivity.java`
**PR:** [#11](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/11)

### [#1929](https://github.com/Sketchware-Pro/Sketchware-Pro/issues/1929) — Ошибка R8 при использовании библиотек с большим количеством зависимостей
**Проблема:** R8/D8 падает с «Missing class» при использовании библиотек с транзитивными зависимостями (Room, Firebase, RuStore).
**Решение:** `getJarLocalLibrary()` и `getDexLocalLibrary()` теперь дополнительно сканируют `.sketchware/libs/local_libs/` и подключают **все** `classes.jar` / `classes.dex`, скачанные резолвером зависимостей, даже если они не зарегистрированы в `local_library` проекта.
**Файлы:** `mod/agus/jcoderz/editor/manage/library/locallibrary/ManageLocalLibrary.java`
**PR:** [#11](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/11)

### [#1971](https://github.com/Sketchware-Pro/Sketchware-Pro/issues/1971) — Проблемы с загрузкой библиотек и кастомные репозитории
**Проблема (часть 1):** библиотеки, требующие `<provider>` или `<meta-data>` в манифесте (RuStore Pay SDK, Firebase), не работали, так как их манифесты не сливались.
**Решение:** добавлен класс `LocalLibraryManifestMerger`, который парсит манифест каждой локальной библиотеки и добавляет недостающие `<uses-permission>`, `<provider>`, `<meta-data>`, `<activity>`, `<service>`, `<receiver>` в итоговый `AndroidManifest.xml`. Дедупликация по `android:name`.
**Файлы:** `mod/jbk/build/LocalLibraryManifestMerger.java` (новый), `a/a/a/Ix.java`, `mod/agus/jcoderz/editor/manage/library/locallibrary/ManageLocalLibrary.java`
**PR:** [#13](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/13)

### [#2035](https://github.com/Sketchware-Pro/Sketchware-Pro/issues/2035) — Google Play отклоняет AAB из-за слабого сертификата
**Проблема:** Play Console отклоняет загруженные AAB с ошибкой «upload certificate with a key that is too weak». Причина — подпись `SHA1withRSA`.
**Решение:** заменено на `SHA256withRSA` во всех местах: `KeySet`, `ZipSigner.signZip`, `CertCreator`.
**Файлы:** `kellinwood/security/zipsigner/KeySet.java`, `ZipSigner.java`, `optional/CertCreator.java`
**PR:** [#11](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/11)

### [#1917](https://github.com/Sketchware-Pro/Sketchware-Pro/issues/1917) — ViewBinding конфликтует с layout `notification`
**Проблема:** если layout называется как класс Android (например, `notification`), ViewBinding генерирует `NotificationBinding`, конфликтующий с `android.app.Notification`, — сборка падает.
**Решение:** список зарезервированных имён расширен (`notification`, `activity`, `fragment`, `item`, `menu`, `dialog`, `android`, `content`, `toolbar`, `layout`). Теперь валидатор покажет ошибку прямо под полем, не давая создать проблемный layout.
**Файлы:** `com/besome/sketch/editor/manage/view/AddViewActivity.java`
**PR:** [#15](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/new/fix-1917-viewbinding-warning)

### Генератор кода — обработка типов в `Fx.java`
**Проблема (часть 1):** `type == 0` (boolean) не конвертировал числовые значения — если в boolean-слот попадал числовой блок, генерировался невалидный Java-код (`int cannot be converted to boolean`).
**Проблема (часть 2):** в четырёх местах `paramsTypes.get(i)` мог выбросить `IndexOutOfBoundsException`, если `spec` и `parameters` были рассинхронизированы.
**Решение:** 
- Числовые значения в boolean-слотах теперь оборачиваются как `(value != 0)`.
- `paramsTypes` запрашивается безопасно с fallback на `%s`.
**Файлы:** `a/a/a/Fx.java`
**PR:** [#16](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/new/fix-fx-type-handling)

---

## ✨ Новые возможности

### Поиск блоков по всем палитрам
**Что добавлено:** в логическом редакторе появилось поле «Поиск блока» над палитрой. Работает мгновенно, ищет **сразу по всем категориям** блоков (Переменные, Списки, Управление, Операторы, Математика, Файлы, Элементы, Компоненты, XML-строки и др.).
**Особенности:**
- **Debounce 350 мс** — поиск запускается после паузы в наборе, не загружая процессор.
- **Компактное поле** — стилизовано как поле поиска палитр справа.
- **Очистка** возвращает палитру к выбранной категории.
**Файлы:** `com/besome/sketch/editor/logic/PaletteBlock.java`, `com/besome/sketch/editor/LogicEditorActivity.java`, `dev/aldi/sayuti/block/ExtraPaletteBlock.java`, `res/layout/logic_editor.xml`
**PR:** [#12](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/12)

---

## 🇷🇺 Локализация

### Полный перевод интерфейса
- Все экраны: Дизайнер, Редактор логики, Редактор кода.
- Менеджеры: блоки, компоненты, события, библиотеки, ресурсы.
- Диалоги: создание, редактирование, настройки.
- Системные сообщения: ошибки компиляции, уведомления (Toast, Snackbar), предупреждения.
- Категории и подразделы блоков.

### Перевод сообщений при сборке APK
**Что сделано:** все сообщения о ходе сборки теперь на русском:
- «Генерация исходного кода...»
- «Распаковка встроенных библиотек...»
- «Генерация ViewBinding...»
- «Компиляция Java...»
- «Объединение DEX-файлов...»
- «Сборка APK...»
- «Подпись APK...»
- «Отмена сборки...»
- и другие.
**Файлы:** `com/besome/sketch/design/DesignActivity.java`, `mod/hey/studios/compiler/kotlin/KotlinCompilerBridge.java`, `mod/hey/studios/project/proguard/ProguardHandler.java`, `mod/hey/studios/project/stringfog/StringfogHandler.java`, `mod/jbk/build/BuiltInLibraries.java`
**PR:** [#14](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/14)

---

## 📋 Сводная таблица всех коммитов

| Дата | Фикс | PR |
|---|---|---|
| 2026-10-02 | #1812 — ConcurrentModificationException в пустой секции | [#10](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/10) |
| 2026-10-02 | #1716 — Run-кнопка залипает | [#11](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/11) |
| 2026-10-02 | #2035 — SHA256withRSA | [#11](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/11) |
| 2026-10-02 | #1929 — Transitive-зависимости библиотек | [#11](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/11) |
| 2026-10-02 | #1971 — Поиск блоков + manifest merger | [#12](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/12), [#13](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/13) |
| 2026-10-02 | #1917 — ViewBinding warning | [#15](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/new/fix-1917-viewbinding-warning) |
| 2026-10-02 | Перевод сообщений сборки | [#14](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/14) |
| 2026-10-02 | Fx.java — обработка типов | [#16](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/new/fix-fx-type-handling) |

---

## 🙏 Благодарности

- **Команде Sketchware Pro** за оригинальный проект и открытый исходный код.
- **Сообществу 4PDA** за обратную связь и тестирование локализации.

**Автор форка и локализации:** RasNikGal

---

## 📅 Обновление от 2026-10-03

Добавлены новые фичи для удобства работы:

### #1 — Сохранение позиции скролла в палитре блоков
**Что:** при переключении между категориями палитры позиция скролла сохраняется отдельно для каждой. При возврате — восстанавливается.
**Файлы:** `PaletteBlock.java`, `LogicEditorActivity.java`
**PR:** [#19](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/19)

### #4 — Экспорт и импорт настроек приложения
**Что:** в Настройках появились пункты:
- **Экспорт настроек** — сохраняет `.sketchware/data/settings.json` в выбранную папку с именем `settings_backup_YYYY-MM-DD_HHmmss.json`.
- **Импорт настроек** — загружает настройки из JSON-файла и предлагает перезапустить приложение.

**Полезно:** при переустановке APK (из-за смены подписи в #2035) настройки больше не теряются.
**Файлы:** `ConfigActivity.java`, `preferences_config_activity.xml`
**PR:** [#19](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/19)

### #6 — Избранные блоки
**Что:** новая категория **«Избранное»** в самом верху списка палитр (с золотой полоской).
**Особенности:**
- Предзаполненный набор популярных блоков: `customToast`, `dialogShow`, `dialogDismiss`, `setVarBoolean`, `setVarInt`, `setVarString`, `mathPi`, `mathRandom`, `getResString`, `fileutilread`, `fileutilwrite`.
- **Иконка звёздочки ☆** рядом с каждым блоком в палитре — тап добавляет/убирает из избранного.
- Заполненная звёздочка ★ — блок уже в избранном.

**Файлы:** `FavoriteBlocksManager.java` (новый), `PaletteSelector.java`, `ExtraPaletteBlock.java`, `PaletteBlock.java`
**PR:** [#19](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/19)

### #7 — Умный поиск блоков
**Что:** поиск теперь нормализует запросы, игнорируя пробелы, дефисы и подчёркивания.
- `dialog show` → находит `dialogShow`.
- `math pi` → находит `mathPi`.
- `get res string` → находит `getResString`.

**Файлы:** `PaletteBlock.java`
**PR:** [#19](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/19)

### #1971 (доп.) — Чекбокс «Заголовки при поиске блоков»
**Что:** новая настройка в Preferences. При поиске заголовки подразделов (Boolean, Number, String и т.д.) скрываются, если под ними нет найденных блоков. Можно отключить — тогда заголовки скрываются полностью.
**Файлы:** `PaletteBlock.java`, `ConfigActivity.java`, `preferences_config_activity.xml`
**PR:** [#18](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/18)

---

## 📅 Обновление от 2026-10-03 (часть 2)

### 🎨 Цветные темы (5 новых)
**Что:** в Настройках → Внешний вид появилась секция **«Цветовая тема»** с 5 вариантами:
- **Фиолетовая тёмная** — based on reference screenshot
- **Чёрная (AMOLED)** — для OLED-экранов
- **Синяя**
- **Зелёная**
- **Золотая**

**Как работает:**
- При выборе темы — **тумблер «Следовать системной»** выключается.
- **Перезапуск** приложения → применяется новая тема.
- При включении System → **все цветные темы отключаются**, Light/Dark активны.
- При выборе Light/Dark → **автоматически отключается System**, цветные не выбраны.

**Что визуально меняется:**
- Основной акцент (кнопки, FAB, ползунки).
- Фон, поверхности (карточки, тулбары).
- Текст, иконки.
- Все цвета соответствуют **Material3** (правильные контрасты, читаемость).

**Файлы:**
- `m3_colors_themes.xml` (новый) — 5 палитр по ~35 цветов.
- `m3_schemes.xml` — 5 новых схем.
- `themes.xml` — 5 обёрток + 5 платформенных цепочек.
- `ThemeManager.java` — константы + `applyCustomTheme()`.
- `BaseAppCompatActivity.java` — вызов `applyCustomTheme()` в `onCreate`.
- `SettingsAppearanceFragment.java` — UI выбора.
- `item_color_theme.xml`, `theme_color_circle.xml` (новые).

**PR:** [#21](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/21)

### 🧠 Block Logic Checker — проверка логики блоков
**Что:** при **добавлении/перемещении/удалении** блока в редакторе логики автоматически проверяется **список блоков текущего события** на 8 типов проблем.

**Правила:**
1. **Дубликаты** `dialogSetTitle` × 2+.
2. **Дубликаты** `dialogSetMessage` × 2+.
3. **Дубликаты** `dialogShow` × 2+.
4. **Дубликаты** `dialogDismiss` × 2+.
5. `dialogSetTitle` без `dialogShow`.
6. `dialogSetMessage` без `dialogShow`.
7. `dialogDismiss` без `dialogShow`.
8. `dialogDismiss` **до** `dialogShow`.

**Диалог:**
> **Проверка логики блоков**
> В событии «onBackPressed» обнаружены проблемы:
> • dialogSetTitle × 2
>
> Удалить лишние блоки?
> **[Удалить] [Оставить]**

**Кнопка «Удалить»:**
- Удаляет **все дубликаты**, оставляя **первый**.
- Для правил 5-7 — **ничего не удаляет** (проблема информационная).

**Журнал:**
- Файл: `/sdcard/.sketchware/block_logic_journal.json`.
- Формат: `[{time, sc_id, event, issues}]`.
- Максимум 200 записей.

**Тумблер:**
- **Настройки → Настройки приложения → Проверка логики блоков** (включён по умолчанию).

**Технические особенности:**
- Throttle 500 мс — не спамит.
- Дедупликация по hash — не показывает диалог повторно.
- Все исключения подавлены — не ломает редактор.

**Файлы:**
- `BlockLogicChecker.java` (новый, 145 строк) — правила.
- `BlockLogicJournal.java` (новый, 56 строк) — журнал.
- `LogicEditorActivity.java` — хук в `onTouch` (после drop + tap).
- `ConfigActivity.java`, `preferences_config_activity.xml` — тумблер.

**PR:** [#22](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/22), [#23](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/23)

### 🎯 Per-project последняя палитра + скролл
**Что:** каждый проект запоминает **свой последний открытый раздел палитры** (Математика, Компоненты и т.д.) и **позицию скролла** внутри него.

**Пример:**
- В проекте А — раздел «Математика», скролл до `mathPi`.
- В проекте Б — раздел «Компоненты», скролл до `dialogShow`.
- Вернулся в А → открывается «Математика» на том же месте.
- Вернулся в Б → «Компоненты» на том же месте.

**Где хранится:**
- `SharedPreferences` `palette_state_per_project`.
- Ключи: `<sc_id>_last_palette_id`, `<sc_id>_palette_<id>`.

**Файлы:**
- `PaletteBlock.java` — `setScId`, `keyPrefix`, сохранение/восстановление скролла.
- `LogicEditorActivity.java` — сохранение/восстановление последнего раздела.

**PR:** [#22](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/22)

### 🔄 Синхронизация тумблера темы и цветных тем
**Что:** логика взаимодействия **System ↔ Light/Dark ↔ Color themes**:

| Состояние | Light/Dark | Цветные темы |
|---|---|---|
| **System ON** | активны | серые |
| **System OFF** | активны | активны |
| **Выбрал Light/Dark** | галка | активны, без галки |
| **Выбрал цветную** | активны, без галки | галка на выбранной |

**Поведение:**
- Тап на **Light/Dark** → System **выключается**.
- Тап на **цветную** → System **выключается**.
- Включение **System** → все галки снимаются, цветные отключаются.

**Файлы:** `SettingsAppearanceFragment.java`
**PR:** [#22](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/22)

### 💾 Диалоги сохранения/восстановления
**Что исправлено:**
1. **Диалог «Восстановить»** больше **не появляется** при каждом открытии проекта (была проблема с jar-флагами).
2. **Кнопка Back** теперь **всегда спрашивает** «Сохранить и выйти?» — раньше диалог не показывался из-за неверного флага `t.c("P12I2")`.

**Файлы:** `DesignActivity.java`
**PR:** [#22](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/22)

---

## 📋 Сводная таблица всех фич и фиксов

| # | Фикс/Фича | PR |
|---|---|---|
| **1812** | ConcurrentModificationException | [#10](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/10) |
| **1716** | Run-кнопка залипает | [#11](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/11) |
| **1929** | Транзитивные зависимости | [#11](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/11) |
| **2035** | SHA256withRSA | [#11](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/11) |
| **1971** (search) | Поиск блоков | [#12](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/12) |
| **1971** (manifest) | Manifest merger | [#13](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/13) |
| Переводы | Сообщения сборки | [#14](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/14) |
| **1917** | ViewBinding warning | [#15](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/15) |
| **Fx.java** | Типы + IndexOutOfBounds | [#16](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/16) |
| FIXES.md | Документация | [#17](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/17) |
| **1971** (headers) | Заголовки при поиске | [#18](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/18) |
| Usability pack | Скролл, экспорт, избранное, поиск | [#19](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/19) |
| FIXES.md update | Документация | [#20](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/20) |
| **Color themes** | 5 новых тем | [#21](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/21) |
| **Save/Restore + theme sync + palette + logic** | Много | [#22](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/22) |
| **Delete duplicates** | Block logic checker | [#23](https://github.com/rasnikgal-pixel/Sketchware-Pro/pull/23) |
