package vue;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import controleur.SolitaireController;
import controleur.Souris;
import modele.Carte;

public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton boutonSolitaire;
	private JButton boutonQuitter;
	private Souris souris;
	JPanel panelSolitaire = new JPanel();
	List<List<Carte>> colonnesDeDepart = SolitaireController.creerColonnesDeDepart();

	private boolean isPileVideLabelSelected = false;

	int gamecolumnsxStart = 130; // Position de départ pour la première carte sur l'axe X
	int gamecolumnsyStart = 190; // Position de départ pour la première carte sur l'axe Y
	int gamecolumnsxSpacing = 10; // Espace horizontal entre les cartes
	int gamecolumnsySpacing = 20; // Espace vertical entre les cartes pour l'effet empilé

	public Carte carteSelectionnee = null;
	private int colonneSourceSelectionnee = -1;
	private int positionCarteDansColonne;

	public Carte cartePiochee = null;

	List<Carte> deck = SolitaireController.getDeck();

	// Parcourir les colonnes d
	public Gui() {
		this.souris = new Souris(this);
		initGUI();
		rendreDernieresCartesVisibles();
	}

	private void initGUI() {
		setTitle("Jeu de cartes");
		initCustomFonts();
		setSize(960, 540); // Taille de la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Centre la fenêtre
		setIconImage(new ImageIcon("src\\Logo.png").getImage());
		setResizable(false); // Empêche le redimensionnement de la fenêtre
		setContentPane(MainPage()); // Utilisation du panel principal comme content pane
		setVisible(true); // Rendre la fenêtre visible

	}

	public void setPanel(JPanel panel) {
		setContentPane(panel); // Définit le panel spécifié comme le content pane
		if (panel instanceof JPanel) {
			panelSolitaire = panel; // Assurez-vous que panelSolitaire référence le panel actuel si nécessaire
		}
		revalidate();
		repaint();
	}

	private void initCustomFonts() {
		try {
			// Chemin relatif au fichier de police dans le dossier des ressources
			File fontFile = new File("src\\Gotham-Black.otf");
			Font gothamBlack = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);
			System.out.println("Nom de la police chargée: " + gothamBlack.getFontName());

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// Enregistre la police
			ge.registerFont(gothamBlack);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
			// Gérez l'erreur ici (par exemple, en utilisant une police par défaut)
		}
	}

	public JPanel MainPage() {

		// Icones chargées

		Icon Leave = new ImageIcon("src\\Leave.png");
		Icon PlaySolitaire = new ImageIcon("src\\Solitaire.png");

		//

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(null);

		// Arrière-plan
		ImageIcon bgIcon = new ImageIcon("src\\Background.png");
		JLabel bgLabel = new JLabel(bgIcon);
		bgLabel.setBounds(0, 0, 960, 540);
		panelPrincipal.add(bgLabel);

		// Image en plus
		ImageIcon overlayIcon = new ImageIcon("src\\Background2.png");
		JLabel overlayLabel = new JLabel(overlayIcon);
		overlayLabel.setBounds(0, 0, 960, 540);
		bgLabel.add(overlayLabel);

		// Création du panel pour les boutons avec une transparence
		JPanel panelBoutons = new JPanel();
		panelBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panelBoutons.setOpaque(false); // Rend le panel transparent

		boutonSolitaire = new JButton("Solitaire", PlaySolitaire);
		boutonSolitaire.setBackground(new Color(91, 4, 75));
		boutonSolitaire.setForeground(Color.WHITE);
		boutonSolitaire.setFocusPainted(false);
		boutonSolitaire.setFont(new Font("Gotham Black", Font.BOLD, 26));

		boutonQuitter = new JButton("Quitter", Leave);
		boutonQuitter.setBackground(new Color(91, 4, 75));
		boutonQuitter.setForeground(Color.WHITE);
		boutonQuitter.setFocusPainted(false);
		boutonQuitter.setFont(new Font("Gotham Black", Font.BOLD, 26));

		panelBoutons.add(boutonSolitaire);
		panelBoutons.add(boutonQuitter);

		boutonSolitaire.addActionListener(souris);
		boutonQuitter.addActionListener(souris);

		boutonSolitaire.setActionCommand("Solitaire");
		boutonQuitter.setActionCommand("Quitter");

		boutonSolitaire.addMouseListener(souris);
		boutonQuitter.addMouseListener(souris);

		// Création du label de titre
		JLabel labelTitre = new JLabel("Bienvenue au choix de jeu de cartes", SwingConstants.CENTER);
		labelTitre.setFont(new Font("Gotham Black", Font.BOLD, 26));
		labelTitre.setForeground(Color.WHITE); // Couleur du texte
		labelTitre.setBounds(0, 20, 960, 30);

		// Ajout du label et des boutons au panel principal
		panelBoutons.setBounds(270, 420, 420, 60);
		bgLabel.add(panelBoutons);
		bgLabel.add(labelTitre);

		return panelPrincipal;
	}

	// Les images ont une grande taille, on les redéfinit donc par rapport à la
	// taille de l'écran
	// de jeu
	private ImageIcon resizeCardImage(String imagePath, int width, int height) {
		ImageIcon originalIcon = new ImageIcon(imagePath);
		Image image = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(image);
	}

	public JPanel PanelSolitaire() {

		JPanel panelSolitaire = new JPanel();
		panelSolitaire.setLayout(null);

		// Arrière-plan
		ImageIcon bgIcon = new ImageIcon("src\\Background.png");
		JLabel bgLabel = new JLabel(bgIcon);
		bgLabel.setBounds(0, 0, 960, 540);
		panelSolitaire.add(bgLabel);

		// Création et placement des cartes retournées pour les colonnes de départ
		ImageIcon cardBackIcon = new ImageIcon("src\\cartes\\CACHEE_CACHEE.png");
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		int gamecolumnsxStart = 130; // Position de départ pour la première carte sur l'axe X
		int gamecolumnsyStart = 190; // Position de départ pour la première carte sur l'axe Y
		int gamecolumnsxSpacing = 10; // Espace horizontal entre les cartes
		int gamecolumnsySpacing = 20; // Espace vertical entre les cartes pour l'effet empilé

		/*
		 * Initialisation des cartes pour les reconnaitre String[] suits = { "COEUR",
		 * "CARREAU", "TREFLE", "PIQUE" }; String[] values = { "DEUX", "TROIS",
		 * "QUATRE", "COEUR", "SIX", "SEPT", "HUIT", "NEUF", "DIX", "VALET", "REINE",
		 * "ROI", "ace" };
		 * 
		 * // Ajoute les cartes au deck for (String suit : suits) { for (String value :
		 * values) { if (!suit.equals("CACHEE") && !value.equals("CACHEE")) { String
		 * cardPath = "src\\cartes\\" + value + "_" + suit + ".png";
		 * deck.add(resizeCardImage(cardPath, cardWidth, cardHeight)); } } }
		 */

		// Placement des cartes en colonnes avec décalage vertical pour effet empilé
		for (int col = 0; col < colonnesDeDepart.size(); col++) {
			List<Carte> colonne = colonnesDeDepart.get(col);
			for (int cardIndex = 0; cardIndex < colonne.size(); cardIndex++) {
				JLabel cardLabel;
				final int finalCardIndex = cardIndex;
				final int finalCol = col;
				int x = gamecolumnsxStart + (cardWidth + gamecolumnsxSpacing) * col;
				int y = gamecolumnsyStart + gamecolumnsySpacing * cardIndex;

				Carte carte = colonne.get(cardIndex);
				if (carte != null) {
					ImageIcon carteImage;
					// Vérifie si la carte doit être affichée comme visible ou cachée
					if (carte.estVisible()) {
						String cardPath = "src\\cartes\\" + carte.getNom().toString() + "_"
								+ carte.getCouleur().toString() + ".png";
						carteImage = resizeCardImage(cardPath, cardWidth, cardHeight);
					} else {
						// Utiliser l'image de dos de carte si la carte n'est pas supposée être visible
						carteImage = cardBackIcon;
					}
					cardLabel = new JLabel(carteImage);

					cardLabel.setBounds(x, y, cardWidth, cardHeight);
					cardLabel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (carte.estVisible()) {
								if (colonneSourceSelectionnee == -1) {
									colonneSourceSelectionnee = finalCol;
									positionCarteDansColonne = finalCardIndex;
									carteSelectionnee = carte;
									System.out.println("Carte sélectionnée : " + carte);
									cardLabel.setBorder(new LineBorder(Color.GREEN, 3)); // Marquez la sélection avec
																							// une
																							// bordure verte
									System.out.println("Carte sélectionnée dans la colonne: " + finalCol
											+ ", position: " + finalCardIndex);
								} else {
									// Si une carte est déjà sélectionnée et qu'on clique sur une autre colonne
									if (finalCol != colonneSourceSelectionnee) {
										boolean reussi = SolitaireController.deplacerCarteTest(colonnesDeDepart,
												colonneSourceSelectionnee, finalCol);
										if (reussi) {
											System.out.println("Déplacement réussi de la colonne "
													+ colonneSourceSelectionnee + " vers la colonne " + finalCol);
											reconstruireAffichageColonnes();
										} else {
											System.out.println("Déplacement échoué");
											reconstruireAffichageColonnes();
										}
										colonneSourceSelectionnee = -1; // Réinitialiser la sélection après un
																		// déplacement
										positionCarteDansColonne = -1;
									} else if (finalCol == colonneSourceSelectionnee
											&& finalCardIndex == positionCarteDansColonne) {
										// Si l'utilisateur clique à nouveau sur la même carte, annuler la sélection
										cardLabel.setBorder(null);
										colonneSourceSelectionnee = -1;
										positionCarteDansColonne = -1;
										System.out.println("Annulation de la sélection de la carte");
									}
								}
							}
						}
					});
					bgLabel.add(cardLabel);
					bgLabel.setComponentZOrder(cardLabel, colonne.size() - cardIndex - 1);
				}
			}

		}

		creerPioche(bgLabel);
		creerColonneFinale(bgLabel);

		// Boutons pour revenir à l'accueil

		JPanel panelBoutons = new JPanel();
		panelBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panelBoutons.setOpaque(false);

		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 26));

		panelBoutons.add(boutonRetour);

		boutonRetour.addActionListener(souris);
		boutonRetour.setActionCommand("Retour");
		boutonRetour.addMouseListener(souris);

		panelBoutons.setBounds(-130, 450, 420, 60);
		bgLabel.add(panelBoutons);

		return panelSolitaire;
	}

	private void reconstruireAffichageColonnes() {
		// Supprime tous les composants du panelSolitaire pour recommencer.
		panelSolitaire.removeAll();
		panelSolitaire.setLayout(null); // Assurez-vous que le layout est correctement défini pour le positionnement
										// manuel.
		ImageIcon cardBackIcon = new ImageIcon("src\\cartes\\CACHEE_CACHEE.png");
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		// Redessine l'arrière-plan sur le panelSolitaire.
		ImageIcon bgIcon = new ImageIcon("src\\Background.png");
		JLabel bgLabel = new JLabel(bgIcon);
		bgLabel.setBounds(0, 0, 960, 540);
		panelSolitaire.add(bgLabel);

		// Itère à travers chaque colonne de départ pour reconstruire l'affichage des
		// cartes.
		for (int col = 0; col < colonnesDeDepart.size(); col++) {
			List<Carte> colonne = colonnesDeDepart.get(col);

			for (int cardIndex = 0; cardIndex < colonne.size(); cardIndex++) {
				Carte carte = colonne.get(cardIndex);
				JLabel cardLabel;
				int x = gamecolumnsxStart + (cardWidth + gamecolumnsxSpacing) * col;
				int y = gamecolumnsyStart + gamecolumnsySpacing * cardIndex;
				final int finalCardIndex = cardIndex;
				final int finalCol = col;
				// Si nous sommes à la dernière carte de la colonne source, utilisez une carte
				// aléatoire du deck
				if (col == colonneSourceSelectionnee && cardIndex == colonne.size() - 1) {
					ImageIcon carteIcon = obtenirImageCarte(carte);
					cardLabel = new JLabel(carteIcon);
				} else {
					// Utilise l'image de dos de la carte ou l'image de la carte en fonction de
					// estVisible
					String imagePath = carte.estVisible()
							? "src\\cartes\\" + carte.getNom().toString() + "_" + carte.getCouleur().toString() + ".png"
							: "src\\cartes\\CACHEE_CACHEE.png";
					ImageIcon carteImage = resizeCardImage(imagePath, cardWidth, cardHeight);
					cardLabel = new JLabel(carteImage);
				}

				cardLabel.setBounds(x, y, cardWidth, cardHeight);
				bgLabel.add(cardLabel);

				cardLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (finalCardIndex == colonne.size() - 1) {
							if (colonneSourceSelectionnee == -1) {
								colonneSourceSelectionnee = finalCol;
								positionCarteDansColonne = finalCardIndex;
								carteSelectionnee = carte;
								System.out.println("Carte sélectionnée : " + carte);
								cardLabel.setBorder(new LineBorder(Color.GREEN, 3)); // Marquez la sélection avec une
																						// bordure verte
								System.out.println("Carte sélectionnée dans la colonne: " + finalCol + ", position: "
										+ finalCardIndex);
							} else {
								// Si une carte est déjà sélectionnée et qu'on clique sur une autre colonne
								if (finalCol != colonneSourceSelectionnee) {
									boolean reussi = SolitaireController.deplacerCarteTest(colonnesDeDepart,
											colonneSourceSelectionnee, finalCol);
									if (reussi) {
										System.out.println("Déplacement réussi de la colonne "
												+ colonneSourceSelectionnee + " vers la colonne " + finalCol);
										reconstruireAffichageColonnes();
									} else {
										System.out.println("Déplacement échoué");
										reconstruireAffichageColonnes();
									}
									colonneSourceSelectionnee = -1; // Réinitialiser la sélection après un déplacement
									positionCarteDansColonne = -1;
								} else if (finalCol == colonneSourceSelectionnee
										&& finalCardIndex == positionCarteDansColonne) {
									// Si l'utilisateur clique à nouveau sur la même carte, annuler la sélection
									cardLabel.setBorder(null);
									colonneSourceSelectionnee = -1;
									positionCarteDansColonne = -1;
									System.out.println("Annulation de la sélection de la carte");
								}
							}
						}
					}
				});

				panelSolitaire.add(cardLabel);

				// Ajustez l'ordre Z pour que les cartes soient empilées correctement.
				panelSolitaire.setComponentZOrder(cardLabel, 0);
			}
		}

		creerPioche(bgLabel);
		creerColonneFinale(bgLabel);

		// Boutons pour revenir à l'accueil

		JPanel panelBoutons = new JPanel();
		panelBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panelBoutons.setOpaque(false);

		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 26));

		panelBoutons.add(boutonRetour);

		boutonRetour.addActionListener(souris);
		boutonRetour.setActionCommand("Retour");
		boutonRetour.addMouseListener(souris);

		panelBoutons.setBounds(-130, 450, 420, 60);
		bgLabel.add(panelBoutons);

		// Ces appels assurent que le panelSolitaire est mis à jour pour afficher les
		// nouvelles colonnes de cartes.

		rendreDernieresCartesVisibles();

		panelSolitaire.revalidate();
		panelSolitaire.repaint();
	}

	private void creerPioche(JLabel bgLabel) {
		System.out.println("Taille du deck après initialisation : " + SolitaireController.getDeck().size());
		ImageIcon cardBackIcon = new ImageIcon("src/cartes/CACHEE_CACHEE.png");
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		// Pioche vide initialement
		JLabel pileVideLabel = new JLabel();
		pileVideLabel.setBounds(630, 30, cardWidth, cardHeight);
		bgLabel.add(pileVideLabel);

		JLabel piocheLabel = new JLabel(cardBackIcon);
		piocheLabel.setBounds(730, 30, cardWidth, cardHeight);
		bgLabel.add(piocheLabel);

		// Ajout d'un MouseListener à piocheLabel pour gérer les clics et piocher une
		// carte
		piocheLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (deck.isEmpty()) {
					// Initialisez un compteur pour suivre l'image de mélange actuelle
					final int[] compteur = { 0 };

					Timer timer = new Timer(500, null); // Créer un timer avec un délai de 500 ms
					timer.addActionListener(actionEvent -> {
						compteur[0]++; // Incrémentez le compteur à chaque tick

						// En fonction du compteur, changez l'icône
						switch (compteur[0]) {
						case 1:
							piocheLabel.setIcon(new ImageIcon("src\\cartes\\melange1.png"));
							break;
						case 2:
							piocheLabel.setIcon(new ImageIcon("src\\cartes\\melange2.png"));
							break;
						case 3:
							piocheLabel.setIcon(new ImageIcon("src\\cartes\\melange3.png"));
							// Remélangez le deck ici pour s'assurer que le remélange se fait au dernier
							// changement d'image
							remelangerPiocheDansDeck(colonnesDeDepart);
							break;
						default:
							// Après le dernier changement, revenez à l'icône de carte retournée et arrêtez
							// le timer
							piocheLabel.setIcon(cardBackIcon);
							timer.stop();
						}
					});
					timer.setInitialDelay(0); // Commencez immédiatement sans retard
					timer.start();
				}
				if (!deck.isEmpty()) {
					// Continuez avec la logique de pioche existante ici, car maintenant le deck
					// n'est plus vide
					Carte carteTiree = deck.remove(deck.size() - 1);
					cartePiochee = carteTiree;
					cartePiochee.setVisible(true);
					List<Carte> colonne = colonnesDeDepart.get(11);
					colonne.add(carteTiree); // Ajoute la carte tirée à la colonne de pioche

					ImageIcon carteTireeIconBrute = carteToImageIcon(carteTiree);
					ImageIcon carteTireeIcon = resizeCardImage(carteTireeIconBrute.getDescription(), cardWidth,
							cardHeight);
					pileVideLabel.setIcon(carteTireeIcon);
//Désélection automatique de la carte piochée
					//carteSelectionnee = carteTiree;
					//colonneSourceSelectionnee = SolitaireController.INDEX_COLONNE_PIOCHE;

				} else {
					// Ici, vous pouvez gérer le cas où il n'y a plus de cartes à piocher après le
					// remélange, si nécessaire
				}
			}
		});

		// Gérer la sélection/déplacement de la carte piochée
		pileVideLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (cartePiochee != null && isPileVideLabelSelected) {
					isPileVideLabelSelected = false;
					carteSelectionnee = null;
					colonneSourceSelectionnee = -1;
					pileVideLabel.setBorder(null);
				} else if (cartePiochee != null) {
					isPileVideLabelSelected = true;
					carteSelectionnee = cartePiochee;
					System.out.println("Carte piochée: " + carteSelectionnee);
					System.out.println(colonnesDeDepart.get(11));
					colonneSourceSelectionnee = SolitaireController.INDEX_COLONNE_PIOCHE;
					pileVideLabel.setBorder(new LineBorder(Color.GREEN, 3));
				}
			}
		});

	}

	public static void remelangerPiocheDansDeck(List<List<Carte>> colonnesDeDepart) {
		// Assumons que l'index 11 est utilisé pour la pioche
		List<Carte> pioche = colonnesDeDepart.get(11); // Obtient la liste des cartes piochées
		SolitaireController.getDeck().addAll(pioche); // Ajoute toutes les cartes de la pioche au deck
		pioche.clear(); // Vide la pioche
		Collections.shuffle(SolitaireController.getDeck()); // Mélange le deck
	}

	private ImageIcon carteToImageIcon(Carte carte) {
		String nom = carte.getNom().toString();
		String couleur = carte.getCouleur().toString();
		String cheminImage = "src/cartes/" + nom + "_" + couleur + ".png";
		return new ImageIcon(cheminImage);
	}

	private ImageIcon obtenirImageCarte(Carte carte) {
		ImageIcon cardBackIcon = new ImageIcon("src\\cartes\\CACHEE_CACHEE.png");
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();
		if (carte == null)
			return null;
		String chemin = "src\\cartes\\" + carte.getNom() + "_" + carte.getCouleur() + ".png";
		return resizeCardImage(chemin, cardWidth, cardHeight); // Utilisez la méthode existante pour redimensionner
																// l'image
	}

	private void rendreDernieresCartesVisibles() {
		for (List<Carte> colonne : colonnesDeDepart) {
			if (!colonne.isEmpty()) {
				Carte derniereCarte = colonne.get(colonne.size() - 1);
				derniereCarte.setVisible(true);
			}
		}
	}

	public JPanel getMainPage() {
		return MainPage();
	}

	public JPanel getPanelSolitaire() {
		return PanelSolitaire();
	}

	public void creerColonneFinale(JLabel bgLabel) {
		int piocheXStart = 130; // Position de départ pour la première pile vide sur l'axe X
		int piocheYStart = 30; // Position de départ pour la première pile vide sur l'axe Y
		int piocheSpacing = 10; // Espace horizontal entre les piles vides
		ImageIcon cardBackIcon = new ImageIcon("src\\cartes\\CACHEE_CACHEE.png");
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		// Création des quatre colonnes finales
		for (int i = 0; i < 4; i++) {
			ImageIcon pileVideIcon = new ImageIcon("src\\cartes\\empty_pile.png"); // Image d'une pile vide
			JLabel pileVideLabel = new JLabel(pileVideIcon);
			int x = piocheXStart + (cardWidth + piocheSpacing) * i;
			int y = piocheYStart;
			pileVideLabel.setBounds(x, y, cardWidth, cardHeight);
			bgLabel.add(pileVideLabel);
		}

	}
}
