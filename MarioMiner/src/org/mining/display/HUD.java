package org.mining.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class HUD{
	
	/** The size of the tool-bar squares */
	private static final int tbItemSize = 40;
	/** The total width of the tool-bar */
	private static final int tbTotalWidth = 9 * tbItemSize + 8 * tbItemSize / 2;
	/** The position the tool-bars top left corner is */
	private static int tbX, tbY;
	
	/** The Game instance the HUD is part of */
	private Game game;
	
	public HUD(Game game) {
		this.game = game;
	}
	
	public void tick() {
		
	}

	public void render(Graphics g) {
		renderHealthbar(g);
		renderToolbar(g);
	}
	
	public void reset() {
		tbX = Game.CENTER_X - tbTotalWidth / 2;
		tbY = Game.HEIGHT - tbItemSize - 50;
	}
	
	private void renderHealthbar(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(20, 20, 120, 20);
		
		g.setColor(Color.RED);
		g.fillRect(20, 20, (int) (1.2f * game.grid.player.getHealth()), 20);
		
		g.setColor(Color.BLACK);
		g.drawRect(20, 20, 120, 20);
	}
	
	private void renderToolbar(Graphics g) {
		for (int i = 0; i < 9; i++) {
			g.setColor(Color.BLACK);
			g.fillRect(tbX + i * 60, tbY, tbItemSize, tbItemSize);
			
			g.setColor(Color.LIGHT_GRAY);
			if (game.grid.player.getSelectedSlot() == i)
				g.fillRect(tbX + i * 60 + 4, tbY + 4, tbItemSize - 8, tbItemSize - 8);
			else
				g.fillRect(tbX + i * 60 + 2, tbY + 2, tbItemSize - 4, tbItemSize - 4);
			
			if (game.grid.player.getInventory()[i] != null) {
				BufferedImage icon = game.grid.player.getInventory()[i].getTBIcon();
				g.drawImage(icon, tbX + i * 60 + 4, tbY + 4, null);
				
				// Draw amount of items in the stack
				g.setColor(Color.WHITE);
				g.setFont(Game.SMALL_FONT);
				String amountString = String.valueOf(game.grid.player.getInventoryAmounts()[i]);
				
				g.drawString(amountString, 
						tbX + i * 60 + tbItemSize - g.getFontMetrics().stringWidth(amountString) - 7,
						tbY + tbItemSize - 7);
			}
		}
	}
}
