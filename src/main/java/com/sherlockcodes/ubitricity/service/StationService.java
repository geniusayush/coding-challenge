package com.sherlockcodes.ubitricity.service;

import com.sherlockcodes.ubitricity.enums.ChargeMode;
import com.sherlockcodes.ubitricity.exception.ParkException;
import com.sherlockcodes.ubitricity.repository.StationRepository;
import com.sherlockcodes.ubitricity.state.ParkState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.sherlockcodes.ubitricity.enums.ChargeMode.FAST_CHARGE;

@Service
public class StationService {

    @Resource(name = "waitQ")
    public ConcurrentLinkedQueue<Integer> waitQ;
    @Resource(name = "powerExtractQ")
    public ConcurrentLinkedQueue<Integer> powerQ;
    @Autowired
    private StationRepository repository;
    @Resource(name = "parkState")
    private ParkState state;

    public void add(int chargingPoint) throws Exception {
        if (repository.getStationValue(chargingPoint) != 0) throw new ParkException("point Already Occupied");

        if (state.getCurrentPower().get() < state.maxPower)
            givePowerFromReserve(chargingPoint);
        else if (state.getCurrentCars().get() != state.maxCars
            if (state.getCurrentCars().get() == state.maxCars - 1) {
                powerQ.add(ChargeMode.SLOW_CHARGE.getValue());
            } else {
                powerQ.add(FAST_CHARGE.getValue());
            }
            waitQ.add(chargingPoint);
            state.getCurrentCars().incrementAndGet();
        }
    }

    public void delete(int n) throws Exception {

        if (repository.getStationValue(n) == 0) throw new ParkException("station is already empty");

        int vehiclePower = repository.unPlugVehicle(n);
        if (vehiclePower == ChargeMode.SLOW_CHARGE.getValue()) {
            repository.unMarkAsSlowCharge(n);
        } else {
            repository.unMarkasFastCharge(n);
        }
        state.getCurrentCars().decrementAndGet();
        state.getCurrentPower().addAndGet(-1 * vehiclePower);

    }

    public ConcurrentHashMap<Integer, Integer> getStatus() {
        return repository.getStatus();
    }

    private synchronized void givePowerFromReserve(int n) {
        repository.addVehicle(n, FAST_CHARGE);
        repository.markAsFastCharge(n);
        state.getCurrentPower().addAndGet(FAST_CHARGE.getValue());
        state.getCurrentCars().incrementAndGet();
    }
    /* divertPowerFromOtherCars(chargingPoint);
     if (!repository.canDivertPower()) repository.markAsSlowCharge(chargingPoint);
     else {
         divertPowerFromOtherCars(chargingPoint);
         repository.markAsFastCharge(chargingPoint);
     }
 }
    public synchronized void divertPowerFromOtherCars(int n) {

        Integer old = fastQueue.remove(0);
        map.put(old, 1);
        map.put(n, map.get(n) + 1);
        slowQueue.add(old);
        currentPower += 10;

    }*/
}
