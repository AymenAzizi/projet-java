import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe principale qui lance le jeu
 * @author Aymen Azizi
 * @version 2.0
 */
public class Main {

	private static final String GAME_TITLE = "Brick Breaker Deluxe";
	private static final int WINDOW_WIDTH = 490;
	private static final int WINDOW_HEIGHT = 600;

	/**
	 * Méthode principale qui lance le jeu
	 * @param args Arguments de la ligne de commande (non utilisés)
	 */
	public static void main(String[] args) {
		// Définir le look and feel du système
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Créer les fenêtres
		JFrame gameFrame = createGameFrame();
		JFrame startScreen = createStartScreen(gameFrame);

		// Créer le panneau de jeu
		BlockBreakerPanel panel = new BlockBreakerPanel(gameFrame, startScreen);
		gameFrame.getContentPane().add(panel);

		// Afficher l'écran de démarrage
		startScreen.setVisible(true);
	}

	/**
	 * Crée la fenêtre principale du jeu
	 * @return JFrame La fenêtre du jeu
	 */
	private static JFrame createGameFrame() {
		JFrame frame = new JFrame(GAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); // Centrer la fenêtre
		frame.setVisible(false);
		return frame;
	}

	/**
	 * Crée l'écran de démarrage du jeu
	 * @param gameFrame La fenêtre principale du jeu
	 * @return JFrame L'écran de démarrage
	 */
	private static JFrame createStartScreen(JFrame gameFrame) {
		JFrame startScreen = new JFrame(GAME_TITLE + " - Menu Principal");
		startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startScreen.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		startScreen.setResizable(false);
		startScreen.setLocationRelativeTo(null); // Centrer la fenêtre

		// Créer le panneau principal
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(new Color(0, 0, 50)); // Bleu foncé

		// Titre du jeu
		JLabel titleLabel = new JLabel(GAME_TITLE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

		// Panneau des boutons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setOpaque(false);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 100, 100));

		// Bouton Jouer
		JButton playButton = createMenuButton("Jouer");
		playButton.addActionListener(e -> {
			startScreen.setVisible(false);
			gameFrame.setVisible(true);
		});

		// Bouton Meilleurs scores
		JButton highScoresButton = createMenuButton("Meilleurs scores");
		highScoresButton.addActionListener(e -> showHighScores());

		// Bouton Instructions
		JButton instructionsButton = createMenuButton("Instructions");
		instructionsButton.addActionListener(e -> showInstructions());

		// Bouton Quitter
		JButton quitButton = createMenuButton("Quitter");
		quitButton.addActionListener(e -> {
			int response = JOptionPane.showConfirmDialog(startScreen,
					"Voulez-vous vraiment quitter le jeu?",
					"Quitter",
					JOptionPane.YES_NO_OPTION);

			if (response == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		});

		// Ajouter les boutons au panneau
		buttonPanel.add(playButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		buttonPanel.add(highScoresButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		buttonPanel.add(instructionsButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		buttonPanel.add(quitButton);

		// Ajouter les composants au panneau principal
		mainPanel.add(titleLabel, BorderLayout.NORTH);
		mainPanel.add(buttonPanel, BorderLayout.CENTER);

		// Ajouter le panneau principal à la fenêtre
		startScreen.getContentPane().add(mainPanel);

		return startScreen;
	}

	/**
	 * Crée un bouton stylisé pour le menu
	 * @param text Le texte du bouton
	 * @return JButton Le bouton créé
	 */
	private static JButton createMenuButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setBackground(new Color(0, 100, 200));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		button.setMaximumSize(new Dimension(200, 50));
		button.setPreferredSize(new Dimension(200, 50));

		return button;
	}

	/**
	 * Affiche les meilleurs scores
	 */
	private static void showHighScores() {
		List<Integer> highScores = loadHighScores();

		StringBuilder message = new StringBuilder("Meilleurs scores:\n\n");

		if (highScores.isEmpty()) {
			message.append("Aucun score enregistré.");
		} else {
			for (int i = 0; i < highScores.size(); i++) {
				message.append(i + 1).append(". ").append(highScores.get(i)).append(" points\n");
			}
		}

		JOptionPane.showMessageDialog(null, message.toString(), "Meilleurs scores", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Charge les meilleurs scores depuis le fichier
	 * @return List<Integer> La liste des meilleurs scores
	 */
	private static List<Integer> loadHighScores() {
		List<Integer> highScores = new ArrayList<>();
		File file = new File("highscores.txt");

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

		return highScores;
	}

	/**
	 * Affiche les instructions du jeu
	 */
	private static void showInstructions() {
		String instructions =
				"Instructions du jeu Brick Breaker Deluxe:\n\n" +
				"- Utilisez les flèches GAUCHE et DROITE pour déplacer la raquette\n" +
				"- Appuyez sur ESPACE pour lancer la balle\n" +
				"- Appuyez sur P pour mettre le jeu en pause\n" +
				"- Appuyez sur ECHAP pour quitter la partie\n\n" +
				"Objectif:\n" +
				"- Détruisez tous les blocs pour passer au niveau suivant\n" +
				"- Certains blocs nécessitent plusieurs coups pour être détruits\n" +
				"- Collectez les power-ups qui tombent des blocs pour obtenir des bonus\n" +
				"- Vous avez 3 vies au départ\n\n" +
				"Power-ups:\n" +
				"- Agrandir la raquette\n" +
				"- Rétrécir la raquette\n" +
				"- Ralentir la balle\n" +
				"- Accélérer la balle\n" +
				"- Vie supplémentaire\n\n" +
				"Bonne chance!";

		JOptionPane.showMessageDialog(null, instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE);
	}
}
