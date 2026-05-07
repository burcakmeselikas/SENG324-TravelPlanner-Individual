package edu.travelplanner.observer;

import edu.travelplanner.model.City;

import java.util.List;

public interface WeatherObserver {
    void onWeatherUpdated(List<City> updatedCities);
}