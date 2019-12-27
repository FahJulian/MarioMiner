package org.mining.managers;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import org.mining.display.Game;
import org.mining.display.GameGrid;
import org.mining.display.GameState;
import org.mining.game.Direction;
import org.mining.game.Player;
import org.mining.game.ToolType;

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
	/** A switch for whether or not ctrl is pressed */
	private boolean ctrlPressed = false;
	
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
			ctrlPressed = false;
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
			
		case KeyEvent.VK_T:
			if (player.isInGodMode() && ctrlPressed) {
				String s = JOptionPane.showInputDialog("Choose a tool");
				if (s == null) player.emptyInventorySlot(player.getSelectedSlot());
				switch (s) {
				case "WP": player.pickUpTool(ToolType.WOODEN_PICKAXE, player.getSelectedSlot()); 	break;
				case "SP": player.pickUpTool(ToolType.STONE_PICKAXE, player.getSelectedSlot()); 	break;
				case "GP": player.pickUpTool(ToolType.GOLD_PICKAXE, player.getSelectedSlot()); 		break;
				case "IP": player.pickUpTool(ToolType.IRON_PICKAXE, player.getSelectedSlot()); 		break;
				case "DP": player.pickUpTool(ToolType.DIAMOND_PICKAXE, player.getSelectedSlot()); 	break;
				case "WA": player.pickUpTool(ToolType.WOODEN_AXE, player.getSelectedSlot()); 		break;
				case "SA": player.pickUpTool(ToolType.STONE_AXE, player.getSelectedSlot()); 		break;
				case "GA": player.pickUpTool(ToolType.GOLD_AXE, player.getSelectedSlot()); 			break;
				case "IA": player.pickUpTool(ToolType.IRON_AXE, player.getSelectedSlot()); 			break;
				case "DA": player.pickUpTool(ToolType.DIAMOND_AXE, player.getSelectedSlot()); 		break;
				case "WS": player.pickUpTool(ToolType.WOODEN_SHOVEL, player.getSelectedSlot()); 	break;
				case "SS": player.pickUpTool(ToolType.STONE_SHOVEL, player.getSelectedSlot()); 		break;
				case "GS": player.pickUpTool(ToolType.GOLD_SHOVEL, player.getSelectedSlot()); 		break;
				case "IS": player.pickUpTool(ToolType.IRON_SHOVEL, player.getSelectedSlot()); 		break;
				case "DS": player.pickUpTool(ToolType.DIAMOND_SHOVEL, player.getSelectedSlot()); 	break;
				}
				break;
			}
			
		case KeyEvent.VK_SHIFT:
			shiftPressed = true;
			break;
		case KeyEvent.VK_CONTROL:
			ctrlPressed = true;
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
		case KeyEvent.VK_CONTROL:
			ctrlPressed = false;
			break;
		}	
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (!(game.getGameState() == GameState.INGAME))
			return;

	}

}
