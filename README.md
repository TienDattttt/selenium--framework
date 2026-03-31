# Selenium POM Framework

Selenium POM Framework with TestNG and Maven.

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
