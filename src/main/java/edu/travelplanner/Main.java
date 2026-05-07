package edu.travelplanner;

import edu.travelplanner.model.City;
import edu.travelplanner.repository.CityRepository;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                CityRepository cityRepository = CityRepository.getInstance();

                System.out.println("Travel Planner System is starting...");
                System.out.println("Singleton CityRepository loaded.");
                System.out.println("Loaded city count: " + cityRepository.getCityCount());

                System.out.println("Cities:");
                for (City city : cityRepository.getCities()) {
                    System.out.println("- " + city);
                }

            } catch (Exception e) {
                System.err.println("Application could not start.");
                e.printStackTrace();
            }
        });
    }
}