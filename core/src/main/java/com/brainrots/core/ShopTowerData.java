package com.brainrots.core;

public class ShopTowerData {
    private String id;
    private String name;
    private String description;
    private String behaviorDescription;
    private int price;

    public ShopTowerData() {}

    public ShopTowerData(String id, String name, String description, String behaviorDescription, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.behaviorDescription = behaviorDescription;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getBehaviorDescription() { return behaviorDescription; }
    public int getPrice() { return price; }
}
