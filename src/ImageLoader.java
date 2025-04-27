import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * Classe utilitaire pour charger les images du jeu
 */
public class ImageLoader {
    
    // Chemins possibles pour les images
    private static final String[] PATHS = {
        "",                  // Répertoire courant
        "bin/",              // Dossier bin
        "src/",              // Dossier src
        "resources/",        // Dossier resources
        "../src/",           // Dossier src parent
        "../resources/"      // Dossier resources parent
    };
    
    /**
     * Charge une image à partir de son nom
     * @param imageName Nom de l'image
     * @return Image chargée ou null si l'image n'a pas été trouvée
     */
    public static Image loadImage(String imageName) {
        // Essayer tous les chemins possibles
        for (String path : PATHS) {
            try {
                String fullPath = path + imageName;
                File file = new File(fullPath);
                
                if (file.exists()) {
                    System.out.println("Image trouvée: " + file.getAbsolutePath());
                    return new ImageIcon(file.getAbsolutePath()).getImage();
                }
            } catch (Exception e) {
                // Ignorer et essayer le chemin suivant
            }
        }
        
        // Si aucun chemin n'a fonctionné, essayer avec ImageIcon directement
        try {
            ImageIcon icon = new ImageIcon(imageName);
            if (icon.getIconWidth() > 0) {
                System.out.println("Image chargée avec ImageIcon: " + imageName);
                return icon.getImage();
            }
        } catch (Exception e) {
            // Ignorer
        }
        
        System.err.println("Impossible de charger l'image: " + imageName);
        return null;
    }
}
