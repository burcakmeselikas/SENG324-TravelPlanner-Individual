# SENG 324 Smart Travel Planner

## Project Overview

Smart Travel Planner is an individual Java desktop application developed for the SENG 324 Software Design Patterns course.

The application allows users to explore cities, monitor simulated live weather conditions, sort and filter destinations, and create a travel itinerary with optional activities.

This project is not a web application. It is a standalone Java Swing desktop application.

## Technologies Used

- Java 17
- Java Swing
- Maven
- Jackson Databind
- JFreeChart
- JSON
- Maven Shade Plugin

## Implemented Design Patterns

### 1. Singleton Pattern

Implemented in CityRepository.

The Singleton Pattern is used to load and manage city data from the JSON file. The repository provides one shared access point for city information and prevents repeated loading of the same data.

### 2. Strategy Pattern

Implemented in CitySortStrategy, NameSortStrategy, PopulationSortStrategy, AreaSortStrategy, and CitySorter.

The Strategy Pattern is used for dynamic city sorting. The user can change the sorting algorithm from the GUI and sort cities by name, population, or area.

### 3. Iterator Pattern

Implemented in WeatherCityIterator, AbstractWeatherCityIterator, SunnyCityIterator, CloudyCityIterator, RainyCityIterator, SnowyCityIterator, and WeatherIteratorFactory.

The Iterator Pattern is used to filter cities according to their current weather state. Each weather condition has its own iterator implementation.

### 4. Observer Pattern

Implemented in WeatherObserver, WeatherReportProvider, ConsoleWeatherObserver, and TravelPlannerFrame.

The Observer Pattern is used for live weather simulation. The weather provider updates city weather data every 3 seconds and notifies observers. The GUI automatically refreshes city lists and charts when weather data changes.

### 5. Decorator Pattern

Implemented in TravelPlanComponent, BasicCityPlan, ActivityDecorator, MuseumVisit, ShoppingMallVisit, ParkVisit, and CityCenterVisit.

The Decorator Pattern is used to build a travel itinerary. A basic city plan can be extended with optional activities without modifying the original City class.

## Main Features

- Java Swing dashboard interface
- City list loaded from JSON
- Dynamic sorting by name, population, and area
- Weather-based city filtering
- Simulated live weather updates
- Live temperature chart
- Weather distribution chart
- Search city feature
- Travel itinerary builder
- Activity selection with cost and duration calculation
- TXT export for generated travel plans
- UML class diagram included under the docs folder

## Project Structure

src/main/java/edu/travelplanner

- decorator
- gui
- iterator
- model
- observer
- repository
- strategy
- Main.java

src/main/resources

- cities.json

docs

- Contribution_Report.md
- UML_Class_Diagram.png
- UML_Class_Diagram.puml

## How to Build

Run this command in the project root directory:

mvn clean package

After a successful build, the executable JAR file is generated under the target folder.

## How to Run

Run the application with:

java -jar target/TravelPlannerSystem.jar

The Java Swing dashboard will open after running the command.

## Weather Simulation Note

The application uses simulated weather data instead of a real weather API. The weather provider runs on a background thread and updates city weather states and temperatures every 3 seconds.

Temperature values are generated according to the selected weather state. For example, snowy weather produces cold temperatures, while sunny weather produces warmer temperatures.

## UML Diagram

The UML class diagram is available in the docs folder:

- docs/UML_Class_Diagram.png
- docs/UML_Class_Diagram.puml

## Contribution Report

Since this is an individual project, the contribution report explains the responsibilities completed during the development process.

- docs/Contribution_Report.md

## Author

Burcak Meselikas  
SENG 324 Software Design Patterns  
Individual Term Project