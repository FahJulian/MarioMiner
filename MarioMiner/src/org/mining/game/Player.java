package org.mining.game;

import java.awt.Graphics;
import java.awt.Point;

import org.mining.display.GameGrid;
import org.mining.managers.PlayerCollisionHandler;

public class Player extends GridObject{
	
	/** The collision handler responsible for all player related collisions */
	public final PlayerCollisionHandler COLLISIONS = new PlayerCollisionHandler(this);
	
	/** Whether or not the player is an admin / has additional rights */
	private boolean godMode;
	
	/** The direction the player is currently digging in */
	private Direction digDir = null;
	/** The state the Digging is currently in */
	private int digState = 0;
	/** The timer used to handle digging State */
	private long digTimer;
	/** The block position that is being dug */
	private Point digPos;
	
	/** The players health: min = 0, max = 100 */
	private int health;
	
	/** Keeps track of whether or not the player is walking left (index 0) and right (index 1) */
	private boolean[] walking = new boolean[] {false, false};
	
	/** The players inventory -> Index 0-8 is the tool-bar, 9-35 the rest */
	private Holdable[] inventory;
	/** The correlating numbers of blocks in each grid slot */
	private int[] inventoryAmounts;
	/** The tool-bar slot that is currently selected */
	private int selectedSlot = 0;
	
	public Player(int x, int y, int size, GameGrid grid, boolean godMode) {
		super(x, y, size, grid);
		this.godMode = godMode;
		
		inventory = new Holdable[36];
		for (int i = 0; i < inventory.length; i ++) inventory[i] = null; 
		inventoryAmounts = new int[36];
		for (int i = 0; i < inventoryAmounts.length; i++) inventoryAmounts[i] = 0;
		
		health = 100;
		
		setImg("rsc/img/player/" + getTool().getPlayerImgFileExtension() + ".png");
	}

	@Override
	public void tick() {
		tickMovement();
		tickDig();
		tickGravity();
	}

	@Override
	public void render(Graphics g) {
		setImg("rsc/img/player/" + getTool().getPlayerImgFileExtension() + ".png");
		super.render(g);
	}
	
	/**
	 * Resets all values to default and changes position to the spawning position
	 */
	public void respawn() {
		health = 100;
		x = GameGrid.SPAWN_POS.x;
		y = GameGrid.SPAWN_POS.y;
		velX = 0;
		velY = 0;
		walking = new boolean[] {false, false};
		inventory = new Holdable[36];
		for (int i = 0; i < inventory.length; i ++) inventory[i] = null; 
		inventoryAmounts = new int[36];
		for (int i = 0; i < inventoryAmounts.length; i++) inventoryAmounts[i] = 0;
	}

	private void tickMovement() {
		for (int i = 1; i <= velX; i++) 
			if (COLLISIONS.canMoveOnePx(Direction.RIGHT))
				moveOnePx(Direction.RIGHT);
		
		for (int i = -1; i >= velX; i--) 
			if (COLLISIONS.canMoveOnePx(Direction.LEFT)) 
				moveOnePx(Direction.LEFT);
		
		for (int j = 1; j <= velY; j++) 
			if (COLLISIONS.canMoveOnePx(Direction.DOWN))
				moveOnePx(Direction.DOWN);
			else {
				if (velY >= 19) health -= Math.pow(velY, 1.5) / 4;
				velY = 0;
			}
		
		for (int j = -1; j >= velY; j--) 
			if (COLLISIONS.canMoveOnePx(Direction.UP)) 
				moveOnePx(Direction.UP);
			else velY = 0;
	}
	
	private void tickGravity() {
		if (!COLLISIONS.isStandingOnBlock()) velY += 1;
	}
	
	private void tickDig() {
		if (!COLLISIONS.isDigStillAllowed()) stopDigging();
		
		if (digDir != null && System.currentTimeMillis() - digTimer > 
			grid.getBlockAtPosition(0, digPos).getType().getDigSpeed(getTool())) {
			
			if (digState < 4) {
				digState++;
				digTimer = System.currentTimeMillis();
			}
			if (digState == 4) {
				pickUpBlock(grid.digBlock(digPos.y / GameGrid.BLOCK_SIZE, digPos.x / GameGrid.BLOCK_SIZE).getType(), selectedSlot);
				stopDigging();
			}
		}
	}
	
	/**
	 * Digs the next diggable block in a given direction, if it is near enough
	 * @param dir
	 */
	public void startDiggingIfViable(Direction dir) {
		if (isDigging()) return;
		
		this.digDir = dir;
		this.digTimer = System.currentTimeMillis();
		this.digState = 1;
		
		try {
			this.digPos = grid.getNextDiggableBlock(0, getCenterRow(), getCenterCol(), dir).getPos();
		}catch (NullPointerException npe) {
			this.digPos = null;
		}
		
		if (!COLLISIONS.isDigStartAllowed()) stopDigging();
	}
	
