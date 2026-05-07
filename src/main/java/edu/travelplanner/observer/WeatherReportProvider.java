package edu.travelplanner.observer;

import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class WeatherReportProvider implements Runnable {
    private static final int UPDATE_INTERVAL_MILLISECONDS = 3000;

    private final List<City> cities;
    private final List<WeatherObserver> observers;
    private final Random random;

    private volatile boolean running;
    private Thread workerThread;

    public WeatherReportProvider(List<City> cities) {
        this.cities = cities;
        this.observers = new CopyOnWriteArrayList<>();
        this.random = new Random();
        this.running = false;
    }

    public void addObserver(WeatherObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }

    public void start() {
        if (running) {
            return;
        }

        running = true;
        workerThread = new Thread(this, "Weather-Report-Provider-Thread");
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public void stopProvider() {
        running = false;

        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(UPDATE_INTERVAL_MILLISECONDS);
                updateWeatherRandomly();
                notifyObservers();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void updateWeatherRandomly() {
        for (City city : cities) {
            double newTemperature = generateRandomTemperature();
            WeatherState newWeatherState = generateRandomWeatherState();

            city.setCurrentTemperature(newTemperature);
            city.setCurrentWeatherState(newWeatherState);
        }
    }

    private double generateRandomTemperature() {
        double temperature = -5 + (random.nextDouble() * 40);
        return Math.round(temperature * 10.0) / 10.0;
    }

    private WeatherState generateRandomWeatherState() {
        WeatherState[] states = WeatherState.values();
        return states[random.nextInt(states.length)];
    }

    private void notifyObservers() {
        List<City> snapshot = Collections.unmodifiableList(new ArrayList<>(cities));

        for (WeatherObserver observer : observers) {
            observer.onWeatherUpdated(snapshot);
        }
    }
}