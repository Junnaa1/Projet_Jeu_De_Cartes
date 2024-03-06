package vue;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controleur.Souris;

public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton boutonSolitaire;
	private JButton boutonQuitter;
	private Souris souris;

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

	public JPanel PanelSolitaire() {
		JPanel panelSolitaire = new JPanel();
		panelSolitaire.setLayout(null);

		// Arrière-plan
		ImageIcon bgIcon = new ImageIcon("src\\BackgroundGame.png");
		JLabel bgLabel = new JLabel(bgIcon);
		bgLabel.setBounds(0, 0, 960, 540);
		panelSolitaire.add(bgLabel);

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

		panelBoutons.setBounds(660, 450, 420, 60);
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
