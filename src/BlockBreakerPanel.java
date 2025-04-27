import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Panneau principal du jeu qui gère la logique et l'affichage
 */
public class BlockBreakerPanel extends JPanel implements KeyListener {

	private ArrayList<Block> blocks;
	private Block ball;
	private Block paddle;
	private ArrayList<PowerUp> powerUps;
	private Level currentLevel;
	private GameStats stats;

	private JFrame mainFrame, startScreen;

	private Thread thread;
	private boolean paused;
	private boolean gameStarted;

	private static final int BALL_SPEED_NORMAL = 3;
	private static final int PADDLE_SPEED = 15;

	/**
	 * Réinitialise le jeu pour une nouvelle partie ou un nouveau niveau
	 */
	void reset() {
		blocks = new ArrayList<Block>();
		powerUps = new ArrayList<PowerUp>();
		ball = new Block(300, 435, 50, 45, "ball.png");
		paddle = new Block(175, 480, 200, 40, "paddle.png");

		// Charger le niveau actuel
		currentLevel = new Level(stats.getCurrentLevel());
		blocks = currentLevel.getBlocks();

		// Vérifier si les blocs ont été correctement chargés
		if (blocks.isEmpty()) {
			System.out.println("ATTENTION: Aucun bloc n'a été chargé! Création manuelle des blocs...");
			// Créer manuellement les blocs si le niveau n'a pas fonctionné
			for (int i = 0; i < 8; i++)
				blocks.add(new Block((i*60+2), 0, 60, 25, "blue.png"));
			for (int i = 0; i < 8; i++)
				blocks.add(new Block((i*60+2), 25, 60, 25, "green.png"));
			for (int i = 0; i < 8; i++)
				blocks.add(new Block((i*60+2), 50, 60, 25, "yellow.png"));
			for (int i = 0; i < 8; i++)
				blocks.add(new Block((i*60+2), 75, 60, 25, "red.png"));
		}

		System.out.println("Nombre de blocs chargés: " + blocks.size());

		ball.movX = BALL_SPEED_NORMAL;
		ball.movY = BALL_SPEED_NORMAL;

		paused = false;
		gameStarted = false;

		addKeyListener(this);
		setFocusable(true);
	}

	/**
	 * Constructeur du panneau de jeu
	 * @param frame Fenêtre principale du jeu
	 * @param startScreen Écran de démarrage
	 */
	BlockBreakerPanel(JFrame frame, JFrame startScreen) {
		this.mainFrame = frame;
		this.startScreen = startScreen;

		// Initialiser les statistiques
		stats = new GameStats();

		reset();

		thread = new Thread(() -> {
			while (true) {
				if (!paused) {
					update();
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException err) {
					err.printStackTrace();
				}
			}
		});
		thread.start();
	}

