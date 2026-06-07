package com.brainrots.core;

import java.util.Map;

import com.brainrots.exception.InvalidGameActionException;
import com.brainrots.exception.PersistanceException;

public class GlobalShop {
    private Map<String, Integer> itemPrices;

    public Map<String, Integer> getItemPrices() {
        return itemPrices;
    }

    private GameSaveHandler saveHandler;

    public GlobalShop(String readFile) {
        saveHandler = new JsonGameSaveHandler("", readFile);
        try {
            this.itemPrices = saveHandler.loadShopData();
        } catch (PersistanceException e) {
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
