package com.brainrots.core;

import java.util.Map;

import com.brainrots.exception.InvalidGameActionException;

public class GlobalShop {
    private Map<String, Integer> itemPrices;

    public void purchaseTower(PlayerProfile playerProfile, String towerId) throws InvalidGameActionException {
        int price = itemPrices.get(towerId);
        if (playerProfile.getGlobalCoins() < price)
            throw new InvalidGameActionException("Seu dinheiro não é suficiente para comprar essa torre!");

        playerProfile.removeGlobalCoins(price);
        playerProfile.unlockTower(towerId);
    }
}
