package com.sonic.sonic_backend.domain;

public enum Tier {
    BRONZE_I,
    BRONZE_II,
    BRONZE_III,
    SILVER_I,
    SILVER_II,
    SILVER_III,
    GOLD_I,
    GOLD_II,
    GOLD_III;

    public String name;
    public String url;
    public int top;

    Tier() {
    }

    Tier(String name, String url, int top) {
        this.name = name;
        this.url = url;
        this.top = top;
    }
}
