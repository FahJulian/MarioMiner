package org.mining.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.mining.game.Block;
import org.mining.game.BlockType;
import org.mining.game.Direction;
import org.mining.game.Player;

public class GameGrid{
	
	/** The number of pixels that make up one tile */
	public static final int BLOCK_SIZE = 64;
	/** The number of columns on the board */
	public static final int COL_COUNT = Game.CONTENT_WIDTH / BLOCK_SIZE;	
	/** The number of rows on the board */
	public static final int ROW_COUNT = Game.CONTENT_HEIGHT / BLOCK_SIZE;
	
	/** The default spawn position of the player */
	public static final Point SPAWN_POS = new Point(Game.CONTENT_CENTER_X,  Game.CONTENT_CENTER_Y - 2 * BLOCK_SIZE);
	
	/** The Game instance the GridPanel is part of */
	private Game game;
	
	/** The Player of the Game */
	public Player player;
	
	/** The blocks that make up the grid */
	private Block[][][] blocks;
	
	public GameGrid(Game game) {
		this.game = game; 
		this.blocks = new Block[2][ROW_COUNT][COL_COUNT];
		this.player = new Player(SPAWN_POS.x, SPAWN_POS.y, BLOCK_SIZE - 7, this,
				"rsc/img/" + BLOCK_SIZE + "/player_small.png");
	}

	public void tick() {
		if (game.getGameState() == GameState.INGAME) {
			player.tick();
		}
	}
	
	public void render(Graphics g) {
		for (int row = 0; row < blocks[0].length; row++) 
			for (int col = 0; col < blocks[0][row].length; col++) {
				if (blocks[0][row][col].getType() == BlockType.VOID)
					blocks[1][row][col].render(g);
				else blocks[0][row][col].render(g);
			}
		player.render(g);
		renderDigging(g);
		
		if (game.getGameState() == GameState.PAUSED) {
			g.setFont(Game.LARGE_FONT);
			g.setColor(Color.RED);
			String msg = "PAUSED";
			g.drawString(msg, Game.CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, Game.CENTER_Y);
		}
	}
	
	/** Resets all blocks in the grid to the default */
	public void reset() {
		// Set the first 25% of the screen to be air blocks
		for (int row = 0; row <= ROW_COUNT * 0.25f; row++)
			for (int col = 0; col < COL_COUNT; col++) {
				setBlock(0, row, col, BlockType.AIR);
				setBlock(0, row, col, BlockType.AIR);
			}
		
		// Set the rest of the screen to be stone with dirt behind
		for (int row = (int) (ROW_COUNT * 0.25f + 1); row < ROW_COUNT; row++)
			for (int col = 0; col < COL_COUNT; col++) {
				setBlock(0, row, col, BlockType.STONE);
				setBlock(1, row, col, BlockType.DIRT);
			}
		
		player.respawn();
	}
	
	
	
	private void renderDigging(Graphics g) {
		if (player.getDigDir() == null) return;
		
		switch(player.getDigState()){
		case 1: BlockType.DIG_STATE_1.render(player.getDigPos().x, player.getDigPos().y, g); break;
		case 2: BlockType.DIG_STATE_2.render(player.getDigPos().x, player.getDigPos().y, g); break;
		case 3: BlockType.DIG_STATE_3.render(player.getDigPos().x, player.getDigPos().y, g); break;
		case 4: BlockType.DIG_STATE_4.render(player.getDigPos().x, player.getDigPos().y, g); break;
		}
	}
	
	/**
	 * Checks where the next diggable Block is from a given position
	 * @return The next Block that can be dug in the given direction, null if no Block found
	 */
	public Block getNextDiggableBlock(int layer, int row, int col, Direction dir) {
		switch (dir) {
		case UP:
			for (int new_row = row - 1; new_row >= 0; new_row--)
				if (blocks[layer][new_row][col].getType().isDiggable()) return blocks[layer][new_row][col];
			break;
		case DOWN:
			for (int new_row = row + 1; new_row < ROW_COUNT; new_row++)
				if (blocks[layer][new_row][col].getType().isDiggable()) return blocks[layer][new_row][col];
			break;
		case LEFT:
			for (int new_col = col - 1; new_col >= 0; new_col--)
				if (blocks[layer][row][new_col].getType().isDiggable()) return blocks[layer][row][new_col];
			break;
		case RIGHT:
			for (int new_col = col + 1; new_col < COL_COUNT; new_col++)
				if (blocks[layer][row][new_col].getType().isDiggable()) return blocks[layer][row][new_col];
			break;
		}
		return null;
	}

	public Block getBlockAtPosition(int layer, Point p) {
		return blocks[layer][p.y / BLOCK_SIZE][p.x / BLOCK_SIZE];
	}
	
	public Block getBlock(int layer, int row, int col) {
		return blocks[layer][row][col];
	}
	
	private void setBlock(int layer, int row, int col, BlockType type) {
		blocks[layer][row][col] = new Block(type, row, col, this);
	}
	
	public void digBlock(int row, int col) {
		setBlock(0, row, col, BlockType.VOID);
	}
}
