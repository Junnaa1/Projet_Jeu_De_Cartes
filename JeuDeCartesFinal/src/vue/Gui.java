package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

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
		setContentPane(MainPage()); // Utilisation du panel principal comme content pane
		setVisible(true); // Rendre la fenêtre visible
	}

	public JPanel MainPage() {
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());

		// Arrière-plan
		ImageIcon bgIcon = new ImageIcon("src\\Background.png");
		JLabel bgLabel = new JLabel(bgIcon);
		panelPrincipal.add(bgLabel, BorderLayout.CENTER);

		// Création du panel pour les boutons avec une transparence
		JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		panelBoutons.setOpaque(false); // Rend le panel transparent

		boutonSolitaire = new JButton("Solitaire");
		boutonQuitter = new JButton("Quitter");

		panelBoutons.add(boutonSolitaire);
		panelBoutons.add(boutonQuitter);

		// Création du label de titre
		JLabel labelTitre = new JLabel("Bienvenue au choix de jeu de cartes", SwingConstants.CENTER);
		labelTitre.setFont(new Font("Serif", Font.BOLD, 18));
		labelTitre.setForeground(Color.BLACK);

		// Ajout du label et des boutons au panel principal
		panelPrincipal.add(labelTitre, BorderLayout.PAGE_START);
		panelPrincipal.add(panelBoutons, BorderLayout.PAGE_END); // Place les boutons en bas

		return panelPrincipal;
	}
}
