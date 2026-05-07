package edu.travelplanner.strategy;

import edu.travelplanner.model.City;

import java.util.List;

public interface CitySortStrategy {
    List<City> sort(List<City> cities);

    String getStrategyName();
}