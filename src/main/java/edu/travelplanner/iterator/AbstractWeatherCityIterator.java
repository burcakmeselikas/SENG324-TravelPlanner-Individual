package edu.travelplanner.iterator;

import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractWeatherCityIterator implements WeatherCityIterator {
    private final WeatherState weatherState;
    private final List<City> filteredCities;
    private int currentIndex;

    protected AbstractWeatherCityIterator(List<City> cities, WeatherState weatherState) {
        this.weatherState = weatherState;
        this.filteredCities = new ArrayList<>();
        this.currentIndex = 0;

        for (City city : cities) {
            if (city.getCurrentWeatherState() == weatherState) {
                filteredCities.add(city);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return currentIndex < filteredCities.size();
    }

    @Override
    public City next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There are no more cities for weather state: " + weatherState);
        }

        City city = filteredCities.get(currentIndex);
        currentIndex++;
        return city;
    }

    @Override
    public void reset() {
        currentIndex = 0;
    }

    @Override
    public WeatherState getWeatherState() {
        return weatherState;
    }
}