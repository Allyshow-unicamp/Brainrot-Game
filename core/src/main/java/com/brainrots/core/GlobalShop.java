package com.brainrots.core;

import java.util.HashMap;

import com.brainrots.exception.InvalidGameActionException;

public class GlobalShop {
    private HashMap<String, Integer> itemPrices;

    public HashMap<String, Integer> getItemPrices() {
        return itemPrices;
    }

    private GameSaveHandler saveHandler;

    public GlobalShop(String shopSaveFileName) {
        saveHandler = new JsonGameSaveHandler("", shopSaveFileName);
        try {
            this.itemPrices = saveHandler.loadShopData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void purchaseTower(PlayerProfile playerProfile, String towerId) throws InvalidGameActionException {
        int price = itemPrices.get(towerId);
        if (playerProfile.getGlobalCoins() < price)
            throw new InvalidGameActionException("Seu dinheiro não é suficiente para comprar essa torre!");

        playerProfile.removeGlobalCoins(price);
        playerProfile.unlockTower(towerId);
    }
}
