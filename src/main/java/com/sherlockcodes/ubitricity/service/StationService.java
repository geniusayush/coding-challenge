package com.sherlockcodes.ubitricity.service;

import com.sherlockcodes.ubitricity.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationService {

    @Autowired
    StationRepository repository;

    public void add(int chargingPoint) throws Exception {
        if (!repository.isSlotFree(chargingPoint)) throw new Exception();
        if (repository.isPowerAvailable()) {
            repository.givePowerFromReserve(chargingPoint);
        } else {
            if (repository.canDivertPower()) {
                repository.divertPowerFromOtherCars(chargingPoint);
                if (!repository.canDivertPower()) repository.markAsSlowCharge(chargingPoint);
                else {
                    repository.divertPowerFromOtherCars(chargingPoint);
                    repository.markAsFastCharge(chargingPoint);
                }
            }
        }
    }


    public void delete(int n) throws Exception {
        if (repository.isSlotFree(n)) throw new Exception();
        int vehiclePower = repository.unPlugVehicle(n);
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


    public Object getStatus() {
       return repository.getStatus();
    }
}
