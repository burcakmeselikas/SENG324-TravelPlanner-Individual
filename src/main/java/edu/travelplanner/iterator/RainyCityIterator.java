package edu.travelplanner.iterator;

import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;

import java.util.List;

public class RainyCityIterator extends AbstractWeatherCityIterator {
    public RainyCityIterator(List<City> cities) {
        super(cities, WeatherState.RAINY);
    }
}