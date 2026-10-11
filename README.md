# Currency Converter

A Java desktop application for converting amounts between currencies using reference exchange rates from the [Frankfurter API](https://frankfurter.dev/). Built with Java Swing, the application provides a graphical interface, dynamically loaded currencies, conversion history, and reliable decimal calculations using `BigDecimal`.

The project emphasizes clean code, input validation, automated testing, persistent data, and practical Java application development.

## Features

* **Desktop GUI:** A graphical interface built with Java Swing.
* **Live reference exchange rates:** Retrieves exchange rates from the Frankfurter API.
* **Dynamic currency discovery:** Loads supported currencies from the API instead of relying solely on a hard-coded list.
* **Accurate calculations:** Uses `BigDecimal` for monetary calculations.
* **Currency selection:** Select source and target currencies from dropdown lists.
* **Currency swapping:** Quickly reverse the selected source and target currencies.
* **Input validation:** Handles invalid amounts and unsupported currency selections.
* **Exchange-rate caching:** Caches successful exchange rates in memory for up to 24 hours.
* **Conversion history:** Displays previous conversions, including timestamps.
* **Persistent history:** Saves conversion history to a JSON file so records can be restored after restarting the application.
* **History management:** Allows users to clear saved conversion history.
* **Responsive interface:** Uses `SwingWorker` to perform network requests without blocking the GUI.
* **Error handling:** Handles API errors, network failures, and request timeouts.
* **Automated testing:** Includes unit tests for conversion logic and history management.
* **Executable JAR:** Packages the application and its dependencies into a runnable JAR file using Maven Shade Plugin.

## Technologies

* Java 21
* Java Swing
* Maven
* Frankfurter Exchange Rates API
* Jackson Databind
* JUnit 5
* HTTP Client
* JSON-based persistence
* Git and GitHub

## Requirements

To build the project from source, you need:

* JDK 21 or a compatible Java development environment
* Apache Maven
* Internet access for retrieving exchange rates and currency data

To run the packaged JAR, you need a compatible Java runtime unless you use a future self-contained application package.

## Getting Started

### 1. Clone the repository

Replace `YOUR_GITHUB_USERNAME` with your GitHub username.

```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/CurrencyConverter.git
cd CurrencyConverter
```

### 2. Build the application

Run the following command from the project root:

```bash
mvn clean package
```

Maven compiles the application, runs the automated tests, and creates the packaged JAR.

### 3. Run the application

Launch the packaged application with:

```bash
java -jar target/CurrencyConverter-1.0-SNAPSHOT.jar
```

The graphical interface should open, and the application will attempt to load the supported currencies.

Alternatively, open the project in an IDE such as Visual Studio Code and run `Main.java` with the required Maven dependencies available.

### 4. Run the tests

Execute the test suite with:

```bash
mvn clean test
```

The test suite covers currency-conversion calculations, validation, and conversion-history behavior.

## Supported Currencies

The application retrieves its supported currency list dynamically from the Frankfurter API. The number of available currencies depends on the API response and the currencies supported by the service.

The application previously loaded 165 supported currencies successfully.

Currency availability may change, and not every currency is necessarily available for every exchange-rate request.

## Exchange Rate Information

Exchange rates and currency information are retrieved from the [Frankfurter API](https://frankfurter.dev/).

The application caches successful exchange rates in memory for up to 24 hours. This cache is cleared when the application restarts.

**Important:** These are reference exchange rates, not guaranteed bank, card, remittance, or cash-exchange transaction rates. Financial institutions and payment providers may apply different rates, fees, and spreads.

The application requires an internet connection to retrieve fresh rates and currency data. If the API is unavailable, currency loading may use the application's fallback list, where applicable.

## Conversion History and Data Persistence

Conversion history is saved in `conversion-history.json`.

The application stores conversion details, including the amount, source and target currencies, exchange rate, converted amount, and timestamp.

History is loaded when the application starts, allowing previous conversions to remain available between sessions.

The JSON file is stored relative to the application's working directory. Running the application from a different working directory may cause it to use a different history file.

**Privacy note:** The history file is stored locally and is not intended to be a secure financial record or encrypted storage system.

## Project Structure

```text
CurrencyConverter/
├── pom.xml
├── README.md
├── .gitignore
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── damilare/
│   │               └── currencyconverter/
│   │                   ├── Main.java
│   │                   ├── Currency.java
│   │                   ├── CurrencyConverter.java
│   │                   ├── ExchangeRateService.java
│   │                   ├── ExchangeRateResponse.java
│   │                   ├── Conversion.java
│   │                   ├── ConversionHistory.java
│   │                   ├── InputHandler.java
│   │                   ├── CurrencyService.java
│   │                   └── CurrencyConverterGUI.java
│   └── test/
│       └── java/
│           └── com/
│               └── damilare/
│                   └── currencyconverter/
│                       ├── CurrencyConverterTest.java
│                       └── ConversionHistoryTest.java
└── target/
    └── CurrencyConverter-1.0-SNAPSHOT.jar
```

The `target/` directory is generated during the Maven build and should generally not be committed to Git.

## Testing Status

The project has successfully passed the current automated test suite:

* **13 tests executed**
* **0 failures**
* **0 errors**
* **0 skipped**

Manual verification has also confirmed that the packaged application launches, loads currencies, performs conversions, and preserves conversion history across restarts.

## Roadmap

* [x] Build the currency-conversion logic
* [x] Integrate live exchange rates
* [x] Use `BigDecimal` for calculations
* [x] Add exchange-rate caching
* [x] Add conversion history and timestamps
* [x] Persist history using JSON
* [x] Load supported currencies dynamically
* [x] Build a desktop GUI using Java Swing
* [x] Add automated unit tests
* [x] Package the application as an executable JAR
* [ ] Add application screenshots to the README
* [ ] Create a Windows installer or self-contained application package
* [ ] Perform further usability and cross-environment testing

## Contributing

This is a learning and portfolio project. Suggestions, bug reports, and constructive feedback are welcome.

## Author

**Damilare David Adebesin**

Portfolio: [damilare-david-adebesin.onrender.com](https://damilare-david-adebesin.onrender.com/)
