package edu.travelplanner.decorator;

public class ShoppingMallVisit extends ActivityDecorator {
    private static final double COST = 750.0;
    private static final double TIME_IN_HOURS = 3.0;

    public ShoppingMallVisit(TravelPlanComponent wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    public String getDescription() {
        return wrappedPlan.getDescription() + " + Shopping Mall Visit";
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