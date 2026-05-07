package edu.travelplanner;

import edu.travelplanner.decorator.BasicCityPlan;
import edu.travelplanner.decorator.CityCenterVisit;
import edu.travelplanner.decorator.MuseumVisit;
import edu.travelplanner.decorator.ParkVisit;
import edu.travelplanner.decorator.ShoppingMallVisit;
import edu.travelplanner.decorator.TravelPlanComponent;
import edu.travelplanner.iterator.WeatherCityIterator;
import edu.travelplanner.iterator.WeatherIteratorFactory;
import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;
import edu.travelplanner.observer.ConsoleWeatherObserver;
import edu.travelplanner.observer.WeatherReportProvider;
import edu.travelplanner.repository.CityRepository;
import edu.travelplanner.strategy.AreaSortStrategy;
import edu.travelplanner.strategy.CitySorter;
import edu.travelplanner.strategy.NameSortStrategy;
import edu.travelplanner.strategy.PopulationSortStrategy;

import javax.swing.UIManager;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            CityRepository cityRepository = CityRepository.getInstance();

            System.out.println("Travel Planner System is starting...");
            System.out.println("Singleton CityRepository loaded.");
            System.out.println("Loaded city count: " + cityRepository.getCityCount());

            runStrategyTest(cityRepository);
            runIteratorTest(cityRepository);
            runDecoratorTest(cityRepository);
            runObserverTest(cityRepository);

        } catch (Exception e) {
            System.err.println("Application could not start.");
            e.printStackTrace();
        }
    }

    private static void runStrategyTest(CityRepository cityRepository) {
        CitySorter citySorter = new CitySorter(new NameSortStrategy());

        System.out.println();
        System.out.println("=== Strategy Test: Sort by Name ===");
        printCities(citySorter.sortCities(cityRepository.getCities()));

        citySorter.setStrategy(new PopulationSortStrategy());
        System.out.println();
        System.out.println("=== Strategy Test: Sort by Population ===");
        printCities(citySorter.sortCities(cityRepository.getCities()));

        citySorter.setStrategy(new AreaSortStrategy());
        System.out.println();
        System.out.println("=== Strategy Test: Sort by Area ===");
        printCities(citySorter.sortCities(cityRepository.getCities()));
    }

    private static void runIteratorTest(CityRepository cityRepository) {
        System.out.println();
        System.out.println("=== Iterator Test: Weather Filtered Cities ===");

        for (WeatherState weatherState : WeatherState.values()) {
            printCitiesByWeather(weatherState, cityRepository.getCities());
        }
    }

    private static void runDecoratorTest(CityRepository cityRepository) {
        System.out.println();
        System.out.println("=== Decorator Test: City Activity Planner ===");

        City selectedCity = cityRepository.getCities().get(0);

        TravelPlanComponent plan = new BasicCityPlan(selectedCity);
        plan = new MuseumVisit(plan);
        plan = new ShoppingMallVisit(plan);
        plan = new ParkVisit(plan);
        plan = new CityCenterVisit(plan);

        System.out.println("Selected city: " + selectedCity.getName());
        System.out.println("Plan description: " + plan.getDescription());
        System.out.println("Total cost: " + plan.getTotalCost() + " TL");
        System.out.println("Total time: " + plan.getTotalTimeInHours() + " hours");
    }

    private static void runObserverTest(CityRepository cityRepository) throws InterruptedException {
        System.out.println();
        System.out.println("=== Observer Test: Weather Report Provider ===");
        System.out.println("Weather provider will update city weather every 3 seconds.");
        System.out.println("ConsoleWeatherObserver is subscribed to the provider.");

        WeatherReportProvider provider = new WeatherReportProvider(cityRepository.getCities());
        ConsoleWeatherObserver consoleObserver = new ConsoleWeatherObserver();

        provider.addObserver(consoleObserver);
        provider.start();

        Thread.sleep(9500);

        provider.stopProvider();

        System.out.println();
        System.out.println("Observer test finished.");
    }

    private static void printCities(List<City> cities) {
        for (City city : cities) {
            System.out.println("- " + city);
        }
    }

    private static void printCitiesByWeather(WeatherState weatherState, List<City> cities) {
        WeatherCityIterator iterator = WeatherIteratorFactory.createIterator(weatherState, cities);

        System.out.println();
        System.out.println("Cities with weather " + weatherState + ":");

        if (!iterator.hasNext()) {
            System.out.println("- No cities found.");
            return;
        }

        while (iterator.hasNext()) {
            System.out.println("- " + iterator.next());
        }
    }
}