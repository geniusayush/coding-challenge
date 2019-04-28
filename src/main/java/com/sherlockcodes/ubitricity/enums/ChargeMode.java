package com.sherlockcodes.ubitricity.enums;

public enum ChargeMode {

    SLOW_CHARGE(10, "OCCUPIED 0A"),
    FAST_CHARGE(20, "OCCUPIED 20A"),
    AVAILABLE(0, "AVAILABLE");

    private final int value;
    private final String text;

    ChargeMode(int i, String text) {
        this.value = i;
        this.text = text;
    }

    public int getValue() {
        return value;
    }


    public String getText() {
        return text;
    }
}
