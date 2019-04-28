package com.sherlockcodes.ubitricity.repository;

import com.sherlockcodes.ubitricity.enums.ChargeMode;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class StationRepository {
    private ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
    private CopyOnWriteArrayList<Integer> fastQueue = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Integer> slowQueue = new CopyOnWriteArrayList<>();



    public StationRepository() {
        for (int i = 1; i <= 10; i++) {
            map.put(i, 0);
        }
    }


    public synchronized void markAsSlowCharge(int n) {
        slowQueue.add(n);
    }

    public synchronized void markAsFastCharge(int n) {
        fastQueue.add(n);
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


    public boolean anySlowChargeExists() {
        return slowQueue.size() != 0;
    }

    public void unMarkasFastCharge(int n) {
        fastQueue.remove(n);
    }

    public void unMarkAsSlowCharge(int n) {
        slowQueue.remove(n);
    }


    public ConcurrentHashMap<Integer, Integer> getStatus() {
        return map;
    }

    public int getStationValue(int n) {
        return map.get(n);
    }
    public synchronized int unPlugVehicle(int n) {
        return map.put(n,0);

    }
    public void addVehicle(int index,ChargeMode power){
        map.put(index,power.getValue());
    }
}
