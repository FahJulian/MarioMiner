package org.mining.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.mining.display.GameGrid;
import org.mining.managers.DataHandler;

public enum BlockType implements Holdable{
	
	/** Tile that covers everything in layer2, black with low opacity */
	LAYER2_COVER("layer2Cover.png", false),
	
	/** tile that is currently being digged - state 1 */
	DIG_STATE_1("digging/state_1.png", false),
	/** tile that is currently being digged - state 2 */
	DIG_STATE_2("digging/state_2.png", false),
	/** tile that is currently being digged - state 3 */
	DIG_STATE_3("digging/state_3.png", false),
	/** tile that is currently being digged - state 4 */
	DIG_STATE_4("digging/state_4.png", false),	
	
	/** Tile that renders a transparent screen */
	VOID("void.png", true),
	/** Background (air) tile */
	AIR("air.png", true),
	
	/** Dirt tile */
	DIRT("dirt.png", false, true, ToolType.WOODEN_SHOVEL),	
	/** Grass tile */
	GRASS("grass.png", false, true, ToolType.WOODEN_SHOVEL),
	/* Stone tile */
	STONE("stone.png", false, false, ToolType.WOODEN_PICKAXE);

	private boolean DEBUGGING = false;
	
	/** The Tile's rendering image */
	private BufferedImage img;
	
	/** The Tile's tool-bar icon image */
	private BufferedImage tbIcon;
	
	/** Specifies whether a tile can be dug -> e.g air cant, stone can */
	private boolean diggable;
	
	/** Specifies exactly with what tool the block is diggable */
	private ToolType minimumToolType;
	
	/** Specifies whether or not that tool can be dug with bare hands too */
	private boolean diggableByHand;
	
	/** Specifies whether a tile can be walked through by the player */
	private boolean walkable;
	
	/**
	 * Creates a new BlockType with all diggable related values to null / 0
	 * @param image_src The path to the Tile's rendering image
	 * @param walkable Whether or not the player can walk in the blocks grid slot
	 */
	private BlockType(String image_src, boolean walkable) {
		this.img = DataHandler.loadImage("rsc/img/blocks/" + image_src);
		
		this.walkable = walkable;
		
		this.diggable = false;
		this.diggableByHand = false;
		this.minimumToolType = null;
	}
	
	/**
	 * Creates a new diggable BlockType
	 * @param image_src The path to the Tile's rendering image
	 * @param walkable Whether or not the player can walk in the blocks grid slot
	 * @param diggableByHand Whether or not the Block can be dug by hand
	 * @param mimimumToolType The minimum Tool to get a speed buff / to dig the block at all
	 */
	private BlockType(String image_src, boolean walkable, boolean diggableByHand, ToolType minimumToolType) {
		this.img = DataHandler.loadImage("rsc/img/blocks/" + image_src);
		this.tbIcon = DataHandler.loadImage("rsc/img/icons/" + image_src);
		
		this.walkable = walkable;
		
		this.diggable = true;
		this.diggableByHand = diggableByHand;
		this.minimumToolType = minimumToolType;
	}
	
	/** Renders a tile at a given position. Used to render
	 * Digging State Blocks
	 */
	public void render(int x, int y, Graphics g) {
		g.drawImage(img, x, y, null);
		
		if (DEBUGGING) {
			g.setColor(Color.RED);
			g.fillRect(x + GameGrid.BLOCK_SIZE / 2 - 5, y + GameGrid.BLOCK_SIZE / 2 - 5, 10, 10);
		}
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
	 * @return Whether or no the tile can be dug away with a given tool
	 */
	public boolean isDiggableWithTool(ToolType tool) {
		if (this.minimumToolType.getType() == 0) return false;	// 0 means it isnt diggable at all
		if (diggableByHand) return true;
		
		if (this.minimumToolType.getType() != 1) {
			if (this.minimumToolType.getType() == tool.getType() && 
					this.minimumToolType.getSubType() <= tool.getSubType())
				return true;
		}
		
		return false;
	}
	
	public int getDigSpeed(ToolType tool) {
		if (this.minimumToolType.getType() == tool.getType() && 
				this.minimumToolType.getSubType() <= tool.getSubType()) {
			switch (tool.getSubType()) {
			case 0: return 900;
			case 1: return 700;
			case 2:	return 200;
			case 3: return 400;
			case 4: return 100;
			}
		}
		
		return 1200;
	}
	
	/**
	 * Returns whether or not the tool can be dug at all, tool is irrelevant
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
	
	@Override
	public BufferedImage getTBIcon() {
		return this.tbIcon;
	}
}
