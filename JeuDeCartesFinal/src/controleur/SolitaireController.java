package controleur;

import java.util.ArrayList;
import java.util.List;

import modele.Carte;

public class SolitaireController {
	// Déclaration des colonnes de jeu, 4 pour le résultat final,7 pour les colonnes de base
	private List<Carte> colJ1 = new ArrayList<>();
	private List<Carte> colJ2 = new ArrayList<>();
	private List<Carte> colJ3 = new ArrayList<>();
	private List<Carte> colJ4 = new ArrayList<>();
	private List<Carte> colJ5 = new ArrayList<>();
	private List<Carte> colJ6 = new ArrayList<>();
	private List<Carte> colJ7 = new ArrayList<>();
	private List<Carte> colR1 = new ArrayList<>();
	private List<Carte> colR2 = new ArrayList<>();
	private List<Carte> colR3 = new ArrayList<>();
	private List<Carte> colR4 = new ArrayList<>();
	  
	public List<Carte> createJeu52Cartes() {
		List<Carte> listeCartes = new ArrayList<>();
	        for (modele.CouleurCarte couleur : modele.CouleurCarte.values()) {
	            for (modele.NomCarte nom : modele.NomCarte.values()) {
	                listeCartes.add(new Carte(nom, couleur));
	            }
	        }
	        return listeCartes;
	    }
    public void initialiserPartie() {
    	createJeu52Cartes();
    	//ToDo Reset les colonnes
    }

    public void deplacerCarte(int sourceLigne, int sourceColonne, int destinationLigne, int destinationColonne) {
    	
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
