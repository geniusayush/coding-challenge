package com.sherlockcodes.ubitricity;

import java.util.concurrent.atomic.AtomicInteger;

public class ParkState {
    private AtomicInteger currentCars;
    private AtomicInteger currentPower;

    public ParkState() {
        this.currentCars= new AtomicInteger(0);
        this.currentPower= new AtomicInteger(0);
    }

    public AtomicInteger getCurrentCars() {
        return currentCars;
    }

    public AtomicInteger getCurrentPower() {
        return currentPower;
    }

}
