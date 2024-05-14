package controleur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import modele.Carte;
import modele.CouleurCarte;
import modele.NomCarte;
import vue.Gui;

public class SolitaireController {
	private static List<Carte> deck = initDeck();
	private static DeckType deckType = DeckType.DECK_52;

	public static final int INDEX_COLONNE_PIOCHE = creerColonnesDeDepart().size() - 1;

	public enum DeckType {
		DECK_52, DECK_32
	}

	public static void setDeckType(DeckType type) {
		deckType = type;
	}

	public static DeckType getDeckType() {
		return deckType;
	}

	public static void adjustCardValuesForDeckType() {
		if (deckType == DeckType.DECK_32) {
			NomCarte.AS.setPoints(6);
		} else {
			NomCarte.AS.setPoints(1);
		}
	}

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

	public static List<Carte> initDeck() {
		List<Carte> listeCartes = new ArrayList<>();
		for (CouleurCarte couleur : CouleurCarte.values()) {
			for (NomCarte nom : NomCarte.values()) {
				if (couleur != CouleurCarte.CACHEE && nom != NomCarte.CACHEE) {
					if (deckType == DeckType.DECK_52) {
						listeCartes.add(new Carte(nom, couleur));
					} else if (deckType == DeckType.DECK_32 && (nom == NomCarte.AS || nom.getPoints() >= 7)) {
						listeCartes.add(new Carte(nom, couleur));
					}
				}
			}
		}
		Collections.shuffle(listeCartes);
		return listeCartes;
	}

	public static List<List<Carte>> creerColonnesDeDepart() {
		// Initialisation et mélange du deck
		deck = initDeck(); // Assurez-vous que cette ligne n'est pas redondante avec d'autres appels
							// initDeck()

		List<List<Carte>> colonnes = new ArrayList<>();

		// Générer les colonnes de départ
		for (int i = 0; i < 7; i++) {
			List<Carte> colonne = new ArrayList<>();
			for (int j = 0; j <= i; j++) {
				// Vérifiez si le deck contient encore des cartes
				if (!deck.isEmpty()) {
					Carte carteTiree = deck.remove(0); // Retire la première carte du deck
					colonne.add(carteTiree);
					// Seules les dernières cartes de chaque colonne sont visibles
					carteTiree.setVisible(j == i);
				} else {
					System.out.println("Le deck est vide, impossible de continuer à distribuer.");
					break;
				}
			}
			colonnes.add(colonne);
		}

		// Ajout de 4 colonnes de destination initialement vides
		for (int i = 0; i < 4; i++) {
			colonnes.add(new ArrayList<>());
		}

		// Ajoutez une colonne pour la pioche si nécessaire
		colonnes.add(new ArrayList<>());

		return colonnes;
	}



	// Fonction pour afficher les colonnes du solitaire (pour le test)
	public static void afficherColonnes(List<List<Carte>> colonnes) {
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
		new Gui();
	}

	/**
	 * @param sourceLigne
	 * @param sourceColonne
	 * @param destinationLigne
	 * @param destinationColonne
	 */
	// Fonction pour déplacer une carte d'une colonne à une autre

	

	// Méthode pour vérifier si le déplacement d'une carte depuis la pioche vers une
	// colonne est valide
	public static boolean estDeplacementDepuisPiocheValide(List<Carte> colonnePioche, List<Carte> colonneDestination) {
		Carte cartePioche = colonnePioche.get(colonnePioche.size() - 1);
		if (colonneDestination.isEmpty()) {
			// Permettre le déplacement si la carte est un AS.
			return cartePioche.getNom() == NomCarte.AS;
		} else {
			Carte carteSommet = colonneDestination.get(colonneDestination.size() - 1);
			return cartePioche.getValeur() == carteSommet.getValeur() - 1
					&& (cartePioche.getCouleur().getPoints() < 3 && carteSommet.getCouleur().getPoints() >= 3
							|| cartePioche.getCouleur().getPoints() > 2 && carteSommet.getCouleur().getPoints() <= 2);
		}
	}

	public static boolean deplacerCarteSimplifie(List<List<Carte>> colonnes, int colonneSource,
			int colonneDestination) {
		if (!colonnes.get(colonneSource).isEmpty()) {
			// Supprime la carte de la colonne source
			Carte carteADeplacer = colonnes.get(colonneSource).remove(colonnes.get(colonneSource).size() - 1);
			// Assurez-vous que la carte est visible lorsque vous la déplacez
			carteADeplacer.setVisible(true); // Cette ligne s'assure que la carte est visible
			// Ajoute la carte à la colonne de destination
			colonnes.get(colonneDestination).add(carteADeplacer);

			// Si la colonne source n'est pas vide, rendez la nouvelle dernière carte
			// visible
			if (!colonnes.get(colonneSource).isEmpty()) {
				Random random = new Random();
				NomCarte nomCarte;
				CouleurCarte couleurCarte;
				List<Carte> source = colonnes.get(colonneSource);
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
			}
			return true;
		}
		return false;
	}