	/**
	 * Dessine tous les éléments du jeu
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dessiner l'arrière-plan
		g.setColor(new Color(0, 0, 30)); // Bleu foncé
		g.fillRect(0, 0, getWidth(), getHeight());

		// Dessiner les blocs avec information de débogage
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		g.drawString("Nombre de blocs: " + blocks.size(), 10, 80);

		// Dessiner chaque bloc avec un contour visible
		for (Block block : blocks) {
			// Dessiner le bloc normalement
			block.draw(g, this);

			// Ajouter un contour visible pour le débogage
			if (!block.destroyed && !block.isPaddle() && !block.isBall()) {
				g.setColor(Color.WHITE);
				g.drawRect(block.x, block.y, block.width, block.height);
			}
		}

		// Dessiner les power-ups
		powerUps.forEach(powerUp -> {
			powerUp.draw(g, this);
		});

		// Dessiner la balle et la raquette
		ball.draw(g, this);
		paddle.draw(g, this);

		// Afficher le score et les vies
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString("Score: " + stats.getScore(), 10, 20);
		g.drawString("Vies: " + stats.getLives(), 10, 40);
		g.drawString("Niveau: " + stats.getCurrentLevel(), 10, 60);

		// Afficher un message si le jeu est en pause
		if (paused) {
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.drawString("PAUSE", getWidth()/2 - 50, getHeight()/2);
			g.setFont(new Font("Arial", Font.PLAIN, 16));
			g.drawString("Appuyez sur P pour continuer", getWidth()/2 - 100, getHeight()/2 + 30);
		}

		// Afficher un message pour démarrer le jeu
		if (!gameStarted) {
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Appuyez sur ESPACE pour lancer la balle", getWidth()/2 - 180, getHeight()/2 + 100);
		}
	}

	/**
	 * Met à jour l'état du jeu
	 */
	public void update() {
		// Ne pas mettre à jour si le jeu n'a pas commencé
		if (!gameStarted) {
			// La balle reste sur la raquette
			ball.x = paddle.x + paddle.width/2 - ball.width/2;
			ball.y = paddle.y - ball.height;
			repaint();
			return;
		}

		// Mettre à jour la position de la balle
		ball.x += ball.movX;

		// Rebondir sur les bords latéraux
		if(ball.x > (getWidth() - ball.width) || ball.x < 0)
			ball.movX *= -1;

		// Rebondir sur le haut ou sur la raquette
		if(ball.y < 0)
			ball.movY *= -1;

		// Rebondir sur la raquette avec un angle différent selon l'endroit où la balle touche
		if (ball.intersects(paddle)) {
			ball.movY *= -1;

			// Calculer l'angle de rebond en fonction de l'endroit où la balle touche la raquette
			// Centre de la raquette = rebond droit, côtés = rebond angulé
			int paddleCenter = paddle.x + paddle.width/2;
			int ballCenter = ball.x + ball.width/2;
			int offset = ballCenter - paddleCenter;

			// Modifier la direction horizontale en fonction de l'offset
			ball.movX = offset / 10;

			// S'assurer que la balle a toujours une vitesse horizontale minimale
			if (ball.movX > -1 && ball.movX < 1) {
				ball.movX = (ball.movX >= 0) ? 1 : -1;
			}

			// Limiter la vitesse maximale
			if (ball.movX > 5) ball.movX = 5;
			if (ball.movX < -5) ball.movX = -5;
		}

		ball.y += ball.movY;

		// Si la balle tombe en bas de l'écran
		if(ball.y > getHeight()) {
			stats.loseLife();

			if (stats.isGameOver()) {
				// Fin de partie
				showGameOver();
			} else {
				// Réinitialiser la position de la balle et de la raquette
				ball.x = 300;
				ball.y = 435;
				paddle.x = 175;
				paddle.y = 480;
				gameStarted = false;
			}
		}

		// Mettre à jour les power-ups et vérifier les collisions
		Iterator<PowerUp> powerUpIterator = powerUps.iterator();
		while (powerUpIterator.hasNext()) {
			PowerUp powerUp = powerUpIterator.next();
			powerUp.update();

			// Vérifier si le power-up est collecté par la raquette
			if (powerUp.intersects(paddle) && !powerUp.isCollected()) {
				powerUp.collect();
				applyPowerUp(powerUp.getType());
				powerUpIterator.remove();
			}

			// Supprimer les power-ups qui sortent de l'écran
			if (powerUp.y > getHeight()) {
				powerUpIterator.remove();
			}
		}

		// Vérifier les collisions avec les blocs
		Iterator<Block> blockIterator = blocks.iterator();
		while (blockIterator.hasNext()) {
			Block block = blockIterator.next();
			if (ball.intersects(block) && !block.destroyed) {
				// Changer la direction de la balle
				ball.movY *= -1;

				// Diminuer les points de vie du bloc
				if (block.hit()) {
					// Le bloc est détruit
					stats.addScore(block.getBlockValue());

					// Vérifier si le bloc contient un power-up
					if (block.hasPowerUp()) {
						powerUps.add(new PowerUp(block.x + block.width/2, block.y,
								(int)(Math.random() * 5) + 1)); // Type aléatoire
					}

					// Mettre à jour le nombre de blocs restants
					currentLevel.blockDestroyed();

					// Vérifier si le niveau est terminé
					if (currentLevel.isCompleted()) {
						stats.setLevelCompleted(true);

						// Vérifier si c'est le dernier niveau
						if (stats.getCurrentLevel() >= 3) {
							// Victoire finale
							showVictory();
						} else {
							// Passer au niveau suivant
							JOptionPane.showMessageDialog(this,
									"Niveau " + stats.getCurrentLevel() + " terminé !\n" +
									"Score actuel : " + stats.getScore() + "\n" +
									"Passez au niveau suivant !");
							stats.nextLevel();
							reset();
						}
					}
				}
			}
		}

		repaint();
	}

