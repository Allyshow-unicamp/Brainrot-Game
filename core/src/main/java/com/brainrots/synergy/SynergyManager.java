package com.brainrots.synergy;

import com.badlogic.gdx.utils.Array;
import com.brainrots.tower.Tower;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Gerencia a avaliação e aplicação de sinergias (Observer indireto).
 *
 * A cada frame, reavalia todas as regras de sinergia contra a lista
 * atual de torres. Quando uma sinergia é ativada ou desativada, notifica
 * todas as torres aplicando ou removendo os boosts correspondentes.
 *
 * Funciona como Observer porque "observa" o estado das torres (via
 * evaluate()) e reage a mudanças (ativa/desativa sinergias), sem que
 * as torres precisem notificá-lo explicitamente. Isso evita acoplamento
 * bidirecional entre Tower e SynergyManager.
 *
 * O método evaluate() é idempotente: se a sinergia já estava ativa e
 * continua ativa, não faz nada (evita acumular boosts a cada frame).
 * A transição é detectada comparando o conjunto de regras ativas entre
 * frames sucessivos.
 */
public class SynergyManager {

    private List<SynergyRule> rules;
    private Set<SynergyRule> activeRules;

    public SynergyManager() {
        this.rules = new ArrayList<>();
        this.activeRules = new HashSet<>();
    }

    /**
     * Adiciona uma regra de sinergia ao gerenciador.
     * As regras são avaliadas na ordem em que são adicionadas.
     */
    public void addRule(SynergyRule rule) {
        rules.add(rule);
    }

    /**
     * Reavalia todas as regras contra a lista atual de torres.
     *
     * Deve ser chamada a cada frame no LevelManager.update(),
     * após o loop de atualização das torres.
     *
     * Para cada regra:
     * - Se estava inativa e agora está ativa → aplica boost em TODAS
     *   as torres (não apenas nas que ativaram, para cobrir o caso
     *   de torres adicionadas depois da ativação)
     * - Se estava ativa e agora está inativa → remove boost de todas
     * - Caso contrário → não faz nada
     *
     * Aplicar o boost em todas as torres (não apenas nas do grupo)
     * é intencional: algumas sinergias podem afetar torres de outros
     * grupos (ex: "Support Synergy" buffa todas as torres no alcance).
     * Cada SynergyRule.apply() decide internamente quem recebe o boost.
     */
    public void evaluate(Array<Tower> towers) {
        for (SynergyRule rule : rules) {
            boolean wasActive = activeRules.contains(rule);
            boolean isActive = rule.isActivated(towers);

            if (isActive) {
                // Regra ativa (nova ou já ativa): garantir que todas as torres
                // elegíveis recebam o boost (cobre torres colocadas depois).
                for (Tower tower : towers) {
                    rule.apply(tower);
                }
                if (!wasActive) {
                    activeRules.add(rule);
                }
            } else if (wasActive) {
                // Transição ativo → inativo: remover boost de todas
                for (Tower tower : towers) {
                    rule.remove(tower);
                }
                activeRules.remove(rule);
            }
        }
    }

    /** Verifica se uma regra específica está ativa no momento. */
    public boolean isActive(SynergyRule rule) {
        return activeRules.contains(rule);
    }

    /** Retorna o conjunto de regras atualmente ativas (apenas leitura). */
    public Set<SynergyRule> getActiveRules() {
        return activeRules;
    }
}
