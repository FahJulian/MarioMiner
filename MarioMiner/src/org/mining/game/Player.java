package org.mining.game;

import java.awt.Graphics;
import java.awt.Point;

import org.mining.display.GameGrid;
import org.mining.managers.PlayerCollisionHandler;

public class Player extends GridObject{
	
	/** The collision handler responsible for all player related collisions */
	public final PlayerCollisionHandler COLLISIONS = new PlayerCollisionHandler(this);

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
	private BlockType[] inventory;
	/** The tool-bar slot that is currently selected */
	private int selectedSlot = 0;
	
	public Player(int x, int y, int size, GameGrid grid, String img_path) {
		super(x, y, size, grid, img_path);
		
		inventory = new BlockType[36];
		inventory[0] = BlockType.STONE;
				
		health = 100;
	}

	@Override
	public void tick() {
		tickMovement();
		tickDig();
		tickGravity();
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
	}
	
	public void respawn() {
		health = 100;
		x = GameGrid.SPAWN_POS.x;
		y = GameGrid.SPAWN_POS.y;
		velX = 0;
		velY = 0;
		walking = new boolean[] {false, false};
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
				if (velY >= 19) health -= velY / 2;
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
		
		if (digDir != null && System.currentTimeMillis() - digTimer > 1000) {
			if (digState < 4) {
				digState++;
				digTimer = System.currentTimeMillis();
			}
			if (digState == 4) {
				grid.digBlock(digPos.y / GameGrid.BLOCK_SIZE, digPos.x / GameGrid.BLOCK_SIZE);
				stopDigging();
			}
		}
	}
	
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
	
	public void jump() {
		if (COLLISIONS.isStandingOnBlock()) velY -= 13;
	}
	
	public void placeSelectedBlock(Direction dir) {
		if (inventory[selectedSlot] != null && COLLISIONS.isBlockPlaceAllowed(dir))
			
			switch (dir) {
			case UP: 	grid.setBlock(0, getRow() - 1, getCol(), inventory[selectedSlot]); break;
			case DOWN: 	grid.setBlock(0, getRow() + 1, getCol(), inventory[selectedSlot]); break;
			case LEFT: 	grid.setBlock(0, getRow(), getCol() - 1, inventory[selectedSlot]); break;
			case RIGHT: grid.setBlock(0, getRow(), getCol() + 1, inventory[selectedSlot]); break;
			}
	}
	
	private void moveOnePx(Direction dir) {
		switch (dir) {
		case LEFT: x -= 1; return;
		case RIGHT: x += 1; return;
		case UP: y -= 1; return;
		case DOWN: y += 1; return;
		}
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
	
	public BlockType[] getInventory() {
		return this.inventory;
	}
	
	public int getSelectedSlot() {
		return this.selectedSlot;
	}
	
	public void selectSlot(int slot) {
		this.selectedSlot = slot;
	}
}
