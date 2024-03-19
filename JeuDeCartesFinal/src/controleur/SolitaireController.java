package controleur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modele.Carte;
import modele.CouleurCarte;
import modele.NomCarte;

public class SolitaireController {
	// Déclaration des colonnes de jeu, 4 pour le résultat final,7 pour les colonnes
	// de base
	private List<Carte> colR1 = new ArrayList<>();
	private List<Carte> colR2 = new ArrayList<>();
	private List<Carte> colR3 = new ArrayList<>();
	private List<Carte> colR4 = new ArrayList<>();

	// Creation d'un jeu de cartes de 52 cartes
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
					// Génération d'une carte aléatoire pour la première carte visible
					NomCarte nomCarte;
					CouleurCarte couleurCarte;
					boolean carteDejaPresente;
					do {
						nomCarte = NomCarte.values()[random.nextInt(NomCarte.values().length - 1)];
						couleurCarte = CouleurCarte.values()[random.nextInt(CouleurCarte.values().length - 1)];
						carteDejaPresente = false;
						for (List<Carte> col : colonnes) {
							for (Carte c : col) {
								if (c != null && c.getNom() == nomCarte && c.getCouleur() == couleurCarte) {
									carteDejaPresente = true;
									break;
								}
							}
						}
					} while (carteDejaPresente);
					colonne.add(new Carte(nomCarte, couleurCarte));
				} else {
					// Ajoutez une carte spéciale pour représenter les cartes cachées
					colonne.add(new Carte(NomCarte.CACHEE, CouleurCarte.CACHEE));
				}
			}
			colonnes.add(colonne);
		}

		// Ajout de 4 colonnes de destination initialement vides
		for (int i = 0; i < 4; i++) {
			colonnes.add(new ArrayList<>());
		}

		// Ajout d'une colonne pour la pioche et remplissage avec 24 cartes cachées
		List<Carte> pioche = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			pioche.add(new Carte(NomCarte.CACHEE, CouleurCarte.CACHEE));
		}
		colonnes.add(pioche);

		return colonnes;
	}

	// Fonction qui sert uniquement à réaliser des tests
	public static void testDeplacement(List<List<Carte>> colonnes) {
		deplacerCarte(colonnes, 0, 8); // Déplace une carte de la colonne 0 à la colonne 8
		deplacerCarte(colonnes, 1, 8);
		deplacerCarte(colonnes, 2, 8);
		deplacerCarte(colonnes, 3, 8);
		deplacerCarte(colonnes, 4, 8);
		deplacerCarte(colonnes, 5, 8);
		deplacerCarte(colonnes, 6, 8);
		deplacerColonne(colonnes, 8, 9, 0);// Déplacer les cartes de la colonne 8 à la colonne 9 à partir de l'index 0
											// de la colonne 8
	}

	// Fonction pour afficher les colonnes du solitaire (pour le test)
	public static void afficherColonnes(List<List<Carte>> colonnes) {
		testDeplacement(colonnes);
		for (int i = 0; i < colonnes.size(); i++) {
			System.out.print("Colonne " + i + ": "); // Affichage du numéro de la colonne
			List<Carte> colonne = colonnes.get(i);
			for (Carte carte : colonne) {
				if (carte == null) {
					// Si la carte est cachée (null), affichez un espace ou tout autre caractère
					System.out.print("  "); // Afficher un espace pour les cartes cachées
				} else {
					System.out.print(carte.toString() + " ");
				}
			}
			System.out.println();
		}
	}

	// Fonction principale (pour tester la création des colonnes)

	public void demarrerJeu() {
		List<List<Carte>> colonnesDeDepart = creerColonnesDeDepart();
		afficherColonnes(colonnesDeDepart);

		if (aGagner(colonnesDeDepart)) {
			System.out.println("Félicitations ! Vous avez gagné !");
		} else {
			System.out.println("Le jeu continue...");
		}
	}

	public static void main(String[] args) {
		new SolitaireController().demarrerJeu(); // Modifiez cette ligne
	}

	/**
	 * @param sourceLigne
	 * @param sourceColonne
	 * @param destinationLigne
	 * @param destinationColonne
	 */
	// Fonction pour déplacer une carte d'une colonne à une autre
	public static boolean deplacerCarte(List<List<Carte>> colonnes, int colonneSource, int colonneDestination) {
		if (colonneSource < 0 || colonneSource >= colonnes.size() || colonneDestination < 0
				|| colonneDestination >= colonnes.size()) {
			// Vérification des index de colonne valides
			return false;

		}

		List<Carte> source = colonnes.get(colonneSource);
		List<Carte> destination = colonnes.get(colonneDestination);

		// Vérification si la colonne source et la colonne destination sont différentes
		if (source == destination || source.isEmpty()) {
			return false;
		}
		// Impossibilité de déplacer une carte dans une colonne de départ qui a été
		// vidée
		if (colonneDestination < 6 && destination.isEmpty()) {
			return false;
		}

		Carte carteADeplacer = source.get(source.size() - 1); // La carte à déplacer est la carte la plus haute dans la
																// colonne source
		// Impossibilité de déplacer une carte dans une colonne de destination vide si
		// la carte selectionnée n'est pas un AS
		if (destination.isEmpty() && carteADeplacer.getNom().toString() != "AS") {
			return false;
		}

		if (!destination.isEmpty()) {
			Carte derniereCarte = destination.get(destination.size() - 1);
			if (carteADeplacer.getValeur() != derniereCarte.getValeur() + 1
					|| carteADeplacer.getCouleur() != derniereCarte.getCouleur()) {
				return false;
			}
		}
		// Exemple de règle simple : vous pouvez déplacer une carte si la valeur de la
		// carte à déplacer est inférieure d'une unité à la carte du dessus de la
		// colonne destination
		if (destination.isEmpty()
				|| carteADeplacer.getValeur() == destination.get(destination.size() - 1).getValeur() + 1) {
			destination.add(carteADeplacer);
			source.remove(source.size() - 1);
			Random random = new Random();
			NomCarte nomCarte;
			CouleurCarte couleurCarte;
			if (source.size() >= 1 && source.get(source.size() - 1).getNom() == NomCarte.CACHEE) {
				boolean cartePresente;
				do {
					// Remplacer la dernière carte si elle a pour nom "CACHEE"
					nomCarte = NomCarte.values()[random.nextInt(NomCarte.values().length) - 1];
					couleurCarte = CouleurCarte.values()[random.nextInt(CouleurCarte.values().length) - 1];
					cartePresente = false;
					Carte nouvelleCarte = new Carte(nomCarte, couleurCarte);
					for (List<Carte> col : colonnes) {
						for (Carte c : col) {
							if (c != null && c.equals(nouvelleCarte)) {
								cartePresente = true;
								break;
							}
						}
					}
					source.set(source.size() - 1, nouvelleCarte);
				} while (cartePresente);

			}
			return true;
		}

		return false;
	}

	public boolean aGagner(List<List<Carte>> colonnes) {
		// Vérifier si toutes les colonnes de destination contiennent 13 cartes
		for (int i = 7; i < 11; i++) {
			if (colonnes.get(i).size() != 13) {
				return false;
			}
		}
		return true;
	}

	public void carteCliquee(Carte carte, int colonneIndex) {
		// Ici, vous pouvez définir ce qui se passe lorsqu'une carte est cliquée
		if (carte.estVisible()) {
			System.out.println("Carte cliquée: " + carte + " dans la colonne " + colonneIndex);
			// Ajoutez la logique de jeu en réponse au clic de la carte ici.
		}
	}

	public static boolean deplacerColonne(List<List<Carte>> colonnes, int colonneSource, int colonneDestination,
			int indexCarte) {
		if (colonneSource < 0 || colonneSource >= colonnes.size() || colonneDestination < 0
				|| colonneDestination >= colonnes.size()) {
			// Vérification des index de colonne valides
			return false;
		}

		List<Carte> source = colonnes.get(colonneSource);
		List<Carte> destination = colonnes.get(colonneDestination);

		// Vérification si la colonne source et la colonne destination sont différentes
		if (source == destination || source.isEmpty() || indexCarte < 0 || indexCarte >= source.size()) {
			return false;
		}

		// Déplacement des cartes dévoilées de la colonne source vers la colonne
		// destination à partir de l'index indiqué
		for (int i = indexCarte; i < source.size(); i++) {
			Carte carteADeplacer = source.get(i);
			if (carteADeplacer != null) {
				destination.add(carteADeplacer);
				source.set(i, null); // Carte déplacée, donc la case est désormais nulle
			}
		}

		return true;
	}

	public boolean estDeplacementValide(int sourceLigne, int sourceColonne, int destinationLigne,
			int destinationColonne) {
		// ToDo
		// 1: est ce que la carte source est une suite de la dernière carte
		// destionationColonne
		// 2:
		return false; // Si déplacement non valide
	}

	public boolean peutPiocher() {
		// ToDo
		return false;
	}

	// ToDo A FAIRE BIEN PLUS TARD
	public void sauvegarderPartie() {

	}

	// ToDo
	public void chargerSauvegarde() {

	}

}
