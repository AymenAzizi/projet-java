import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe qui représente un power-up qui peut tomber d'un bloc détruit
 */
public class PowerUp extends Rectangle {
    
    public static final int TYPE_EXPAND_PADDLE = 1;
    public static final int TYPE_SHRINK_PADDLE = 2;
    public static final int TYPE_SLOW_BALL = 3;
    public static final int TYPE_FAST_BALL = 4;
    public static final int TYPE_EXTRA_LIFE = 5;
    
    private Image pic;
    private boolean active;
    private boolean collected;
    private int type;
    private int fallSpeed;
    
    /**
     * Constructeur de la classe PowerUp
     * @param x Position x du power-up
     * @param y Position y du power-up
     * @param type Type de power-up
     */
    public PowerUp(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 30;
        this.type = type;
        this.active = true;
        this.collected = false;
        this.fallSpeed = 2;
        
        // Chargement de l'image en fonction du type
        String imageName = "";
        switch(type) {
            case TYPE_EXPAND_PADDLE:
                imageName = "expand.png"; // À créer
                break;
            case TYPE_SHRINK_PADDLE:
                imageName = "shrink.png"; // À créer
                break;
            case TYPE_SLOW_BALL:
                imageName = "slow.png"; // À créer
                break;
            case TYPE_FAST_BALL:
                imageName = "fast.png"; // À créer
                break;
            case TYPE_EXTRA_LIFE:
                imageName = "life.png"; // À créer
                break;
            default:
                imageName = "powerup.png"; // À créer
        }
        
        try {
            pic = ImageIO.read(new File("src/" + imageName));
        } catch (IOException e) {
            // Si l'image n'existe pas, on utilise une image par défaut
            try {
                pic = ImageIO.read(new File("src/ball.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Dessine le power-up
     * @param g Graphics
     * @param c Component
     */
    public void draw(Graphics g, Component c) {
        if (active && !collected) {
            g.drawImage(pic, x, y, width, height, c);
        }
    }
    
    /**
     * Met à jour la position du power-up (le fait tomber)
     */
    public void update() {
        if (active && !collected) {
            y += fallSpeed;
        }
    }
    
    /**
     * Collecte le power-up
     */
    public void collect() {
        this.collected = true;
    }
    
    /**
     * Vérifie si le power-up est actif
     * @return boolean true si le power-up est actif, false sinon
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Vérifie si le power-up a été collecté
     * @return boolean true si le power-up a été collecté, false sinon
     */
    public boolean isCollected() {
        return collected;
    }
    
    /**
     * Retourne le type de power-up
     * @return int Le type de power-up
     */
    public int getType() {
        return type;
    }
    
    /**
     * Désactive le power-up
     */
    public void deactivate() {
        this.active = false;
    }
}
