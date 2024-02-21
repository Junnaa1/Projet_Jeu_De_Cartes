package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton boutonSolitaire;
	private JButton boutonQuitter;

	public Gui() {
		initGUI();
	}

	private void initGUI() {
		setTitle("Jeu de cartes");
		setSize(960, 540); // Taille de la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Centre la fenêtre
		setIconImage(new ImageIcon("src\\Logo.png").getImage());
		setResizable(false); // Empêche le redimensionnement de la fenêtre

		// Création des boutons
		boutonSolitaire = new JButton("Solitaire");
		boutonQuitter = new JButton("Quitter");

		// Création du panel pour les boutons
		JPanel panelBoutons = new JPanel(new GridLayout(3, 1, 10, 10));
		panelBoutons.add(boutonSolitaire);
		panelBoutons.add(boutonQuitter);

		// Création du label de titre
		JLabel labelTitre = new JLabel("Bienvenue au choix de jeu de cartes", SwingConstants.CENTER);
		labelTitre.setFont(new Font("Serif", Font.BOLD, 18));
		labelTitre.setForeground(Color.BLACK);

		// Ajout des composants à la fenêtre
		add(labelTitre, BorderLayout.PAGE_START);
		add(panelBoutons, BorderLayout.CENTER);

		// Rendre la fenêtre visible
		setVisible(true);
	}
}