	public static boolean aGagner(List<List<Carte>> colonnes) {

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

	public static List<Carte> getDeck() {
		return deck; // Assurez-vous que ce deck est le deck unique utilisé partout
	}


	public static boolean deplacerCarteTest(List<List<Carte>> colonnes, int colonneSource, int colonneDestination) {
		// Cas spécial pour la pioche
		if (colonneSource == INDEX_COLONNE_PIOCHE) {
			List<Carte> colonnePioche = colonnes.get(11);

			if (!colonnePioche.isEmpty()) {
				Carte cartePiochee = colonnePioche.remove(colonnePioche.size() - 1); // Retirer la dernière carte de la
																						// pioche
				List<Carte> colonneDest = colonnes.get(colonneDestination);
				colonneDest.add(cartePiochee); // Ajouter la carte piochée à la colonne de destination

				// Après déplacement, vérifier si la pioche est vide pour gérer cet état
				if (colonnePioche.isEmpty()) {
					// Logique pour gérer une pioche vide, par exemple, actualiser l'UI
				}
				return true; // Le déplacement a été effectué
			}
			// Logique pour déplacer depuis la pioche
			if (deck.isEmpty())
				return false; // Ne rien faire si la pioche est vide

			List<Carte> destination = colonnes.get(colonneDestination);

			Carte cartePioche = colonnePioche.get(colonnePioche.size() - 1);
			System.out.println(deck);
			System.out.println(cartePioche);
			System.out.println(colonnePioche);
			System.out.println(destination);

			System.out.println(estDeplacementDepuisPiocheValide(colonnePioche, destination));
			// Vérifiez si le déplacement est valide.
			if (!colonnes.get(colonneSource).isEmpty()) {
				List<Carte> colonneSrc = colonnes.get(colonneSource);
				Carte derniereCarteSrc = colonneSrc.get(colonneSrc.size() - 1);
				// Assurez-vous que la carte du dessus est visible
				if (!derniereCarteSrc.estVisible()) {
					derniereCarteSrc.setVisible(true);
				}
			}
			if (estDeplacementDepuisPiocheValide(colonnePioche, destination)) {
				System.out.println("Déplacement de la pioche vers la colonne destination: " + colonneDestination); // Ajouté
				destination.add(cartePioche);

				colonnePioche.remove(colonnePioche.size() - 1);// Ajoutez la carte à la colonne de destination
				return true;
			} else {
				System.out.println("Déplacement de la pioche vers la colonne destination: " + colonneDestination); // Ajouté
				return false; // Le déplacement n'est pas valide
			}
		} else {
			// Votre logique existante pour déplacer une carte d'une colonne à une autre
			if (colonneSource < 0 || colonneSource >= colonnes.size() || colonneDestination < 0
					|| colonneDestination >= colonnes.size() || colonneSource == colonneDestination) {
				return false;
			}

			List<Carte> source = colonnes.get(colonneSource);
			if (source.isEmpty()) {
				return false;
			}

			Carte carteADeplacer = source.get(source.size() - 1);
			List<Carte> destination = colonnes.get(colonneDestination);

			if (!destination.isEmpty()) {
				Carte derniereCarte = destination.get(destination.size() - 1);
				// Vérifiez si le déplacement est valide selon les règles du jeu
				if (!estDeplacementValide(carteADeplacer, derniereCarte)) {
					return false;
				}
			}

			// Ajoutez la carte à la colonne de destination et retirez-la de la source
			destination.add(carteADeplacer);
			source.remove(source.size() - 1);

			return true;
		}
	}

	// Méthode supplémentaire pour vérifier la validité d'un déplacement entre deux
	// cartes
	public static boolean estDeplacementValide(Carte carteADeplacer, Carte carteDestination) {
		return carteADeplacer.getValeur() == carteDestination.getValeur() - 1
				&& ((carteADeplacer.getCouleur().getPoints() < 3) != (carteDestination.getCouleur().getPoints() < 3));
	}

	public static boolean deplacementVersPileFinale(Carte carteADeplacer, int colonneDestination, List<List<Carte>> colonnes) {
		if (carteADeplacer == null) {
			// La carte à déplacer ne peut pas être nulle
			return false;
		}
		List<Carte> destination = colonnes.get(colonneDestination);
		if (destination.isEmpty() && colonneDestination >= 7) {
			// Seule une carte AS peut être déplacée vers une pile finale vide
			return carteADeplacer.getNom() == NomCarte.AS;
		} else if (destination.isEmpty() && colonneDestination < 7){
			return carteADeplacer.getNom() == NomCarte.ROI;
		} else {
			
			Carte derniereCartePileFinale = destination.get(destination.size() - 1);
			// Vérifier si la carte à déplacer peut être placée sur la pile finale selon les
			// règles du solitaire
			
			return (carteADeplacer.getValeur() == derniereCartePileFinale.getValeur() + 1) && // La carte à déplacer doit avoir une valeur supérieure d'une unité
					(carteADeplacer.getCouleur() == derniereCartePileFinale.getCouleur()); // Les couleurs doivent être
																							// différentes
		}
	}

}
