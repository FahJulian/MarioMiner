package org.mining.game;

import java.awt.image.BufferedImage;

import org.mining.managers.DataHandler;

public enum ToolType implements Holdable{
	HAND(1, 0, "bare_hands"), 
	BLOCK(1, 0, "bare_hands"),
	
	WOODEN_PICKAXE(2, 0, "wooden_pickaxe"), 
	STONE_PICKAXE(2, 1, "stone_pickaxe"),
	IRON_PICKAXE(2, 3, "iron_pickaxe"), 
	GOLD_PICKAXE(2, 2, "gold_pickaxe"), 
	DIAMOND_PICKAXE(2, 4, "diamond_pickaxe"),
	
	WOODEN_AXE(3, 0, "wooden_axe"), 
	STONE_AXE(3, 1, "stone_axe"), 
	IRON_AXE(3, 3, "iron_axe"), 
	GOLD_AXE(3, 2, "gold_axe"), 
	DIAMOND_AXE(3, 4, "diamond_axe"),
	
	WOODEN_SHOVEL(4, 0, "wooden_shovel"), 
	STONE_SHOVEL(4, 1, "stone_shovel"), 
	IRON_SHOVEL(4, 3, "iron_shovel"), 
	GOLD_SHOVEL(4, 2, "gold_shovel"), 
	DIAMOND_SHOVEL(4, 4, "diamond_shovel");
	
	/** 
	 * The type specifies which kind of tool it is:
	 * 0: null
	 * 1: bare hand / equal
	 * 2: pickaxe
	 * 3: axe
	 * 4: shovel
	 */
	private int type;
	
	/**
	 * The subtype specifies which level the tool is:
	 * 0: wooden / bare hand / equal
	 * 1: stone
	 * 2: gold
	 * 3: iron
	 * 4: diamond
	 */
	private int subtype;
	
	/** The Tile's tool-bar icon image */
	private BufferedImage tbIcon;
	
	/** The Tile's extension to the players img src to visualize the tool */
	private String imgFileExtension;
	
	private ToolType(int type, int subtype, String imgFileExtension) {
		this.type = type;
		this.subtype = subtype;
		this.imgFileExtension = imgFileExtension;
		this.tbIcon = DataHandler.loadImage("rsc/img/icons/" + imgFileExtension + ".png");
	}
	
	public int getType() {
		return this.type;
	}
	public int getSubType() {
		return this.subtype;
	}
	
	@Override
	public BufferedImage getTBIcon() {
		return this.tbIcon;
	}
	
	public String getPlayerImgFileExtension() {
		return this.imgFileExtension;
	}
}
