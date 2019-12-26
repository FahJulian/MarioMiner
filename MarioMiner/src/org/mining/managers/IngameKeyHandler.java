package org.mining.managers;

import java.awt.event.KeyEvent;

import org.mining.display.Game;
import org.mining.display.GameGrid;
import org.mining.display.GameState;
import org.mining.game.Direction;
import org.mining.game.Player;

public class IngameKeyHandler implements java.awt.event.KeyListener {

	/** The Game thats being controlled */
	private Game game;
	/** The GameGrid thats being controlled */
	@SuppressWarnings("unused")
	private GameGrid grid;
	/** The player thats being controlled */
	private Player player;
	
	/** A switch for whether or not shift is pressed */
	private boolean shiftPressed = false;

	public IngameKeyHandler(Game game, GameGrid grid, Player player) {
		this.game = game;
		this.grid = grid;
		this.player = player;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!(game.getGameState() == GameState.INGAME)) return;
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			player.stopDigging();
			game.pause();
			shiftPressed = false;
			break;
			
		case KeyEvent.VK_UP:
			if (shiftPressed) player.placeSelectedBlock(Direction.UP);
			else player.startDiggingIfViable(Direction.UP);
			break;
		case KeyEvent.VK_DOWN:
			if (shiftPressed) player.placeSelectedBlock(Direction.DOWN);
			else player.startDiggingIfViable(Direction.DOWN);
			break;
		case KeyEvent.VK_LEFT:
			if (shiftPressed) player.placeSelectedBlock(Direction.LEFT);
			else player.startDiggingIfViable(Direction.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			if (shiftPressed) player.placeSelectedBlock(Direction.RIGHT);
			else player.startDiggingIfViable(Direction.RIGHT);
			break;
		
		case KeyEvent.VK_A:
			if (!player.isWalking(Direction.LEFT)) player.startWalking(Direction.LEFT);
			break;
		case KeyEvent.VK_D:
			if (!player.isWalking(Direction.RIGHT)) player.startWalking(Direction.RIGHT);
			break;
			
		case KeyEvent.VK_SPACE:
			player.jump();
			break;
			
		case KeyEvent.VK_SHIFT:
			shiftPressed = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!(game.getGameState() == GameState.INGAME))
			return;
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN: 		// fall-through
		case KeyEvent.VK_LEFT:		// fall-through
		case KeyEvent.VK_RIGHT:		// fall-through
			if (player.isDigging()) player.stopDigging();
			break;
			
		case KeyEvent.VK_A:
			if (player.isWalking(Direction.LEFT)) player.stopWalking(Direction.LEFT);
			break;
		case KeyEvent.VK_D:
			if (player.isWalking(Direction.RIGHT)) player.stopWalking(Direction.RIGHT);
			break;
			
		case KeyEvent.VK_1: player.selectSlot(0); break;
		case KeyEvent.VK_2: player.selectSlot(1); break;
		case KeyEvent.VK_3: player.selectSlot(2); break;
		case KeyEvent.VK_4: player.selectSlot(3); break;
		case KeyEvent.VK_5: player.selectSlot(4); break;
		case KeyEvent.VK_6: player.selectSlot(5); break;
		case KeyEvent.VK_7: player.selectSlot(6); break;
		case KeyEvent.VK_8: player.selectSlot(7); break;
		case KeyEvent.VK_9: player.selectSlot(8); break;
		
		case KeyEvent.VK_SHIFT:
			shiftPressed = false;
			break;
		}	
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (!(game.getGameState() == GameState.INGAME))
			return;

	}

}
