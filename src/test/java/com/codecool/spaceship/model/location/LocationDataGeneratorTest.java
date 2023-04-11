package com.codecool.spaceship.model.location;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationDataGeneratorTest {


    @Test
    void PlanetFoundTestLowLevel() {
        int scannerEfficiency = 1;
        double activityHours = 2;
        int distance = 10;
        List<Boolean> results = new ArrayList<>();
        LocationDataGenerator locationDataGenerator = new LocationDataGenerator(new Random());
        for (int i = 0; i < 1000; i++) {
            results.add(locationDataGenerator.determinePlanetFound(scannerEfficiency, activityHours, distance));
        }
        double successful = results.stream().filter(b -> b).count() / ((double)results.size());
        System.out.println(successful);
        assertTrue(0.7 < successful && successful < 0.8);
    }

    @Test
    void ResourceReserveTestLowLevelPrioritized() {
        int scannerEfficiency = 1;
        double activityHours = 2;
        boolean prioritize = true;
        List<Integer> results = new ArrayList<>();
        LocationDataGenerator locationDataGenerator = new LocationDataGenerator(new Random());
        for (int i = 0; i < 1000; i++) {
            results.add(locationDataGenerator.determineResourceReserves(scannerEfficiency, activityHours, prioritize));
        }
        int min = results.stream().mapToInt(i -> i).min().getAsInt();
        int max = results.stream().mapToInt(i -> i).max().getAsInt();
        double average = results.stream().mapToInt(i -> i).average().getAsDouble();
        System.out.println(min);
        System.out.println(max);
        System.out.println(average);
        assertTrue(90 < min && min < 100);
        assertTrue(290 < max && max < 300);
        assertTrue(190 < average && average < 210);
    }

    @Test
    void ResourceReserveTestHighLevelPrioritized() {
        int scannerEfficiency = 25;
        double activityHours = 2;
        boolean prioritize = true;
        LocationDataGenerator locationDataGenerator = new LocationDataGenerator(new Random());
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            results.add(locationDataGenerator.determineResourceReserves(scannerEfficiency, activityHours, prioritize));
        }
        int min = results.stream().mapToInt(i -> i).min().getAsInt();
        int max = results.stream().mapToInt(i -> i).max().getAsInt();
        double average = results.stream().mapToInt(i -> i).average().getAsDouble();
        System.out.println(min);
        System.out.println(max);
        System.out.println(average);
        assertTrue(400 < min && min < 500);
        assertTrue(900 < max && max <= 1000);
        assertTrue(700 < average && average < 800);
    }

    @Test
    void DistanceTestLowLevelPrioritized() {
        int scannerEfficiency = 1;
        int distance = 15;
        boolean prioritize = true;
        List<Integer> results = new ArrayList<>();
        LocationDataGenerator locationDataGenerator = new LocationDataGenerator(new Random());
        for (int i = 0; i < 1000; i++) {
            results.add(locationDataGenerator.determineDistance(scannerEfficiency, distance, prioritize));
        }
        int min = results.stream().mapToInt(i -> i).min().getAsInt();
        int max = results.stream().mapToInt(i -> i).max().getAsInt();
        double average = results.stream().mapToInt(i -> i).average().getAsDouble();
        System.out.println(min);
        System.out.println(max);
        System.out.println(average);
        assertTrue(10 < average && average < 20);
    }


    @Test
    void DistanceTestHighLevelPrioritized() {
        int scannerEfficiency = 25;
        int distance = 15;
        boolean prioritize = true;
        List<Integer> results = new ArrayList<>();
        LocationDataGenerator locationDataGenerator = new LocationDataGenerator(new Random());
        for (int i = 0; i < 1000; i++) {
            results.add(locationDataGenerator.determineDistance(scannerEfficiency, distance, prioritize));
        }
        int min = results.stream().mapToInt(i -> i).min().getAsInt();
        int max = results.stream().mapToInt(i -> i).max().getAsInt();
        double average = results.stream().mapToInt(i -> i).average().getAsDouble();
        System.out.println(min);
        System.out.println(max);
        System.out.println(average);
        assertTrue(5 < average && average < 10);
    }

}