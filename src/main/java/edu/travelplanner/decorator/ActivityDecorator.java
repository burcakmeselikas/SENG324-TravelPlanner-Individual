package edu.travelplanner.decorator;

public abstract class ActivityDecorator implements TravelPlanComponent {
    protected final TravelPlanComponent wrappedPlan;

    protected ActivityDecorator(TravelPlanComponent wrappedPlan) {
        this.wrappedPlan = wrappedPlan;
    }

    @Override
    public String getDescription() {
        return wrappedPlan.getDescription();
    }

    @Override
    public double getTotalCost() {
        return wrappedPlan.getTotalCost();
    }

    @Override
    public double getTotalTimeInHours() {
        return wrappedPlan.getTotalTimeInHours();
    }
}