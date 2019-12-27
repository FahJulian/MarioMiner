package org.mining.game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.mining.display.GameGrid;
import org.mining.managers.DataHandler;

public abstract class GridObject {
	protected int size, x, y, velX, velY;
	public GameGrid grid;
	protected BufferedImage img;
	
	public GridObject(int x, int y, int size, GameGrid grid) {
		this.size = size;
		this.x = x;
		this.y = y;
		this.velX = 0;
		this.velY = 0;
		this.grid = grid;
	}
	
	public GridObject(int x, int y, int size, GameGrid grid, String img_path) {	
		this.size = size;
		this.x = x;
		this.y = y;
		this.velX = 0;
		this.velY = 0;
		this.grid = grid;
		setImg(img_path);
	}
	
	public abstract void tick();
	
	public void render(Graphics g) {
		g.drawImage(img, x, y, null);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, size, size);
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getRow() {
		return y / GameGrid.BLOCK_SIZE;
	}
	public int getCol() {
		return x / GameGrid.BLOCK_SIZE;
	}
	public Point getCenter() {
		return new Point(getCenterX(), getCenterY());
	}
	public int getCenterX() {
		return this.x + this.size / 2;
	}
	public int getCenterY() {
		return this.y + this.size / 2;
	}
	public int getCenterRow() {
		return getCenterY() / GameGrid.BLOCK_SIZE;
	}
	public int getCenterCol() {
		return getCenterX() / GameGrid.BLOCK_SIZE;
	}
	public int getRightCol() {
		return (getX() + getSize()) / GameGrid.BLOCK_SIZE;
	}
	public int getBottomRow() {
		return (getY() + getSize()) / GameGrid.BLOCK_SIZE;
	}
	public Point getPos() {
		return new Point(x, y);
	}
	
	public int getVelX() {
		return velX;
	}
	public void setVelX(int velX) {
		this.velX = velX;
	}
	public int getVelY() {
		return velY;
	}
	public void setVelY(int velY) {
		this.velY = velY;
	}
	
	public BufferedImage getImg() {
		return img;
	}
	public void setImg(String img_path) {
		this.img = DataHandler.loadImage(img_path);
	}
}
