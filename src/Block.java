import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 * Classe qui représente un bloc, la balle ou la raquette
 */
public class Block extends Rectangle {

	private Image pic;
	boolean destroyed;
	private int hitPoints; // Nombre de coups nécessaires pour détruire le bloc
	private int blockValue; // Valeur en points du bloc
	private boolean isPowerUp; // Indique si le bloc contient un power-up
	private Color fallbackColor; // Couleur de secours si l'image ne se charge pas

	int movX, movY;

	/**
	 * Constructeur de la classe Block
	 * @param x Position x du bloc
	 * @param y Position y du bloc
	 * @param w Largeur du bloc
	 * @param h Hauteur du bloc
	 * @param s Nom de l'image du bloc
	 */
	Block(int x, int y, int w, int h, String s) {
		this.x = x;
		this.y = y;

		movX = 3;
		movY = 3;

		this.width = w;
		this.height = h;
		this.hitPoints = 1;
		this.destroyed = false;
		this.isPowerUp = false;

		// Définir la valeur du bloc en fonction de sa couleur
		if (s.contains("blue")) {
			this.blockValue = 10;
		} else if (s.contains("green")) {
			this.blockValue = 20;
		} else if (s.contains("yellow")) {
			this.blockValue = 30;
		} else if (s.contains("red")) {
			this.blockValue = 40;
		} else {
			this.blockValue = 0;
		}

		// 20% de chance d'avoir un power-up
		if (Math.random() < 0.2 && !s.equals("ball.png") && !s.equals("paddle.png")) {
			this.isPowerUp = true;
		}

		// Définir la couleur de secours en fonction du nom de l'image
		this.fallbackColor = Color.WHITE;
		if (s.contains("blue")) this.fallbackColor = Color.BLUE;
		else if (s.contains("green")) this.fallbackColor = Color.GREEN;
		else if (s.contains("yellow")) this.fallbackColor = Color.YELLOW;
		else if (s.contains("red")) this.fallbackColor = Color.RED;
		else if (s.contains("expand")) this.fallbackColor = new Color(0, 150, 255);
		else if (s.contains("shrink")) this.fallbackColor = new Color(255, 100, 100);
		else if (s.contains("slow")) this.fallbackColor = new Color(100, 255, 100);
		else if (s.contains("fast")) this.fallbackColor = new Color(255, 100, 255);
		else if (s.contains("life")) this.fallbackColor = new Color(255, 215, 0);

		// Essayer de charger l'image de plusieurs façons
		boolean imageLoaded = false;

		// Utiliser directement ImageIcon qui est plus robuste pour trouver les images
		ImageIcon icon = new ImageIcon(s);
		if (icon.getIconWidth() > 0) {
			pic = icon.getImage();
			imageLoaded = true;
			System.out.println("Image chargée avec succès: " + s);
		} else {
			System.err.println("Image non trouvée: " + s);
		}

		if (!imageLoaded) {
			// Stocker la couleur de secours pour l'utiliser dans la méthode draw
			System.err.println("Impossible de charger l'image: " + s + " - Utilisation d'une couleur de secours");
		}
	}

	/**
	 * Dessine le bloc
	 * @param g Graphics
	 * @param c Component
	 */
	public void draw(Graphics g, Component c) {
		if (!destroyed) {
			if (pic != null) {
				// Dessiner l'image si elle est chargée
				g.drawImage(pic, x, y, width, height, c);
			} else {
				// Utiliser la couleur de secours si l'image n'est pas chargée
				Color oldColor = g.getColor();
				g.setColor(fallbackColor);
				g.fillRect(x, y, width, height);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, width, height);
				g.setColor(oldColor);
			}

			// Si le bloc nécessite plusieurs coups, afficher le nombre de coups restants
			if (hitPoints > 1 && !isPaddle() && !isBall()) {
				g.setColor(Color.WHITE);
				g.drawString(String.valueOf(hitPoints), x + width/2 - 5, y + height/2 + 5);
			}
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
