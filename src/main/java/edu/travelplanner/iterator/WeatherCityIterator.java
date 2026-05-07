package edu.travelplanner.iterator;

import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;

public interface WeatherCityIterator {
    boolean hasNext();

    City next();

    void reset();

    WeatherState getWeatherState();
}