package edu.travelplanner.model;

public class City {
    private String name;
    private int population;
    private double area;
    private double currentTemperature;
    private WeatherState currentWeatherState;

    public City() {
        // Required by Jackson for JSON deserialization
    }

    public City(String name, int population, double area, double currentTemperature, WeatherState currentWeatherState) {
        this.name = name;
        this.population = population;
        this.area = area;
        this.currentTemperature = currentTemperature;
        this.currentWeatherState = currentWeatherState;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public double getArea() {
        return area;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public WeatherState getCurrentWeatherState() {
        return currentWeatherState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public void setCurrentWeatherState(WeatherState currentWeatherState) {
        this.currentWeatherState = currentWeatherState;
    }

    @Override
    public String toString() {
        return name
                + " | Population: " + population
                + " | Area: " + area + " km2"
                + " | Temp: " + currentTemperature + " C"
                + " | Weather: " + currentWeatherState;
    }
}