package com.sherlockcodes.ubitricity.unit;

import com.sherlockcodes.ubitricity.ParkState;
import com.sherlockcodes.ubitricity.repository.StationRepository;
import com.sherlockcodes.ubitricity.service.StationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.atomic.AtomicInteger;

import static com.sherlockcodes.ubitricity.enums.ChargeMode.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {
    private static final Logger logger = LogManager.getLogger(ServiceTest.class);
    @Mock
    private StationRepository repo;
    @Mock
    private ParkState state;

    private StationService service;

    @Before
    public void setup() {
        service = new StationService(repo, state);
    }

    @Test(expected = ResponseStatusException.class)
    public void exceptionIfCarPresentThereAlready() {
        when(repo.getStationValue(Mockito.anyInt())).thenReturn(FAST_CHARGE);
        service.add(2);
    }

    @Test(expected = ResponseStatusException.class)
    public void exceptionIfCarAbsentThereAlready() {
        when(repo.getStationValue(Mockito.anyInt())).thenReturn(AVAILABLE);
        service.delete(2);
    }

    @Test()
    public void testNothingHappensIfCarsLessThan5andCarleaves() {
        when(repo.getStationValue(2)).thenReturn(FAST_CHARGE);
        when(repo.addVehicle(2, AVAILABLE)).thenReturn(FAST_CHARGE);
        when(state.getCurrentCars()).thenReturn(new AtomicInteger(4));
        when(state.getCurrentPower()).thenReturn(new AtomicInteger(80));
        when(repo.anySlowChargeExists()).thenReturn(false);
        service.delete(2);

        verifyACarWasGivenFastCharge(0);
        Mockito.verify(repo, times(1)).unMarkAsFastCharge(2);
        Mockito.verify(repo, times(0)).unMarkAsSlowCharge(0);
    }

    @Test()
    public void testIfSlowChargeLeaves() {
        when(repo.getStationValue(2)).thenReturn(SLOW_CHARGE);
        when(repo.addVehicle(2, AVAILABLE)).thenReturn(SLOW_CHARGE);
        when(state.getCurrentCars()).thenReturn(new AtomicInteger(7));
        service.delete(2);

        verifyACarWasGivenFastCharge(1);
    }


    @Test()
    public void testIfFastChargeLeavesWhenSlowChargingCarsAvailable() {
        when(repo.getStationValue(Mockito.anyInt())).thenReturn(FAST_CHARGE);
        when(repo.addVehicle(2, AVAILABLE)).thenReturn(FAST_CHARGE);
        when(state.getCurrentCars()).thenReturn(new AtomicInteger(7));
        when(repo.anySlowChargeExists()).thenReturn(true);
        service.delete(2);

        verifyACarWasGivenFastCharge(2);
        Mockito.verify(repo, times(1)).unMarkAsFastCharge(2);

    }

    @Test()
    public void testPowerDivertedDuringAddCarWhen7Cars() {
        when(repo.getStationValue(2)).thenReturn(AVAILABLE);
        when(state.getCurrentPower()).thenReturn(new AtomicInteger(100));
        when(state.getCurrentCars()).thenReturn(new AtomicInteger(7));
        service.add(2);
        verifyPowerWasTakenFromCars(2);
        Mockito.verify(repo, times(1)).markAsFastCharge(2);
        Mockito.verify(repo, times(1)).addVehicle(2, FAST_CHARGE);

    }

    @Test()
    public void testNOPowerDivertedDuringAddCarWhen3Cars() {
        when(repo.getStationValue(2)).thenReturn(AVAILABLE);
        when(state.getCurrentPower()).thenReturn(new AtomicInteger(60));
        when(state.getCurrentCars()).thenReturn(new AtomicInteger(3));
        service.add(2);
        verifyPowerWasTakenFromCars(0);
        Mockito.verify(repo, times(1)).markAsFastCharge(2);
        Mockito.verify(repo, times(1)).addVehicle(2, FAST_CHARGE);

    }

    @Test()
    public void testSlowChargeDuringAddCarWhen9Cars() {
        when(repo.getStationValue(2)).thenReturn(AVAILABLE);
        when(state.getCurrentPower()).thenReturn(new AtomicInteger(100));
        when(state.getCurrentCars()).thenReturn(new AtomicInteger(9));
        when(repo.getCArToBeChargedSlowly()).thenReturn(0);
        service.add(2);
        verifyPowerWasTakenFromCars(1);
        Mockito.verify(repo, times(1)).markAsSlowCharge(2);
        Mockito.verify(repo, times(1)).addVehicle(2, SLOW_CHARGE);

    }

    private void verifyACarWasGivenFastCharge(int times) {
        Mockito.verify(repo, times(times)).getCarToBeUpgraded();
        Mockito.verify(repo, times(times)).addVehicle(0, FAST_CHARGE);
        Mockito.verify(repo, times(times)).markAsFastCharge(0);
    }

    private void verifyPowerWasTakenFromCars(int times) {
        Mockito.verify(repo, times(times)).getCArToBeChargedSlowly();
        Mockito.verify(repo, times(times)).markAsSlowCharge(0);
        Mockito.verify(repo, times(times)).addVehicle(0, SLOW_CHARGE);
    }

}
