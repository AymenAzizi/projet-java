import java.util.ArrayList;

/**
 * Classe qui gère les niveaux du jeu
 * Chaque niveau a une configuration différente de blocs
 */
public class Level {
    private int levelNumber;
    private ArrayList<Block> blocks;
    private int remainingBlocks;
    
    /**
     * Constructeur de la classe Level
     * @param levelNumber Le numéro du niveau
     */
    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.blocks = new ArrayList<Block>();
        createLevel();
    }
    
    /**
     * Crée les blocs pour le niveau en fonction du numéro de niveau
     */
    private void createLevel() {
        blocks.clear();
        
        switch(levelNumber) {
            case 1:
                // Niveau 1 - Configuration standard
                for (int i = 0; i < 8; i++)
                    blocks.add(new Block((i*60+2), 0, 60, 25, "blue.png"));
                for (int i = 0; i < 8; i++)
                    blocks.add(new Block((i*60+2), 25, 60, 25, "green.png"));
                for (int i = 0; i < 8; i++)
                    blocks.add(new Block((i*60+2), 50, 60, 25, "yellow.png"));
                for (int i = 0; i < 8; i++)
                    blocks.add(new Block((i*60+2), 75, 60, 25, "red.png"));
                break;
                
            case 2:
                // Niveau 2 - Configuration en pyramide
                for (int i = 0; i < 8; i++)
                    blocks.add(new Block((i*60+2), 0, 60, 25, "blue.png"));
                for (int i = 1; i < 7; i++)
                    blocks.add(new Block((i*60+2), 25, 60, 25, "green.png"));
                for (int i = 2; i < 6; i++)
                    blocks.add(new Block((i*60+2), 50, 60, 25, "yellow.png"));
                for (int i = 3; i < 5; i++)
                    blocks.add(new Block((i*60+2), 75, 60, 25, "red.png"));
                break;
                
            case 3:
                // Niveau 3 - Configuration en damier
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 4; j++) {
                        if ((i + j) % 2 == 0) {
                            String color = j == 0 ? "blue.png" : j == 1 ? "green.png" : j == 2 ? "yellow.png" : "red.png";
                            blocks.add(new Block((i*60+2), j*25, 60, 25, color));
                        }
                    }
                }
                break;
                
            default:
                // Niveau par défaut - Configuration aléatoire plus difficile
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (Math.random() > 0.3) { // 70% de chance d'avoir un bloc
                            String color = j == 0 ? "blue.png" : j == 1 ? "green.png" : j == 2 ? "yellow.png" : "red.png";
                            Block block = new Block((i*60+2), j*25, 60, 25, color);
                            // Certains blocs nécessitent plusieurs coups (blocs spéciaux)
                            if (Math.random() > 0.7) {
                                block.setHitPoints(2);
                            }
                            blocks.add(block);
                        }
                    }
                }
                break;
        }
        
        remainingBlocks = blocks.size();
    }
    
    /**
     * Retourne la liste des blocs du niveau
     * @return ArrayList<Block> La liste des blocs
     */
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
    
    /**
     * Retourne le numéro du niveau
     * @return int Le numéro du niveau
     */
    public int getLevelNumber() {
        return levelNumber;
    }
    
    /**
     * Diminue le nombre de blocs restants
     */
    public void blockDestroyed() {
        remainingBlocks--;
    }
    
    /**
     * Vérifie si tous les blocs ont été détruits
     * @return boolean true si tous les blocs sont détruits, false sinon
     */
    public boolean isCompleted() {
        return remainingBlocks <= 0;
    }
}
