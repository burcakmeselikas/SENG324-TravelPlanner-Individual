package edu.travelplanner.decorator;

public interface TravelPlanComponent {
    String getDescription();

    double getTotalCost();

    double getTotalTimeInHours();
}