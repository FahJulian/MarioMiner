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
			break;
			
		case KeyEvent.VK_UP:
			player.startDiggingIfViable(Direction.UP);
			break;
		case KeyEvent.VK_DOWN:
			player.startDiggingIfViable(Direction.DOWN);
			break;
		case KeyEvent.VK_LEFT:
			player.startDiggingIfViable(Direction.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			player.startDiggingIfViable(Direction.RIGHT);
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
		}	
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (!(game.getGameState() == GameState.INGAME))
			return;

	}

}
