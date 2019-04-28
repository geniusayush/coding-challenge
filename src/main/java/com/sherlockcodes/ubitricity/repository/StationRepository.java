package com.sherlockcodes.ubitricity.repository;

import com.sherlockcodes.ubitricity.enums.ChargeMode;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class StationRepository {

    private final ConcurrentHashMap<Integer, ChargeMode> map = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<Integer> fastQueue = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Integer> slowQueue = new CopyOnWriteArrayList<>();

    public StationRepository() {
        for (int i = 1; i <= 10; i++) {
            map.put(i, ChargeMode.AVAILABLE);
        }
    }

    public  void markAsSlowCharge(int n) {
        slowQueue.add(n);
    }

    public  void markAsFastCharge(int n) {
        fastQueue.add(n);
    }

    public boolean anySlowChargeExists() {
        return slowQueue.size() != 0;
    }

    public void unMarkAsFastCharge(int n) {
        fastQueue.remove(fastQueue.indexOf(n));
    }

    public void unMarkAsSlowCharge(int n) {
        slowQueue.remove(slowQueue.indexOf(n));
    }

    public synchronized Map<Integer, String> getStatus() {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getText()));
    }

    public ChargeMode getStationValue(int n) {
        return map.get(n);
    }

    public Integer getCArToBeChargedSlowly() {
        return fastQueue.remove(0);
    }

    public ChargeMode addVehicle(int n, ChargeMode fastCharge) {
        return map.put(n, fastCharge);
    }

    public int getCarToBeUpgraded() {
        return slowQueue.remove(slowQueue.size() - 1);
    }
}
