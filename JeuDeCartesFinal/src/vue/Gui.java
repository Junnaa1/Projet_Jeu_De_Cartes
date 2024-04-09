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
	private static boolean isMusicMuted = false;

	public Carte cartePiochee = null;
	private Carte derniereCartePiochee = null;
	private JLabel pileVideLabel;
	private boolean doitRemelanger = false;

	List<Carte> deck = SolitaireController.getDeck();

	// Parcourir les colonnes d
	public Gui() {
		this.souris = new Souris(this);
		initGUI();
		rendreDernieresCartesVisibles();
	}

	private void initGUI() {
		setTitle("Jeu de cartes");
		playMusic("src\\testtheme.wav");
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
			File fontFile = new File("src\\GOTHICI.ttf");
			Font gothici = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);
			System.out.println("Nom de la police chargée: " + gothici.getFontName());

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// Enregistre la police
			ge.registerFont(gothici);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
			// Gérez l'erreur ici (par exemple, en utilisant une police par défaut)
		}
		try {
			// Chemin relatif au fichier de police dans le dossier des ressources
			File fontFile = new File("src\\Gotham-Black.otf");
			Font gothamblack = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);
			System.out.println("Nom de la police chargée: " + gothamblack.getFontName());

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
				musicClip.start();
				musicClip.loop(Clip.LOOP_CONTINUOUSLY);
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
		ImageIcon bgIcon = new ImageIcon("src\\Background.png");
		JLabel bgLabel = new JLabel(bgIcon);
		bgLabel.setBounds(0, 0, 960, 540);
		panelPrincipal.add(bgLabel);

		ImageIcon particlesIcon = new ImageIcon("src\\particles.gif");
		JLabel particlesLabel = new JLabel(particlesIcon);
		particlesLabel.setBounds(-240, 0, 960, 540);
		panelPrincipal.add(particlesLabel);

		ImageIcon particlesIcon2 = new ImageIcon("src\\particles.gif");
		JLabel particlesLabel2 = new JLabel(particlesIcon2);
		particlesLabel2.setBounds(250, 0, 960, 540);
		panelPrincipal.add(particlesLabel2);

		ImageIcon loadingIconOriginal = new ImageIcon("src\\loading.gif");
		Image tempLoading = loadingIconOriginal.getImage().getScaledInstance(125, 100, Image.SCALE_DEFAULT);
		ImageIcon resizedIcon = new ImageIcon(tempLoading);
		JLabel loadingLabel = new JLabel(resizedIcon);
		loadingLabel.setBounds(420, 200, 960, 540);
		panelPrincipal.add(loadingLabel);

		ImageIcon loading2IconOriginal = new ImageIcon("src\\loading.gif");
		Image tempLoading2 = loading2IconOriginal.getImage().getScaledInstance(125, 100, Image.SCALE_DEFAULT);
		ImageIcon resizedIcon2 = new ImageIcon(tempLoading2);
		JLabel loadingLabel2 = new JLabel(resizedIcon2);
		loadingLabel2.setBounds(-430, 200, 960, 540);
		panelPrincipal.add(loadingLabel2);

		// Image sous "Nouvelle partie"
		ImageIcon imageIcon = new ImageIcon("src\\bouton1.png");
		JLabel imageLabel = new JLabel(imageIcon);
		// Assurez-vous que la position est correcte pour que l'image apparaisse sous
		// "Nouvelle partie"
		imageLabel.setBounds(-20, -15, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		imageLabel.setVisible(false); // Invisible par défaut
		bgLabel.add(imageLabel);

		// Image sous "Nouvelle partie"
		ImageIcon bouton2icon = new ImageIcon("src\\bouton1.png");
		JLabel bouton2Label = new JLabel(bouton2icon);

		bouton2Label.setBounds(-20, 45, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		bouton2Label.setVisible(false); // Invisible par défaut
		bgLabel.add(bouton2Label);

		// Image sous "Nouvelle partie"
		ImageIcon bouton3icon = new ImageIcon("src\\bouton1.png");
		JLabel bouton3Label = new JLabel(bouton3icon);

		bouton3Label.setBounds(-20, 105, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		bouton3Label.setVisible(false); // Invisible par défaut
		bgLabel.add(bouton3Label);

		// Image sous "Nouvelle partie"
		ImageIcon bouton4icon = new ImageIcon("src\\bouton1.png");
		JLabel bouton4Label = new JLabel(bouton4icon);

		bouton4Label.setBounds(-20, 165, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		bouton4Label.setVisible(false); // Invisible par défaut
		bgLabel.add(bouton4Label);

		// Image sous "Nouvelle partie"
		ImageIcon bouton5icon = new ImageIcon("src\\bouton1.png");
		JLabel bouton5Label = new JLabel(bouton5icon);

		bouton5Label.setBounds(-20, 225, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		bouton5Label.setVisible(false); // Invisible par défaut
		bgLabel.add(bouton5Label);

		// "Nouvelle partie"
		JLabel nouvellepartie = new JLabel("Nouvelle partie");
		nouvellepartie.setFont(new Font("Century Gothic Italic", Font.PLAIN, 30));
		nouvellepartie.setForeground(Color.WHITE);
		nouvellepartie.setBounds(20, 20, 500, 30);
		nouvellepartie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setPanel(getPanelSolitaire()); // Change le panel pour afficher le jeu Solitaire
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

		JLabel continuer = new JLabel("Continuer");
		continuer.setFont(new Font("Century Gothic Italic", Font.PLAIN, 30));
		continuer.setForeground(Color.WHITE);
		continuer.setBounds(20, 80, 500, 30);
		continuer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setPanel(getPanelSolitaire()); // Change le panel pour afficher le jeu Solitaire
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
		bgLabel.add(continuer);

		JLabel regles = new JLabel("Règles");
		regles.setFont(new Font("Century Gothic Italic", Font.PLAIN, 30));
		regles.setForeground(Color.WHITE);
		regles.setBounds(20, 140, 500, 30);
		regles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

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
		bgLabel.add(regles);

		JLabel options = new JLabel("Options");
		options.setFont(new Font("Century Gothic Italic", Font.PLAIN, 30));
		options.setForeground(Color.WHITE);
		options.setBounds(20, 200, 500, 30);
		options.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

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
		bgLabel.add(options);

		JLabel quitter = new JLabel("Quitter");
		quitter.setFont(new Font("Century Gothic Italic", Font.PLAIN, 30));
		quitter.setForeground(Color.WHITE);
		quitter.setBounds(20, 260, 500, 30);
		quitter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				bouton5Label.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				bouton5Label.setVisible(false);
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
		bgLabel.setComponentZOrder(bouton5Label, 0);

		// Les labels de texte viennent au-dessus des images de bouton
		bgLabel.setComponentZOrder(nouvellepartie, 0);
		bgLabel.setComponentZOrder(continuer, 0);
		bgLabel.setComponentZOrder(regles, 0);
		bgLabel.setComponentZOrder(options, 0);
		bgLabel.setComponentZOrder(quitter, 0);

		// Le gif de chargement vient tout en haut
		bgLabel.setComponentZOrder(loadingLabel, 0);
		bgLabel.setComponentZOrder(loadingLabel2, 0);

		ImageIcon musicControlIcon = new ImageIcon(isMusicMuted ? "src\\unmute.png" : "src\\mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted; // Inverse l'état
				if (isMusicMuted) {
					musicClip.stop();
					musicControlLabel.setIcon(new ImageIcon("src\\unmute.png"));
				} else {
					musicClip.start();
					musicClip.loop(Clip.LOOP_CONTINUOUSLY);
					musicControlLabel.setIcon(new ImageIcon("src\\mute.png"));
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
		ImageIcon bgIcon = new ImageIcon("src\\BackgroundGame.png");
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

		ImageIcon musicControlIcon = new ImageIcon(isMusicMuted ? "src\\unmute.png" : "src\\mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted; // Inverse l'état
				if (isMusicMuted) {
					musicClip.stop();
					musicControlLabel.setIcon(new ImageIcon("src\\unmute.png"));
				} else {
					musicClip.start();
					musicClip.loop(Clip.LOOP_CONTINUOUSLY);
					musicControlLabel.setIcon(new ImageIcon("src\\mute.png"));
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
		ImageIcon cardBackIcon = new ImageIcon("src\\cartes\\CACHEE_CACHEE.png");
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		// Redessine l'arrière-plan sur le panelSolitaire.
		ImageIcon bgIcon = new ImageIcon("src\\BackgroundGame.png");
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
				JLabel pileVideLabel = new JLabel(new ImageIcon("src\\cartes\\empty_pile.png"));
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

		ImageIcon musicControlIcon = new ImageIcon(isMusicMuted ? "src\\unmute.png" : "src\\mute.png");
		JLabel musicControlLabel = new JLabel(musicControlIcon);
		musicControlLabel.setBounds(895, 10, musicControlIcon.getIconWidth(), musicControlIcon.getIconHeight());
		musicControlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isMusicMuted = !isMusicMuted; // Inverse l'état
				if (isMusicMuted) {
					musicClip.stop();
					musicControlLabel.setIcon(new ImageIcon("src\\unmute.png"));
				} else {
					musicClip.start();
					musicClip.loop(Clip.LOOP_CONTINUOUSLY);
					musicControlLabel.setIcon(new ImageIcon("src\\mute.png"));
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
		ImageIcon cardBackIcon = new ImageIcon("src/cartes/CACHEE_CACHEE.png");
		int cardWidth = cardBackIcon.getIconWidth();
		int cardHeight = cardBackIcon.getIconHeight();

		this.pileVideLabel = new JLabel();
		this.pileVideLabel.setBounds(630, 30, cardWidth, cardHeight);
		bgLabel.add(this.pileVideLabel);

		JLabel piocheLabel = new JLabel(cardBackIcon);
		piocheLabel.setBounds(730, 30, cardWidth, cardHeight);
		bgLabel.add(piocheLabel);

		if (deck.isEmpty()) {
			piocheLabel.setIcon(new ImageIcon("src\\cartes\\empty_pile_pioche.png"));
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
					piocheLabel.setIcon(new ImageIcon("src\\cartes\\empty_pile_pioche.png"));
					// Initialisez un compteur pour suivre l'image de mélange actuelle
					final int[] compteur = { 0 };

					Timer timer = new Timer(80, null); // Créer un timer avec un délai de 500 ms
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
							break;
						case 4:
							piocheLabel.setIcon(new ImageIcon("src\\cartes\\melange4.png"));
							break;
						case 5:
							piocheLabel.setIcon(new ImageIcon("src\\cartes\\melange5.png"));
							break;
						case 6:
							piocheLabel.setIcon(new ImageIcon("src\\cartes\\melange1.png"));
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
					piocheLabel.setIcon(new ImageIcon("src\\cartes\\empty_pile_pioche.png"));
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

	private void creerColonneFinale(JLabel bgLabel) {
		int piocheXStart = 130; // Position de départ pour la première pile vide sur l'axe X
		int piocheYStart = 30; // Position de départ pour la première pile vide sur l'axe Y
		int piocheSpacing = 10; // Espace horizontal entre les piles vides
		ImageIcon cardBackIcon = new ImageIcon("src\\cartes\\CACHEE_CACHEE.png");
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
			ImageIcon pileVideIcon = new ImageIcon("src\\cartes\\empty_pile.png"); // Image d'une pile vide
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

}
