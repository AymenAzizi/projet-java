import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class TestWindow {
    public static void main(String[] args) {
        // Créer une fenêtre simple
        JFrame frame = new JFrame("Test Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        // Créer un panneau
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        // Ajouter un label
        JLabel label = new JLabel("Si vous voyez cette fenêtre, l'interface graphique fonctionne !");
        panel.add(label, BorderLayout.CENTER);
        
        // Ajouter un bouton
        JButton button = new JButton("Fermer");
        button.addActionListener(e -> System.exit(0));
        panel.add(button, BorderLayout.SOUTH);
        
        // Ajouter le panneau à la fenêtre
        frame.getContentPane().add(panel);
        
        // Afficher la fenêtre
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        System.out.println("La fenêtre de test est affichée !");
    }
}
