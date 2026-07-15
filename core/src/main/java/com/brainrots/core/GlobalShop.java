package com.brainrots.core;

import java.util.HashMap;

import com.brainrots.exception.InvalidGameActionException;
import com.brainrots.exception.PersistanceException;
import com.brainrots.interfaces.GameSaveHandler;

public class GlobalShop {
    private HashMap<String, ShopTowerData> shopData;

    public HashMap<String, ShopTowerData> getShopData() {
        return shopData;
    }

    public GlobalShop(GameSaveHandler saveHandler) {
        try {
            this.shopData = saveHandler.loadShopData();
        } catch (PersistanceException e) {
            e.printStackTrace();
        }
    }

    public void purchaseTower(PlayerProfile playerProfile, String towerId) throws InvalidGameActionException {
        ShopTowerData data = shopData.get(towerId);
        if (data == null)
            throw new InvalidGameActionException("Tower not found in the shop!");

        if (playerProfile.getUnlockedTowerIds().contains(towerId))
            throw new InvalidGameActionException("You already own this tower!");

        int price = data.getPrice();
        if (playerProfile.getGlobalCoins() < price)
            throw new InvalidGameActionException("Not enough coins to buy this tower!");

        playerProfile.removeGlobalCoins(price);
        playerProfile.unlockTower(towerId);
    }
}
