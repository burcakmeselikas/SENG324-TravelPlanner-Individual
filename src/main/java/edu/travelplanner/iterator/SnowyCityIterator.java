package edu.travelplanner.iterator;

import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;

import java.util.List;

public class SnowyCityIterator extends AbstractWeatherCityIterator {
    public SnowyCityIterator(List<City> cities) {
        super(cities, WeatherState.SNOWY);
    }
}