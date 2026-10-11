# Currency Converter

A Java desktop application for converting amounts between currencies using reference exchange rates from the [Frankfurter API](https://frankfurter.dev/).

Built with **Java, Java Swing, and Maven**, this project features a graphical user interface, dynamically loaded currencies, accurate decimal calculations, exchange-rate caching, and persistent conversion history.

![Currency Converter Application](image.png)

## Features

- **Desktop GUI:** Clean graphical interface built with Java Swing.
- **Exchange rates:** Retrieves reference exchange rates from the Frankfurter API.
- **Dynamic currency discovery:** Loads supported currencies from the API instead of relying solely on a hard-coded list.
- **Accurate calculations:** Uses `BigDecimal` for monetary calculations.
- **Currency selection:** Select source and target currencies using dropdown menus.
- **Currency swapping:** Quickly reverse the selected currencies.
- **Input validation:** Handles invalid amounts and unsupported currency selections.
- **Exchange-rate caching:** Caches successful exchange rates in memory for up to 24 hours.
- **Conversion history:** Displays previous conversions, including timestamps.
- **Persistent storage:** Saves conversion history to a local JSON file and restores it between sessions.
- **History management:** Allows users to clear conversion history.
- **Responsive interface:** Uses `SwingWorker` to perform network operations without blocking the graphical interface.
- **Error handling:** Handles network failures, API errors, and request timeouts.
- **Offline fallback:** Can use available fallback or cached data when live data is unavailable, subject to the application's current implementation.
- **Automated testing:** Includes unit tests for conversion logic and history management.
- **Executable JAR:** Packages the application and its dependencies using Maven Shade Plugin.
- **Windows application package:** Includes a self-contained Windows application and a Windows installer built with `jpackage`.

## Technologies

- Java 21-compatible source and bytecode
- Java Swing
- Apache Maven
- Frankfurter Exchange Rates API
- Java HTTP Client
- Jackson Databind
- JUnit 5
- `BigDecimal`
- JSON-based persistence
- Git and GitHub
- `jpackage` and WiX Toolset for Windows packaging

## Requirements

### To build from source

- JDK 21 or a compatible Java development environment
- Apache Maven
- Internet access for retrieving fresh exchange-rate and currency data

### To run the packaged application

- **Windows installer:** Use the generated Windows installer; the application includes its own Java runtime.
- **Executable JAR:** Requires a compatible Java runtime.

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Dammy-The-Traveller/CurrencyConverter.git
cd CurrencyConverter
```

### 2. Build the application

Run the following command from the project root:

```bash
mvn clean package
```

Maven compiles the application, runs the automated tests, and packages the application with its dependencies.

The executable JAR is generated at:

```text
target/CurrencyConverter-1.0-SNAPSHOT.jar
```

### 3. Run the application

Launch the JAR using:

```bash
java -jar target/CurrencyConverter-1.0-SNAPSHOT.jar
```

The Currency Converter graphical interface should open.

Alternatively, open the project in an IDE such as Visual Studio Code and run `Main.java` with the required Maven dependencies available.

### 4. Run the tests

Execute the automated test suite with:

```bash
mvn clean test
```

The tests cover currency-conversion calculations and conversion-history functionality.

## Windows Application and Installer

The project has been packaged into a self-contained Windows application and a Windows installer using Java's `jpackage` tool.

The generated artifacts are:

- **Windows application:** `release/Currency Converter/Currency Converter.exe`
- **Windows installer:** `release/Currency Converter-1.0.0.exe`

The self-contained application includes a runtime, allowing it to run without requiring the user to install Java separately.

The installer can be used to install the application on a Windows computer. 



## Supported Currencies

The application retrieves its supported currency list dynamically from the Frankfurter API. The available currencies depend on the API response and the currencies supported by the service.

The application has successfully loaded 165 supported currencies during testing.

Currency availability may change, and not every currency is necessarily available for every exchange-rate request.

## Exchange Rate Information

Exchange rates and currency information are retrieved from the [Frankfurter API](https://frankfurter.dev/).

Successful exchange rates are cached in memory for up to 24 hours. The in-memory cache is cleared when the application restarts.

**Important:** These are reference exchange rates, not guaranteed bank, card, remittance, or cash-exchange transaction rates. Financial institutions and payment providers may apply different rates, fees, and spreads.

The application uses available currency data, cached rates, or fallback data where supported when live data cannot be retrieved. Offline conversions may therefore use rates that are outdated or are not current market rates.

## Conversion History and Data Persistence

The application saves conversion history to a JSON file.

By default, the history file is stored at:

```text
%APPDATA%\CurrencyConverter\conversion-history.json
```

On Windows, `%APPDATA%` typically points to the current user's roaming application-data directory.

The history records conversion details such as the amount, source and target currencies, exchange rate, converted amount, and timestamp.

Saved history is loaded when the application starts, allowing previous conversions to remain available between sessions.

The application creates the required directory when saving history.

**Privacy note:** Conversion history is stored locally in a JSON file. It is not encrypted and should not be treated as secure financial record storage.

## Project Structure

```text
CurrencyConverter/
├── pom.xml
├── README.md
├── .gitignore
├── image.png
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
├── target/                       # Generated by Maven
└── release/                      # Generated Windows packages
```

The `target/` and `release/` directories contain generated build artifacts and do not need to be committed to Git unless you intentionally choose to distribute them through the repository.

## Testing Status

The project has successfully passed its current automated test suite:

- **13 tests executed**
- **0 failures**
- **0 errors**
- **0 skipped**

Manual testing has also confirmed that the packaged application launches, performs conversions, and can save conversion history to the expected application-data location.

## Development Roadmap

- [x] Implement currency-conversion logic
- [x] Integrate the Frankfurter API
- [x] Use `BigDecimal` for calculations
- [x] Add input validation and error handling
- [x] Add exchange-rate caching
- [x] Add conversion history and timestamps
- [x] Persist history using JSON
- [x] Load supported currencies dynamically
- [x] Build a desktop GUI using Java Swing
- [x] Add automated unit tests
- [x] Package the application as an executable JAR
- [x] Build a self-contained Windows application
- [x] Generate a Windows installer
- [ ] Improve offline-rate transparency in the interface
- [ ] Perform further usability and cross-environment testing
- [ ] Publish a versioned GitHub Release

## Contributing

This is a learning and portfolio project. Suggestions, bug reports, and constructive feedback are welcome.

## Author

**Damilare David Adebesin**

Portfolio: [damilare-david-adebesin.onrender.com](https://damilare-david-adebesin.onrender.com/)

GitHub: [Dammy-The-Traveller](https://github.com/Dammy-The-Traveller)
