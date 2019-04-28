package com.sherlockcodes.ubitricity.service;

import com.sherlockcodes.ubitricity.ParkState;
import com.sherlockcodes.ubitricity.enums.ChargeMode;
import com.sherlockcodes.ubitricity.repository.StationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static com.sherlockcodes.ubitricity.enums.ChargeMode.*;

@Service
public class StationService {
    private static final Logger logger = LogManager.getLogger(StationService.class);
    private static final Integer MAX_CARS = 10;
    private static final Integer MAX_POWER = 100;

    private final StationRepository repo;
    private final ParkState state;


    @Autowired
    public StationService(final StationRepository repo, final ParkState state) {
        this.state = state;
        this.repo = repo;
    }

    public synchronized void add(int chargingPoint) {
        if (repo.getStationValue(chargingPoint) != AVAILABLE) {
            logger.info("Invalid request passed as %d is already charging", chargingPoint);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "the point is already connected");
        }
        if (state.getCurrentPower().get() < MAX_POWER) {
            givePowerFromReserve(chargingPoint);
        } else if (state.getCurrentCars().get() != MAX_CARS) {

            if (state.getCurrentCars().get() == MAX_CARS - 1) {
                divertPowerFromOtherCars(1);
                repo.addVehicle(chargingPoint, SLOW_CHARGE);
                repo.markAsSlowCharge(chargingPoint);

            } else {
                divertPowerFromOtherCars(2);
                repo.addVehicle(chargingPoint, FAST_CHARGE);
                repo.markAsFastCharge(chargingPoint);

            }
            state.getCurrentCars().incrementAndGet();
        } else {
            logger.info("ignoring request as the park is full ");
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "the park is already full");
        }
    }

    public synchronized void delete(int n) {

        if (repo.getStationValue(n) == AVAILABLE) throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "the point is already removed");

        ChargeMode vehiclePower = repo.addVehicle(n, AVAILABLE);
        state.getCurrentCars().decrementAndGet();
        if (vehiclePower == SLOW_CHARGE) {
            repo.unMarkAsSlowCharge(n);
            donatePower(1);
        } else {
            repo.unMarkAsFastCharge(n);
            if (repo.anySlowChargeExists()) {
                donatePower(2);

            } else {
                state.getCurrentPower().addAndGet(-1 * vehiclePower.getValue());
            }
        }
    }


    public Map<Integer, String> getStatus() {
        return repo.getStatus();
    }

    private synchronized void donatePower(int num) {
        for (int i = 0; i < num; i++) {
            int index = repo.getCarToBeUpgraded();
            repo.addVehicle(index, FAST_CHARGE);
            repo.markAsFastCharge(index);
        }
    }

    private void givePowerFromReserve(int n) {
        logger.info("point %d was given charge from reserve quota", n);
        repo.addVehicle(n, FAST_CHARGE);
        repo.markAsFastCharge(n);
        state.getCurrentPower().addAndGet(FAST_CHARGE.getValue());
        state.getCurrentCars().incrementAndGet();

    }

    private void divertPowerFromOtherCars(int num) {
        logger.info("two card will be put to slow charge");
        for (int i = 0; i < num; i++) {
            Integer old = repo.getCArToBeChargedSlowly();
            repo.markAsSlowCharge(old);
            repo.addVehicle(old, SLOW_CHARGE);
        }

    }
}