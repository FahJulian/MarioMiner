package org.mining.display;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.mining.managers.IngameKeyHandler;
import org.mining.managers.MenuKeyHandler;

public class Window extends javax.swing.JFrame{

	private static final long serialVersionUID = 610813257643608931L;
	
	public Window(int width, int height, String title, Game game) {
		
		// Set up the JFrame with the name and size
		setTitle(title);
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
		
		// Set other JFrame variables, add the game to the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		add(game);
		setVisible(true);
	}
}
