package vue;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import controleur.SolitaireController;
import controleur.Souris;
import modele.Carte;
import modele.NomCarte;

public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;

	// Constantes Layout
	private static final int GAME_COLUMNS_X_START = 130;
	private static final int GAME_COLUMNS_Y_START = 190;
	private static final int GAME_COLUMNS_X_SPACING = 10;
	private static final int GAME_COLUMNS_Y_SPACING = 20;

	// Variables de jeu
	private List<List<Carte>> colonnesDeDepart = SolitaireController.createStartColumns();
	private List<Carte> deck = SolitaireController.getDeck();
	private Carte carteSelectionnee = null;
	private Carte derniereCartePiochee = null;
	private Carte cartePiochee = null;
	private int colonneSourceSelectionnee = -1;
	private int positionCarteDansColonne;
	private boolean doitRemelanger = false;
	private boolean isPileVideLabelSelected = false;

	// Composants GUI
	private JPanel panelSolitaire = new JPanel();
	private JLabel pileVideLabel;
	private Clip musicClip;

	// Thèmes
	public static String currentTheme = "default";
	public static String currentCardTheme = "default";

	// Contrôle audio
	private static boolean isMusicMuted = true;

	// Contrôle souris
	private Souris souris;

	//////////////////////////////////////
	// INITIALISATION BACK DU JEU ////////
	//////////////////////////////////////

	// Gui
	public Gui() {
		this.souris = new Souris(this);
		initGUI();
		rendreDernieresCartesVisibles();
	}

	// Initialisation du GUI
	private void initGUI() {
		setTitle("Solitaire"); // Titre de l'application
		playMusic("src/ressources/Sounds/GameOST.wav"); // Choix de la musique
		initCustomFonts(); // Chargement fonts globalisé
		setSize(960, 540); // Taille de la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Leave
		setLocationRelativeTo(null); // Centre la fenêtre
		setIconImage(new ImageIcon("src/ressources/Images/Logo.png").getImage()); // Logo
		setResizable(false); // Empêche le redimensionnement de la fenêtre
		JPanel mainPanel = MainPage(); // Lancement de la page
		setContentPane(mainPanel);
		setVisible(true); // Rendre la fenêtre visible

	}

	// Définition du panel général
	public void setPanel(JPanel panel) {
		setContentPane(panel);
		if (panel instanceof JPanel) {
			panelSolitaire = panel;
		}
		revalidate();
		repaint();
	}

	// Chargements des polices globalisés
	private void initCustomFonts() {
		try {
			// Chemin relatif au fichier de police dans le dossier des ressources
			File fontFile = new File("src/ressources/Fonts/GOTHICI.ttf");
			Font gothici = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// Enregistre la police
			ge.registerFont(gothici);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		try {
			File fontFile = new File("src/ressources/Fonts/Gotham-Black.otf");
			Font gothamblack = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(gothamblack);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

	// Lancement de la musique
	public void playMusic(String filepath) {
		// Si musique, lancement en boucle
		try {
			File musicPath = new File(filepath);
			if (musicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				musicClip = AudioSystem.getClip();
				musicClip.open(audioInput);
				if (!isMusicMuted) {
					musicClip.start();
					musicClip.loop(Clip.LOOP_CONTINUOUSLY);
				}
			} else {
				System.out.println("No file");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	///////////////////////////
	// PAGE D'ACCUEIL DU JEU //
	///////////////////////////

	// Page principale du jeu
	public JPanel MainPage() {

		// Configuration initiale du JPanel principal
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(null);

		// Arrière-plan : chargement et mise en place de l'image de fond
		ImageIcon bgIcon = new ImageIcon("src/ressources/Background/Background.png");
		Image image = bgIcon.getImage();
		Image newimg = image.getScaledInstance(946, 503, Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		JLabel bgLabel = new JLabel(newIcon);
		bgLabel.setBounds(0, 0, 946, 503);
		panelPrincipal.add(bgLabel);

		// Particules d'arrière-plan
		ImageIcon particlesIcon = new ImageIcon("src/ressources/Images/particles.gif");
		JLabel particlesLabel = new JLabel(particlesIcon);
		particlesLabel.setBounds(-240, 0, 960, 540);
		panelPrincipal.add(particlesLabel);

		ImageIcon particlesIcon2 = new ImageIcon("src/ressources/Images/particles.gif");
		JLabel particlesLabel2 = new JLabel(particlesIcon2);
		particlesLabel2.setBounds(250, 0, 960, 540);
		panelPrincipal.add(particlesLabel2);

		// Icônes de chargement animé
		ImageIcon loadingIconOriginal = new ImageIcon("src/ressources/Images/loading.gif");
		Image tempLoading = loadingIconOriginal.getImage().getScaledInstance(125, 100, Image.SCALE_DEFAULT);
		ImageIcon resizedIcon = new ImageIcon(tempLoading);
		JLabel loadingLabel = new JLabel(resizedIcon);
		loadingLabel.setBounds(420, 200, 960, 540);
		panelPrincipal.add(loadingLabel);

		ImageIcon loading2IconOriginal = new ImageIcon("src/ressources/Images/loading.gif");
		Image tempLoading2 = loading2IconOriginal.getImage().getScaledInstance(125, 100, Image.SCALE_DEFAULT);
		ImageIcon resizedIcon2 = new ImageIcon(tempLoading2);
		JLabel loadingLabel2 = new JLabel(resizedIcon2);
		loadingLabel2.setBounds(-430, 200, 960, 540);
		panelPrincipal.add(loadingLabel2);

		// Configuration des étiquettes sous les boutons
		ImageIcon imageIcon = new ImageIcon("src/ressources/Images/BackgroundButton.png");
		JLabel imageLabel = new JLabel(imageIcon);
		imageLabel.setBounds(-20, -15, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		imageLabel.setVisible(false);
		bgLabel.add(imageLabel);

		ImageIcon bouton2icon = new ImageIcon("src/ressources/Images/BackgroundButton.png");
		JLabel bouton2Label = new JLabel(bouton2icon);
		bouton2Label.setBounds(-20, 45, bouton2icon.getIconWidth(), bouton2icon.getIconHeight());
		bouton2Label.setVisible(false);
		bgLabel.add(bouton2Label);

		ImageIcon bouton3icon = new ImageIcon("src/ressources/Images/BackgroundButton.png");
		JLabel bouton3Label = new JLabel(bouton3icon);
		bouton3Label.setBounds(-20, 105, bouton3icon.getIconWidth(), bouton3icon.getIconHeight());
		bouton3Label.setVisible(false);
		bgLabel.add(bouton3Label);

		ImageIcon bouton4icon = new ImageIcon("src/ressources/Images/BackgroundButton.png");
		JLabel bouton4Label = new JLabel(bouton4icon);
		bouton4Label.setBounds(-20, 165, bouton4icon.getIconWidth(), bouton4icon.getIconHeight());
		bouton4Label.setVisible(false);
		bgLabel.add(bouton4Label);

		// Configuration des boutons interactifs avec MouseListeners simplifiés
		JLabel nouvellepartie = new JLabel("Nouvelle partie");
		nouvellepartie.setFont(new Font("Century Gothic Italic", Font.PLAIN, 30));
		nouvellepartie.setForeground(Color.WHITE);
		nouvellepartie.setBounds(20, 20, 500, 30);
		nouvellepartie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				nouvellePartie();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				imageLabel.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				imageLabel.setVisible(false);
			}
		});
		bgLabel.add(nouvellepartie);

		JLabel regles = new JLabel("Règles");
		regles.setFont(new Font("Century Gothic Italic", Font.PLAIN, 30));
		regles.setForeground(Color.WHITE);
		regles.setBounds(20, 80, 500, 30);
		regles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setPanel(PanelRegles());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				bouton2Label.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				bouton2Label.setVisible(false);
			}
		});
		bgLabel.add(regles);

		JLabel options = new JLabel("Options");
		options.setFont(new Font("Century Gothic Italic", Font.PLAIN, 30));
		options.setForeground(Color.WHITE);
		options.setBounds(20, 140, 500, 30);
		options.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setPanel(PanelOptions());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				bouton3Label.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				bouton3Label.setVisible(false);
			}
		});
		bgLabel.add(options);

		JLabel quitter = new JLabel("Quitter");
		quitter.setFont(new Font("Century Gothic Italic", Font.PLAIN, 30));
		quitter.setForeground(Color.WHITE);
		quitter.setBounds(20, 200, 500, 30);
		quitter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				bouton4Label.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				bouton4Label.setVisible(false);
			}
		});
		bgLabel.add(quitter);

		// Ajout de la musique
		panelPrincipal.add(musicIcon());
		bgLabel.setComponentZOrder(musicIcon(), 0);

		// Gestion des ordres d'affichage par couche

		bgLabel.setComponentZOrder(particlesLabel, bgLabel.getComponentCount() - 1);
		bgLabel.setComponentZOrder(particlesLabel2, bgLabel.getComponentCount() - 1);

		bgLabel.setComponentZOrder(imageLabel, 0);
		bgLabel.setComponentZOrder(bouton2Label, 0);
		bgLabel.setComponentZOrder(bouton3Label, 0);
		bgLabel.setComponentZOrder(bouton4Label, 0);

		bgLabel.setComponentZOrder(nouvellepartie, 0);
		bgLabel.setComponentZOrder(regles, 0);
		bgLabel.setComponentZOrder(options, 0);
		bgLabel.setComponentZOrder(quitter, 0);

		bgLabel.setComponentZOrder(loadingLabel2, 0);
		bgLabel.setComponentZOrder(loadingLabel, 0);

		return panelPrincipal;
	}

	////////////////////////////
	// FONCTIONNEMENT DU JEU //
	////////////////////////////

	// Panel du solitaire d'initialisation
	public JPanel PanelSolitaire() {
		JPanel panelSolitaire = new JPanel();
		panelSolitaire.setLayout(null);

		// Chargement de l'arrière-plan
		ImageIcon bgIcon = new ImageIcon(getBackgroundImagePath());
		JLabel bgLabel = new JLabel(bgIcon);
		bgLabel.setBounds(0, 0, 960, 540);
		panelSolitaire.add(bgLabel);

		// Chargement de l'icône de dos de carte et définition de sa taille
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		// Parcours des colonnes
		for (int col = 0; col < colonnesDeDepart.size(); col++) {
			List<Carte> colonne = colonnesDeDepart.get(col);
			// Parcours des cartes dans chaque colonne
			for (int cardIndex = 0; cardIndex < colonne.size(); cardIndex++) {
				JLabel cardLabel;
				// Index final de la carte et de la colonne pour la gestion des événements de
				// souris
				final int finalCardIndex = cardIndex;
				final int finalCol = col;
				// Calcul des coordonnées x et y pour le placement de la carte
				int x = GAME_COLUMNS_X_START + (cardWidth + GAME_COLUMNS_X_SPACING) * col;
				int y = GAME_COLUMNS_Y_START + GAME_COLUMNS_Y_SPACING * cardIndex;

				Carte carte = colonne.get(cardIndex);
				if (carte != null) {
					ImageIcon carteImage;
					// Chargement de l'image de la carte en fonction de sa visibilité
					if (carte.estVisible()) {
						String cardPath = "src/ressources/Cards/" + carte.getNom().toString() + "_"
								+ carte.getCouleur().toString() + ".png";
						carteImage = resizeCardImage(cardPath, cardWidth, cardHeight);
					} else {
						// Utilisation de l'image de dos de carte si la carte est cachée
						carteImage = cardBackIcon;
					}
					// Création de l'étiquette pour afficher l'image de la carte
					cardLabel = new JLabel(carteImage);

					// Définition des dimensions et position de l'étiquette de la carte
					cardLabel.setBounds(x, y, cardWidth, cardHeight);

					// Ajout Listener pour gérer les clics sur les cartes
					cardLabel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (carte.estVisible()) {
								// Logique de sélection de carte
								if (colonneSourceSelectionnee == -1) {
									colonneSourceSelectionnee = finalCol;
									positionCarteDansColonne = finalCardIndex;
									carteSelectionnee = carte;

									cardLabel.setBorder(new LineBorder(Color.GREEN, 3));

								} else {
									// Logique de déplacement de carte
									if (finalCol != colonneSourceSelectionnee) {
										boolean reussi = SolitaireController.moveCard(colonnesDeDepart,
												colonneSourceSelectionnee, finalCol);
										if (reussi) {

											derniereCartePiochee = null;
											pileVideLabel.setIcon(null);

											reconstruireAffichageColonnes();
										} else {

											reconstruireAffichageColonnes();
										}
										colonneSourceSelectionnee = -1;
										positionCarteDansColonne = -1;
									} else if (finalCol == colonneSourceSelectionnee
											&& finalCardIndex == positionCarteDansColonne) {
										// Annulation de la sélection
										cardLabel.setBorder(null);
										colonneSourceSelectionnee = -1;
										positionCarteDansColonne = -1;

									}
								}
							}
						}
					});
					// Ajout de la carte + gestion de l'ordre d'affichage
					bgLabel.add(cardLabel);
					bgLabel.setComponentZOrder(cardLabel, colonne.size() - cardIndex - 1);
				}
			}
		}

		// Création de la pioche et des colonnes finales
		creerPioche(bgLabel);
		creerColonneFinale(bgLabel);

		// Configuration et ajout des boutons de retour)
		bgLabel.add(backButton());

		// Ajout du contrôle de la musique
		panelSolitaire.add(musicIcon());
		bgLabel.setComponentZOrder(musicIcon(), 0);

		return panelSolitaire;
	}

	// Recréation des colonnes en fonction du jeu
	private void reconstruireAffichageColonnes() {

		// Suppression des composants existants pour une réinitialisation de l'affichage
		panelSolitaire.removeAll();
		panelSolitaire.setLayout(null);

		// Redessin de l'arrière-plan sur le panelSolitaire
		ImageIcon bgIcon = new ImageIcon(getBackgroundImagePath());
		JLabel bgLabel = new JLabel(bgIcon);
		bgLabel.setBounds(0, 0, 960, 540);
		panelSolitaire.add(bgLabel);

		// Chargement de l'icône de dos de carte et réinitialisation des dimensions
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		// Itération à travers chaque colonne pour reconstruire l'affichage des cartes
		for (int col = 0; col < 7; col++) {
			List<Carte> colonne = colonnesDeDepart.get(col);
			if (colonne.isEmpty()) {
				final int finalCol = col;

				// Affiche un JLabel pour une colonne vide
				JLabel pileVideLabel = new JLabel(new ImageIcon("src/ressources/Cards/empty_pile.png"));
				int x = GAME_COLUMNS_X_START + (cardWidth + GAME_COLUMNS_X_SPACING) * col;
				int y = GAME_COLUMNS_Y_START;

				pileVideLabel.setBounds(x, y, cardWidth, cardHeight);
				bgLabel.add(pileVideLabel);
				bgLabel.setComponentZOrder(pileVideLabel, 0);
				pileVideLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (carteSelectionnee != null && carteSelectionnee.getNom() == NomCarte.ROI) {
							// Ajoute la carte sélectionnée à la colonne vide
							colonnesDeDepart.get(finalCol).add(carteSelectionnee);
							// Supprime la carte de sa colonne source
							colonnesDeDepart.get(colonneSourceSelectionnee).remove(carteSelectionnee);
							colonnesDeDepart.get(colonneSourceSelectionnee)
									.get(colonnesDeDepart.get(colonneSourceSelectionnee).size() - 1).setVisible(true);
							// Réinitialise les variables de sélection
							carteSelectionnee = null;
							colonneSourceSelectionnee = -1;
							// Reconstruit l'affichage pour refléter le changement
							reconstruireAffichageColonnes();
						}
					}
				});
			}

			// Itération sur chaque colonne pour placer les cartes
			for (int cardIndex = 0; cardIndex < colonne.size(); cardIndex++) {
				Carte carte = colonne.get(cardIndex);
				JLabel cardLabel;
				int x = GAME_COLUMNS_X_START + (cardWidth + GAME_COLUMNS_X_SPACING) * col;
				int y = GAME_COLUMNS_Y_START + GAME_COLUMNS_Y_SPACING * cardIndex;
				final int finalCardIndex = cardIndex;
				final int finalCol = col;

				// Si dernière carte, tire carte aléatoire
				if (col == colonneSourceSelectionnee && cardIndex == colonne.size() - 1) {
					ImageIcon carteIcon = obtenirImageCarte(carte);
					cardLabel = new JLabel(carteIcon);
				} else {
					// Choisis l'image en fonction du mouvement
					String imagePath = carte.estVisible()
							? "src/ressources/Cards/" + carte.getNom().toString() + "_" + carte.getCouleur().toString()
									+ ".png"
							: getCardBackImagePath();
					ImageIcon carteImage = resizeCardImage(imagePath, cardWidth, cardHeight);
					cardLabel = new JLabel(carteImage);
				}

				// Placement de la carte
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

								cardLabel.setBorder(new LineBorder(Color.GREEN, 3)); // Bordure de sélection

							} else {
								// Si une carte est déjà sélectionnée et qu'on clique sur une autre colonne
								if (finalCol != colonneSourceSelectionnee) {
									boolean reussi = SolitaireController.moveCard(colonnesDeDepart,
											colonneSourceSelectionnee, finalCol);
									if (reussi) {

										derniereCartePiochee = null;
										pileVideLabel.setIcon(null);

										reconstruireAffichageColonnes();

									} else {

										reconstruireAffichageColonnes();
									}
									colonneSourceSelectionnee = -1; // Réinitialiser la sélection après un déplacement
									positionCarteDansColonne = -1;
								} else if (finalCol == colonneSourceSelectionnee
										&& finalCardIndex == positionCarteDansColonne) {
									// Annulation sélection
									cardLabel.setBorder(null);
									colonneSourceSelectionnee = -1;
									positionCarteDansColonne = -1;

								}
							}
						}
					}
				});

				// Gestion colonnes finales
				int xStartFinale = 130;
				for (int i = 7; i <= 10; i++) { // Parcourir les colonnes finales
					List<Carte> colonneFinale = colonnesDeDepart.get(i);
					int y2 = 30;
					int x2 = xStartFinale + ((i - 7) * (cardWidth + GAME_COLUMNS_X_SPACING));

					for (Carte carte2 : colonneFinale) {
						// Afficher chaque carte de la colonne finale
						ImageIcon carteImage = obtenirImageCarte(carte2);
						JLabel cardLabel2 = new JLabel(carteImage);
						cardLabel2.setBounds(x2, y2, carteImage.getIconWidth(), carteImage.getIconHeight());
						bgLabel.add(cardLabel2);
						bgLabel.setComponentZOrder(cardLabel2, 0);
					}
				}
				panelSolitaire.add(cardLabel);
				panelSolitaire.setComponentZOrder(cardLabel, 0);
			}
		}

		// Création de la pioche
		creerPioche(bgLabel);
		creerColonneFinale(bgLabel);

		// Boutons pour revenir à l'accueil

		bgLabel.add(backButton());

		// Rendre chaque dernière carte visible
		rendreDernieresCartesVisibles();

		// Ajout de la musique
		JLabel musicControlLabel = musicIcon();
		panelSolitaire.add(musicControlLabel);

		bgLabel.add(musicControlLabel);
		bgLabel.setComponentZOrder(musicControlLabel, 0);

		// Actualisation
		panelSolitaire.revalidate();
		panelSolitaire.repaint();
	}

	// Création de la pioche
	private void creerPioche(JLabel bgLabel) {

		// Chargement de l'icône de dos
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		// Initialisation et positionnement du label pour une pile vide
		this.pileVideLabel = new JLabel();
		this.pileVideLabel.setBounds(630, 30, cardWidth, cardHeight);
		bgLabel.add(this.pileVideLabel);

		// Création et positionnement du label pour la pioche avec l'image de dos de
		// carte
		JLabel piocheLabel = new JLabel(cardBackIcon);
		piocheLabel.setBounds(730, 30, cardWidth, cardHeight);
		bgLabel.add(piocheLabel);

		// Pioche vide
		if (deck.isEmpty()) {
			piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/empty_pile_pioche.png"));
			doitRemelanger = true;
		} else {
			// Pioche "non" vide
			piocheLabel.setIcon(cardBackIcon);
			doitRemelanger = false;
		}

		if (derniereCartePiochee != null) {
			// Vérification si la derniereCartePiochee a été déplacée
			if (colonnesDeDepart.get(SolitaireController.INDEX_COLONNE_PIOCHE).contains(derniereCartePiochee)) {
				// Si la dernière carte piochée est toujours dans la pioche, affichez-la
				ImageIcon cartePiocheeIcon = carteToImageIcon(derniereCartePiochee);
				ImageIcon cartePiocheeIconRedimensionnee = resizeCardImage(cartePiocheeIcon.getDescription(), cardWidth,
						cardHeight);
				pileVideLabel.setIcon(cartePiocheeIconRedimensionnee);
			} else {
				// Si la dernière carte piochée a été déplacée, ne l'affichez pas
				pileVideLabel.setIcon(null);
				derniereCartePiochee = null;
			}
		} else {
			pileVideLabel.setIcon(null);
		}

		// Ajout d'un Listener à piocheLabel pour gérer les clics et piocher une carte
		piocheLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (doitRemelanger) {
					piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/empty_pile_pioche.png"));
					// Animation de mélange
					final int[] compteur = { 0 };

					Timer timer = new Timer(80, null);
					timer.addActionListener(actionEvent -> {
						compteur[0]++;

						switch (compteur[0]) {
						case 1:
							piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/melange1.png"));
							break;
						case 2:
							piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/melange2.png"));
							break;
						case 3:
							piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/melange3.png"));
							break;
						case 4:
							piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/melange4.png"));
							break;
						case 5:
							piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/melange5.png"));
							break;
						case 6:
							piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/melange1.png"));
							remelangerPiocheDansDeck(colonnesDeDepart);
							doitRemelanger = false;
							piocheLabel.setIcon(cardBackIcon);
							break;
						default:
							piocheLabel.setIcon(cardBackIcon);
							timer.stop();
						}
					});
					timer.setInitialDelay(0);
					timer.start();
				} else if (deck.isEmpty()) {
					// Si attente de mélange
					piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/empty_pile_pioche.png"));
					doitRemelanger = true;
				} else {
					// Si pioche
					Carte carteTiree = deck.remove(deck.size() - 1); // Retire la dernière carte du deck
					derniereCartePiochee = carteTiree;
					cartePiochee = carteTiree;
					cartePiochee.setVisible(true);
					List<Carte> colonnePioche = colonnesDeDepart.get(SolitaireController.INDEX_COLONNE_PIOCHE);
					colonnePioche.add(carteTiree);
					ImageIcon carteTireeIcon = carteToImageIcon(carteTiree);
					ImageIcon carteTireeIconRedimensionnee = resizeCardImage(carteTireeIcon.getDescription(), cardWidth,
							cardHeight);
					pileVideLabel.setIcon(carteTireeIconRedimensionnee); // Image de la carte tirée
				}
			}
		});

		// Listener de la carte piochée
		pileVideLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Vérifie si pioche vide ou si dernière carte piochée déplacée
				if (cartePiochee == null
						|| !colonnesDeDepart.get(SolitaireController.INDEX_COLONNE_PIOCHE).contains(cartePiochee)) {
					pileVideLabel.setIcon(null);
					isPileVideLabelSelected = false;
					carteSelectionnee = null;
					colonneSourceSelectionnee = -1;
					pileVideLabel.setBorder(null);
					cartePiochee = null;
				} else {
					// Si pioche ne bouge pas
					if (isPileVideLabelSelected) {
						isPileVideLabelSelected = false;
						carteSelectionnee = null;
						colonneSourceSelectionnee = -1;
						pileVideLabel.setBorder(null);
					} else {
						isPileVideLabelSelected = true;
						carteSelectionnee = cartePiochee;
						colonneSourceSelectionnee = SolitaireController.INDEX_COLONNE_PIOCHE;
						pileVideLabel.setBorder(new LineBorder(Color.GREEN, 3));
					}
				}
			}
		});
	}

	// Gestion des colonnes finales
	private void creerColonneFinale(JLabel bgLabel) {

		// Positions de départ
		int piocheXStart = 130;
		int piocheYStart = 30;
		int piocheSpacing = 10;

		// Dos de carte
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		// Création des quatre colonnes finales
		for (int i = 0; i < 4; i++) {
			ImageIcon pileVideIcon = new ImageIcon("src/ressources/Cards/empty_pile.png");
			JLabel pileVideLabel = new JLabel(pileVideIcon);
			int x = piocheXStart + (cardWidth + piocheSpacing) * i;
			int y = piocheYStart;
			pileVideLabel.setBounds(x, y, cardWidth, cardHeight);
			bgLabel.add(pileVideLabel);

			// Gestion clics colonnes finales
			final int finalI = i;
			pileVideLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// Vérification s'il y a une carte sélectionnée dans une colonne source
					if (carteSelectionnee != null) {
						// Vérification si la carte peut être déplacée vers cette pile finale
						if (SolitaireController.moveToFinalStack(carteSelectionnee, finalI + 7, colonnesDeDepart)) {
							// Ajouter la carte à la pile finale
							List<Carte> pileFinale = colonnesDeDepart.get(finalI + 7);
							pileFinale.add(carteSelectionnee);

							if (SolitaireController.aGagner(colonnesDeDepart)) {
								overlayWin();
							}
							// Supprimer la carte de son emplacement précédent (colonne source)
							if (colonneSourceSelectionnee != -1) {
								List<Carte> colonneSource = colonnesDeDepart.get(colonneSourceSelectionnee);
								colonneSource.remove(carteSelectionnee);
							}
							// Mettre à jour l'affichage
							reconstruireAffichageColonnes();

							// Réinitialiser les variables de sélection de carte
							carteSelectionnee = null;
							colonneSourceSelectionnee = -1;
						}
					}
				}
			});
		}
	}

	// Mélange de la pioche
	public static void remelangerPiocheDansDeck(List<List<Carte>> colonnesDeDepart) {
		List<Carte> pioche = colonnesDeDepart.get(11); // Obtient la liste des cartes piochées (11 = pioche)
		SolitaireController.getDeck().addAll(pioche); // Ajoute toutes les cartes de la pioche au deck
		pioche.clear(); // Vide la pioche
		Collections.shuffle(SolitaireController.getDeck()); // Mélange le deck
	}

	// Conversion nécessaire
	private ImageIcon carteToImageIcon(Carte carte) {
		// Conversion de format
		String nom = carte.getNom().toString();
		String couleur = carte.getCouleur().toString();
		String cheminImage = "src/ressources/Cards/" + nom + "_" + couleur + ".png";
		return new ImageIcon(cheminImage);
	}

	// Récupérer image de la carte nécessaire
	private ImageIcon obtenirImageCarte(Carte carte) {
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();
		if (carte == null)
			return null;
		String chemin = "src/ressources/Cards/" + carte.getNom() + "_" + carte.getCouleur() + ".png";
		return resizeCardImage(chemin, cardWidth, cardHeight); // Redimension
	}

	// Rendre les dernières cartes visibles à chaque action
	private void rendreDernieresCartesVisibles() {
		// Pour éviter problèmes de cartes non cliquables, tout retourner
		for (List<Carte> colonne : colonnesDeDepart) {
			if (!colonne.isEmpty()) {
				Carte derniereCarte = colonne.get(colonne.size() - 1);
				derniereCarte.setVisible(true);
			}
		}
	}

	/////////////////////////
	// ONGLETS SECONDAIRES //
	/////////////////////////

	// Panel des règles
	public JPanel PanelRegles() {

		// Création et configuration du JPanel
		JPanel panelRegles = new JPanel();
		panelRegles.setLayout(null);

		// Configuration et ajout de l'arrière-plan
		ImageIcon rulesIcon = new ImageIcon(getRulesBackgroundPath());
		JLabel rulesLabel = new JLabel(rulesIcon);
		rulesLabel.setBounds(0, 0, 946, 503);
		panelRegles.add(rulesLabel);

		// Création et configuration du JPanel pour les boutons
		JPanel panelBoutons = new JPanel();
		panelBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panelBoutons.setOpaque(false);

		// Configuration et ajout du bouton de retour
		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 24));
		boutonRetour.setBounds(20, 450, 140, 40);
		panelRegles.add(boutonRetour);

		// Ordre superposition
		panelRegles.setComponentZOrder(boutonRetour, 0);
		panelRegles.setComponentZOrder(rulesLabel, 1);
		boutonRetour.addActionListener(e -> setPanel(getMainPage())); // Action pour retourner à la page principale

		// Ajout du contrôle de la musique
		panelRegles.add(musicIcon());
		rulesLabel.setComponentZOrder(musicIcon(), 0);

		return panelRegles;
	}

	// Panel d'options général
	public JPanel PanelOptions() {

		// Création du panel
		JPanel panelOptions = new JPanel();
		panelOptions.setLayout(null);

		// Arrière plan
		ImageIcon optionsIcon = new ImageIcon(getPersonnalisationBackgroundPath());
		JLabel optionsLabel = new JLabel(optionsIcon);
		optionsLabel.setBounds(0, 0, 946, 503);
		panelOptions.add(optionsLabel);

		// Panel de boutons
		JPanel panelBoutons = new JPanel();
		panelBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panelBoutons.setOpaque(false);

		// Bouton de retour
		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 24));
		boutonRetour.setBounds(20, 450, 140, 40);
		boutonRetour.addActionListener(e -> setPanel(getMainPage()));
		panelOptions.add(boutonRetour);

		// Bouton pour changer la couleur du fond
		JButton changeBackgroundButton = new JButton("Changer couleur du fond");
		changeBackgroundButton.setBackground(new Color(22, 120, 44));
		changeBackgroundButton.setForeground(Color.WHITE);
		changeBackgroundButton.setFocusPainted(false);
		changeBackgroundButton.setFont(new Font("Gotham Black", Font.BOLD, 24));
		changeBackgroundButton.setBounds(230, 180, 500, 50); // Position centrée
		changeBackgroundButton.addActionListener(e -> setPanel(PanelBackgroundOptions()));
		changeBackgroundButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				changeBackgroundButton.setBackground(new Color(15, 82, 30)); // Couleur lors du survol
			}

			@Override
			public void mouseExited(MouseEvent e) {
				changeBackgroundButton.setBackground(new Color(22, 120, 44)); // Revenir à la couleur initiale
			}
		});
		panelOptions.add(changeBackgroundButton);

		// Bouton pour changer la couleur des cartes
		JButton changeCardColorButton = new JButton("Changer couleur des cartes");
		changeCardColorButton.setBackground(new Color(231, 159, 165));
		changeCardColorButton.setForeground(Color.WHITE);
		changeCardColorButton.setFocusPainted(false);
		changeCardColorButton.setFont(new Font("Gotham Black", Font.BOLD, 24));
		changeCardColorButton.setBounds(230, 280, 500, 50); // Position centrée
		changeCardColorButton.addActionListener(e -> setPanel(PanelCardOptions()));
		changeCardColorButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				changeCardColorButton.setBackground(new Color(191, 129, 135)); // Couleur lors du survol
			}

			@Override
			public void mouseExited(MouseEvent e) {
				changeCardColorButton.setBackground(new Color(231, 159, 165)); // Revenir à la couleur initiale
			}
		});

		// Statut actuel des thèmes
		// Pour le thème de fond
		String[] backgroundThemeInfo = getBackgroundThemeNameAndColor(currentTheme);
		JLabel backgroundThemeLabel = new JLabel("Thème actuel du fond : " + backgroundThemeInfo[0]);
		backgroundThemeLabel.setForeground(Color.WHITE);
		backgroundThemeLabel.setFont(new Font("Gotham Black", Font.BOLD, 18));
		backgroundThemeLabel.setBounds(230, 230, 500, 30);
		panelOptions.add(backgroundThemeLabel);

		// Pour le thème des cartes
		String[] cardThemeInfo = getCardThemeNameAndColor(currentCardTheme);
		JLabel cardThemeLabel = new JLabel("Thème actuel des cartes : " + cardThemeInfo[0]);
		cardThemeLabel.setForeground(Color.WHITE);
		cardThemeLabel.setFont(new Font("Gotham Black", Font.BOLD, 18));
		cardThemeLabel.setBounds(230, 330, 500, 30);
		panelOptions.add(cardThemeLabel);

		// Changement du mode de jeu (52/32)
		// Création des boutons et du statut
		JLabel deckModeLabel = new JLabel();
		deckModeLabel.setFont(new Font("Gotham Black", Font.BOLD, 18));
		deckModeLabel.setForeground(Color.WHITE);
		deckModeLabel.setBounds(230, 440, 500, 30);
		deckModeLabel.setText("Mode actuel: "
				+ (SolitaireController.getDeckType() == SolitaireController.DeckType.DECK_52 ? "52 cartes"
						: "32 cartes"));
		panelOptions.add(deckModeLabel);
		panelOptions.add(changeCardColorButton);

		JButton change52cards = new JButton("Mode 52 cartes");
		change52cards.setBackground(new Color(32, 60, 182));
		change52cards.setForeground(Color.WHITE);
		change52cards.setFocusPainted(false);
		change52cards.setFont(new Font("Gotham Black", Font.BOLD, 24));
		change52cards.setBounds(230, 380, 230, 50);
		change52cards.addActionListener(e -> {
			SolitaireController.setDeckType(SolitaireController.DeckType.DECK_52);
			SolitaireController.adjustCardValuesForDeckType();
			deckModeLabel.setText("Mode actuel: 52 cartes");

		});
		panelOptions.add(change52cards);

		JButton change32cards = new JButton("Mode 32 cartes");
		change32cards.setBackground(new Color(223, 195, 73));
		change32cards.setForeground(Color.WHITE);
		change32cards.setFocusPainted(false);
		change32cards.setFont(new Font("Gotham Black", Font.BOLD, 24));
		change32cards.setBounds(500, 380, 230, 50);
		change32cards.addActionListener(e -> {
			SolitaireController.setDeckType(SolitaireController.DeckType.DECK_32);
			SolitaireController.adjustCardValuesForDeckType();
			deckModeLabel.setText("Mode actuel: 32 cartes");

		});
		panelOptions.add(change32cards);

		// Ajout du contrôle de la musique
		panelOptions.add(musicIcon());
		optionsLabel.setComponentZOrder(musicIcon(), 0);

		// Ordre superposition
		panelOptions.setComponentZOrder(optionsLabel, panelOptions.getComponentCount() - 1);
		panelOptions.setComponentZOrder(changeCardColorButton, 0);
		panelOptions.setComponentZOrder(changeBackgroundButton, 0);
		panelOptions.setComponentZOrder(boutonRetour, 0);

		return panelOptions;
	}

	// Panel d'options du fond
	public JPanel PanelBackgroundOptions() {

		// Configuration du panel pour les options d'arrière-plan.
		JPanel panelBackgroundOptions = new JPanel();
		panelBackgroundOptions.setLayout(null);

		// Ajout de l'image d'arrière-plan au panel.
		ImageIcon backgroundOptionsIcon = new ImageIcon(getColorBackgroundPath());
		JLabel backgroundOptionsLabel = new JLabel(backgroundOptionsIcon);
		backgroundOptionsLabel.setBounds(0, 0, 946, 503);
		panelBackgroundOptions.add(backgroundOptionsLabel);

		// Création et ajout du bouton de retour avec action pour revenir aux options
		// principales.
		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 24));
		boutonRetour.setBounds(20, 450, 140, 40);
		boutonRetour.addActionListener(e -> setPanel(PanelOptions()));
		panelBackgroundOptions.add(boutonRetour);

		// Couleurs des boutons
		Color green = new Color(22, 120, 44);
		Color purple = new Color(69, 3, 55);
		Color red = new Color(113, 13, 27);
		Color blue = new Color(5, 52, 83);

		// Création des boutons + refresh du background en temps réel
		JButton boutonVert = createButton("Vert", green, 260, 200, 200, 50);
		boutonVert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentTheme = "default"; // Change le thème
				refreshBackground();
			}
		});

		JButton boutonViolet = createButton("Violet", purple, 510, 200, 200, 50);
		boutonViolet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentTheme = "purple"; // Change le thème
				refreshBackground();
			}
		});

		JButton boutonRouge = createButton("Rouge", red, 260, 270, 200, 50);
		boutonRouge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentTheme = "red"; // Change le thème
				refreshBackground();
			}
		});

		JButton boutonBleu = createButton("Bleu", blue, 510, 270, 200, 50);
		boutonBleu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentTheme = "blue"; // Change le thème
				refreshBackground();
			}
		});

		panelBackgroundOptions.add(boutonVert);
		panelBackgroundOptions.add(boutonViolet);
		panelBackgroundOptions.add(boutonRouge);
		panelBackgroundOptions.add(boutonBleu);

		// Ajout du contrôle de la musique
		panelBackgroundOptions.add(musicIcon());
		backgroundOptionsLabel.setComponentZOrder(musicIcon(), 0);

		// Ordre
		panelBackgroundOptions.setComponentZOrder(backgroundOptionsLabel,
				panelBackgroundOptions.getComponentCount() - 1);
		panelBackgroundOptions.setComponentZOrder(boutonRetour, 0);

		return panelBackgroundOptions;
	}

	// Méthode de bouton générique
	private JButton createButton(String text, Color backgroundColor, int x, int y, int width, int height) {
		JButton button = new JButton(text);
		button.setBackground(backgroundColor);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setFont(new Font("Gotham Black", Font.BOLD, 24));
		button.setBounds(x, y, width, height);
		return button;
	}

	// Méthodes pour les changements de thème //
	// Récupérer la couleur en fonction du thème
	public static String getBackgroundImagePath() {
		switch (currentTheme) {
		case "purple":
			return "src/ressources/Background/BackgroundGamePurple.png";
		case "red":
			return "src/ressources/Background/BackgroundGameRed.png";
		case "blue":
			return "src/ressources/Background/BackgroundGameBlue.png";
		default:
			return "src/ressources/Background/BackgroundGame.png";
		}
	}

	// Récupérer la couleur en fonction du thème
	public static String getPersonnalisationBackgroundPath() {
		switch (currentTheme) {
		case "purple":
			return "src/ressources/Background/BackgroundPersonnalisationPurple.png";
		case "red":
			return "src/ressources/Background/BackgroundPersonnalisationRed.png";
		case "blue":
			return "src/ressources/Background/BackgroundPersonnalisationBlue.png";
		default:
			return "src/ressources/Background/BackgroundPersonnalisation.png";
		}
	}

	// Récupérer la couleur en fonction du thème
	public static String getColorBackgroundPath() {
		switch (currentTheme) {
		case "purple":
			return "src/ressources/Background/BackgroundColorPurple.png";
		case "red":
			return "src/ressources/Background/BackgroundColorRed.png";
		case "blue":
			return "src/ressources/Background/BackgroundColorBlue.png";
		default:
			return "src/ressources/Background/BackgroundColor.png";
		}
	}

	// Récupérer la couleur en fonction du thème
	public static String getColorCardBackgroundPath() {
		switch (currentTheme) {
		case "purple":
			return "src/ressources/Background/BackgroundCardColorPurple.png";
		case "red":
			return "src/ressources/Background/BackgroundCardColorRed.png";
		case "blue":
			return "src/ressources/Background/BackgroundCardColorBlue.png";
		default:
			return "src/ressources/Background/BackgroundCardColor.png";
		}
	}

	// Récupérer la couleur en fonction du thème
	public static String getRulesBackgroundPath() {
		switch (currentTheme) {
		case "purple":
			return "src/ressources/Background/rulesPurple.png";
		case "red":
			return "src/ressources/Background/rulesRed.png";
		case "blue":
			return "src/ressources/Background/rulesBlue.png";
		default:
			return "src/ressources/Background/rules.png";
		}
	}

	// Rafraichir l'affichage du fond
	public void refreshBackground() {
		setContentPane(PanelBackgroundOptions()); // Reconstruire et afficher le panel principal
		validate();
		repaint();
	}

	// Panel du choix de couleur de cartes
	public JPanel PanelCardOptions() {

		// Panel
		JPanel panelCardOptions = new JPanel();
		panelCardOptions.setLayout(null);

		// Arrière plan
		ImageIcon cardOptionsIcon = new ImageIcon(getColorCardBackgroundPath());
		JLabel cardOptionsLabel = new JLabel(cardOptionsIcon);
		cardOptionsLabel.setBounds(0, 0, 946, 503);
		panelCardOptions.add(cardOptionsLabel);

		// Bouton retour
		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 24));
		boutonRetour.setBounds(20, 450, 140, 40);
		boutonRetour.addActionListener(e -> setPanel(PanelOptions()));
		panelCardOptions.add(boutonRetour);

		// Définir les couleurs pour les boutons
		Color green = new Color(22, 120, 44);
		Color purple = new Color(69, 3, 55);
		Color red = new Color(113, 13, 27);
		Color blue = new Color(5, 52, 83);

		// Boutons pour choisir la couleur des cartes
		JButton boutonVert = createButton("Vert", green, 260, 270, 200, 50);
		boutonVert.addActionListener(e -> {
			currentCardTheme = "green";
			refreshCard();
		});

		JButton boutonViolet = createButton("Violet", purple, 510, 200, 200, 50);
		boutonViolet.addActionListener(e -> {
			currentCardTheme = "purple";
			refreshCard();
		});

		JButton boutonRouge = createButton("Rouge", red, 260, 200, 200, 50);
		boutonRouge.addActionListener(e -> {
			currentCardTheme = "default";
			refreshCard();
		});

		JButton boutonBleu = createButton("Bleu", blue, 510, 270, 200, 50);
		boutonBleu.addActionListener(e -> {
			currentCardTheme = "blue";
			refreshCard();
		});

		panelCardOptions.add(boutonVert);
		panelCardOptions.add(boutonViolet);
		panelCardOptions.add(boutonRouge);
		panelCardOptions.add(boutonBleu);

		// Ajout du contrôle de la musique
		panelCardOptions.add(musicIcon());
		cardOptionsLabel.setComponentZOrder(musicIcon(), 0);
		panelCardOptions.add(cardOptionsLabel);

		// Ordre
		panelCardOptions.setComponentZOrder(cardOptionsLabel, panelCardOptions.getComponentCount() - 1);
		panelCardOptions.setComponentZOrder(boutonRetour, 0);

		return panelCardOptions;
	}

	// Récupérer la couleur en fonction du thème
	private String getCardBackImagePath() {
		switch (currentCardTheme) {
		case "green":
			return "src/ressources/Cards/CACHEE_CACHEEVERT.png";
		case "blue":
			return "src/ressources/Cards/CACHEE_CACHEEBLEU.png";
		case "purple":
			return "src/ressources/Cards/CACHEE_CACHEEVIOLET.png";
		default:
			return "src/ressources/Cards/CACHEE_CACHEE.png";
		}
	}

	// Rafraichir l'affichage des cartes
	public void refreshCard() {
		setContentPane(PanelCardOptions()); // Reconstruire et afficher le panel principal
		validate();
		repaint();
	}

	// Récupérer thème actuel du fond
	private String[] getBackgroundThemeNameAndColor(String theme) {
		switch (theme) {
		case "default":
			return new String[] { "Vert (Défaut)", "green" };
		case "purple":
			return new String[] { "Violet", "purple" };
		case "red":
			return new String[] { "Rouge", "red" };
		case "blue":
			return new String[] { "Bleu", "blue" };
		default:
			return new String[] { "Non défini", "black" };
		}
	}

	// Récupérer thème actuel des cartes
	private String[] getCardThemeNameAndColor(String theme) {
		switch (theme) {
		case "default":
			return new String[] { "Rouge (Défaut)", "red" };
		case "purple":
			return new String[] { "Violet", "purple" };
		case "green":
			return new String[] { "Vert", "green" };
		case "blue":
			return new String[] { "Bleu", "blue" };
		default:
			return new String[] { "Non défini", "black" };
		}
	}

	/////////////////////
	// MÉTHODES AUTRES //
	/////////////////////

	// Réintialisation des variables
	public void nouvellePartie() {
		// Réinitialiser le deck et les colonnes
		colonnesDeDepart = SolitaireController.createStartColumns();
		deck = SolitaireController.getDeck();

		// Réinitialiser l'état du GUI
		carteSelectionnee = null;
		colonneSourceSelectionnee = -1;
		positionCarteDansColonne = -1;
		isPileVideLabelSelected = false;

		// Réinitialiser l'affichage
		setPanel(PanelSolitaire());
	}

	// Overlay pour quitter la partie
	public void overlayLeaveGameImage() {
		// JPanel sous forme de glass pour s'afficher au dessus et pouvoir revenir
		JPanel glass = new JPanel();
		glass.setLayout(null);
		glass.setOpaque(false);

		// Image du leave
		ImageIcon leaveGameIcon = new ImageIcon("src/ressources/Background/leaveGame.png");
		JLabel leaveGameLabel = new JLabel(leaveGameIcon);
		leaveGameLabel.setBounds(0, 0, 946, 503);
		glass.add(leaveGameLabel);

		// Boutons "Oui" et "Non"
		// Configuration du bouton "Oui"
		JButton btnYes = new JButton("Oui");
		btnYes.setBounds(300, 295, 150, 40);
		btnYes.setBackground(new Color(64, 198, 23)); // Vert
		btnYes.setForeground(Color.WHITE);
		btnYes.setFont(new Font("Gotham Black", Font.BOLD, 24));
		btnYes.setFocusPainted(false);
		btnYes.addActionListener(e -> {
			setPanel(getMainPage()); // Change pour la page principale
			glass.setVisible(false); // Cache le glassPane
		});

		// Configuration du bouton "Non"
		JButton btnNo = new JButton("Non");
		btnNo.setBounds(500, 295, 150, 40); // Placer à côté du bouton "Oui"
		btnNo.setBackground(new Color(198, 23, 23)); // Rouge
		btnNo.setForeground(Color.WHITE);
		btnNo.setFont(new Font("Gotham Black", Font.BOLD, 24));
		btnNo.setFocusPainted(false);
		btnNo.addActionListener(e -> {
			glass.setVisible(false); // Cache le glassPane et revient sur l'écran d'avant
		});

		// Ajout des boutons au glassPane
		glass.add(btnYes);
		glass.add(btnNo);

		glass.setComponentZOrder(leaveGameLabel, 1);
		glass.setComponentZOrder(btnYes, 0);
		glass.setComponentZOrder(btnNo, 0);

		// Bloquage des clics
		glass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Empêche les clics de passer à travers le glassPane
			}
		});
		setGlassPane(glass);
		glass.setVisible(true);
	}

	// Overlay de victoire
	public void overlayWin() {
		// JPanel en glass pour afficher par dessus
		JPanel glass = new JPanel();
		glass.setLayout(null);
		glass.setOpaque(false);

		// JLabel pour l'image
		ImageIcon winGameIcon = new ImageIcon("src/ressources/Background/winGame.png");
		JLabel winGameLabel = new JLabel(winGameIcon);
		winGameLabel.setBounds(0, 0, 946, 503);
		glass.add(winGameLabel);

		// Création des boutons "Oui" et "Non"
		// Configuration du bouton "Oui"
		JButton btnYes = new JButton("Oui");
		btnYes.setBounds(330, 385, 150, 30); // Placer à une position calculée pour le centrage
		btnYes.setBackground(new Color(64, 198, 23)); // Vert
		btnYes.setForeground(Color.WHITE);
		btnYes.setFont(new Font("Gotham Black", Font.BOLD, 24));
		btnYes.setFocusPainted(false);
		btnYes.addActionListener(e -> {
			nouvellePartie();
			glass.setVisible(false); // Cache le glassPane
		});

		// Configuration du bouton "Non"
		JButton btnNo = new JButton("Non");
		btnNo.setBounds(510, 385, 150, 30); // Placer à côté du bouton "Oui"
		btnNo.setBackground(new Color(198, 23, 23)); // Rouge
		btnNo.setForeground(Color.WHITE);
		btnNo.setFont(new Font("Gotham Black", Font.BOLD, 24));
		btnNo.setFocusPainted(false);
		btnNo.addActionListener(e -> {
			setPanel(getMainPage());
			glass.setVisible(false); // Cache le glassPane sans changer de panel
		});

		// Ajout des boutons au glassPane
		glass.add(btnYes);
		glass.add(btnNo);

		glass.setComponentZOrder(winGameLabel, 1); // L'image est derrière les boutons
		glass.setComponentZOrder(btnYes, 0); // Le bouton "Oui" est au premier plan
		glass.setComponentZOrder(btnNo, 0); // Le bouton "Non" est au premier plan

		// Bloquer les clics
		glass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Empêche les clics de passer à travers le glassPane
			}
		});
		setGlassPane(glass);
		glass.setVisible(true);
	}

	// Redéfinition des tailles d'image adaptées au jeu
	private ImageIcon resizeCardImage(String imagePath, int width, int height) {
		ImageIcon originalIcon = new ImageIcon(imagePath);
		Image image = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(image);
	}

	// Icône de mute/demute
	private JLabel musicIcon() {
		ImageIcon musicControlIcon = new ImageIcon(
				isMusicMuted ? "src/ressources/Images/unmute.png" : "src/ressources/Images/mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted;
				if (isMusicMuted) {
					musicClip.stop();
					musicControlLabel.setIcon(new ImageIcon("src/ressources/Images/unmute.png"));
				} else {
					musicClip.start();
					musicClip.loop(Clip.LOOP_CONTINUOUSLY);
					musicControlLabel.setIcon(new ImageIcon("src/ressources/Images/mute.png"));
				}
			}
		});
		return musicControlLabel;
	}

	// Bouton retour générique du jeu
	private JPanel backButton() {
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
		return panelBoutons;
	}

	public JPanel getMainPage() {
		return MainPage();
	}

	public JPanel getPanelSolitaire() {
		return PanelSolitaire();
	}
}
