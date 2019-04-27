package com.sherlockcodes.ubitricity.repository;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class StationRepository {
    private ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
    private CopyOnWriteArrayList<Integer> fastQueue = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Integer> slowQueue = new CopyOnWriteArrayList<>();
    private int currentPower = 0;


    public StationRepository() {
        for (int i = 1; i <= 10; i++) {
            map.put(i, 0);
        }
    }
    public synchronized boolean isSlotFree(int n) {
        return map.get(n).equals(0);
    }
    public synchronized boolean isPowerAvailable() {
        return getTotalSupply() < 100;
    }

    public synchronized void markAsSlowCharge(int n) {
        slowQueue.add(n);
    }

    public synchronized void markAsFastCharge(int n) {
        fastQueue.add(n);
    }

    public synchronized  void givePowerFromReserve(int n) {
        map.put(n, 2);
        fastQueue.add(n);
        currentPower += 20;
    }

    public synchronized double getTotalSupply() {
        return currentPower;
    }
    public synchronized void divertPowerFromOtherCars(int n) {

        Integer old = fastQueue.remove(0);
        map.put(old, 1);
        map.put(n, map.get(n) + 1);
        slowQueue.add(old);
        currentPower += 10;

    }
    public synchronized boolean canDivertPower() {
        if (fastQueue.size() == 0) return false;
        return true;
    }
    public synchronized void donatePower() {
        int index = slowQueue.remove(slowQueue.size() - 1);
        map.put(index, 2);
        fastQueue.add(index);
    }

    public synchronized int unPlugVehicle(int n) {
        int val;
        val = map.get(n);
        map.put(n, 0);
        return val;
    }
    public boolean anySlowChargeExists() {
        return slowQueue.size() != 0;
    }

    public void unMarkasFastCharge(int n) {
        fastQueue.remove(n);
    }

    public void unMarkAsSlowCharge(int n) {
        slowQueue.remove(n);
    }


    public Object getStatus() {
        return map;
    }
}
