package vue;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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

	int gamecolumnsxStart = 130; // Position de départ pour la première carte sur l'axe X
	int gamecolumnsyStart = 190; // Position de départ pour la première carte sur l'axe Y
	int gamecolumnsxSpacing = 10; // Espace horizontal entre les cartes
	int gamecolumnsySpacing = 20; // Espace vertical entre les cartes pour l'effet empilé

	// Parcourir les colonnes d
	public Gui() {
		this.souris = new Souris(this);
		initGUI();
	}

	private void initGUI() {
		setTitle("Jeu de cartes");
		setSize(960, 540); // Taille de la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Centre la fenêtre
		setIconImage(new ImageIcon("src\\Logo.png").getImage());
		setResizable(false); // Empêche le redimensionnement de la fenêtre
		setContentPane(MainPage()); // Utilisation du panel principal comme content pane
		setVisible(true); // Rendre la fenêtre visible
	}

	public void setPanel(JPanel panel) {
		setContentPane(panel);
		revalidate();
		repaint();
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

		List<ImageIcon> deck = new ArrayList<>(); // Liste vide du deck

		// Initialisation des cartes pour les reconnaitre
		String[] suits = { "COEUR", "CARREAU", "TREFLE", "PIQUE" };
		String[] values = { "DEUX", "TROIS", "QUATRE", "COEUR", "SIX", "SEPT", "HUIT", "NEUF", "DIX", "VALET", "REINE",
				"ROI", "ace" };

		// Ajoute les cartes au deck
		for (String suit : suits) {
			for (String value : values) {
				String cardPath = "src\\cartes\\" + suit + "_" + suit + ".png";
				deck.add(resizeCardImage(cardPath, cardWidth, cardHeight));
			}
		}

		// Placement des cartes en colonnes avec décalage vertical pour effet empilé
		for (int col = 0; col < colonnesDeDepart.size(); col++) {
			List<Carte> colonne = colonnesDeDepart.get(col);
			for (int cardIndex = 0; cardIndex < colonne.size(); cardIndex++) {
				JLabel cardLabel;
				int x = gamecolumnsxStart + (cardWidth + gamecolumnsxSpacing) * col;
				int y = gamecolumnsyStart + gamecolumnsySpacing * cardIndex;

				Carte carte = colonne.get(cardIndex);
				if (carte != null) {
					// Créer un JLabel pour afficher la carte
					String cardPath = "src\\cartes\\" + carte.getNom().toString() + "_" + carte.getCouleur().toString()
							+ ".png";
					ImageIcon carteImage = resizeCardImage(cardPath, cardWidth, cardHeight);
					cardLabel = new JLabel(carteImage);
				} else {
					// Si la carte est nulle, afficher le dos de la carte
					cardLabel = new JLabel(cardBackIcon);
				}

				cardLabel.setBounds(x, y, cardWidth, cardHeight);
				final int finalCol = col;
				// Ajout d'un MouseListener à chaque JLabel de carte
				cardLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						System.out.println("Colonne cliquée : " + ((finalCol) + 1));
					}
				});
				bgLabel.add(cardLabel);
				bgLabel.setComponentZOrder(cardLabel, colonne.size() - cardIndex - 1);
			}

		}
		int piocheXStart = 130; // Position de départ pour la première pile vide sur l'axe X
		int piocheYStart = 30; // Position de départ pour la première pile vide sur l'axe Y
		int pilocheSpacing = 10; // Espace horizontal entre les piles vides

		// Création des quatre colonnes finales
		for (int i = 0; i < 4; i++) {
			ImageIcon pileVideIcon = new ImageIcon("src\\cartes\\empty_pile.png"); // Image d'une pile vide
			JLabel pileVideLabel = new JLabel(pileVideIcon);
			int x = piocheXStart + (cardWidth + pilocheSpacing) * i;
			int y = piocheYStart;
			pileVideLabel.setBounds(x, y, cardWidth, cardHeight);
			bgLabel.add(pileVideLabel);
		}

		// Colonnes de la pioche
		ImageIcon pileVideIcon = new ImageIcon("src\\cartes\\empty_pile.png"); // Image d'une pile vide
		JLabel pileVideLabel = new JLabel(pileVideIcon);
		pileVideLabel.setBounds(630, 30, cardWidth, cardHeight);
		bgLabel.add(pileVideLabel);

		JLabel piocheLabel = new JLabel(cardBackIcon);
		piocheLabel.setBounds(730, 30, cardWidth, cardHeight);
		bgLabel.add(piocheLabel);

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

	public JPanel getMainPage() {
		return MainPage();
	}

	public JPanel getPanelSolitaire() {
		return PanelSolitaire();
	}

}
