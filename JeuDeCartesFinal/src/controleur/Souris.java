package controleur;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import vue.Gui;

public class Souris implements ActionListener, MouseListener {
	private Gui gui;

	public Souris(Gui gui) {
		this.gui = gui;
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if ("Quitter".equals(command)) {
			System.exit(0);
		} else if ("Solitaire".equals(command)) {
			gui.setPanel(gui.getPanelSolitaire());
		} else if ("Retour".equals(command)) {
			gui.setPanel(gui.getMainPage());
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		JButton source = (JButton) e.getSource();
		source.setBackground(new Color(52, 14, 40));
	}

	public void mouseExited(MouseEvent e) {
		JButton source = (JButton) e.getSource();
		source.setBackground(new Color(91, 4, 75));
	}
}