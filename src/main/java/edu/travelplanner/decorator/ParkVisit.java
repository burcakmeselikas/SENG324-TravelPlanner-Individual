package edu.travelplanner.decorator;

public class ParkVisit extends ActivityDecorator {
    private static final double COST = 150.0;
    private static final double TIME_IN_HOURS = 1.5;

    public ParkVisit(TravelPlanComponent wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    public String getDescription() {
        return wrappedPlan.getDescription() + " + Park Visit";
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