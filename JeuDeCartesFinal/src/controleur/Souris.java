package controleur;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class Souris implements ActionListener, MouseListener {

	public JButton boutonQuitter;
	public JButton boutonSolitaire;

	public Souris(JButton... boutons) {
		for (JButton bouton : boutons) {
			bouton.addActionListener(this);
			bouton.addMouseListener(this);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

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

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.getActionCommand().equals("Quitter")) {
			System.exit(0);
		}
	}
}
