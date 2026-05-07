# Contribution Report

## Project Information

**Course:** SENG 324 - Software Design Patterns  
**Project:** Smart Travel Planner - Individual Project  
**Student:** Burcak Meselikas  
**Project Type:** Individual Java Desktop Application  

---

## Contribution Summary

This project was completed as an individual software design patterns project. All analysis, design, implementation, testing, documentation, and packaging tasks were completed individually.

The project was developed as a Java Swing desktop application and demonstrates the use of five design patterns: Singleton, Strategy, Iterator, Observer, and Decorator.

---

## Completed Responsibilities

### 1. Project Setup and Architecture

- Created the Java Maven project structure.
- Organized the project into clear packages:
  - `model`
  - `repository`
  - `strategy`
  - `iterator`
  - `observer`
  - `decorator`
  - `gui`
- Configured Maven dependencies and fat JAR generation.
- Added JSON-based city data input.

---

### 2. Singleton Pattern Implementation

Implemented `CityRepository` as a Singleton class.

**Purpose:**

- Load city data only once.
- Provide a single shared access point for all city data.
- Prevent unnecessary repeated JSON loading.

---

### 3. Strategy Pattern Implementation

Implemented dynamic city sorting using the Strategy Pattern.

**Classes implemented:**

- `CitySortStrategy`
- `NameSortStrategy`
- `PopulationSortStrategy`
- `AreaSortStrategy`
- `CitySorter`

**Purpose:**

- Allow users to sort cities by name, population, or area.
- Change sorting behavior dynamically from the GUI.

---

### 4. Iterator Pattern Implementation

Implemented weather-based city filtering using the Iterator Pattern.

**Classes implemented:**

- `WeatherCityIterator`
- `AbstractWeatherCityIterator`
- `SunnyCityIterator`
- `CloudyCityIterator`
- `RainyCityIterator`
- `SnowyCityIterator`
- `WeatherIteratorFactory`

**Purpose:**

- Filter cities according to selected weather condition.
- Provide a separate iterator for each weather state.

---

### 5. Observer Pattern Implementation

Implemented live weather updates using the Observer Pattern.

**Classes implemented:**

- `WeatherObserver`
- `WeatherReportProvider`
- `ConsoleWeatherObserver`
- `TravelPlannerFrame` as a GUI observer

**Purpose:**

- Run a simulated weather provider on a background thread.
- Update city weather data every 3 seconds.
- Automatically refresh city lists, bar chart, and pie chart when weather changes.

---

### 6. Decorator Pattern Implementation

Implemented the travel itinerary builder using the Decorator Pattern.

**Classes implemented:**

- `TravelPlanComponent`
- `BasicCityPlan`
- `ActivityDecorator`
- `MuseumVisit`
- `ShoppingMallVisit`
- `ParkVisit`
- `CityCenterVisit`

**Purpose:**

- Add optional travel activities to a city plan.
- Calculate total cost and duration.
- Extend travel plan behavior without modifying the `City` class.

---

### 7. GUI Implementation

Implemented a Java Swing desktop dashboard.

**GUI features:**

- Dashboard-style main window.
- Sorting combo box.
- Weather filter combo box.
- Search field.
- All cities list.
- Weather-filtered cities list.
- Live temperature bar chart.
- Live weather distribution pie chart.
- Smart itinerary builder.
- Trip cost and duration summary.
- TXT export feature for generated travel plans.

---

### 8. Testing and Validation

The application was tested by:

- Running `mvn clean package`.
- Running the generated fat JAR with `java -jar target/TravelPlannerSystem.jar`.
- Testing sorting options.
- Testing weather filtering.
- Testing live weather updates.
- Testing chart refresh behavior.
- Testing activity selection and cost/time calculation.
- Testing TXT travel plan export.
- Testing search functionality.

---

## Final Deliverables Prepared

- Full Java source code.
- Maven project.
- Executable fat JAR.
- README documentation.
- UML class diagram source file.
- UML class diagram PNG image.
- Contribution report.

---

## Individual Contribution Statement

I completed this project individually. I was responsible for all parts of the project including project planning, software architecture, design pattern implementation, GUI development, testing, documentation, UML diagram preparation, and final packaging.

---

## Conclusion

The project successfully demonstrates five software design patterns in a Java Swing desktop application. Each pattern is used for a specific responsibility in the system, and the final application provides an interactive smart travel planning experience with live weather simulation and itinerary generation.