package controleur;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import modele.Carte;
import modele.CouleurCarte;
import modele.NomCarte;

public class SolitaireController {
	// Déclaration des colonnes de jeu, 4 pour le résultat final,7 pour les colonnes de base
	private List<Carte> colR1 = new ArrayList<>();
	private List<Carte> colR2 = new ArrayList<>();
	private List<Carte> colR3 = new ArrayList<>();
	private List<Carte> colR4 = new ArrayList<>();
	  
	//Creation d'un jeu de cartes de 52 cartes
	public List<Carte> createJeu52Cartes() {
		List<Carte> listeCartes = new ArrayList<>();
	        for (modele.CouleurCarte couleur : modele.CouleurCarte.values()) {
	            for (modele.NomCarte nom : modele.NomCarte.values()) {
	                listeCartes.add(new Carte(nom, couleur));
	            }
	        }
	        return listeCartes;
	    }
	
    public static List<List<Carte>> creerColonnesDeDepart() {
        List<List<Carte>> colonnes = new ArrayList<>();

        // Création des colonnes avec des cartes
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            List<Carte> colonne = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                // La première carte est visible, les autres sont cachées
                if (j == i) {
                    // Génération d'une carte aléatoire
                    NomCarte nomCarte;
                    CouleurCarte couleurCarte;
                    boolean carteDejaPresente;
                    do {
                        nomCarte = NomCarte.values()[random.nextInt(NomCarte.values().length)];
                        couleurCarte = CouleurCarte.values()[random.nextInt(CouleurCarte.values().length)];
                        Carte carte = new Carte(nomCarte, couleurCarte);
                        carteDejaPresente = false;
                        for (List<Carte> col : colonnes) {
                            for (Carte c : col) {
                                if (c != null && c.equals(carte)) {
                                    carteDejaPresente = true;
                                    break;
                                }
                            }
                        }
                    } while (carteDejaPresente);
                    colonne.add(new Carte(nomCarte, couleurCarte));
                } else {
                    colonne.add(null); // Carte cachée
                }
            }
            colonnes.add(colonne);
        }
     // Ajout de 4 colonnes de destination initialement vides
        for (int i = 0; i < 4; i++) {
            colonnes.add(new ArrayList<>());
        }


        return colonnes;
    }
    public static void testDeplacement(List<List<Carte>> colonnes) {
    	deplacerCarte(colonnes, 0, 8); // Déplace une carte de la colonne 0 à la colonne 8
    }
    

    // Fonction pour afficher les colonnes du solitaire (pour le test)
    public static void afficherColonnes(List<List<Carte>> colonnes) {
    	testDeplacement(colonnes);
        for (int i = 0; i < colonnes.size(); i++) {
            System.out.print("Colonne " + i + ": "); // Affichage du numéro de la colonne
            List<Carte> colonne = colonnes.get(i);
            for (Carte carte : colonne) {
                if (carte == null) {
                    System.out.print("X "); // Carte cachée
                } else {
                    System.out.print(carte.toString() + " ");
                }
            }
            System.out.println();
        }
    }


    // Fonction principale (pour tester la création des colonnes)
    public static void main(String[] args) {
        List<List<Carte>> colonnesDeDepart = creerColonnesDeDepart();
        afficherColonnes(colonnesDeDepart);
    }

    /**
     * @param sourceLigne
     * @param sourceColonne
     * @param destinationLigne
     * @param destinationColonne
     */
 // Fonction pour déplacer une carte d'une colonne à une autre
    public static boolean deplacerCarte(List<List<Carte>> colonnes, int colonneSource, int colonneDestination) {
        if (colonneSource < 0 || colonneSource >= colonnes.size() || colonneDestination < 0 || colonneDestination >= colonnes.size()) {
            // Vérification des index de colonne valides
            return false;
        }
        
        List<Carte> source = colonnes.get(colonneSource);
        List<Carte> destination = colonnes.get(colonneDestination);
        
        // Vérification si la colonne source et la colonne destination sont différentes
        if (source == destination || source.isEmpty()) {
            return false;
        }
        
        Carte carteADeplacer = source.get(source.size() - 1); // La carte à déplacer est la carte la plus haute dans la colonne source
        
        // Exemple de règle simple : vous pouvez déplacer une carte si la valeur de la carte à déplacer est inférieure d'une unité à la carte du dessus de la colonne destination
        if (destination.isEmpty() || carteADeplacer.getValeur() == destination.get(destination.size() - 1).getValeur() - 1) {
            destination.add(carteADeplacer);
            source.remove(source.size() - 1);
            return true;
        }
        
        return false;
    }
    


    public boolean estDeplacementValide(int sourceLigne, int sourceColonne, int destinationLigne, int destinationColonne) {
        //ToDo
    	// 1: est ce que la carte source est une suite de la dernière carte destionationColonne
    	// 2: 
        return false; // Si déplacement non valide
    }
    
    public boolean peutPiocher() {
    	//ToDo
    	return false;
    }
    
    //ToDo A FAIRE BIEN PLUS TARD
    public void sauvegarderPartie() {
    	
    }
    
    //ToDo
	public void chargerSauvegarde() {

	}

}
