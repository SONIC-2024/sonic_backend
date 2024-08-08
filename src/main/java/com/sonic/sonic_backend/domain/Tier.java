package com.sonic.sonic_backend.domain;

public enum Tier {
    BRONZE_I("BRONZE I","BRONZEI.png",70),
    BRONZE_II("BRONZE II","BRONZEII.png",80),
    BRONZE_III("BRONZE III", "BRONZEIII.png", 90),
    SILVER_I("SILVER I", "SILVERI.png", 40),
    SILVER_II("SILVER II", "SILVERII.png", 50),
    SILVER_III("SILVER III", "SILVERIII.png", 60),
    GOLD_I("GOLD I","GOLDI.png", 10),
    GOLD_II("GOLD II","GOLDII.png",20),
    GOLD_III("GOLD III","GOLDIII.png",30),
    IRON("IRON", "IRON.ong",100);

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
