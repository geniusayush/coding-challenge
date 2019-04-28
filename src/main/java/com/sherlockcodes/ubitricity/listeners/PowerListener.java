package com.sherlockcodes.ubitricity.listeners;

import com.sherlockcodes.ubitricity.enums.ChargeMode;
import com.sherlockcodes.ubitricity.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Scope("prototype")
public class PowerListener extends Thread {

    @Resource(name = "powerExtractQ")
    public LinkedBlockingQueue<Integer> powerQ;
    @Autowired
    StationRepository repository;

    @Override
    public void run() {

        System.out.println(getName() + " is running");
        while (true) {
            try {
                Integer val = powerQ.take();
                if(val.equals(ChargeMode.FAST_CHARGE.getValue())){

                }
                else{
                    repository.
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


    }

}