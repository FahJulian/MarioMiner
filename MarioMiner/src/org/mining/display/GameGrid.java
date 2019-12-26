package org.mining.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import org.mining.game.Block;
import org.mining.game.BlockType;
import org.mining.game.Direction;
import org.mining.game.Player;

public class GameGrid extends javax.swing.JPanel{

	private static final long serialVersionUID = 5682318183794056493L;
	
	/** The number of pixels that make up one tile */
	public static final int BLOCK_SIZE = 64;
	/** The number of columns on the board */
	public static final int COL_COUNT = 960 / BLOCK_SIZE;	
	/** The number of rows on the board */
	public static final int ROW_COUNT = 540 / BLOCK_SIZE;	
	/** The central x coordinate of the grid */
	public static final int CENTER_X = COL_COUNT * BLOCK_SIZE / 2;	
	/** The central y coordinate of the grid */
	public static final int CENTER_Y = ROW_COUNT * BLOCK_SIZE / 2;	
	/** The total width of the panel */
	public static final int WIDTH = COL_COUNT * BLOCK_SIZE;	
	/** The total height of the panel */
	public static final int HEIGHT = ROW_COUNT * BLOCK_SIZE;
	
	/** The larger font to draw */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);
	/** The smaller font to draw */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 16);
	
	/** The default spawn position of the player */
	private static final Point SPAWN_POS = new Point(CENTER_X - (CENTER_X % BLOCK_SIZE),
													 CENTER_Y - (CENTER_Y % BLOCK_SIZE) - 2 * BLOCK_SIZE);
	
	/** The Mining instance the GridPanel is part of */
	private Mining mining;
	
	/** The Player of the Game */
	public Player player;
	
	/** The blocks that make up the grid */
	private Block[][][] blocks;
	
	public GameGrid(Mining mining) {
		this.mining = mining; 
		this.blocks = new Block[2][ROW_COUNT][COL_COUNT];
		this.player = new Player(SPAWN_POS.x, SPAWN_POS.y, BLOCK_SIZE - 7, this,
				"rsc/img/" + BLOCK_SIZE + "/player_small.png");
		reset();
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.BLACK);
	}

	public void tick() {
		if (mining.getGameState() == GameState.INGAME) {
			player.tick();
		}
	}
	
	public void render() {
		repaint();
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
	}
	
	public void digBlock(int row, int col) {
		setBlock(0, row, col, BlockType.VOID);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		String msg;
		boolean drawPausedString = false;
		switch (mining.getGameState()) {
		case NEW_GAME:
		case GAME_OVER:	// fall-trough
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			msg = (mining.getGameState() == GameState.NEW_GAME) ? 
				"MARIO MINER" : "GAME OVER";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2,  150);
			
			g.setFont(SMALL_FONT);
			msg = "Press Enter to Play" + ((mining.getGameState() == GameState.NEW_GAME) ?
					"!" : " again!");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
			break;
		
		case PAUSED:
			drawPausedString = true;	
		case INGAME:	// fall-trough
			for (int row = 0; row < blocks[0].length; row++) 
				for (int col = 0; col < blocks[0][row].length; col++) {
					if (blocks[0][row][col].getType() == BlockType.VOID)
						blocks[1][row][col].render(g);
					else blocks[0][row][col].render(g);
				}
			player.render(g);
			renderDigging(g);
			
			if (drawPausedString) {
				g.setFont(LARGE_FONT);
				g.setColor(Color.RED);
				msg = "PAUSED";
				g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
			}
			break;
		}
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
	
	private void setBlock(int layer, int row, int col, BlockType type) {
		blocks[layer][row][col] = new Block(type, row, col, this);
	}
	
	public Block getBlock(int layer, int row, int col) {
		return blocks[layer][row][col];
	}
	
	public Block getBlockAtPosition(int layer, Point p) {
		return blocks[layer][p.y / BLOCK_SIZE][p.x / BLOCK_SIZE];
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
}