	public void stopDigging() {
		this.digDir = null;
		this.digPos = null;
		this.digTimer = 0;
		this.digState = 0;
	}
	
	public void startWalking(Direction dir) {
		switch (dir) {
		case LEFT:
			velX -= 5;
			walking[0] = true;
			break;
		case RIGHT:
			velX += 5;
			walking[1] = true;
			break;
		default: System.err.println("Cant walk down or up!"); return;
		}
	}
	
	public void stopWalking(Direction dir) {
		switch (dir) {
		case LEFT:
			velX += 5;
			walking[0] = false;
			break;
		case RIGHT:
			velX -= 5;
			walking[1] = false;
			break;
		default: System.err.println("Cant walk up or down!"); return;
		}
	}
	
	/**
	 * If player is standing on solid ground, starts jumping up
	 */
	public void jump() {
		if (COLLISIONS.isStandingOnBlock()) velY -= 13;
	}
	
	public void placeSelectedBlock(Direction dir) {
		if (!(inventory[selectedSlot] instanceof BlockType)) return;
		
		BlockType blockToPlace = (BlockType) (inventory[selectedSlot]);
		
		if (inventory[selectedSlot] != null && COLLISIONS.isBlockPlaceAllowed(dir) && blockToPlace.isDiggable()) {
			
			switch (dir) {
			case UP: 	grid.setBlock(0, getRow() - 1, getCenterCol(), blockToPlace);	 	break;
			case DOWN: 	grid.setBlock(0, getBottomRow() + 1, getCenterCol(), blockToPlace); break;
			case LEFT: 	grid.setBlock(0, getCenterRow(), getCol() - 1, blockToPlace); 		break;
			case RIGHT: grid.setBlock(0, getCenterRow(), getRightCol() + 1, blockToPlace); 	break;
			}
		
			inventoryAmounts[selectedSlot] -= 1;
			if (inventoryAmounts[selectedSlot] == 0)
				inventory[selectedSlot] = null;
		}
	}
	
	private void pickUpHoldable(Holdable type, int slot, int max, boolean firstCycle) {
		// Try putting the item in the requested slot
		if (inventory[slot] == type && inventoryAmounts[slot] < max) {
			inventoryAmounts[slot] += 1;
			return;
		}else if (inventory[slot] == null) {
			inventory[slot] = type;
			inventoryAmounts[slot] += 1;
			return;
			
		// If slot can't be filled, find another slot via recursion -> Start from slot 0, go up then
		}else if (firstCycle)
			pickUpHoldable(type, 0, max, false);
		else if (slot < 35)
			pickUpHoldable(type, slot + 1, max, false);
		else
			System.err.println("Inventory full");
	}
	
	public void pickUpTool(ToolType type, int slot) {
		pickUpHoldable(type, slot, 1, true);
	}
	
	private void pickUpBlock(BlockType type, int slot) {
		pickUpHoldable(type, slot, 64, true);
	}
	
	private void moveOnePx(Direction dir) {
		switch (dir) {
		case LEFT: x -= 1; return;
		case RIGHT: x += 1; return;
		case UP: y -= 1; return;
		case DOWN: y += 1; return;
		}
	}
	
	public void emptyInventorySlot(int slot) {
		this.inventory[slot] = null;
	}
	
	public void kill() {
		this.health = 0;
	}
	
	public boolean isDigging() {
		return digDir != null;
	}
	
	public Direction getDigDir() {
		return this.digDir;
	}
	
	public int getDigState() {
		return this.digState;
	}
	
	public Point getDigPos() {
		return this.digPos;
	}
	
	public boolean isWalking(Direction dir) {
		switch (dir) {
		case LEFT: return walking[0];
		case RIGHT: return walking[1];
		default: System.err.println("Cant walk up or down"); return false;
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public Holdable[] getInventory() {
		return this.inventory;
	}
	
	public int[] getInventoryAmounts() {
		return this.inventoryAmounts;
	}
	
	public int getSelectedSlot() {
		return this.selectedSlot;
	}
	
	public void selectSlot(int slot) {
		this.selectedSlot = slot;
	}
	
	public boolean isInGodMode() {
		return this.godMode;
	}
	
	public ToolType getTool() {
		if (inventory[selectedSlot] == null) return ToolType.HAND;
		else if (inventory[selectedSlot] instanceof BlockType) return ToolType.BLOCK;
		
		else return (ToolType) inventory[selectedSlot];
	}
}
