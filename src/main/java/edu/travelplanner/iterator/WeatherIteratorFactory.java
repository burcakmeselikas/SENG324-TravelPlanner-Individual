package edu.travelplanner.iterator;

import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;

import java.util.List;

public class WeatherIteratorFactory {
    private WeatherIteratorFactory() {
        // Utility class
    }

    public static WeatherCityIterator createIterator(WeatherState weatherState, List<City> cities) {
        switch (weatherState) {
            case SUNNY:
                return new SunnyCityIterator(cities);
            case CLOUDY:
                return new CloudyCityIterator(cities);
            case RAINY:
                return new RainyCityIterator(cities);
            case SNOWY:
                return new SnowyCityIterator(cities);
            default:
                throw new IllegalArgumentException("Unsupported weather state: " + weatherState);
        }
    }
}