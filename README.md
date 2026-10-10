# Currency Converter

A Java-based currency converter that retrieves exchange rates from the Frankfurter API and converts amounts between supported currencies. The project is being developed incrementally, with a focus on clean code, reliable calculations, automated testing, and a graphical user interface planned for a future milestone.

## Features

* Live reference exchange rates from the Frankfurter API
* Currency conversion using `BigDecimal`
* Support for USD, GHS, EUR, GBP, and NGN in the current version
* Numbered currency selection
* Input validation for currency choices and amounts
* Exchange-rate caching for up to 24 hours
* Conversion history with timestamps
* API error handling and request timeouts
* Automated unit tests using JUnit 5
* Maven-based project structure

> **Current status:** The console application is working. Dynamic currency discovery and a graphical user interface are planned improvements.

## Technologies

* Java 21
* Maven
* Frankfurter Exchange Rates API
* Jackson Databind
* JUnit 5

## Requirements

* JDK 21 or a compatible Java installation
* Maven
* Internet access for retrieving exchange rates

## Getting Started

### 1. Clone the repository

Replace `YOUR_GITHUB_USERNAME` with your GitHub username.

```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/CurrencyConverter.git
cd CurrencyConverter
```

### 2. Build the project

```bash
mvn clean package
```

### 3. Run the application

Open the project in an IDE such as Visual Studio Code and run `Main.java` with Maven dependencies available on the runtime classpath.

### 4. Run the tests

```bash
mvn clean test
```

The current test suite covers currency calculations, invalid exchange rates, amount validation, and conversion history.

## Supported Currencies

The current implementation includes:

| Code | Currency       |
| ---- | -------------- |
| USD  | US Dollar      |
| GHS  | Ghanaian Cedi  |
| EUR  | Euro           |
| GBP  | British Pound  |
| NGN  | Nigerian Naira |

The supported list is currently defined in the application's Java code. Expanding it through the API is planned.

## Exchange Rate Information

Exchange rates are retrieved from the [Frankfurter API](https://frankfurter.dev/).

The application caches successful rates in memory for up to 24 hours. The cache is cleared when the application restarts.

These are reference exchange rates, not guaranteed bank, card, remittance, or cash-exchange transaction rates. Actual providers may apply different rates and fees.

## Project Structure

```text
CurrencyConverter/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── damilare/
    │               └── currencyconverter/
    │                   ├── Main.java
    │                   ├── Currency.java
    │                   ├── CurrencyConverter.java
    │                   ├── ExchangeRateService.java
    │                   ├── ExchangeRateResponse.java
    │                   ├── Conversion.java
    │                   ├── ConversionHistory.java
    │                   └── InputHandler.java
    └── test/
        └── java/
            └── com/
                └── damilare/
                    └── currencyconverter/
                        ├── CurrencyConverterTest.java
                        └── ConversionHistoryTest.java
```

## Roadmap

* [x] Build the console-based converter
* [x] Integrate live exchange rates
* [x] Use `BigDecimal` for conversion calculations
* [x] Add conversion history
* [x] Add exchange-rate caching
* [x] Add automated unit tests
* [ ] Load supported currencies dynamically from the API
* [ ] Build a desktop GUI using Java Swing
* [ ] Improve the GUI's conversion history and user experience
* [ ] Complete final testing and packaging

## Contributing

This is a learning and portfolio project. Suggestions and constructive feedback are welcome.

## Author

Damilare David Adebesin (https://damilare-david-adebesin.onrender.com/)
