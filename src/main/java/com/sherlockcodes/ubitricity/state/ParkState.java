package com.sherlockcodes.ubitricity.state;

import java.util.concurrent.atomic.AtomicInteger;

public class ParkState {
    public final Integer maxPower = 100;
    public final Integer maxCars = 10;
    private final AtomicInteger currentPower;
    private final AtomicInteger currentCars;

    public ParkState() {
        currentPower = new AtomicInteger(0);
        currentCars = new AtomicInteger(0);
    }

    public AtomicInteger getCurrentPower() {
        return currentPower;
    }

    public AtomicInteger getCurrentCars() {
        return currentCars;
    }
}
