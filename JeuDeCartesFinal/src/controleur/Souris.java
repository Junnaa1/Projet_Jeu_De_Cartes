package controleur;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import vue.Gui;

public class Souris implements ActionListener, MouseListener {
	private JButton boutonQuitter;
	private JButton boutonSolitaire;
	private Gui gui; // Gardez une référence à l'interface GUI pour accéder à ses méthodes

	// Modifiez le constructeur pour accepter directement les boutons et l'interface
	// GUI
	public Souris(Gui gui, JButton boutonQuitter, JButton boutonSolitaire) {
		this.gui = gui;
		this.boutonQuitter = boutonQuitter;
		this.boutonSolitaire = boutonSolitaire;

		boutonQuitter.addActionListener(this);
		boutonSolitaire.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source == boutonQuitter) {
			System.exit(0);
		} else if (source == boutonSolitaire) {
			JFrame fenetrePrincipale = (JFrame) SwingUtilities.getWindowAncestor(source);
			JPanel panelSolitaire = gui.PanelSolitaire(); // Utilisez directement la méthode de gui
			fenetrePrincipale.setContentPane(panelSolitaire);
			fenetrePrincipale.revalidate();
			fenetrePrincipale.repaint();
		}
	}

	// Implémentez les autres méthodes de MouseListener selon vos besoins
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		JButton source = (JButton) e.getSource();
		source.setBackground(new Color(52, 14, 40));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		JButton source = (JButton) e.getSource();
		source.setBackground(new Color(91, 4, 75));
	}
}