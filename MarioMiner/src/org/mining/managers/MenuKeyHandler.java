package org.mining.managers;

import java.awt.event.KeyEvent;

import org.mining.display.GameState;
import org.mining.display.Mining;

public class MenuKeyHandler implements java.awt.event.KeyListener{
	
	/** The Mining instance thats being controlled */
	private Mining mining;
	/** A variable to check whether ESC was pressed when in menu or when ingame */
	private boolean escPressedInMenu;
	
	public MenuKeyHandler(Mining mining) {
		this.mining = mining;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (mining.getGameState() == GameState.INGAME) return;
		
		switch (mining.getGameState()) {
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
		if (mining.getGameState() == GameState.INGAME) return;
		
		switch (mining.getGameState()) {
		case NEW_GAME:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				mining.resume();
				break;
			case KeyEvent.VK_ESCAPE:
				if (escPressedInMenu) mining.resume();
				escPressedInMenu = false;
			}
		case PAUSED:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				if (escPressedInMenu) mining.resume();
				escPressedInMenu = false;
				break;
			}
		case INGAME:
			break;
		case GAME_OVER:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (mining.getGameState() == GameState.INGAME) return;
		
	}

}
