package com.example.recognitionsystem_lab1;

public class Airplane {
    double weight;
    double speed;
    double wingspan;
    String category;

    public Airplane(double weight, double speed, double wingspan, String category) {
        this.weight = weight;
        this.speed = speed;
        this.wingspan = wingspan;
        this.category = category;
    }

    public double distanceTo(Airplane other) {
        double weightDiff = this.weight - other.weight;
        double speedDiff = this.speed - other.speed;
        double wingspanDiff = this.wingspan - other.wingspan;
        return Math.sqrt(weightDiff * weightDiff + speedDiff * speedDiff + wingspanDiff * wingspanDiff);
    }

    public double getWeight() {
        return weight;
    }

    public double getSpeed() {
        return speed;
    }

    public double getWingspan() {
        return wingspan;
    }

    public String getCategory() {
        return category;
    }
}
