# ⚔️ Age of War — Jeu de stratégie en Java

Un jeu de stratégie en temps réel inspiré du célèbre jeu Flash **Age of War**, développé en Java avec **Swing** dans le cadre d'un projet académique.

> **Projet réalisé par** Romain Sebire & Joris Schwarz — IMT Mines Alès

---

## 📖 Description

Age of War est un jeu de stratégie en 1v1 (joueur vs IA) où deux bases s'affrontent à travers **3 époques** d'évolution. Le joueur doit détruire la base adverse tout en protégeant la sienne.

### Mécaniques de jeu

- **3 types d'unités** : Mêlée, Distance et Tank — chacun avec des caractéristiques propres (PV, dégâts, portée, coût, vitesse)
- **3 époques d'évolution** : Faites évoluer votre civilisation pour débloquer des unités plus puissantes (visuels et stats améliorés)
- **Système économique** : Gagnez de l'argent en éliminant des ennemis pour recruter des troupes ou évoluer
- **IA adverse** : L'adversaire recrute et envoie automatiquement des unités
- **Bande-son** : Musique d'ambiance intégrée
- **Interface graphique complète** : Affichage du plateau, sprites animés, boutons de contrôle, HUD avec stats en temps réel

### Captures d'écran conceptuelles

Le jeu affiche :
- Un **plateau de jeu** avec un arrière-plan thématique
- Les **bases** du joueur (gauche) et de l'adversaire (droite) avec barres de vie
- Les **unités** se déplaçant et combattant sur le champ de bataille
- Un **HUD** avec l'argent disponible, les coûts des unités et un bouton d'évolution

---

## 🏗️ Architecture du projet

```
src/projet_ageofwar/
├── Projet_AgeOfWar.java    # Point d'entrée (main)
├── Affichage.java           # Interface graphique (Swing JFrame, boutons, HUD)
├── PlateauDeJeu.java        # Logique du plateau (déplacements, combats, timers)
├── Base.java                # Gestion des bases (PV, argent, évolution, rendu)
├── Unite.java               # Unité du joueur (déplacement, combat, rendu)
├── UniteAdversaire.java     # Unité adverse (IA, déplacement inversé)
├── Donnees.java             # Chargement des données depuis XML (JDOM2)
└── graphiques/              # Sprites (PNG) + fond + bande-son (WAV)
    ├── background.jpg
    ├── base_epoque{1-3}.png / base_epoque{1-3}_adversaire.png
    ├── melee_epoque{1-3}.png / melee_epoque{1-3}_adversaire.png
    ├── distance_epoque{1-3}.png / distance_epoque{1-3}_adversaire.png
    ├── tank_epoque{1-3}.png / tank_epoque{1-3}_adversaire.png
    └── soundtrack.wav
```

### Données XML

Les caractéristiques de chaque unité et base (PV, dégâts, portée, coût, dimensions des sprites) sont stockées dans `caracteristiquesPersonnages.xml` et chargées dynamiquement au lancement via la bibliothèque **JDOM2**.

---

## 🛠️ Technologies

| Composant | Technologie |
|-----------|-------------|
| Langage | **Java** |
| Interface graphique | **Java Swing** (JFrame, JPanel, JButton, JLabel) |
| Rendu graphique | `paintComponent()` personnalisé avec `Graphics.drawImage()` |
| Données de jeu | **XML** parsé avec **JDOM2** |
| Audio | `javax.sound.sampled` (Clip, AudioInputStream) |
| Temporisation | `javax.swing.Timer` pour la boucle de jeu |
| Build | **Apache Ant** (NetBeans) |

---

## 🚀 Prérequis & Exécution

### Prérequis

- **Java JDK 8+**
- **JDOM2** (`jdom-2.0.6.1.jar`) — bibliothèque de parsing XML

### Compilation et exécution

```bash
# Depuis le dossier version-soumise/ (ou version-travail/)
javac -cp ".:path/to/jdom-2.0.6.1.jar" -d build src/projet_ageofwar/*.java
java -cp "build:path/to/jdom-2.0.6.1.jar" projet_ageofwar.Projet_AgeOfWar
```

Ou via **NetBeans** : ouvrir le projet et exécuter directement.

---

## 📂 Versions du projet

Ce dépôt contient **deux versions** du projet :

| Dossier | Description |
|---------|-------------|
| `version-travail/` | Version de travail (développement initial) |
| `version-soumise/` | Version finale soumise pour évaluation — avec Javadoc complète sur l'ensemble des méthodes |

Les deux versions partagent **exactement la même logique de jeu**. La version soumise ajoute une documentation Javadoc complète et supprime les imports inutilisés.

---

## 🎮 Comment jouer

1. Lancez le jeu et cliquez sur **"Démarrer le jeu"**
2. Utilisez les boutons pour recruter des unités :
   - **Guerrier mêlée** — Unité de combat rapproché (bon marché)
   - **Guerrier distance** — Unité à distance (portée supérieure)
   - **Tank** — Unité lourde (PV et dégâts élevés, coûteux)
3. Cliquez sur **"Évoluer"** pour passer à l'époque suivante (améliore toutes les stats)
4. Détruisez la base adverse pour gagner !
5. **Rejouer** ou **Terminer** en fin de partie

---

## 📚 Concepts Java démontrés

- Programmation orientée objet (héritage, encapsulation)
- Interface graphique avec Java Swing
- Rendu personnalisé (`paintComponent`, `Graphics2D`)
- Gestion d'événements (ActionListener, Timer)
- Parsing XML avec JDOM2
- Gestion audio avec `javax.sound.sampled`
- Chargement de ressources depuis le classpath
- Architecture modulaire (séparation affichage / logique / données)

---

## 📝 Licence

Projet académique — IMT Mines Alès.