	/**
	 * Applique l'effet d'un power-up
	 * @param type Type de power-up
	 */
	private void applyPowerUp(int type) {
		switch (type) {
			case PowerUp.TYPE_EXPAND_PADDLE:
				paddle.resizePaddle(1.5);
				break;
			case PowerUp.TYPE_SHRINK_PADDLE:
				paddle.resizePaddle(0.75);
				break;
			case PowerUp.TYPE_SLOW_BALL:
				ball.movX = (ball.movX > 0) ? 2 : -2;
				ball.movY = (ball.movY > 0) ? 2 : -2;
				break;
			case PowerUp.TYPE_FAST_BALL:
				ball.movX = (ball.movX > 0) ? 5 : -5;
				ball.movY = (ball.movY > 0) ? 5 : -5;
				break;
			case PowerUp.TYPE_EXTRA_LIFE:
				stats.addLife();
				break;
		}
	}

	/**
	 * Affiche l'écran de fin de partie
	 */
	private void showGameOver() {
		thread = null;

		// Sauvegarder le score
		if (stats.isNewHighScore()) {
			stats.saveHighScores();
			JOptionPane.showMessageDialog(this,
					"Game Over!\nNouveau record : " + stats.getScore() + " points!");
		} else {
			JOptionPane.showMessageDialog(this,
					"Game Over!\nScore final : " + stats.getScore() + " points");
		}

		// Réinitialiser les statistiques
		stats.reset();

		// Revenir à l'écran de démarrage
		mainFrame.setVisible(false);
		startScreen.setVisible(true);
	}

	/**
	 * Affiche l'écran de victoire
	 */
	private void showVictory() {
		thread = null;

		// Sauvegarder le score
		if (stats.isNewHighScore()) {
			stats.saveHighScores();
			JOptionPane.showMessageDialog(this,
					"Félicitations! Vous avez terminé le jeu!\n" +
					"Nouveau record : " + stats.getScore() + " points!");
		} else {
			JOptionPane.showMessageDialog(this,
					"Félicitations! Vous avez terminé le jeu!\n" +
					"Score final : " + stats.getScore() + " points");
		}

		// Réinitialiser les statistiques
		stats.reset();

		// Revenir à l'écran de démarrage
		mainFrame.setVisible(false);
		startScreen.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Non utilisé
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Déplacer la raquette
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddle.x < (getWidth() - paddle.width)) {
			paddle.x += PADDLE_SPEED;
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT && paddle.x > 0) {
			paddle.x -= PADDLE_SPEED;
		}

		// Lancer la balle
		if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameStarted) {
			gameStarted = true;
		}

		// Mettre le jeu en pause
		if (e.getKeyCode() == KeyEvent.VK_P) {
			paused = !paused;
		}

		// Quitter le jeu
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			int response = JOptionPane.showConfirmDialog(this,
					"Voulez-vous quitter le jeu?",
					"Quitter",
					JOptionPane.YES_NO_OPTION);

			if (response == JOptionPane.YES_OPTION) {
				mainFrame.setVisible(false);
				startScreen.setVisible(true);
				stats.reset();
				reset();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Non utilisé
	}
}
