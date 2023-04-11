package com.codecool.spaceship.model.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class LocationDataGenerator {

    private static final String[] NAMES = {"Mebbomia", "Cruxeliv", "Zeron", "Etov", "Cueter", "Soiter", "Ogantu", "Lorix",
            "Vothagantu", "Tiliea", "Brosie", "Ema", "Vegenov", "Nomia", "Hietania", "Iclite", "Draloturn", "Thomone", "Nion",
    "Drypso", "Euruta", "Crosie", "Noveria", "Abatov", "Ferrix", "Rakis"};
    private static final String[] GREEK = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa",
            "Lambda", "Mu", "Nu", "Xi", "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"};
    private final Random random;

    @Autowired
    public LocationDataGenerator(Random random) {
        this.random = random;
    }

    public String determineName() {
        StringBuilder nameBuilder = new StringBuilder(NAMES[random.nextInt(NAMES.length)]);
        nameBuilder.append("-");
        nameBuilder.append(random.nextInt(1, 1000));
        if (random.nextInt(2) == 1) {
            nameBuilder.append((char) random.nextInt(65, 91));
        } else {
            nameBuilder.append("-");
            nameBuilder.append(GREEK[random.nextInt(GREEK.length)]);
        }
        return nameBuilder.toString();
    }


    public boolean determinePlanetFound(int efficiency, double hours, int distance) {
        double base = hours * distance * efficiency;
        double randomNum = random.nextDouble(10.0);
        return base * randomNum > 50;
    }

    public int determineResourceReserves(int efficiency, double hours, boolean prioritize) {
        double weighedEfficiency = efficiency * (prioritize ? 15 : 0.5);
        double weighedHours = hours * (prioritize ? 40 : 5);
        double base = weighedEfficiency + weighedHours;
        int bound = (int) (200 * Math.ceil(efficiency / 10.0));
        int randomNum = random.nextInt(bound);
        int generatedNumber = (int) Math.round(base + randomNum);
        return Math.min(1000, generatedNumber);
    }

    public int determineDistance(int efficiency, int distance, boolean prioritize) {
        double weighedEfficiency = efficiency * (prioritize ? 1 : 0.5);
        double weighedDistance = distance * (prioritize ? 0.25 : 0.75);
        int origin = Math.max(1, (int) (distance - weighedEfficiency));
        int bound = (int) (distance + weighedDistance);
        return random.nextInt(origin, bound);
    }
}
