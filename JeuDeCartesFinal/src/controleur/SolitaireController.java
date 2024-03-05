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

        return colonnes;
    }

    // Fonction pour afficher les colonnes du solitaire (pour le test)
    public static void afficherColonnes(List<List<Carte>> colonnes) {
        for (List<Carte> colonne : colonnes) {
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
    public void deplacerCarte(int sourceLigne, int sourceColonne, int destinationLigne, int destinationColonne) {
        //ToDo
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
