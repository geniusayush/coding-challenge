package com.sherlockcodes.ubitricity.enums;

public enum ChargeMode {

    SLOW_CHARGE(10),
    FAST_CHARGE(20),;

    private final int value;

    ChargeMode(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
