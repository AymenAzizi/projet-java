import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Version simplifiée de Block qui utilise des couleurs au lieu d'images
 */
public class ColorBlock extends Rectangle {
    
    private Color color;
    boolean destroyed;
    private int hitPoints;
    private int blockValue;
    private boolean isPowerUp;
    
    int movX, movY;
    
    /**
     * Constructeur de la classe ColorBlock
     * @param x Position x du bloc
     * @param y Position y du bloc
     * @param w Largeur du bloc
     * @param h Hauteur du bloc
     * @param colorName Nom de la couleur ("blue", "green", "yellow", "red", etc.)
     */
    ColorBlock(int x, int y, int w, int h, String colorName) {
        this.x = x;
        this.y = y;
        
        movX = 3;
        movY = 3;
        
        this.width = w;
        this.height = h;
        this.hitPoints = 1;
        this.destroyed = false;
        this.isPowerUp = false;
        
        // Définir la couleur et la valeur en fonction du nom
        if (colorName.equals("blue") || colorName.equals("blue.png")) {
            this.color = Color.BLUE;
            this.blockValue = 10;
        } else if (colorName.equals("green") || colorName.equals("green.png")) {
            this.color = Color.GREEN;
            this.blockValue = 20;
        } else if (colorName.equals("yellow") || colorName.equals("yellow.png")) {
            this.color = Color.YELLOW;
            this.blockValue = 30;
        } else if (colorName.equals("red") || colorName.equals("red.png")) {
            this.color = Color.RED;
            this.blockValue = 40;
        } else if (colorName.equals("ball") || colorName.equals("ball.png")) {
            this.color = Color.WHITE;
            this.blockValue = 0;
        } else if (colorName.equals("paddle") || colorName.equals("paddle.png")) {
            this.color = new Color(200, 200, 200);
            this.blockValue = 0;
        } else {
            this.color = Color.GRAY;
            this.blockValue = 0;
        }
        
        // 20% de chance d'avoir un power-up
        if (Math.random() < 0.2 && !colorName.equals("ball.png") && !colorName.equals("paddle.png")) {
            this.isPowerUp = true;
        }
    }
    
    /**
     * Dessine le bloc
     * @param g Graphics
     * @param c Component
     */
    public void draw(Graphics g, Component c) {
        if (!destroyed) {
            Color oldColor = g.getColor();
            
            // Remplir le bloc avec sa couleur
            g.setColor(color);
            g.fillRect(x, y, width, height);
            
            // Ajouter un contour
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
            
            // Si c'est la balle, la dessiner comme un cercle
            if (width == 50 && height == 45) {
                g.setColor(Color.WHITE);
                g.fillOval(x, y, width, height);
                g.setColor(Color.BLACK);
                g.drawOval(x, y, width, height);
            }
            
            // Si le bloc nécessite plusieurs coups, afficher le nombre
            if (hitPoints > 1 && !isPaddle() && !isBall()) {
                g.setColor(Color.WHITE);
                g.drawString(String.valueOf(hitPoints), x + width/2 - 5, y + height/2 + 5);
            }
            
            g.setColor(oldColor);
        }
    }
    
    /**
     * Diminue le nombre de coups nécessaires pour détruire le bloc
     * @return boolean true si le bloc est détruit, false sinon
     */
    public boolean hit() {
        hitPoints--;
        if (hitPoints <= 0) {
            destroyed = true;
            return true;
        }
        return false;
    }
    
    /**
     * Définit le nombre de coups nécessaires pour détruire le bloc
     * @param hitPoints Nombre de coups
     */
    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }
    
    /**
     * Retourne le nombre de coups nécessaires pour détruire le bloc
     * @return int Nombre de coups
     */
    public int getHitPoints() {
        return hitPoints;
    }
    
    /**
     * Retourne la valeur en points du bloc
     * @return int Valeur en points
     */
    public int getBlockValue() {
        return blockValue;
    }
    
    /**
     * Vérifie si le bloc contient un power-up
     * @return boolean true si le bloc contient un power-up, false sinon
     */
    public boolean hasPowerUp() {
        return isPowerUp;
    }
    
    /**
     * Vérifie si le bloc est la raquette
     * @return boolean true si c'est la raquette, false sinon
     */
    public boolean isPaddle() {
        return width == 200 && height == 40;
    }
    
    /**
     * Vérifie si le bloc est la balle
     * @return boolean true si c'est la balle, false sinon
     */
    public boolean isBall() {
        return width == 50 && height == 45;
    }
    
    /**
     * Modifie la taille de la raquette
     * @param factor Facteur de modification (1.5 pour agrandir, 0.5 pour réduire)
     */
    public void resizePaddle(double factor) {
        if (isPaddle()) {
            int oldWidth = width;
            width = (int)(width * factor);
            // Ajuster la position pour que la raquette reste centrée
            x += (oldWidth - width) / 2;
        }
    }
}
