package com.example.recognitionsystem_lab1;

import java.util.ArrayList;
import java.util.List;

public class AirplaneRecognitionSystem {
    List<Airplane> airplanes = new ArrayList<>();

    public void addAirplane(Airplane airplane) {
        airplanes.add(airplane);
    }

    public String classifyAirplane(Airplane unknown) {
        double minDistance = Double.MAX_VALUE;
        String bestCategory = "Невідомий";
        double threshold = 10000;

        for (Airplane airplane : airplanes) {
            double distance = unknown.distanceTo(airplane);
            if (distance < minDistance) {
                minDistance = distance;
                bestCategory = airplane.category;
            }
        }
        if (minDistance > threshold) {
            return "Невідомий";
        }

        return bestCategory;
    }
    public double calculateProbability(Airplane unknown, Airplane known) {
        double distanceToKnown = unknown.distanceTo(known);
        double minDistance = airplanes.stream()
                .mapToDouble(airplane -> unknown.distanceTo(airplane))
                .min()
                .orElse(1);

        return 1 - (distanceToKnown / minDistance);
    }
    public double[] calculateRecognitionAverages() {
        double totalWeight = 0;
        double totalSpeed = 0;
        double totalWingspan = 0;

        for (Airplane airplane : airplanes) {
            totalWeight += airplane.weight;
            totalSpeed += airplane.speed;
            totalWingspan += airplane.wingspan;
        }

        int count = airplanes.size();
        return new double[] {
                count > 0 ? totalWeight / count : 0,
                count > 0 ? totalSpeed / count : 0,
                count > 0 ? totalWingspan / count : 0
        };
    }
}
