package org.mining.managers;

import java.awt.event.KeyEvent;

import org.mining.display.Game;
import org.mining.display.GameState;

public class MenuKeyHandler implements java.awt.event.KeyListener{
	
	/** The Mining instance thats being controlled */
	private Game game;
	/** A variable to check whether ESC was pressed when in menu or when ingame */
	private boolean escPressedInMenu;
	
	public MenuKeyHandler(Game game) {
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (game.getGameState() == GameState.INGAME) return;
		
		switch (game.getGameState()) {
		case NEW_GAME:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				escPressedInMenu = true;
				break;
			}
		case PAUSED:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				escPressedInMenu = true;
				break;
			}
		case INGAME:
			break;
		case GAME_OVER:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (game.getGameState() == GameState.INGAME) return;
		
		switch (game.getGameState()) {
		case NEW_GAME:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				game.resume();
				break;
			case KeyEvent.VK_ESCAPE:
				if (escPressedInMenu) game.resume();
				escPressedInMenu = false;
			}
		case PAUSED:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				if (escPressedInMenu) game.resume();
				escPressedInMenu = false;
				break;
			}
		case INGAME:
			break;
		case GAME_OVER:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				game.reset();
				game.resume();
			}
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (game.getGameState() == GameState.INGAME) return;
		
	}

}
