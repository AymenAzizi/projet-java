import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Classe de lancement qui s'assure que les images sont correctement copiées
 * avant de démarrer le jeu
 */
public class Launcher {
    
    public static void main(String[] args) {
        System.out.println("=== Lancement du jeu Brick Breaker Deluxe ===");
        
        // Vérifier et copier les images
        copyImages();
        
        // Lancer le jeu
        System.out.println("Démarrage du jeu...");
        Main.main(args);
    }
    
    /**
     * Copie les images du dossier src vers le dossier courant
     */
    private static void copyImages() {
        System.out.println("Vérification des images...");
        
        String[] imageNames = {
            "ball.png", "blue.png", "green.png", "green2.png", "life.png",
            "red.png", "shrink.png", "expand.png", "fast.png", "paddle.png",
            "powerup.png", "slow.png", "yellow.png"
        };
        
        // Vérifier si les images existent dans le répertoire courant
        boolean allImagesExist = true;
        for (String imageName : imageNames) {
            File imageFile = new File(imageName);
            if (!imageFile.exists()) {
                allImagesExist = false;
                System.out.println("Image manquante: " + imageName);
            }
        }
        
        // Si toutes les images existent, pas besoin de les copier
        if (allImagesExist) {
            System.out.println("Toutes les images sont présentes.");
            return;
        }
        
        // Essayer de copier les images depuis le dossier src
        System.out.println("Copie des images depuis le dossier src...");
        try {
            for (String imageName : imageNames) {
                Path source = Paths.get("src", imageName);
                Path destination = Paths.get(imageName);
                
                if (Files.exists(source)) {
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Image copiée: " + imageName);
                } else {
                    System.out.println("Image source introuvable: " + source);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la copie des images: " + e.getMessage());
        }
    }
}
