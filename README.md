# Brick Breaker Deluxe

Un jeu de casse-briques (Brick Breaker) amélioré, développé en Java avec Swing.

> **Note:** Ce projet a été développé dans le cadre d'un exercice de programmation Java et d'amélioration d'une application existante.

## Fonctionnalités

- Interface utilisateur moderne et attrayante
- Système de score et de vies
- Plusieurs niveaux avec différentes configurations de blocs
- Blocs spéciaux qui nécessitent plusieurs coups pour être détruits
- Power-ups qui tombent des blocs détruits (agrandir la raquette, ralentir la balle, etc.)
- Système de sauvegarde des meilleurs scores
- Mode pause
- Rebond dynamique de la balle sur la raquette en fonction de l'angle d'impact

## Comment jouer

### Contrôles
- Utilisez les flèches **GAUCHE** et **DROITE** pour déplacer la raquette
- Appuyez sur **ESPACE** pour lancer la balle
- Appuyez sur **P** pour mettre le jeu en pause
- Appuyez sur **ECHAP** pour quitter la partie

### Objectif
- Détruisez tous les blocs pour passer au niveau suivant
- Certains blocs nécessitent plusieurs coups pour être détruits
- Collectez les power-ups qui tombent des blocs pour obtenir des bonus
- Vous avez 3 vies au départ

### Power-ups
- **Agrandir la raquette** : Augmente la taille de la raquette
- **Rétrécir la raquette** : Diminue la taille de la raquette
- **Ralentir la balle** : Réduit la vitesse de la balle
- **Accélérer la balle** : Augmente la vitesse de la balle
- **Vie supplémentaire** : Ajoute une vie

## Installation

### Prérequis
- Java JDK 8 ou supérieur

### Étapes d'installation
1. Clonez ce dépôt : `git clone https://github.com/AymenAzizi/projet-java.git`
2. Naviguez vers le répertoire du projet : `cd projet-java`
3. Compilez le projet : `javac -d bin src/*.java`
4. Exécutez le jeu : `java -cp bin Main`

## Structure du projet

```
projet-java/
├── src/                  # Code source
│   ├── Main.java         # Point d'entrée du programme
│   ├── Block.java        # Classe représentant les blocs, la balle et la raquette
│   ├── BlockBreakerPanel.java # Panneau principal du jeu
│   ├── GameStats.java    # Gestion des statistiques (score, vies)
│   ├── Level.java        # Gestion des niveaux
│   ├── PowerUp.java      # Gestion des power-ups
│   └── *.png             # Images du jeu
├── .gitignore            # Fichier de configuration Git
├── highscores.txt        # Fichier des meilleurs scores
└── README.md             # Ce fichier
```

## Améliorations futures

- Ajout d'effets sonores et de musique
- Plus de niveaux avec des configurations plus complexes
- Nouveaux types de power-ups
- Mode multijoueur
- Éditeur de niveaux
- Thèmes visuels personnalisables

## Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou à soumettre une pull request.

## Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## Auteur

- **Aymen Azizi** - [GitHub](https://github.com/AymenAzizi)

---

Développé dans le cadre d'un projet personnel pour améliorer mes compétences en programmation Java.
