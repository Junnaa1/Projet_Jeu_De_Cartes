package controleur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modele.Carte;
import modele.CouleurCarte;
import modele.NomCarte;
import vue.Gui;

public class SolitaireController {

	// Variables de classe pour le jeu de cartes
	private static List<Carte> deck = initDeck(); // Pile de cartes pour le jeu
	private static DeckType deckType = DeckType.DECK_52; // Type de jeu de cartes utilisé (52 par défaut)

	// Index de la colonne de pioche dans le jeu
	public static final int INDEX_COLONNE_PIOCHE = createStartColumns().size() - 1;

	// Enumération pour les types de jeux de cartes
	public enum DeckType {
		DECK_52, DECK_32
	}

	// Définir le type de deck utilisé
	public static void setDeckType(DeckType type) {
		deckType = type;
	}

	// Récupérer le type de deck actuel
	public static DeckType getDeckType() {
		return deckType;
	}

	// Ajuste valeur de l'as selon le type de deck choisi
	public static void adjustCardValuesForDeckType() {
		if (deckType == DeckType.DECK_32) {
			NomCarte.AS.setPoints(6);
		} else {
			NomCarte.AS.setPoints(1);
		}
	}

	// Initialise la pile de cartes selon le type de jeu de cartes sélectionné
	public static List<Carte> initDeck() {
		List<Carte> listCards = new ArrayList<>();
		// Itère sur toutes les couleurs et noms de cartes, sauf les cartes "Cachées"
		for (CouleurCarte couleur : CouleurCarte.values()) {
			for (NomCarte nom : NomCarte.values()) {
				if (couleur != CouleurCarte.CACHEE && nom != NomCarte.CACHEE) { // Exclut les cartes cachées
					if (deckType == DeckType.DECK_52) {
						listCards.add(new Carte(nom, couleur)); // Ajoute toutes les cartes pour un jeu de 52 cartes
					} else if (deckType == DeckType.DECK_32 && (nom == NomCarte.AS || nom.getPoints() >= 7)) {
						// Pour un jeu de 32 cartes, inclut seulement les as et les cartes de 7 à l'as
						listCards.add(new Carte(nom, couleur));
					}
				}
			}
		}
		Collections.shuffle(listCards); // Mélange la pile de cartes pour aléatoiriser l'ordre
		return listCards;
	}

	// Colonnes de départ (back)
	public static List<List<Carte>> createStartColumns() {
		// Initialisation et mélange du deck de cartes
		deck = initDeck();
		List<List<Carte>> columns = new ArrayList<>();

		// Générer les colonnes de départ pour le tableau de jeu
		for (int i = 0; i < 7; i++) {
			List<Carte> column = new ArrayList<>();
			for (int j = 0; j <= i; j++) {
				// Vérifiez si le deck contient encore des cartes avant d'en tirer une
				if (!deck.isEmpty()) {
					Carte cardTiree = deck.remove(0); // Retire la première carte du deck
					column.add(cardTiree);
					// Rend la dernière carte de chaque colonne visible
					cardTiree.setVisible(j == i);
				} else {
					System.out.println("Le deck est vide, impossible de continuer à distribuer.");
					break;
				}
			}
			columns.add(column);
		}

		// Ajout de 4 colonnes de destination initialement vides
		for (int i = 0; i < 4; i++) {
			columns.add(new ArrayList<>());
		}

		// Ajoutez une colonne pour la pioche si nécessaire
		columns.add(new ArrayList<>());

		return columns;
	}

	// Méthode pour vérifier si le déplacement est valide
	public static boolean isMoveFromDrawValid(List<Carte> drawColumn, List<Carte> columnDestination) {
		Carte drawCard = drawColumn.get(drawColumn.size() - 1);
		if (columnDestination.isEmpty()) {
			// Permettre le déplacement si la card est un AS.
			return drawCard.getNom() == NomCarte.AS;
		} else {
			Carte cardSommet = columnDestination.get(columnDestination.size() - 1);
			return drawCard.getValeur() == cardSommet.getValeur() - 1
					&& (drawCard.getCouleur().getPoints() < 3 && cardSommet.getCouleur().getPoints() >= 3
							|| drawCard.getCouleur().getPoints() > 2 && cardSommet.getCouleur().getPoints() <= 2);
		}
	}

	public static boolean aGagner(List<List<Carte>> columns) {
		// Vérifier si toutes les columns de destination contiennent 13 cartes
		for (int i = 7; i < 11; i++) {
			if (columns.get(i).size() != 13 && deckType == DeckType.DECK_52
					|| columns.get(i).size() != 8 && deckType == DeckType.DECK_32) {
				return false;
			}
		}
		return true;
	}

