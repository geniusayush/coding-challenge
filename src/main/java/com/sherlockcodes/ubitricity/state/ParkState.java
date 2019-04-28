package com.sherlockcodes.ubitricity.state;

import io.swagger.models.auth.In;

import java.util.concurrent.atomic.AtomicInteger;

public class ParkState
{
    public final Integer maxPower=100;
    public AtomicInteger currentPower;
    public  AtomicInteger currentCars;
    public final Integer maxCars=10;

    public ParkState() {
        currentPower=new AtomicInteger(0);
        currentCars= new AtomicInteger(0);
    }
}
