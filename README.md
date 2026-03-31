# Selenium POM Framework

[![Test Status](https://github.com/TienDattttt/selenium--framework/actions/workflows/selenium-full.yml/badge.svg)](https://github.com/TienDattttt/selenium--framework/actions)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange)](https://TienDattttt.github.io/selenium--framework/)

## Mo ta
Selenium 4 + TestNG + Page Object Model framework voi CI/CD tu dong.

## Requirements

- Java 17
- Maven 3.8+
- Chrome

## Run Local

```bash
mvn test -Denv=dev -Dbrowser=chrome -DsuiteXmlFile=testng-parallel.xml
```

## Run Staging

```bash
mvn test -Denv=staging
```

## CI Evidence Flow

- Push a normal commit to get a green pipeline.
- Push a commit with `[red-ci]` in the commit message to force the intentional failing test and get a red pipeline with uploaded artifacts.
- You can also use `workflow_dispatch` and set `intentional_fail=true` for a manual red run.