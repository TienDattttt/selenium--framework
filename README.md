# Selenium Framework - SauceDemo
Framework kiểm thử tự động cho https://www.saucedemo.com

## Tech Stack
- Java 17, Selenium 4.18.1, TestNG 7.9.0, WebDriverManager 5.7.0, Allure 2.26.0, Maven

## Cấu trúc project
- `base`: chứa lớp nền tảng cho page object, quản lý driver, setup/teardown test.
- `pages`: chứa các page object đại diện cho màn hình đăng nhập và inventory.
- `tests`: chứa các test case TestNG cho luồng đăng nhập.
- `utils`: chứa tiện ích đọc cấu hình từ `config.properties`.

## Chạy local
```bash
# Chạy smoke test
mvn clean test -DsuiteXmlFile=testng-smoke.xml -Dbrowser=chrome

# Chạy full suite
mvn clean test

# Chạy với Firefox
mvn clean test -Dbrowser=firefox

# Xem Allure report
mvn allure:serve
```

## Biến môi trường (CI)
| Biến | Mô tả |
|------|-------|
| APP_USERNAME | Username đăng nhập |
| APP_PASSWORD | Password đăng nhập |
| CI | Tự động set bởi GitHub Actions, kích hoạt headless mode |
