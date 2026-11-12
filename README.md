# UI-test-selenide-jenkins

Автотесты UI интернет-магазина пиццерии **pizzeria.skillbox.cc** на Java.
Проект построен на **Selenide + TestNG**, отчётность — **Allure**,
запуск тестов автоматизирован в **Jenkins**, который поднимается в **Docker**.

## Стек

| Технология | Версия | Назначение |
|---|---|---|
| Java | 17 | Язык разработки |
| Maven | 3.9+ | Система сборки |
| Selenide | 7.8.1 | Обёртка над Selenium, ожидания |
| Selenium Edge Driver | 4.31.0 | Драйвер Microsoft Edge |
| TestNG | 7.11.0 | Тест-фреймворк, листенеры |
| AssertJ | 3.27.3 | Проверки, `SoftAssertions` |
| Awaitility | 4.3.0 | Ожидания |
| Lombok | 1.18.36 | Геттеры, `@UtilityClass` |
| Logback | 1.5.18 | Логирование |
| Allure | 2.29.1 (плагин 2.12.0) | Отчёты, шаги, скриншоты |
| AspectJ | 1.9.23 | Интеграция Allure с TestNG |
| Jenkins LTS + Docker Compose | — | CI-сервер и его запуск |

## Структура проекта

```
.
├── src
│   ├── main/java
│   │   ├── formData/           # Данные формы оформления заказа
│   │   ├── pages/              # Page Object'ы
│   │   │   ├── BasePage.java
│   │   │   ├── account/
│   │   │   ├── auth/
│   │   │   ├── bonus/
│   │   │   ├── cart/
│   │   │   ├── checkout/
│   │   │   ├── delivery/
│   │   │   ├── main/
│   │   │   ├── menu/
│   │   │   └── promo/
│   │   └── utils/              # Constants, CustomAwait, DateUtils, StringGenerator
│   └── test
│       ├── java
│       │   ├── listeners/UIListener.java   # Настройка Selenide и Allure
│       │   ├── ui/                         # Тестовые классы
│       │   └── utils/                      # Тестовые данные и сообщения
│       └── resources
│           └── allure.properties
├── docker-compose.yaml         # Jenkins в Docker
├── jenkins.groovy              # Declarative pipeline
└── pom.xml
```

## Что покрыто тестами

- **`MainPageTest`** — переключение слайдера пицц клавишами и кнопками, появление ссылки
  «В корзину» при наведении, переход на страницу десертов, стрелка «наверх» при прокрутке,
  открытие соцсетей в новой вкладке.
- **`MenuPageTest`** — сортировка пицц, фильтрация по цене, добавление пиццы в корзину.
- **`CartPageTest`** — изменение количества товара, обновление корзины, переход к оформлению
  для авторизованного пользователя, применение промокода из раздела «Акции».
- **`RegisterPageTest`** — успешная регистрация, повторная регистрация существующего
  пользователя, регистрация без пароля.
- **`AccountPageTest`** — смена изображения профиля, проверка добавленного заказа
  в истории заказов.
- **`BonusPageTest`** — создание бонусной карты.
- **`CheckoutFillingDataPageTest`** — время заказа, оформление с оплатой наличными.
- **`DeliveryPageTest`** — минимальная сумма доставки.
- **`HeaderTest`** — параметризованная проверка категорий меню.

## Требования

- JDK 17
- Maven 3.9+
- Microsoft Edge или Google Chrome
- Docker Desktop — для запуска Jenkins

## Настройка

Параметры читаются в `src/test/java/listeners/UIListener.java` из системных свойств:

| Свойство | По умолчанию | Описание |
|---|---|---|
| `base_url` | `https://pizzeria.skillbox.cc` | Базовый URL приложения |
| `browser` | `edge` | Браузер: `edge` или `chrome` |
| `execution` | — | Если `jenkins`, браузер запускается в headless-режиме |
| `allure_screenshots` | `true` | Прикреплять скриншоты к Allure-отчёту |
| `allure_page_sources` | `true` | Прикреплять page source к Allure-отчёту |

Базовый URL также задан в `src/test/java/utils/TestConstants.java`.

## Запуск тестов локально

```bash
mvn clean test
```

Примеры:

```bash
mvn clean test -Dbrowser=chrome
mvn clean test -Dexecution=jenkins -Dbrowser=chrome
mvn clean test -Dbase_url=https://pizzeria.skillbox.cc
```

> В headless-режиме с флагами `--no-sandbox`, `--disable-dev-shm-usage`, `--disable-gpu`
> тесты запускаются при `-Dexecution=jenkins`.

## Jenkins в Docker

Запуск Jenkins:

```bash
docker compose up -d
```

- Jenkins доступен по адресу `http://localhost:18080`
- Данные сохраняются в `./jenkins_data` (каталог исключён из Git)
- Порт агента: `50000`

В Jenkins создаётся pipeline-задача, использующая `jenkins.groovy`. Пайплайн:

1. Клонирует репозиторий (`REPOSITORY`, ветка из параметра `BRANCH`).
2. Запускает `mvn clean test` с параметрами `execution`, `base_url`, `browser`,
   `allure_screenshots`, `allure_page_sources`.
3. Публикует Allure-отчёт.

Параметры сборки:

| Параметр | Значение по умолчанию | Описание |
|---|---|---|
| `BRANCH` | `master` | Ветка для сборки |
| `BROWSER` | `edge` | Браузер для тестов (`edge` / `chrome`) |

Для работы пайплайна в Jenkins должны быть настроены Maven (`maven-jenkins`)
и плагин Allure.

## Отчёты

- **Allure**: результаты пишутся в `target/allure-results`
- **Jenkins**: отчёт публикуется шагом `allure` из `jenkins.groovy`

Локальный просмотр Allure-отчёта:

```bash
mvn allure:report
mvn allure:serve
```

## Особенности реализации

- **Page Object Model**: каждый экран приложения — отдельный класс в `pages/`.
- **`UIListener`** настраивает Selenide (`baseUrl`, браузер, размер окна, headless),
  подключает `AllureSelenide` и очищает cookies/local storage после каждого теста.
- **CI-конфигурация**: для Jenkins включается headless-режим и параметры Chrome/Edge,
  необходимые для работы в контейнере.
- **Мягкие проверки**: используется `SoftAssertions`, поэтому в одном тесте проверяются
  все значения, а не только первое упавшее.
