package com.brainrots.synergy;

import com.badlogic.gdx.utils.Array;
import com.brainrots.tower.Tower;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Avalia e aplica sinergias entre torres a cada frame.
 * Detecta transições de estado (ativação/desativação) para aplicar
 * ou remover boosts apenas quando necessário.
 */
public class SynergyManager {

    private List<SynergyRule> rules;
    private Set<SynergyRule> activeRules;

    public SynergyManager() {
        this.rules = new ArrayList<>();
        this.activeRules = new HashSet<>();
    }

    public void addRule(SynergyRule rule) {
        rules.add(rule);
    }

    /** Reavalia todas as regras. Deve ser chamado a cada frame no LevelManager. */
    public void evaluate(Array<Tower> towers) {
        for (SynergyRule rule : rules) {
            boolean wasActive = activeRules.contains(rule);
            boolean isActive = rule.isActivated(towers);

            if (isActive) {
                for (Tower tower : towers) {
                    rule.apply(tower);
                }
                if (!wasActive) {
                    activeRules.add(rule);
                }
            } else if (wasActive) {
                for (Tower tower : towers) {
                    rule.remove(tower);
                }
                activeRules.remove(rule);
            }
        }
    }

    public boolean isActive(SynergyRule rule) {
        return activeRules.contains(rule);
    }

    public Set<SynergyRule> getActiveRules() {
        return activeRules;
    }
}
