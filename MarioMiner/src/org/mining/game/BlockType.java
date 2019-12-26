package org.mining.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.mining.display.GameGrid;
import org.mining.managers.DataHandler;

public enum BlockType {
	
	/** Tile that renders a transparent screen */
	VOID("void.png", false, true),
	/** tile that is currently being digged - state 1 */
	DIG_STATE_1("digging/state_1.png", false, false),
	/** tile that is currently being digged - state 2 */
	DIG_STATE_2("digging/state_2.png", false, false),
	/** tile that is currently being digged - state 3 */
	DIG_STATE_3("digging/state_3.png", false, false),
	/** tile that is currently being digged - state 4 */
	DIG_STATE_4("digging/state_4.png", false, false),	
	/** Dirt tile */
	DIRT("dirt.png", true, false),	
	/** Background (air) tile */
	AIR("air.png", false, true),	
	/* Stone tile */
	STONE("stone.png", true, false);

	
	/** The Tile's rendering image */
	private BufferedImage img;
	
	/** Specifies whether a tile can be dug -> e.g air cant, stone can */
	private boolean diggable;
	
	/** Specifies whether a tile can be walked through by the player */
	private boolean walkable;
	
	/**
	 * Creates a new TileType
	 * @param image_src The sourcepath of the Tile's rendering image
	 */ 
	private BlockType(String image_src, boolean diggable, boolean walkable) {
		this.img = DataHandler.loadImage("rsc/img/" + GameGrid.BLOCK_SIZE + "/" + image_src);
		this.diggable = diggable;
		this.walkable = walkable;
	}
	
	/** Renders a tile at a given position. Used to render
	 * Digging State Blocks
	 */
	public void render(int x, int y, Graphics g) {
		g.drawImage(img, x, y, null);
	}
	
	/**
	 * Get the Tiles rendering image
	 * @return The Tiles rendering image
	 */
	public BufferedImage getImg() {
		return this.img;
	}
	
	/**
	 * Get the tiles diggable state
	 * @return Whether or no the tile can be dug away
	 */
	public boolean isDiggable() {
		return this.diggable;
	}
	
	/**
	 * Get the tiles walkable state
	 * @return Whether or not the tile can be walked through by the player
	 */
	public boolean isWalkable() {
		return this.walkable;
	}
}
