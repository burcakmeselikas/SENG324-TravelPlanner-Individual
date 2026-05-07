package edu.travelplanner.iterator;

import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;

import java.util.List;

public class CloudyCityIterator extends AbstractWeatherCityIterator {
    public CloudyCityIterator(List<City> cities) {
        super(cities, WeatherState.CLOUDY);
    }
}