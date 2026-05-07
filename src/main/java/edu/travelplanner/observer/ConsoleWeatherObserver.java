package edu.travelplanner.observer;

import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ConsoleWeatherObserver implements WeatherObserver {
    private int updateCount = 0;

    @Override
    public void onWeatherUpdated(List<City> updatedCities) {
        updateCount++;

        System.out.println();
        System.out.println("=== Observer Update #" + updateCount + " ===");
        System.out.println("WeatherReportProvider published new weather data.");

        printWeatherDistribution(updatedCities);
        printFirstThreeCities(updatedCities);
    }

    private void printWeatherDistribution(List<City> cities) {
        Map<WeatherState, Integer> distribution = new EnumMap<>(WeatherState.class);

        for (WeatherState state : WeatherState.values()) {
            distribution.put(state, 0);
        }

        for (City city : cities) {
            WeatherState state = city.getCurrentWeatherState();
            distribution.put(state, distribution.get(state) + 1);
        }

        System.out.println("Weather distribution:");
        for (WeatherState state : WeatherState.values()) {
            System.out.println("- " + state + ": " + distribution.get(state));
        }
    }

    private void printFirstThreeCities(List<City> cities) {
        System.out.println("Sample updated cities:");

        int limit = Math.min(3, cities.size());
        for (int i = 0; i < limit; i++) {
            City city = cities.get(i);
            System.out.println("- " + city.getName()
                    + " | Temp: " + city.getCurrentTemperature() + " C"
                    + " | Weather: " + city.getCurrentWeatherState());
        }
    }
}