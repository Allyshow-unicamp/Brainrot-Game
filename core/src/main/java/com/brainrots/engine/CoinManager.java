package com.brainrots.engine;

/** Gerencia moedas da partida. Valida saldo e impede subtração abaixo de zero. */
public class CoinManager {

    private int coins;

    public CoinManager(int startingCoins) {
        this.coins = startingCoins;
    }

    public int getCoins() {
        return coins;
    }

    public boolean canAfford(int cost) {
        return coins >= cost;
    }

    public void subtractCoins(int cost) {
        if (canAfford(cost)) {
            coins -= cost;
        }
    }

    public void addCoins(int amount) {
        coins += amount;
    }
}
