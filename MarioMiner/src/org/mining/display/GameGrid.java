package org.mining.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.mining.game.Block;
import org.mining.game.BlockType;
import org.mining.game.Direction;
import org.mining.game.Player;
import org.mining.managers.MapBuilder;

public class GameGrid{
	
	/** The number of pixels that make up one tile */
	public static final int BLOCK_SIZE = 64;
	/** The number of columns on the board */
	public static final int COL_COUNT = Game.CONTENT_WIDTH / BLOCK_SIZE;	
	/** The number of rows on the board */
	public static final int ROW_COUNT = Game.CONTENT_HEIGHT / BLOCK_SIZE;
	
	/** The default spawn position of the player */
	public static final Point SPAWN_POS = new Point(Game.CONTENT_CENTER_X, (int) (ROW_COUNT * 0.25f) * BLOCK_SIZE);
	
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
				"rsc/img/blocks/player_small.png", true);
	}

	public void tick() {
		if (game.getGameState() == GameState.INGAME) {
			player.tick();
		}
	}
	
	public void render(Graphics g) {
		for (int row = 0; row < blocks[0].length; row++) 
			for (int col = 0; col < blocks[0][row].length; col++) {
				if (blocks[0][row][col].getType() == BlockType.VOID) {
					blocks[1][row][col].render(g);
					if (blocks[1][row][col].getType() != BlockType.AIR)
						BlockType.LAYER2_COVER.render(col * BLOCK_SIZE, row * BLOCK_SIZE, g);
				}
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
		blocks = MapBuilder.buildFlatMap(this, ROW_COUNT, COL_COUNT, 
				new int[] {4, 5, 8, ROW_COUNT},
				new BlockType[] {BlockType.VOID, BlockType.GRASS, BlockType.DIRT, BlockType.STONE }, 
				new int[] {4, ROW_COUNT}, 
				new BlockType[] {BlockType.AIR, BlockType.DIRT});
		
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
	
	/**
	 * Check for the block at a given position
	 * @param layer The layer in the grid to check
	 * @param p The position to check
	 * @return The block at the position
	 */
	public Block getBlockAtPosition(int layer, Point p) {
		return blocks[layer][p.y / BLOCK_SIZE][p.x / BLOCK_SIZE];
	}
	
	public Block getBlock(int layer, int row, int col) {
		return blocks[layer][row][col];
	}
	
	public void setBlock(int layer, int row, int col, BlockType type) {
		blocks[layer][row][col] = new Block(type, row, col, this);
	}
	
	public Block digBlock(int row, int col) {
		Block b = getBlock(0, row, col);
		setBlock(0, row, col, BlockType.VOID);
		return b;
	}
	
	/**
	 * Converts an actual coordiante position to a grid position
	 * @param p The coordinates to convert
	 * @return The grid position of the input coordinates
	 */
	public static Point coordToGridPos(Point p) {
		return new Point(p.x / BLOCK_SIZE, p.y / BLOCK_SIZE);
	}
}
