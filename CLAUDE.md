# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "tests.LoginTests"

# Run a single test method
./gradlew test --tests "tests.LoginTests.successfulLoginTest"

# Generate and open Allure report
./gradlew allureServe

# Generate Allure report to build/reports/allure-report
./gradlew allureReport
```

The `REQRES_API_KEY` environment variable must be set before running tests — it is injected as the `x-api-key` request header via `RequestSpecs`.

## Architecture

**Target API:** [reqres.in](https://reqres.in) — a hosted REST mock API. Base URI is `https://reqres.in/api`, configured in `TestBase.java`.

**Test framework:** JUnit 5 + RestAssured 5. Allure reporting is wired via `CustomAllureListener` (a RestAssured filter that attaches custom HTML request/response templates from `src/test/resources/tpl/`). AspectJ weaver is enabled in `build.gradle` so Allure captures HTTP exchanges automatically.

**Layered structure:**

| Layer | Location | Purpose |
|---|---|---|
| Specs | `specs/RequestSpecs.java`, `specs/ResponseSpecs.java` | Reusable RestAssured `RequestSpecification` / `ResponseSpecification`. `ResponseSpecs.responseSpec(int)` builds a spec for any status code; pre-built constants exist for common codes. |
| Endpoints | `endpoints/Endpoints.java` | String constants for all API paths. |
| Models | `models/` | Lombok `@Data` POJOs for request bodies and response deserialization. All response models carry `@JsonIgnoreProperties(ignoreUnknown = true)`. |
| TestData | `TestData/TestData.java` | Constants (email, password, job, etc.) and builder methods that construct request model instances. |
| Tests | `tests/` | One class per endpoint group (`LoginTests`, `GetResourcesTests`, `UpdateUsersTests`, `DeleteTests`). All extend `TestBase`. |

**Adding a new test:** create a model in `models/` if needed, add endpoint constant to `Endpoints`, add test data to `TestData`, write the test in the appropriate `tests/` class (or create a new class extending `TestBase`), and reuse `RequestSpecs.requestSpecification` + `ResponseSpecs` for the given/when/then blocks.

**Test display names** are written in Russian.
