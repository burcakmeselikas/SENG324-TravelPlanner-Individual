package edu.travelplanner.decorator;

public class CityCenterVisit extends ActivityDecorator {
    private static final double COST = 250.0;
    private static final double TIME_IN_HOURS = 2.0;

    public CityCenterVisit(TravelPlanComponent wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    public String getDescription() {
        return wrappedPlan.getDescription() + " + Historic City Center Visit";
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