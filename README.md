# TeamCity UI & API Autotests Framework

[![CI Build Status](https://github.com/vellgordeev/teamcity-autotests/actions/workflows/main.yml/badge.svg)](https://github.com/vellgordeev/teamcity-autotests/actions/workflows/main.yml)

## Overview

This project demonstrates a framework for automated UI and API testing of the TeamCity CI/CD server. It aims to showcase skills in building robust test automation solutions using modern Java technologies, including integration with CI/CD pipelines and comprehensive reporting.

**Key Features:**
*   🖥️ UI tests using Selenide and Page Object/Elements pattern.
*   ⚙️ API tests using RestAssured with a custom extensible request architecture (Checked/Unchecked layers).
*   🧪 Advanced reflection-based test data generation with support for dependencies and randomization.
*   ♻️ Automatic Test Data lifecycle management (creation & cleanup) within test execution.
*   🤖 CI/CD integration with GitHub Actions.
*   📊 Detailed Allure reports automatically published to GitHub Pages, including Selenide steps and API request/responses.
*   📈 API test coverage reporting integrated with Swagger/OpenAPI specification.

## ✨ Highlights & Key Solutions

*   **Robust Multi-Layer API Test Architecture:** Separation into `Unchecked` (raw RestAssured responses for flexibility) and `Checked` (automatic validation and model parsing for convenience) request layers. Provides both fine-grained control and ease of use.
*   **Universal Reflection-Based Data Generator:** A single `TestDataGenerator` class capable of creating complex object graphs for any `BaseModel` subclass, handling dependencies, randomization (`@Random`), and explicit parameter overrides. *Significantly reduces boilerplate code* for test data setup.
*   **Automated Test Data Lifecycle:** Integration between the data generator, `Checked` API requests, and `BaseTest` ensures that all entities created during a test are automatically tracked and cleaned up afterwards using TestNG's `@AfterMethod`. *Guarantees test isolation and clean environment*.
*   **Global Test Suite Setup/Teardown:** `BaseApiTest` utilizes `@BeforeSuite`/`@AfterSuite` to manage global server state (e.g., authentication settings), ensuring a *consistent environment* for the entire test suite run and restoring original settings afterwards.
*   **Combined API & UI for Efficiency:** UI tests leverage API calls (via `CheckedRequests`) for *faster and more reliable state preparation* (e.g., user creation before login in `BaseUiTest.loginAs`).
*   **Swagger API Coverage:** Automated verification of API test coverage against the OpenAPI specification using `swagger-coverage-rest-assured`. *Ensures tests align with API contracts*.
*   **Comprehensive Reporting & CI/CD:** Seamless integration with Allure Report (including Selenide listener for UI steps) and GitHub Actions for automated execution, reporting, and deployment of reports to GitHub Pages.

## 🚀 Live Demo & Reports

*   **CI Pipeline Runs:** [GitHub Actions](https://github.com/vellgordeev/teamcity-autotests/actions) (Select a recent successful run)
*   🖥️ **Allure UI (Web) Test Report:** [Latest UI Report](https://vellgordeev.github.io/teamcity-autotests/web/latest/) (Link might need adjustment based on actual GitHub Pages structure)
*   ⚙️ **Allure API Test Report:** [Latest API Report](https://vellgordeev.github.io/teamcity-autotests/api/latest/) (Link might need adjustment based on actual GitHub Pages structure)
*   📊 **Swagger API Coverage Report:** Generated during CI run and available as a build artifact named `swagger-coverage` in the run summary.

*(Note: Allure reports represent snapshots from specific runs. Using "latest" or specific run number links depends on your CI deployment strategy. Swagger coverage report needs to be downloaded from the artifacts of a specific API test run.)*

## Table of Contents

*   [🛠️ Tech Stack](#tech-stack)
*   [🌟 Features](#features)
    *   [🖥️ UI Tests (Selenide)](#ui-tests-selenide)
    *   [⚙️ API Tests (RestAssured)](#api-tests-restassured)
    *   [🧱 Detailed API Test Architecture](#detailed-api-test-architecture)
    *   [🧪 Universal Test Data Generation](#universal-test-data-generation)
    *   [♻️ Test Data Lifecycle Management](#test-data-lifecycle-management)
    *   [📊 Reporting (Allure & Swagger Coverage)](#reporting-allure--swagger-coverage)
    *   [🔧 Configuration](#configuration)
*   [🏗️ Project Structure](#project-structure)
*   [🚀 Setup & Running Tests](#setup--running-tests)
    *   [Prerequisites](#prerequisites)
    *   [Cloning](#cloning)
    *   [Configuration (Setup)](#configuration-1)
    *   [Running Tests](#running-tests)
    *   [Viewing Reports Locally](#viewing-reports-locally)
*   [🤖 CI/CD (GitHub Actions)](#cicd-github-actions)

---

## 🛠️ Tech Stack

*   **Language:** Java 21
*   **Build Tool:** Maven
*   **Testing Framework:** TestNG
*   **UI Testing:** Selenide
*   **API Testing:** RestAssured
*   **Reporting:** Allure Report, Swagger Coverage Reporter
*   **Configuration:** Custom `Config` class using `java.util.Properties`
*   **Test Data Generation:** Custom Reflection-based Generator (`TestDataGenerator`), `apache-commons-lang3` for random data.
*   **Assertions:** TestNG Asserts (including `SoftAssert`), RestAssured Hamcrest Matchers.
*   **Logging:** Log4j2
*   **CI/CD:** GitHub Actions
*   **Key Libraries:** Jackson (JSON), Lombok, Awaitility

---

## 🌟 Features

### 🖥️ UI Tests (Selenide)

*   Leverages Selenide for concise and stable browser automation.
*   Follows the Page Object / Page Elements pattern (`src/main/java/.../web/pages` and `elements`) for maintainability and readability.
*   Tests cover key user scenarios (`src/test/java/.../web`).
*   Uses Selenide's built-in mechanisms for waits and interactions.
*   **Efficiency Boost:** Uses API calls via `BaseUiTest.loginAs` for faster test setup (e.g., creating users), making UI tests more focused and reliable.
*   **Rich Reporting:** Integrates seamlessly with Allure via `AllureSelenide` listener for detailed steps, automatic screenshots/page source on failure (`BaseUiTest`).

### ⚙️ API Tests (RestAssured)

*   Covers core TeamCity REST API endpoints (`src/test/java/.../api`).
*   Utilizes RestAssured for a fluent API testing experience.
*   Features a robust, custom-built, extensible architecture (see details below).
*   Employs TestNG `SoftAssert` (via `BaseTest`) for comprehensive error reporting without premature test termination – see all failures at once!

#### 🧱 Detailed API Test Architecture

A multi-layered architecture (primarily within `src/main/java/.../api`) ensures flexibility, extensibility, and maintainability:

1.  **Request Layers Separation (`Checked` vs `Unchecked`):**
    *   **`UncheckedRequests` / `UncheckedBase`:** Low-level layer. Executes requests using a `RequestSpecification` (from `Specifications` class) and returns the raw `io.restassured.response.Response`. *Ideal for fine-grained control, testing non-200 status codes, or specific header validation.* Resides in `api.requests.crud` and specific non-CRUD packages.
    *   **`CheckedRequests` / `CheckedBase`:** High-level layer built upon `Unchecked`. Executes the request, *automatically validates success* (typically `200 OK`), deserializes the response body into the corresponding Java model (defined in `Endpoint` enum), and returns the model object. *Simplifies writing positive test scenarios and improves readability.* Resides in `api.requests.crud` and specific non-CRUD packages. Also integrates with `TestDataStorage` for cleanup.

2.  **Centralized Request Specification (`Specifications`):**
    *   The `Specifications` class (`api.spec`) acts as a factory for `RequestSpecification` instances.
    *   Handles common setup: base URI, authentication (`superUserAuth`, `userAuth`, `noAuth`), content type, logging filters, Allure integration (`AllureRestAssured`), and the crucial **Swagger Coverage filter** (`SwaggerCoverageRestAssured`). *Ensures consistency across all requests*.

3.  **Endpoint Abstraction (`Endpoint` Enum & Request Handlers):**
    *   An `Endpoint` enum likely defines API endpoints, their paths, expected model classes, and associated handler classes (`Checked`/`Unchecked`).
    *   `CheckedRequests` and `UncheckedRequests` act as factories, using the `Endpoint` enum to provide the correct request handler (`CheckedBase`, `UncheckedBase`, or custom implementations). *Decouples test logic from request implementation details*.
    *   Interfaces like `CrudInterface`, `SearchProjectsInterface` define contracts for common actions.

4.  **Model Classes (`api.models`):**
    *   POJOs representing API request/response bodies, using Jackson annotations. All models extend a common `BaseModel`.

#### 🧪 Universal Test Data Generation

*   **Central Generic Generator (`TestDataGenerator`):** Located in `api.generators`. Uses **Java Reflection** to instantiate and populate any object extending `BaseModel`. *Eliminates repetitive builder/factory code*.
*   **Flexible Field Population (Priority Order):**
    1.  **Explicit Parameters:** Values passed to `generate(...)` override all other rules. *Full control when needed*.
    2.  **`@Random` Annotation:** Fields marked `@Random` (`api.annotations`) get random data (via `RandomData` helper) if no explicit parameter is given. *Easy realistic data*.
    3.  **Recursive Generation & Reuse:** If a field is another `BaseModel` (or `List<BaseModel>`), the generator tries to reuse an existing instance (from `List<BaseModel>` passed to `generate`). If not found, it recursively calls `generate`. *Handles complex object dependencies automatically*.
*   **Benefits:** Maximum reusability, drastically reduced boilerplate, easier maintenance.

#### ♻️ Test Data Lifecycle Management

*   **Central Storage (`TestDataStorage`):** A utility class (likely Singleton/ThreadLocal) tracks entities created during a test.
*   **Automatic Tracking:** `CheckedBase.create()` automatically registers created entities in `TestDataStorage`.
*   **Automatic Cleanup:** `BaseTest` uses TestNG's `@AfterMethod` to call `TestDataStorage.getStorage().deleteCreatedEntities()`, which sends `DELETE` requests for all tracked entities. *Ensures clean state between tests*.
*   **Global State Management (`BaseApiTest`):** `@BeforeSuite`/`@AfterSuite` in `BaseApiTest` manage global server settings, ensuring test suite runs in a known state and restores it afterwards. *Crucial for reliable integration testing*.
*   **Benefits:** Guarantees test isolation, prevents flakiness from leftover data, simplifies test writing (no manual cleanup needed in most tests).

### 📊 Reporting (Allure & Swagger Coverage)

*   **Allure Report:**
    *   Comprehensive integration via `AllureRestAssured` (API) and `AllureSelenide` (UI).
    *   Detailed, interactive HTML reports with steps, timings, environment info, and attachments.
    *   API requests/responses are automatically logged.
    *   Failed Selenide UI tests automatically include screenshots and page source.
    *   Standard Maven tasks (`allure:report`, `allure:serve`) for generation and viewing.
*   **Swagger Coverage:**
    *   Uses `com.github.viclovsky:swagger-coverage-rest-assured` (via `Specifications` class).
    *   Generates a report (`target/swagger-coverage/index.html`) showing API endpoint coverage based on a Swagger/OpenAPI spec.
    *   *Helps identify gaps in API testing* and ensures alignment with the API contract. Requires a spec file configured.

### 🔧 Configuration

*   Managed by a custom Singleton `Config` class (`ru.gordeev.teamcity.api.config.Config`).
*   Loads key-value pairs from `config.properties` (`src/test/resources`).
*   Accessed statically: `Config.getProperty("your.property.key")`.
*   **Security Best Practice:** Avoid committing sensitive data (tokens, passwords) to `config.properties`. Use environment variables or Java System Properties (`-Dproperty=value`) for CI/local overrides.

---

## 🏗️ Project Structure

```
.
├── .github/workflows     # GitHub Actions CI/CD pipeline
├── .gitignore
├── pom.xml               # Maven Project Configuration
├── mvnw                  # Maven wrapper (Linux/macOS)
├── mvnw.cmd              # Maven wrapper (Windows)
├── .mvn/wrapper          # Maven wrapper helpers
├── checkstyle.xml        # Code style rules
├── config/checkstyle     # Supporting Checkstyle files
├── target/               # Build output (classes, reports)
├── allure-results/       # Raw Allure results
└── src
    ├── main              # Source code (Framework)
    │   └── java/ru/gordeev/teamcity
    │       ├── api         # Core API framework
    │       │   ├── annotations
    │       │   ├── config
    │       │   ├── enums
    │       │   ├── generators
    │       │   ├── models
    │       │   ├── requests
    │       │   ├── spec
    │       │   └── utils
    │       └── web         # Core UI framework (Selenide)
    │           ├── elements
    │           └── pages
    └── test              # Test code
        ├── java/ru/gordeev/teamcity
        │   ├── api         # API test classes
        │   ├── web         # UI test classes
        │   ├── BaseApiTest.java
        │   ├── BaseUiTest.java
        │   └── BaseTest.java # Common test base
        └── resources       # Test resources
            ├── config.properties   # Default configuration
            └── log4j2.xml          # Logging setup
```

*(Reflects standard Maven layout combined with the project's specific package organization.)*

## 🚀 Setup & Running Tests

### Prerequisites

*   **Java Development Kit (JDK):** Version 21+
*   **Maven:** Optional (wrapper included), but useful for IDE integration.
*   **Git:** For cloning.
*   **Running TeamCity Instance:** Accessible via network. Configure URL/credentials.
*   **(UI Tests) Web Browser:** Chrome/Firefox installed locally, OR a configured remote environment (e.g., Selenoid).

### Cloning

```bash
git clone https://github.com/vellgordeev/teamcity-autotests.git
cd teamcity-autotests
```

### Running Tests

Use the Maven wrapper (`mvnw` or `mvnw.cmd` on Windows).

*   **Run all tests:**
    ```bash
    ./mvnw clean test
    ```

*   **Run only API regression tests:**
    *(Targets tests in the `api` package matching the `Regression` TestNG group)*
    ```bash
    ./mvnw clean test -Dtest='ru.gordeev.teamcity.api.**' -Dgroups=Regression
    ```

*   **Run only UI regression tests:**
    *(Targets tests in the `web` package matching the `Regression` TestNG group)*
    ```bash
    ./mvnw clean test -Dtest='ru.gordeev.teamcity.web.**' -Dgroups=Regression
    ```

*   **Passing System Properties (Example):**
    ```bash
    ./mvnw clean test -Dhost=my.teamcity.local -DsuperUserToken=abcdef123 -Dtest='ru.gordeev.teamcity.api.**' -Dgroups=Regression
    ```

### Viewing Reports Locally

1.  **Allure Report (Generate & Open):**
    ```bash
    ./mvnw allure:serve
    ```
    (Runs `allure:report` then opens in browser. `Ctrl+C` to stop.)

2.  **Swagger Coverage Report:**
    *   Ensure tests were run (e.g., `./mvnw clean test -Dtest='ru.gordeev.teamcity.api.**' -Dgroups=Regression`).
    *   The `swagger-coverage-rest-assured` filter should generate output in `target/swagger-coverage-output` (verify path).
    *   Generate the HTML report using the command-line tool (if configured locally similar to CI):
        ```bash
        # Example command, adjust path and URL as needed
        ./.swagger-coverage-commandline/bin/swagger-coverage-commandline -s http://YOUR_TC_HOST:8111/app/rest/swagger.json -i target/swagger-coverage-output
        ```
    *   Open the generated `swagger-coverage-report.html` in your browser.

## 🤖 CI/CD (GitHub Actions)

*   **Orchestration (`main.yml`):** The main workflow ([`.github/workflows/main.yml`](.github/workflows/main.yml)) orchestrates the entire process:
    *   Triggers on `push` events.
    *   (Optionally) Simulates backend/frontend deployment stages (`backend-stage.yml`, `frontend-stage.yml`).
    *   Runs API and UI test suites in parallel using the reusable `automation.yml` workflow.
    *   Includes a manual approval step for deploying to a 'production' environment.
*   **Reusable Test Workflow (`automation.yml`):** This workflow ([`.github/workflows/automation.yml`](.github/workflows/automation.yml)) handles the testing phase:
    *   Accepts `package` input (`api` or `web`).
    *   Validates code style using `mvnw validate` (Checkstyle).
    *   **Dynamic Test Environment:** Utilizes **custom GitHub Actions** ([`.github/actions/teamcity-setup`](./.github/actions/teamcity-setup), [`./.github/actions/teamcity-agent-setup`](./.github/actions/teamcity-agent-setup)) to automatically **provision a fresh TeamCity server (and agent) within Docker containers** for each test run.
        *   🚀 **Automated Setup:** The `teamcity-setup` action orchestrates the entire setup, including starting Selenoid and the TeamCity server container.
        *   💡 **Setup via Test:** It cleverly automates the initial TeamCity server configuration (license agreement, etc.) by executing a dedicated TestNG test (`SetupServerTest#setupTeamCityServerTest`).
        *   🔑 **Dynamic Token Extraction:** Demonstrates resourcefulness by **extracting the generated superuser token directly from the TeamCity container logs** (`docker logs teamcity-server | grep ...`) and automatically configuring it within `config.properties` for subsequent test execution.
        *   This approach guarantees a *completely clean, isolated, and automatically configured environment* for every CI execution, showcasing advanced automation techniques.
    *   Runs specific test packages (`-Dtest=...`) using the `Regression` TestNG group (`-Dgroups=Regression`).
    *   Generates **Swagger API Coverage Report** (for API tests only) using `swagger-coverage-commandline` and saves it as a **build artifact** named `swagger-coverage`.
    *   Generates **Allure Test Report** using `simple-elf/allure-report-action`, incorporating history from the `gh-pages` branch.
    *   Publishes the Allure report to GitHub Pages (`gh-pages` branch) into `/api` or `/web` subfolders using `peaceiris/actions-gh-pages`.
*   **Secrets:** `TC_URL`, `TC_TOKEN` are securely passed from GitHub secrets as system properties (`-Dhost`, `-DsuperUserToken`) to the Maven test execution.

---