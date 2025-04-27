import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe qui gère les statistiques du jeu (score, vies, etc.)
 */
public class GameStats {
    private int score;
    private int lives;
    private int currentLevel;
    private boolean gameOver;
    private boolean levelCompleted;
    private List<Integer> highScores;
    private static final String HIGH_SCORES_FILE = "highscores.txt";
    private static final int MAX_HIGH_SCORES = 5;
    
    /**
     * Constructeur de la classe GameStats
     */
    public GameStats() {
        this.score = 0;
        this.lives = 3;
        this.currentLevel = 1;
        this.gameOver = false;
        this.levelCompleted = false;
        this.highScores = new ArrayList<>();
        loadHighScores();
    }
    
    /**
     * Ajoute des points au score
     * @param points Le nombre de points à ajouter
     */
    public void addScore(int points) {
        this.score += points;
    }
    
    /**
     * Retourne le score actuel
     * @return int Le score actuel
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Retourne le nombre de vies restantes
     * @return int Le nombre de vies
     */
    public int getLives() {
        return lives;
    }
    
    /**
     * Diminue le nombre de vies
     */
    public void loseLife() {
        this.lives--;
        if (this.lives <= 0) {
            this.gameOver = true;
        }
    }
    
    /**
     * Ajoute une vie
     */
    public void addLife() {
        this.lives++;
    }
    
    /**
     * Vérifie si la partie est terminée
     * @return boolean true si la partie est terminée, false sinon
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Passe au niveau suivant
     */
    public void nextLevel() {
        this.currentLevel++;
        this.levelCompleted = false;
    }
    
    /**
     * Retourne le niveau actuel
     * @return int Le niveau actuel
     */
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    /**
     * Définit si le niveau est terminé
     * @param completed true si le niveau est terminé, false sinon
     */
    public void setLevelCompleted(boolean completed) {
        this.levelCompleted = completed;
    }
    
    /**
     * Vérifie si le niveau est terminé
     * @return boolean true si le niveau est terminé, false sinon
     */
    public boolean isLevelCompleted() {
        return levelCompleted;
    }
    
    /**
     * Réinitialise les statistiques pour une nouvelle partie
     */
    public void reset() {
        this.score = 0;
        this.lives = 3;
        this.currentLevel = 1;
        this.gameOver = false;
        this.levelCompleted = false;
    }
    
    /**
     * Charge les meilleurs scores depuis un fichier
     */
    private void loadHighScores() {
        File file = new File(HIGH_SCORES_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        highScores.add(Integer.parseInt(line));
                    } catch (NumberFormatException e) {
                        // Ignorer les lignes qui ne sont pas des nombres
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Sauvegarde les meilleurs scores dans un fichier
     */
    public void saveHighScores() {
        // Ajouter le score actuel à la liste
        highScores.add(score);
        
        // Trier les scores par ordre décroissant
        Collections.sort(highScores, Collections.reverseOrder());
        
        // Garder seulement les MAX_HIGH_SCORES meilleurs scores
        if (highScores.size() > MAX_HIGH_SCORES) {
            highScores = highScores.subList(0, MAX_HIGH_SCORES);
        }
        
        // Sauvegarder les scores dans un fichier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE))) {
            for (Integer highScore : highScores) {
                writer.write(highScore.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Retourne la liste des meilleurs scores
     * @return List<Integer> La liste des meilleurs scores
     */
    public List<Integer> getHighScores() {
        return highScores;
    }
    
    /**
     * Vérifie si le score actuel est un nouveau record
     * @return boolean true si c'est un nouveau record, false sinon
     */
    public boolean isNewHighScore() {
        if (highScores.isEmpty()) {
            return true;
        }
        
        return score > Collections.min(highScores) || highScores.size() < MAX_HIGH_SCORES;
    }
}
