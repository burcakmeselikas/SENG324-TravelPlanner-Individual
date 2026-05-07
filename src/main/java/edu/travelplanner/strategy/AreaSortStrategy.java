package edu.travelplanner.strategy;

import edu.travelplanner.model.City;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AreaSortStrategy implements CitySortStrategy {
    @Override
    public List<City> sort(List<City> cities) {
        List<City> sortedCities = new ArrayList<>(cities);
        sortedCities.sort(Comparator.comparingDouble(City::getArea).reversed());
        return sortedCities;
    }

    @Override
    public String getStrategyName() {
        return "Sort by Area";
    }
}