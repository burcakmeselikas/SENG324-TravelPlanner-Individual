package edu.travelplanner.decorator;

public class MuseumVisit extends ActivityDecorator {
    private static final double COST = 350.0;
    private static final double TIME_IN_HOURS = 2.5;

    public MuseumVisit(TravelPlanComponent wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    public String getDescription() {
        return wrappedPlan.getDescription() + " + Museum Visit";
    }

    @Override
    public double getTotalCost() {
        return wrappedPlan.getTotalCost() + COST;
    }

    @Override
    public double getTotalTimeInHours() {
        return wrappedPlan.getTotalTimeInHours() + TIME_IN_HOURS;
    }
}