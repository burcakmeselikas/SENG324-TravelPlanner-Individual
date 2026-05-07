package edu.travelplanner;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.out.println("Travel Planner System is starting...");
            } catch (Exception e) {
                System.err.println("Could not set system look and feel.");
            }
        });
    }
}
