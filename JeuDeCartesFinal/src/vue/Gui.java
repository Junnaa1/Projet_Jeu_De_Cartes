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

public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;
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

	private Clip musicClip;
	private static boolean isMusicMuted = true;

	public Carte cartePiochee = null;
	private Carte derniereCartePiochee = null;
	private JLabel pileVideLabel;
	private boolean doitRemelanger = false;

	List<Carte> deck = SolitaireController.getDeck();

	public static String currentTheme = "default";
	public static String currentCardTheme = "default";

	// Parcourir les colonnes d
	public Gui() {
		this.souris = new Souris(this);
		initGUI();
		rendreDernieresCartesVisibles();
	}

	private void initGUI() {
		setTitle("Solitaire");
		playMusic("src/ressources/Sounds/GameOST.wav");
		initCustomFonts();
		setSize(960, 540); // Taille de la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Centre la fenêtre
		setIconImage(new ImageIcon("src/ressources/Images/Logo.png").getImage());
		setResizable(false); // Empêche le redimensionnement de la fenêtre
		JPanel mainPanel = MainPage();
		setContentPane(mainPanel);
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
			File fontFile = new File("src/ressources/Fonts/GOTHICI.ttf");
			Font gothici = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// Enregistre la police
			ge.registerFont(gothici);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
			// Gérez l'erreur ici (par exemple, en utilisant une police par défaut)
		}
		try {
			// Chemin relatif au fichier de police dans le dossier des ressources
			File fontFile = new File("src/ressources/Fonts/Gotham-Black.otf");
			Font gothamblack = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// Enregistre la police
			ge.registerFont(gothamblack);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
			// Gérez l'erreur ici (par exemple, en utilisant une police par défaut)
		}
	}

	public void playMusic(String filepath) {
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
				System.out.println("Can't find file");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JPanel MainPage() {

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(null);

		// Arrière-plan
		ImageIcon bgIcon = new ImageIcon("src/ressources/Background/Background.png");
		Image image = bgIcon.getImage();
		Image newimg = image.getScaledInstance(946, 503, Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg); // Crée un ImageIcon avec l'image redimensionnée

		JLabel bgLabel = new JLabel(newIcon); // Utilise le nouvel ImageIcon pour le JLabel
		bgLabel.setBounds(0, 0, 946, 503); // Définit la taille du JLabel pour correspondre à celle de l'image
		panelPrincipal.add(bgLabel);

		ImageIcon particlesIcon = new ImageIcon("src/ressources/Images/particles.gif");
		JLabel particlesLabel = new JLabel(particlesIcon);
		particlesLabel.setBounds(-240, 0, 960, 540);
		panelPrincipal.add(particlesLabel);

		ImageIcon particlesIcon2 = new ImageIcon("src/ressources/Images/particles.gif");
		JLabel particlesLabel2 = new JLabel(particlesIcon2);
		particlesLabel2.setBounds(250, 0, 960, 540);
		panelPrincipal.add(particlesLabel2);

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

		// Image sous "Nouvelle partie"
		ImageIcon imageIcon = new ImageIcon("src/ressources/Images/BackgroundButton.png");
		JLabel imageLabel = new JLabel(imageIcon);
		// Assurez-vous que la position est correcte pour que l'image apparaisse sous
		// "Nouvelle partie"
		imageLabel.setBounds(-20, -15, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		imageLabel.setVisible(false); // Invisible par défaut
		bgLabel.add(imageLabel);

		// Image sous "Nouvelle partie"
		ImageIcon bouton2icon = new ImageIcon("src/ressources/Images/BackgroundButton.png");
		JLabel bouton2Label = new JLabel(bouton2icon);

		bouton2Label.setBounds(-20, 45, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		bouton2Label.setVisible(false); // Invisible par défaut
		bgLabel.add(bouton2Label);

		// Image sous "Nouvelle partie"
		ImageIcon bouton3icon = new ImageIcon("src/ressources/Images/BackgroundButton.png");
		JLabel bouton3Label = new JLabel(bouton3icon);

		bouton3Label.setBounds(-20, 105, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		bouton3Label.setVisible(false); // Invisible par défaut
		bgLabel.add(bouton3Label);

		// Image sous "Nouvelle partie"
		ImageIcon bouton4icon = new ImageIcon("src/ressources/Images/BackgroundButton.png");
		JLabel bouton4Label = new JLabel(bouton4icon);

		bouton4Label.setBounds(-20, 165, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		bouton4Label.setVisible(false); // Invisible par défaut
		bgLabel.add(bouton4Label);

		// "Nouvelle partie"
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
				setPanel(PanelRegles()); // Affiche le panel des règles
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

		// En arrière-plan, donc c'est l'index le plus élevé.
		bgLabel.setComponentZOrder(particlesLabel, bgLabel.getComponentCount() - 1);
		bgLabel.setComponentZOrder(particlesLabel2, bgLabel.getComponentCount() - 1);

		// Les images de bouton viennent au-dessus des particules
		bgLabel.setComponentZOrder(imageLabel, 0);
		bgLabel.setComponentZOrder(bouton2Label, 0);
		bgLabel.setComponentZOrder(bouton3Label, 0);
		bgLabel.setComponentZOrder(bouton4Label, 0);

		// Les labels de texte viennent au-dessus des images de bouton
		bgLabel.setComponentZOrder(nouvellepartie, 0);
		bgLabel.setComponentZOrder(regles, 0);
		bgLabel.setComponentZOrder(options, 0);
		bgLabel.setComponentZOrder(quitter, 0);

		// Le gif de chargement vient tout en haut
		bgLabel.setComponentZOrder(loadingLabel, 0);
		bgLabel.setComponentZOrder(loadingLabel2, 0);

		ImageIcon musicControlIcon = new ImageIcon(
				isMusicMuted ? "src/ressources/Images/unmute.png" : "src/ressources/Images/mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted; // Inverse l'état
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
		panelPrincipal.add(musicControlLabel);
		bgLabel.setComponentZOrder(musicControlLabel, 0);

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
		ImageIcon bgIcon = new ImageIcon(getBackgroundImagePath());
		JLabel bgLabel = new JLabel(bgIcon);
		bgLabel.setBounds(0, 0, 960, 540);
		panelSolitaire.add(bgLabel);

		// Création et placement des cartes retournées pour les colonnes de départ
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
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
						String cardPath = "src/ressources/Cards/" + carte.getNom().toString() + "_"
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
											derniereCartePiochee = null;
											pileVideLabel.setIcon(null);
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

		ImageIcon musicControlIcon = new ImageIcon(
				isMusicMuted ? "src/ressources/Images/unmute.png" : "src/ressources/Images/mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted; // Inverse l'état
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
		panelSolitaire.add(musicControlLabel);
		bgLabel.setComponentZOrder(musicControlLabel, 0);
		return panelSolitaire;
	}

	private void reconstruireAffichageColonnes() {
		// Supprime tous les composants du panelSolitaire pour recommencer.
		panelSolitaire.removeAll();
		panelSolitaire.setLayout(null); // Assurez-vous que le layout est correctement défini pour le positionnement
										// manuel.
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		// Redessine l'arrière-plan sur le panelSolitaire.
		ImageIcon bgIcon = new ImageIcon(getBackgroundImagePath());
		JLabel bgLabel = new JLabel(bgIcon);
		bgLabel.setBounds(0, 0, 960, 540);
		panelSolitaire.add(bgLabel);

		// Itère à travers chaque colonne de départ pour reconstruire l'affichage des
		// cartes.
		for (int col = 0; col < 7; col++) {
			List<Carte> colonne = colonnesDeDepart.get(col);
			if (colonne.isEmpty()) {
				final int finalCol = col;
				// Affiche un JLabel pour une colonne vide
				JLabel pileVideLabel = new JLabel(new ImageIcon("src/ressources/Cards/empty_pile.png"));
				int x = gamecolumnsxStart + (cardWidth + gamecolumnsxSpacing) * col;
				int y = gamecolumnsyStart; // La position Y reste constante pour la première carte de la colonne
				pileVideLabel.setBounds(x, y, cardWidth, cardHeight);
				bgLabel.add(pileVideLabel);
				bgLabel.setComponentZOrder(pileVideLabel, 0);

				pileVideLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (carteSelectionnee != null) {
							// Ajoute la carte sélectionnée à la colonne vide
							colonnesDeDepart.get(finalCol).add(carteSelectionnee);
							// Supprime la carte de sa colonne source
							colonnesDeDepart.get(colonneSourceSelectionnee).remove(carteSelectionnee);
							// Réinitialise les variables de sélection
							carteSelectionnee = null;
							colonneSourceSelectionnee = -1;
							// Reconstruit l'affichage pour refléter le changement
							reconstruireAffichageColonnes();
						}
					}
				});// Assurez-vous que cela s'affiche au-dessus du fond
			}

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
							? "src/ressources/Cards/" + carte.getNom().toString() + "_" + carte.getCouleur().toString()
									+ ".png"
							: getCardBackImagePath();
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
										derniereCartePiochee = null;
										pileVideLabel.setIcon(null);
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

				int xStartFinale = 130; // Exemple de position de départ x pour les colonnes finales
				for (int i = 7; i <= 10; i++) { // Parcourir les colonnes finales
					List<Carte> colonneFinale = colonnesDeDepart.get(i);
					int y2 = 30; // Position y fixe pour les cartes dans les colonnes finales
					int x2 = xStartFinale + ((i - 7) * (cardWidth + gamecolumnsxSpacing)); // Calculer la position x en
																							// fonction de l'indice de
																							// la
					// colonne finale

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

		ImageIcon musicControlIcon = new ImageIcon(
				isMusicMuted ? "src/ressources/Images/unmute.png" : "src/ressources/Images/mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted; // Inverse l'état
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
		panelSolitaire.add(musicControlLabel);
		bgLabel.setComponentZOrder(musicControlLabel, 0);

		panelSolitaire.revalidate();
		panelSolitaire.repaint();
	}

	private void creerPioche(JLabel bgLabel) {
		System.out.println("Taille du deck après initialisation : " + SolitaireController.getDeck().size());
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		this.pileVideLabel = new JLabel();
		this.pileVideLabel.setBounds(630, 30, cardWidth, cardHeight);
		bgLabel.add(this.pileVideLabel);

		JLabel piocheLabel = new JLabel(cardBackIcon);
		piocheLabel.setBounds(730, 30, cardWidth, cardHeight);
		bgLabel.add(piocheLabel);

		if (deck.isEmpty()) {
			piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/empty_pile_pioche.png"));
			doitRemelanger = true;
		} else {
			// S'assurer que l'icône de la pioche est réinitialisée correctement si le deck
			// n'est pas vide
			piocheLabel.setIcon(cardBackIcon);
			doitRemelanger = false;
		}

		if (derniereCartePiochee != null) {
			// Vérifiez si la derniereCartePiochee a été déplacée
			if (colonnesDeDepart.get(SolitaireController.INDEX_COLONNE_PIOCHE).contains(derniereCartePiochee)) {
				// Si la dernière carte piochée est toujours dans la pioche, affichez-la
				ImageIcon cartePiocheeIcon = carteToImageIcon(derniereCartePiochee);
				ImageIcon cartePiocheeIconRedimensionnee = resizeCardImage(cartePiocheeIcon.getDescription(), cardWidth,
						cardHeight);
				pileVideLabel.setIcon(cartePiocheeIconRedimensionnee);
			} else {
				// Si la dernière carte piochée a été déplacée, ne l'affichez pas
				pileVideLabel.setIcon(null); // Ou affichez une icône par défaut représentant une pioche vide
				derniereCartePiochee = null; // Assurez-vous de réinitialiser derniereCartePiochee puisqu'elle n'est
												// plus dans la pioche
			}
		} else {
			// S'il n'y a pas de "derniereCartePiochee" à afficher, assurez-vous que
			// pileVideLabel n'affiche rien ou une icône par défaut
			pileVideLabel.setIcon(null); // Ou une icône par défaut pour une pioche vide
		}
		// Ajout d'un MouseListener à piocheLabel pour gérer les clics et piocher une
		// carte
		piocheLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (doitRemelanger) {
					piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/empty_pile_pioche.png"));
					// Initialisez un compteur pour suivre l'image de mélange actuelle
					final int[] compteur = { 0 };

					Timer timer = new Timer(80, null); // Créer un timer avec un délai de 500 ms
					timer.addActionListener(actionEvent -> {
						compteur[0]++; // Incrémentez le compteur à chaque tick

						// En fonction du compteur, changez l'icône
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
							// Après le dernier changement, revenez à l'icône de carte retournée et arrêtez
							// le timer
							piocheLabel.setIcon(cardBackIcon);
							timer.stop();
						}
					});
					timer.setInitialDelay(0); // Commencez immédiatement sans retard
					timer.start();
				} else if (deck.isEmpty()) {
					// S'il n'y a pas de cartes dans le deck et qu'on ne doit pas encore remélanger,
					// on affiche l'icône indiquant que la pioche est vide et on se prépare au
					// remélange
					piocheLabel.setIcon(new ImageIcon("src/ressources/Cards/empty_pile_pioche.png"));
					doitRemelanger = true;
				} else {
					Carte carteTiree = deck.remove(deck.size() - 1); // Retire la dernière carte du deck
					derniereCartePiochee = carteTiree;
					cartePiochee = carteTiree;
					cartePiochee.setVisible(true);
					List<Carte> colonnePioche = colonnesDeDepart.get(SolitaireController.INDEX_COLONNE_PIOCHE);
					colonnePioche.add(carteTiree);
					ImageIcon carteTireeIcon = carteToImageIcon(carteTiree);
					ImageIcon carteTireeIconRedimensionnee = resizeCardImage(carteTireeIcon.getDescription(), cardWidth,
							cardHeight);
					pileVideLabel.setIcon(carteTireeIconRedimensionnee); // l'image de la carte tirée
				}
			}
		});

		pileVideLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Vérifie si la pioche (colonne de pioche) est vide ou si la dernière carte
				// piochée a été déplacée
				if (cartePiochee == null
						|| !colonnesDeDepart.get(SolitaireController.INDEX_COLONNE_PIOCHE).contains(cartePiochee)) {
					// Si la pioche est vide ou la dernière carte a été déplacée, désactivez les
					// interactions
					// Vous pouvez également mettre à jour l'icône pour montrer que la pioche est
					// vide
					pileVideLabel.setIcon(null);
					isPileVideLabelSelected = false;
					carteSelectionnee = null;
					colonneSourceSelectionnee = -1;
					pileVideLabel.setBorder(null);
					// Optionnel : Réinitialiser l'état de cartePiochee si nécessaire
					cartePiochee = null;
				} else {
					// Si la dernière carte piochée est toujours dans la pioche, permettez la
					// sélection/déselection
					if (isPileVideLabelSelected) {
						isPileVideLabelSelected = false;
						carteSelectionnee = null;
						colonneSourceSelectionnee = -1;
						pileVideLabel.setBorder(null);
					} else {
						isPileVideLabelSelected = true;
						carteSelectionnee = cartePiochee;
						System.out.println("Carte piochée: " + carteSelectionnee);
						colonneSourceSelectionnee = SolitaireController.INDEX_COLONNE_PIOCHE;
						pileVideLabel.setBorder(new LineBorder(Color.GREEN, 3));
					}
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
		String cheminImage = "src/ressources/Cards/" + nom + "_" + couleur + ".png";
		return new ImageIcon(cheminImage);
	}

	private ImageIcon obtenirImageCarte(Carte carte) {
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();
		if (carte == null)
			return null;
		String chemin = "src/ressources/Cards/" + carte.getNom() + "_" + carte.getCouleur() + ".png";
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

	private void creerColonneFinale(JLabel bgLabel) {
		int piocheXStart = 130; // Position de départ pour la première pile vide sur l'axe X
		int piocheYStart = 30; // Position de départ pour la première pile vide sur l'axe Y
		int piocheSpacing = 10; // Espace horizontal entre les piles vides
		ImageIcon cardBackIcon = new ImageIcon(getCardBackImagePath());
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		for (int i = 0; i < colonnesDeDepart.size(); i++) {
			System.out.println("Colonne " + i + ":");
			List<Carte> colonne = colonnesDeDepart.get(i);
			for (Carte carte : colonne) {
				System.out.println("\t" + carte);
			}
		}

		// Création des quatre colonnes finales
		for (int i = 0; i < 4; i++) {
			ImageIcon pileVideIcon = new ImageIcon("src/ressources/Cards/empty_pile.png"); // Image d'une pile vide
			JLabel pileVideLabel = new JLabel(pileVideIcon);
			int x = piocheXStart + (cardWidth + piocheSpacing) * i;
			int y = piocheYStart;
			pileVideLabel.setBounds(x, y, cardWidth, cardHeight);
			bgLabel.add(pileVideLabel);

			// Ajouter un MouseAdapter à chaque pile finale
			final int finalI = i; // Stocker l'indice final pour accéder à l'intérieur de la classe anonyme
			List<Carte> colonne = colonnesDeDepart.get(finalI + 7);
			pileVideLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// Vérifier s'il y a une carte sélectionnée dans une colonne source
					if (carteSelectionnee != null) {
						// Vérifier si la carte peut être déplacée vers cette pile finale
						if (SolitaireController.deplacementVersPileFinale(carteSelectionnee, colonne)) {
							// Ajouter la carte à la pile finale
							List<Carte> pileFinale = colonnesDeDepart.get(finalI + 7);
							pileFinale.add(carteSelectionnee);

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

	public JPanel PanelRegles() {
		JPanel panelRegles = new JPanel();
		panelRegles.setLayout(null);

		// Arrière-plan
		ImageIcon rulesIcon = new ImageIcon(getRulesBackgroundPath());
		JLabel rulesLabel = new JLabel(rulesIcon);
		rulesLabel.setBounds(0, 0, 946, 503);
		panelRegles.add(rulesLabel);

		JPanel panelBoutons = new JPanel();
		panelBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panelBoutons.setOpaque(false);

		// Bouton Retour
		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 24));
		boutonRetour.setBounds(20, 450, 140, 40); // Ajustement des dimensions pour correspondre à celles de
													// PanelSolitaire
		panelRegles.add(boutonRetour);

		// Cette ligne est très importante pour s'assurer que le bouton retour est
		// visible par dessus le background
		panelRegles.setComponentZOrder(boutonRetour, 0); // Assurez-vous que le bouton est au-dessus
		panelRegles.setComponentZOrder(rulesLabel, 1); // Le JLabel du fond doit être derrière

		boutonRetour.addActionListener(e -> setPanel(getMainPage())); // Action pour retourner à la page principale

		// Bouton Mute/Unmute Music
		ImageIcon musicControlIcon = new ImageIcon(
				isMusicMuted ? "src/ressources/Images/unmute.png" : "src/ressources/Images/mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted; // Inverse l'état
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
		panelRegles.add(musicControlLabel);
		rulesLabel.setComponentZOrder(musicControlLabel, 0);
		return panelRegles;
	}

	public JPanel PanelOptions() {
		JPanel panelOptions = new JPanel();
		panelOptions.setLayout(null);

		// Arrière-plan
		ImageIcon optionsIcon = new ImageIcon(getPersonnalisationBackgroundPath());
		JLabel optionsLabel = new JLabel(optionsIcon);
		optionsLabel.setBounds(0, 0, 946, 503);
		panelOptions.add(optionsLabel);

		JPanel panelBoutons = new JPanel();
		panelBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panelBoutons.setOpaque(false);

		// Bouton Retour
		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 24));
		boutonRetour.setBounds(20, 450, 140, 40);

		panelOptions.add(boutonRetour);

		boutonRetour.addActionListener(e -> setPanel(getMainPage())); // Action pour retourner à la page principale

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

		// Bouton Mute/Unmute Music
		ImageIcon musicControlIcon = new ImageIcon(
				isMusicMuted ? "src/ressources/Images/unmute.png" : "src/ressources/Images/mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted; // Inverse l'état
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
		panelOptions.add(musicControlLabel);

		panelOptions.setComponentZOrder(optionsLabel, panelOptions.getComponentCount() - 1);
		panelOptions.setComponentZOrder(musicControlLabel, 0);
		panelOptions.setComponentZOrder(changeCardColorButton, 0);
		panelOptions.setComponentZOrder(changeBackgroundButton, 0);
		panelOptions.setComponentZOrder(boutonRetour, 0);

		return panelOptions;
	}

	public JPanel PanelBackgroundOptions() {
		JPanel panelBackgroundOptions = new JPanel();
		panelBackgroundOptions.setLayout(null);

		// Arrière-plan
		ImageIcon backgroundOptionsIcon = new ImageIcon(getColorBackgroundPath());
		JLabel backgroundOptionsLabel = new JLabel(backgroundOptionsIcon);
		backgroundOptionsLabel.setBounds(0, 0, 946, 503);
		panelBackgroundOptions.add(backgroundOptionsLabel);

		// Bouton Retour
		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 24));
		boutonRetour.setBounds(20, 450, 140, 40);
		boutonRetour.addActionListener(e -> setPanel(PanelOptions())); // Action pour retourner à PanelOptions
		panelBackgroundOptions.add(boutonRetour);

		Color green = new Color(22, 120, 44);
		Color purple = new Color(69, 3, 55);
		Color red = new Color(113, 13, 27);
		Color blue = new Color(5, 52, 83);

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

		// Bouton Mute/Unmute Music
		ImageIcon musicControlIcon = new ImageIcon(
				isMusicMuted ? "src/ressources/Images/unmute.png" : "src/ressources/Images/mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted; // Inverse l'état
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
		panelBackgroundOptions.add(musicControlLabel);

		// Set component Z-order
		panelBackgroundOptions.setComponentZOrder(backgroundOptionsLabel,
				panelBackgroundOptions.getComponentCount() - 1);
		panelBackgroundOptions.setComponentZOrder(boutonRetour, 0);
		panelBackgroundOptions.setComponentZOrder(musicControlLabel, 0);

		return panelBackgroundOptions;
	}

	private JButton createButton(String text, Color backgroundColor, int x, int y, int width, int height) {
		JButton button = new JButton(text);
		button.setBackground(backgroundColor);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setFont(new Font("Gotham Black", Font.BOLD, 24));
		button.setBounds(x, y, width, height);
		return button;
	}

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

	public void refreshBackground() {
		setContentPane(PanelBackgroundOptions()); // Reconstruire et afficher le panel principal
		validate();
		repaint();
	}

	public JPanel PanelCardOptions() {
		JPanel panelCardOptions = new JPanel();
		panelCardOptions.setLayout(null);

		// Arrière-plan
		ImageIcon cardOptionsIcon = new ImageIcon(getColorCardBackgroundPath());
		JLabel cardOptionsLabel = new JLabel(cardOptionsIcon);
		cardOptionsLabel.setBounds(0, 0, 946, 503);
		panelCardOptions.add(cardOptionsLabel);

		// Bouton Retour
		JButton boutonRetour = new JButton("Retour");
		boutonRetour.setBackground(new Color(91, 4, 75));
		boutonRetour.setForeground(Color.WHITE);
		boutonRetour.setFocusPainted(false);
		boutonRetour.setFont(new Font("Gotham Black", Font.BOLD, 24));
		boutonRetour.setBounds(20, 450, 140, 40);
		boutonRetour.addActionListener(e -> setPanel(PanelOptions())); // Action pour retourner à PanelOptions
		panelCardOptions.add(boutonRetour);

		JLabel messageLabel = new JLabel("");
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setBounds(260, 340, 400, 30);
		panelCardOptions.add(messageLabel);

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
			String[] themeInfo = getCardThemeNameAndColor(currentCardTheme);
			messageLabel.setText("Couleur changée en " + themeInfo[0]);
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

		// Bouton Mute/Unmute Music
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
		panelCardOptions.add(musicControlLabel);

		// Set component Z-order
		panelCardOptions.setComponentZOrder(cardOptionsLabel, panelCardOptions.getComponentCount() - 1);
		panelCardOptions.setComponentZOrder(musicControlLabel, 0);
		panelCardOptions.setComponentZOrder(boutonRetour, 0);

		return panelCardOptions;
	}

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

	public void refreshCard() {
		setContentPane(PanelCardOptions()); // Reconstruire et afficher le panel principal
		validate();
		repaint();

	}

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

	public void nouvellePartie() {
		// Réinitialiser le deck et les colonnes
		colonnesDeDepart = SolitaireController.creerColonnesDeDepart();
		deck = SolitaireController.getDeck();

		// Réinitialiser l'état du GUI
		carteSelectionnee = null;
		colonneSourceSelectionnee = -1;
		positionCarteDansColonne = -1;
		isPileVideLabelSelected = false;

		// Réinitialiser l'affichage
		setPanel(PanelSolitaire());
	}

	public void overlayLeaveGameImage() {
		// Crée un JPanel qui agira comme un glassPane
		JPanel glass = new JPanel();
		glass.setLayout(null); // Aucun layout pour placer les éléments librement
		glass.setOpaque(false); // Rend le fond transparent

		// Crée un nouveau JLabel pour l'image
		ImageIcon leaveGameIcon = new ImageIcon("src/ressources/Background/leaveGame.png");
		JLabel leaveGameLabel = new JLabel(leaveGameIcon);
		leaveGameLabel.setBounds(0, 0, 946, 503);

		glass.add(leaveGameLabel);

		// Création des boutons "Oui" et "Non"
		JButton btnYes = new JButton("Oui");
		JButton btnNo = new JButton("Non");

		// Configuration du bouton "Oui"
		btnYes.setBounds(300, 295, 150, 40); // Placer à une position calculée pour le centrage
		btnYes.setBackground(new Color(64, 198, 23)); // Vert
		btnYes.setForeground(Color.WHITE);
		btnYes.setFont(new Font("Gotham Black", Font.BOLD, 24));
		btnYes.setFocusPainted(false);
		btnYes.addActionListener(e -> {
			setPanel(getMainPage()); // Change pour la page principale
			glass.setVisible(false); // Cache le glassPane
		});

		// Configuration du bouton "Non"
		btnNo.setBounds(500, 295, 150, 40); // Placer à côté du bouton "Oui"
		btnNo.setBackground(new Color(198, 23, 23)); // Rouge
		btnNo.setForeground(Color.WHITE);
		btnNo.setFont(new Font("Gotham Black", Font.BOLD, 24));
		btnNo.setFocusPainted(false);
		btnNo.addActionListener(e -> {
			glass.setVisible(false); // Cache le glassPane sans changer de panel
		});

		// Ajout des boutons au glassPane
		glass.add(btnYes);
		glass.add(btnNo);

		glass.setComponentZOrder(leaveGameLabel, 1); // L'image est derrière les boutons
		glass.setComponentZOrder(btnYes, 0); // Le bouton "Oui" est au premier plan
		glass.setComponentZOrder(btnNo, 0); // Le bouton "Non" est au premier plan
		// Ajoute un MouseAdapter pour bloquer les clics
		glass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Empêche les clics de passer à travers le glassPane
			}
		});

		setGlassPane(glass); // Définit le nouveau glassPane
		glass.setVisible(true); // Affiche le glassPane
	}

}
