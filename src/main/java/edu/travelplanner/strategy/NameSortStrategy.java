package edu.travelplanner.strategy;

import edu.travelplanner.model.City;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NameSortStrategy implements CitySortStrategy {
    @Override
    public List<City> sort(List<City> cities) {
        List<City> sortedCities = new ArrayList<>(cities);
        sortedCities.sort(Comparator.comparing(City::getName, String.CASE_INSENSITIVE_ORDER));
        return sortedCities;
    }

    @Override
    public String getStrategyName() {
        return "Sort by Name";
    }
}