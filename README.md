# The Lord of Brainrots: The Battle for Brainrot Village II: The Rise of the Brainrot King

> Um **Tower Defense INSANO** construído com **libGDX 1.14.0** e **Java 25**.

---

## Sumário

1. [Ideia geral do jogo](#1-ideia-geral-do-jogo)
2. [Conceitos de POO e arquiteturas aplicados](#2-conceitos-de-poo-e-arquiteturas-aplicados)
3. [Visão geral do código](#3-visão-geral-do-código)
4. [Funcionalidades do jogo](#4-funcionalidades-do-jogo)
5. [Planejamento vs Implementação](#5-planejamento-vs-implementação)
6. [Como compilar e executar](#6-como-compilar-e-executar)
7. [Uso de IA generativa](#7-uso-de-ia-generativa)
8. [Autores](#8-autores)

---

## 1. Ideia geral do jogo

**The Lord of Brainrots** é um jogo de **Tower Defense** ambientado no universo dos "brainrots" italianos da internet, e no universo de Angry Birds. O jogador assume o papel de defensor de uma vila, posicionando torres temáticas — cada uma inspirada em um brainrot — para lutar contra ondas cada vez mais numerosas e resistentes de inimigos — Angry Birds — que percorrem um caminho até a base.

### Premissa

Uma horda de criaturas coloridas (bolas vermelhas, azuis, verdes, laranjas e brancas) avança por trilhas sinuosas rumo à base do jogador. O objetivo é posicionar e melhorar torres estrategicamente ao longo do caminho para eliminar todos os inimigos antes que cheguem ao destino. Se a base for destruída, é **game over**.

### Torre temáticas

| Torre | Personalidade | Tipo de ataque |
|-------|---------------|----------------|
| **Tung Tung Tung Sahur** | Básica, custo acessível | Projétil individual |
| **Bombardiro Crocodilo** | Pesada, explosiva | Projétil com área de efeito (AOE) + fogo DOT |
| **Nuclearo Dinosauro** | Devastadora | Dano instantâneo em área + fogo DOT |
| **Cappuccino Assassino** | Econômica | Gera moedas com base em torres aliadas próximas |
| **Ballerina Cappuccina** | Suporte | Aumenta o dano de torres aliadas próximas |

### Mapas disponíveis

- **67Hills** — Mapa inicial com cores vibrantes
- **WinterAura** — Terreno invernal
- **SigmaVolcano** — Vulcão escaldante
- **MiningRizz** — Minas subterrâneas
- **BetaJungle** — Selva exuberante

Cada mapa possui um layout único de caminhos, barreiras e ondas progressivas de inimigos.

---

## 2. Conceitos de POO e arquiteturas aplicados

O projeto faz uso intensivo de conceitos de Programação Orientada a Objetos e padrões de design clássicos da engenharia de software.

### 2.1 Pilares da POO aplicados

#### Encapsulamento

- **`GameBalance.java`** centraliza todas as constantes de balanceamento (HP, dano, custos, thresholds) em um único ponto, protegendo o restante do código de *magic numbers*.
- **`PlayerProfile.java`** encapsula os dados persistentes do jogador (moedas globais, estrelas por nível, torres desbloqueadas) com acesso controlado via getters.
- **`SynergyBoost`** é um *Value Object* imutável que encapsula multiplicadores de dano, alcance e cooldown, garantindo que os valores não sejam alterados externamente.
- **`CoinManager`** encapsula a lógica de validação de saldo, impedindo que moedas fiquem negativas.

#### Herança

A hierarquia de herança é profunda e bem definida:

```
Collidable (interface)
  └── GameObject (abstrato)
        ├── Enemy
        ├── Projectile
        └── Tower (abstrato)
              ├── TowerProjSingle
              ├── TowerProjArea
              ├── TowerArea
              ├── TowerCoin
              └── TowerBoost

ScreenAdapter (libGDX)
  └── GameScreen (abstrato)
        ├── MainMenuScreen
        ├── LevelSelectionScreen
        ├── GameplayScreen
        ├── GlobalShopScreen
        └── EndGameScreen

StatusEffect (abstrato)
  └── FireEffect
```

#### Polimorfismo

- Cada subclasse de `Tower` implementa `update()` de forma diferente, definindo comportamentos de ataque únicos (projétil único, AOE, dano instantâneo, geração de moedas, suporte).
- `GameMode` define um contrato (`evaluate()`) que `StandardMode` implementa com regras específicas de vitória/derrota.
- `WaveGenerationStrategy` permite que `FiniteWaveStrategy` forneça ondas de um arquivo JSON, podendo ser substituída por qualquer outra implementação.
- `SynergyRule` define um contrato que `ElementalSynergy` implementa para regras de contagem de torres.

#### Abstração

- `GameObject` abstrai a noção de "entidade com posição, textura e colisão" para todas as entidades do jogo.
- `GameScreen` abstrai o ciclo de vida de uma tela (inicialização, renderização, disposal) no padrão Template Method.
- `GameSaveHandler` abstrai a persistência, desacoplando a lógica de jogo do formato de armazenamento.

### 2.2 Padrões de design utilizados

| Padrão | Onde | Propósito detalhado |
|--------|------|---------------------|
| **State** | `GameMode` → `StandardMode` | Permite trocar regras de vitória/derrota sem modificar o game loop. Um modo infinito (removido) coexistia com o modo padrão. |
| **Strategy** | `SynergyRule`, `WaveGenerationStrategy`, `StatusEffect` | Comportamentos intercambiáveis: regras de sinergia, geração de ondas e efeitos de status podem ser adicionados sem alterar classes existentes. |
| **Template Method** | `StatusEffect.update()`, `GameScreen.render()` | Define o esqueleto de um algoritmo com hooks (`tick()`, `onExpire()`, `drawScreen()`) que as subclasses preenchem. |
| **Prototype** | `StatusEffect.copy()`, `EnemyTemplate.spawn()`, `BrainrotTower.createEffectPrototype()` | Clona objetos existentes para criar instâncias independentes, evitando construção repetitiva e garantindo isolamento de estado. |
| **Observer** | `SynergyManager.evaluate()` | Avalia continuamente o estado do tabuleiro e reage a mudanças (torre adicionada/removida) ativando ou desativando boosters. |
| **Factory** | `EnemyFactory` + `EnemyTemplate`, `TowerPlacementController.createTowerFromType()` | Centraliza a criação de objetos complexos (inimigos e torres) em pontos específicos, simplificando o código cliente. |
| **Singleton** | `EnemyFactory`, `GameResourceManager` | Garante instância única para o gerenciamento de assets e para o repositório de templates de inimigos. |
| **Facade** | `LevelManager` | Coordena e simplifica a interação entre subsistemas complexos (mapa, base, ondas, sinergias, moedas, projéteis, UI). |
| **Value Object** | `WaveData`, `ShopTowerData`, `SynergyBoost`, `GameOutcome` | Objetos imutáveis que carregam dados sem comportamento, facilitando transferência e comparação. |

### 2.3 Princípios SOLID aplicados

- **Single Responsibility**: Cada classe tem uma responsabilidade clara — `CoinManager` gerencia moedas, `WaveManager` gerencia ondas, `SynergyManager` gerencia sinergias, `ProjectileManager` gerencia projéteis.
- **Open/Closed**: Padrões como Strategy e State permitem adicionar novos modos, efeitos e estratégias sem modificar código existente.
- **Liskov Substitution**: Todas as subclasses de `Tower` e `Screen` podem ser usadas onde o tipo base é esperado.
- **Interface Segregation**: Interfaces pequenas e focadas — `Collidable` (apenas colisão), `Spawnable` (apenas criação), `SynergyRule` (apenas regra de sinergia).
- **Dependency Inversion**: `LevelManager` depende de abstrações (`GameMode`, `WaveGenerationStrategy`) e não de implementações concretas.

---

## 3. Visão geral do código

### 3.1 Estrutura do projeto

O projeto é organizado como um **projeto Gradle multi-módulo** com dois módulos principais:

| Módulo | Caminho | Responsabilidade |
|--------|---------|------------------|
| **core** | `core/` | Toda a lógica do jogo, independente de plataforma |
| **lwjgl3** | `lwjgl3/` | Launcher desktop via LWJGL3/OpenGL |

### 3.2 Pacotes do módulo `core`

```
core/src/main/java/com/brainrots/
├── core/               # Classe principal, gerenciamento de assets, save/load, perfil
│   ├── BrainRotGame.java          # Ponto de entrada do jogo, gerencia telas
│   ├── Assets.java                # Constantes de caminhos de todos os assets
│   ├── GameBalance.java           # Constantes de balanceamento centralizadas
│   ├── GameResourceManager.java   # Singleton para carregamento de assets (textures, sounds, fonts)
│   ├── PlayerProfile.java         # Modelo de dados do jogador
│   ├── JsonGameSaveHandler.java   # Persistência em JSON
│   ├── GlobalShop.java            # Loja meta-progression
│   └── ShopTowerData.java         # DTO de dados de torre na loja
│
├── effect/             # Sistema de efeitos de status
│   ├── StatusEffect.java          # Classe abstrata base (Strategy + Template Method + Prototype)
│   └── FireEffect.java            # Efeito de fogo DOT
│
├── enemy/              # Sistema de inimigos
│   ├── Enemy.java                 # Entidade inimiga com movimento, HP, efeitos
│   ├── EnemyFactory.java          # Factory + Singleton para criação de inimigos
│   ├── EnemyTemplate.java         # Template Prototype para spawn de inimigos
│   ├── EnemySpawner.java          # Timer de spawn dentro de uma onda
│   └── Spawnable.java             # Interface de criação
│
├── engine/             # Motor central do jogo
│   ├── LevelManager.java          # Orquestrador principal do game loop (Facade)
│   ├── Map.java                   # Carregador de mapas Tiled (.tmx)
│   ├── Base.java                  # Base do jogador com HP e cálculo de estrelas
│   ├── CoinManager.java           # Gerenciador de moedas da partida
│   ├── ProjectileManager.java     # Ciclo de vida de projéteis (spawn, update, draw, remove)
│   └── TowerPlacementController.java  # Controle de input para posicionamento de torres
│
├── engine/mode/        # Modos de jogo (State Pattern)
│   ├── GameMode.java              # Interface de modo de jogo
│   ├── GameOutcome.java           # Enum: NONE, VICTORY, DEFEAT
│   └── StandardMode.java          # Modo padrão (vitória = todas ondas limpas)
│
├── entity/             # Entidades base do jogo
│   ├── GameObject.java            # Classe abstrata com posição, textura, colisão
│   └── Collidable.java            # Interface de colisão
│
├── exception/          # Exceções customizadas
│   ├── InvalidGameActionException.java
│   └── PersistanceException.java
│
├── interfaces/         # Contratos e utilitários
│   ├── ScreenManager.java         # Interface de navegação entre telas
│   ├── GameSaveHandler.java       # Interface de persistência
│   └── StageHelper.java           # Utilitário para atualização segura de Stage
│
├── MainUX/             # Interface do usuário in-game
│   ├── MainUX.java                # HUD principal (sidebar de torres, botões de onda/velocidade)
│   ├── TowerButton.java           # Botão de seleção de torre
│   ├── TowerUpgradePanel.java     # Painel de upgrade e venda de torres
│   ├── TowerInfoModal.java        # Modal de informações da torre
│   └── ConfirmModal.java          # Modal de confirmação (sair do nível)
│
├── screen/             # Telas do jogo
│   ├── GameScreen.java            # Classe abstrata base (Template Method)
│   ├── MainMenuScreen.java        # Tela principal (Menu/Loja/Sair)
│   ├── LevelSelectionScreen.java  # Seleção de nível com estrelas
│   ├── GameplayScreen.java        # Tela de gameplay principal
│   ├── GlobalShopScreen.java      # Loja de desbloqueio permanente
│   └── EndGameScreen.java         # Tela de vitória/derrota
│
├── synergy/            # Sistema de sinergias
│   ├── SynergyManager.java        # Orquestrador (Observer) que avalia regras
│   ├── SynergyRule.java           # Interface de regra (Strategy)
│   ├── ElementalSynergy.java      # Sinergia por contagem de grupo
│   ├── SynergyBoost.java          # Value Object de multiplicadores
│   └── SynergyGroup.java          # Enum: FIRE, ICE, PHYSICAL, SUPPORT
│
├── tower/              # Sistema de torres
│   ├── Tower.java                 # Classe abstrata base com dano, alcance, upgrades
│   ├── BrainrotTower.java         # Enum com 5 tipos de torre e seus stats
│   ├── Projectile.java            # Entidade de projétil com movimento e colisão
│   ├── TowerProjSingle.java       # Torre de projétil individual
│   ├── TowerProjArea.java         # Torre de projétil AOE
│   ├── TowerArea.java             # Torre de dano instantâneo em área
│   ├── TowerCoin.java             # Torre geradora de moedas
│   └── TowerBoost.java            # Torre de suporte/buff
│
└── wave/               # Sistema de ondas
    ├── WaveManager.java           # Gerenciador de progressão de ondas
    ├── WaveGenerationStrategy.java # Interface de geração (Strategy)
    ├── FiniteWaveStrategy.java    # Estratégia baseada em JSON
    └── WaveData.java              # Value Object de dados de onda
```

### 3.3 Assets

```
assets/
├── Maps/                 # mapas (cada um com .tmx, .png, waves.json)
├── Towers/               # sprites das torres
├── Enemies/              # inimigos (red, blue, green, orange, white)
├── Projectiles/          # projéteis
├── CCoins/               # Sprite de moedas
├── UI/                   # Fontes, botões, modais, skin
├── Soundtracks/          # Trilha sonora (game_song.mp3)
└── data/                 # Dados JSON (level-data, shop-data, player-data)
```

---

## 4. Funcionalidades do jogo

### 4.1 Gameplay principal

- **Sistema de Tower Defense**: inimigos seguem um caminho polilinal do spawn até a base; o jogador posiciona torres para interceptá-los.
- **5 tipos de torre** com mecânicas distintas (projétil, AOE, dano instantâneo, economia, suporte).
- **Sistema de upgrades**: 3 eixos de melhoria por torre — dano, alcance e cooldown — com custos crescentes.
- **Venda de torres**: reembolso de 100% do valor investido em upgrades (tirando dinheiro da compra).
- **Sistema de sinergias**: agrupar 3+ torres do mesmo elemento (FIRE, ICE, PHYSICAL, SUPPORT) ativa multiplicadores de dano, alcance e cooldown. As torres mudam visualmente quando a sinergia é ativada.
- **Efeitos de status**: fogo DOT (dano por tempo) aplicável por projéteis.
- **Controle de velocidade**: 1x e 2x.
- **Controle de ondas**: pausa e resume manual da geração de inimigos.
- **Ondas por nível** com dificuldade progressiva, definidas em JSON.

### 4.2 Inimigos

| Tipo | Cor | Papel |
|------|-----|-------|
| Básico | Vermelho | Inimigo padrão |
| Tanque | Azul | Alta vida, baixa velocidade |
| Veloz | Verde | Alta velocidade, baixa vida |
| Boss | Laranja | Muita vida, recompensa alta |
| Super Boss | Branca | O mais resistente |

### 4.3 Sistema de estrelas e progressão

- **1-3 estrelas** por nível com base na vida restante da base.
- **Recompensas em moedas**: moedas dependendo do número estrelas.
- **Desbloqueio sequencial**: nível N+1 só é acessível após completar o nível N.
- **Loja global**: moedas acumuladas podem ser usadas para desbloquear torres permanentemente entre níveis.
- **Perfil persistente**: dados salvos em JSON (moedas, estrelas, torres desbloqueadas).

### 4.4 Interface do usuário

- **Menu principal**: Jogar, Loja, Sair.
- **Seleção de nível**: lista scrollável com exibição de estrelas conquistadas.
- **HUD in-game**: HP da base, moedas, contador de ondas, botões de velocidade/onda/menu.
- **Sidebar de torres**: painel scrollável com botões de compra, preços e botões de info.
- **Painel de upgrade**: imagem, nome, descrição, 3 botões de upgrade, botão de venda.
- **Modal de info**: overlay com stats completos da torre.
- **Modal de confirmação**: diálogo Yes/No para sair do nível.
- **Tela de fim**: exibição de estrelas, recompensa e botão de retorno.

### 4.5 Mapas

Cada um dos 5 mapas possui:
- Arquivo **TiledMap** (`.tmx`) com layers de caminho (Polyline), barreiras (Polygon) e fundo (Image).
- Background em PNG.
- Arquivo `waves.json` definindo as ondas com tipos, quantidades, intervalos e tempos de spawn.

---

## 5. Planejamento vs Implementação

A seção a seguir compara as funcionalidades propostas nas atividades iniciais da disciplina com o que de fato foi implementado, discutindo as razões técnicas e de tempo que motivaram as mudanças.

### 5.1 Funcionalidades planejadas que foram implementadas

| Funcionalidade planejada | Implementação atual | Status |
|--------------------------|---------------------|--------|
| Selecionar um mapa | `LevelSelectionScreen` com 5 mapas, `Map.java` carrega `.tmx` via TiledMap | **Implementado** |
| Ganhar moedas derrotando inimigos | `CoinManager` adiciona moedas ao saldo ao eliminar inimigos (`LevelManager`) | **Implementado** |
| Comprar torres com moedas | Sidebar em `MainUX` com botões de compra, `CoinManager.canAfford()` / `subtractCoins()` | **Implementado** |
| Posicionar torre fora do caminho | `TowerPlacementController` com validação de área via grid sampling e detecção de barreiras/caminho | **Implementado** |
| Melhorar torres com moedas | `TowerUpgradePanel` com 3 eixos de upgrade (dano, alcance, cooldown) | **Implementado** |
| Ganhar dinheiro ao finalizar mapas | `EndGameScreen` concede moedas globais dependendo do número de estrelas | **Implementado** |
| Comprar novas torres na loja | `GlobalShopScreen` + `GlobalShop` para desbloqueio permanente com moedas globais | **Implementado** |
| Sistema de combinações/sinergias | `SynergyManager` + `ElementalSynergy` (FIRE, ICE, PHYSICAL, SUPPORT) | **Implementado** |
| Estrelas por desempenho | `Base.java` calcula estrelas com base no HP restante | **Implementado** |
| Desbloqueio sequencial de níveis | `LevelSelectionScreen` libera nível N+1 ao completar N com pelo menos 1 estrela | **Implementado** |

### 5.2 Funcionalidades adaptadas ou removidas

| Funcionalidade | O que aconteceu | Motivo |
|----------------|-----------------|--------|
| Efeitos inter-agentes (fogo + gelo + veneno) | Reduzido para apenas fogo DOT | Custo quadrático de implementação e balanceamento entre pares de efeitos |
| Modo infinito | Implementado e depois removido | Conflitava com o sistema de estrelas; balanceamento de escala dinâmica muito complexo |
| Combinações fixas de torres específicas | Reestruturado para grupos elementais (FIRE, ICE, PHYSICAL, SUPPORT) | Combinatória explosiva de interações; modelo por grupo é extensível e previsível |
| Mecânicas exclusivas por nível | Mapas diferem visualmente, mas compartilham mecânica base | Exigiria sistema de modificadores por nível com complexidade de manutenção proibitiva |

Todas as decisões priorizaram **qualidade e estabilidade** do que foi implementado sobre quantidade de funcionalidades.

---

## 6. Como compilar e executar

### 6.1 Pré-requisitos

- **Java 25** (JDK 25 com suporte a `--enable-native-access`)
- **Gradle** (wrapper incluso no projeto)

### 6.2 Executar o jogo

```bash
# Linux / macOS
./gradlew lwjgl3:run

# Windows
gradlew.bat lwjgl3:run
```

### 6.3 Gerar JAR executável

```bash
# JAR multi-plataforma
./gradlew lwjgl3:jar

# JAR otimizado por plataforma (menor tamanho)
./gradlew jarWin      # Windows (~6MB menor)
./gradlew jarLinux    # Linux (~5MB menor)
./gradlew jarMac      # macOS (~7MB menor)
```

### 6.4 Executar testes unitários

```bash
./gradlew :core:test
```

### 6.5 Build completo (distribuição)

```bash
./gradlew dist
```

### 6.6 GraalVM Native Image (opcional)

Habilitar em `gradle.properties`:

```properties
enableGraalNative=true
```

Suporta compilação nativa para Linux x64, macOS (M1 + x64) e Windows x64 via plugin Construo.

---

## 7. Uso de IA generativa

IA generativa foi utilizada ao longo do desenvolvimento nas seguintes áreas:

- **Interfaces (UI/UX)**: Telas e componentes do Scene2D — `MainUX`, `TowerUpgradePanel`, `TowerInfoModal`, `ConfirmModal`, `GameplayScreen`, `LevelSelectionScreen`, `GlobalShopScreen`.
- **Posicionamento de torres** (`TowerPlacementController`): Algoritmo de grid sampling para validação de área, cálculo de distância ponto-segmento, ghost preview com feedback visual, detecção de drag vs click e factory method de criação de torres.
- **Sistema de sinergias** (`SynergyManager`, `ElementalSynergy`): Modelagem do padrão Observer com avaliação contínua, detecção de transição de estado entre frames, design de classe genérica reutilizável, Value Object imutável para multiplicadores e integração visual de troca de texturas.
- **Documentação**: Javadoc consistente em português, este README e comentários de arquitetura explicando padrões de design.
- **Casos de teste**: testes unitários com JUnit 5, incluindo *test doubles* manuais para contornar limitações de mocking do libGDX.
- **Correção de bugs**: Concorrência na remoção de entidades, renderização Scene2D, persistência JSON, memory leaks e balanceamento.
- **Código e refatoração**: Boilerplate Gradle multi-módulo, enums com dados, factory methods e configuração cross-platform (`StartupHelper`).

---

## 8. Autores

|          Nome          |       User        |
|------------------------|-------------------|
| Allyson Gomes De Souza | @Allyshow-unicamp |
