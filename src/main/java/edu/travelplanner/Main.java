package edu.travelplanner;

import edu.travelplanner.iterator.WeatherCityIterator;
import edu.travelplanner.iterator.WeatherIteratorFactory;
import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;
import edu.travelplanner.repository.CityRepository;
import edu.travelplanner.strategy.AreaSortStrategy;
import edu.travelplanner.strategy.CitySorter;
import edu.travelplanner.strategy.NameSortStrategy;
import edu.travelplanner.strategy.PopulationSortStrategy;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                CityRepository cityRepository = CityRepository.getInstance();

                System.out.println("Travel Planner System is starting...");
                System.out.println("Singleton CityRepository loaded.");
                System.out.println("Loaded city count: " + cityRepository.getCityCount());

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

                System.out.println();
                System.out.println("=== Iterator Test: Weather Filtered Cities ===");
                for (WeatherState weatherState : WeatherState.values()) {
                    printCitiesByWeather(weatherState, cityRepository.getCities());
                }

            } catch (Exception e) {
                System.err.println("Application could not start.");
                e.printStackTrace();
            }
        });
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