package com.sherlockcodes.ubitricity.service;

import com.sherlockcodes.ubitricity.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StationService {

    @Autowired
    private StationRepository repository;

    private AtomicInteger currentCount;
    private AtomicInteger currentCars;

    public void add(int chargingPoint) throws Exception {
        if (repository.getStationValue(chargingPoint)!=0) throw new Exception();

        if (currentCount.get()<100) {
            givePowerFromReserve(chargingPoint);
        } else {
            if (repository.canDivertPower()) {
                divertPowerFromOtherCars(chargingPoint);
                if (!repository.canDivertPower()) repository.markAsSlowCharge(chargingPoint);
                else {
                    divertPowerFromOtherCars(chargingPoint);
                    repository.markAsFastCharge(chargingPoint);
                }
            }
        }
    }


    public void delete(int n) throws Exception {

        if (repository.getStationValue(n)==0) throw new Exception();

        int vehiclePower = repository.unPlugVehicle(n);
        currentCars.decrementAndGet();
        currentCount.addAndGet(-1*vehiclePower);
        if (vehiclePower == 1) {
            repository.unMarkAsSlowCharge(n);
            repository.donatePower();
        } else {
            repository.unMarkasFastCharge(n);
            if (repository.anySlowChargeExists()) {
                repository.donatePower();
                repository.donatePower();
            }
        }
    }


    public ConcurrentHashMap<Integer, Integer> getStatus() {
        return repository.getStatus();
    }

    public synchronized  void givePowerFromReserve(int n) {
        map.put(n, 2);
        fastQueue.add(n);
        currentPower += 20;
    }
    public synchronized void divertPowerFromOtherCars(int n) {

        Integer old = fastQueue.remove(0);
        map.put(old, 1);
        map.put(n, map.get(n) + 1);
        slowQueue.add(old);
        currentPower += 10;

    }
}
