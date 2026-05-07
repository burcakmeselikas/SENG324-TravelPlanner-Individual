package edu.travelplanner.decorator;

import edu.travelplanner.model.City;

public class BasicCityPlan implements TravelPlanComponent {
    private final City city;

    public BasicCityPlan(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    @Override
    public String getDescription() {
        return "Trip plan for " + city.getName();
    }

    @Override
    public double getTotalCost() {
        return 0.0;
    }

    @Override
    public double getTotalTimeInHours() {
        return 0.0;
    }
}