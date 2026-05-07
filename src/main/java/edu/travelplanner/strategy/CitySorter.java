package edu.travelplanner.strategy;

import edu.travelplanner.model.City;

import java.util.List;

public class CitySorter {
    private CitySortStrategy strategy;

    public CitySorter(CitySortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(CitySortStrategy strategy) {
        this.strategy = strategy;
    }

    public CitySortStrategy getStrategy() {
        return strategy;
    }

    public List<City> sortCities(List<City> cities) {
        if (strategy == null) {
            throw new IllegalStateException("Sorting strategy is not selected.");
        }

        return strategy.sort(cities);
    }
}