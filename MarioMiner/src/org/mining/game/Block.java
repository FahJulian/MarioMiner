package org.mining.game;

import java.awt.Graphics;

import org.mining.display.GameGrid;

public class Block extends GridObject{
	
	/** The type of the Block */
	private BlockType type;
	
	public Block(BlockType type, int row, int col, GameGrid grid) {
		super(col * GameGrid.BLOCK_SIZE, row * GameGrid.BLOCK_SIZE, GameGrid.BLOCK_SIZE - 1, grid);
		this.type = type;
	}
	
	@Override
	public void render(Graphics g) {
		type.render(x, y, g);
	}
	
	@Override
	public void tick() {
		
	}
	
	public BlockType getType() {
		return this.type;
	}
}