	public static List<Carte> getDeck() {
		return deck; // Assurez-vous que ce deck est le deck unique utilisé partout
	}

	public static boolean moveCard(List<List<Carte>> columns, int sourceColumn, int columnDestination) {
		// Cas spécial pour la pioche
		if (sourceColumn == INDEX_COLONNE_PIOCHE) {
			List<Carte> drawColumn = columns.get(11);

			if (!drawColumn.isEmpty()) {
				Carte drawnCard = drawColumn.remove(drawColumn.size() - 1); // Retirer la dernière carte de la
																			// pioche
				List<Carte> columnDest = columns.get(columnDestination);
				columnDest.add(drawnCard); // Ajouter la card piochée à la column de destination

				return true; // Le déplacement a été effectué
			}
			if (deck.isEmpty())
				return false; // Ne rien faire si la pioche est vide

			List<Carte> destination = columns.get(columnDestination);

			Carte drawCard = drawColumn.get(drawColumn.size() - 1);
			System.out.println(deck);
			System.out.println(drawCard);
			System.out.println(drawColumn);
			System.out.println(destination);

			System.out.println(isMoveFromDrawValid(drawColumn, destination));
			// Vérifie si le déplacement est valide
			if (!columns.get(sourceColumn).isEmpty()) {
				List<Carte> columnSrc = columns.get(sourceColumn);
				Carte lastCardsSrc = columnSrc.get(columnSrc.size() - 1);
				if (!lastCardsSrc.estVisible()) {
					lastCardsSrc.setVisible(true);
				}
			}
			if (isMoveFromDrawValid(drawColumn, destination)) {
				System.out.println("Déplacement de la pioche vers la column destination: " + columnDestination); // Ajouté
				destination.add(drawCard);
				drawColumn.remove(drawColumn.size() - 1);// Ajoutez la carte à la column de destination
				return true;
			} else {
				System.out.println("Déplacement de la pioche vers la column destination: " + columnDestination); // Ajouté
				return false; // Le déplacement n'est pas valide
			}
		} else {
			if (sourceColumn < 0 || sourceColumn >= columns.size() || columnDestination < 0
					|| columnDestination >= columns.size() || sourceColumn == columnDestination) {
				return false;
			}

			List<Carte> source = columns.get(sourceColumn);
			if (source.isEmpty()) {
				return false;
			}

			Carte cardToMove = source.get(source.size() - 1);
			List<Carte> destination = columns.get(columnDestination);

			if (!destination.isEmpty()) {
				Carte lastCard = destination.get(destination.size() - 1);
				// Vérifie si le déplacement est valide selon les règles du jeu
				if (!isMoveValid(cardToMove, lastCard)) {
					return false;
				}
			} else {
				// Si la destination est vide, vérifiez si la card est un AS
				if (cardToMove.getNom() != NomCarte.AS && columnDestination < 7) {
					return false;
				}
			}

			destination.add(cardToMove);
			source.remove(source.size() - 1);
			return true;
		}
	}

	// Méthode de validité
	public static boolean isMoveValid(Carte cardToMove, Carte cardDestination) {
		return cardToMove.getValeur() == cardDestination.getValeur() - 1
				&& ((cardToMove.getCouleur().getPoints() < 3) != (cardDestination.getCouleur().getPoints() < 3));
	}

	public static boolean moveToFinalStack(Carte cardToMove, int columnDestination, List<List<Carte>> columns) {
		if (cardToMove == null) {
			// La card à déplacer ne peut pas être nulle
			return false;
		}
		List<Carte> destination = columns.get(columnDestination);
		if (destination.isEmpty()) {
			// Seule une card AS peut être déplacée vers une pile finale vide
			return cardToMove.getNom() == NomCarte.AS;
		} else {
			Carte lastCardFinalStack = destination.get(destination.size() - 1);
			// Vérifier si la card à déplacer peut être placée sur la pile finale
			return (cardToMove.getValeur() == lastCardFinalStack.getValeur() + 1) && // La card à déplacer
																						// doit avoir une valeur
																						// supérieure d'une
																						// unité
					(cardToMove.getCouleur() == lastCardFinalStack.getCouleur()); // Les couleurs doivent être
																					// différentes
		}
	}

	// Main de test quand on lance depuis le controller
	public static void main(String[] args) {
		new Gui();
	}

}
