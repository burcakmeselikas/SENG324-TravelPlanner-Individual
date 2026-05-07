package edu.travelplanner.iterator;

import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;

import java.util.List;

public class SunnyCityIterator extends AbstractWeatherCityIterator {
    public SunnyCityIterator(List<City> cities) {
        super(cities, WeatherState.SUNNY);
    }
}