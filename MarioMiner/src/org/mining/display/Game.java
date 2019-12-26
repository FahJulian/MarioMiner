package org.mining.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.mining.Clock;
import org.mining.managers.IngameKeyHandler;
import org.mining.managers.MenuKeyHandler;

public class Game extends javax.swing.JPanel{

	/** The Serial Version UID */
	private static final long serialVersionUID = 5905864191282833136L;
	
	public static final int CONTENT_WIDTH = 960 / GameGrid.BLOCK_SIZE * GameGrid.BLOCK_SIZE;
	public static final int CONTENT_HEIGHT = 512 / GameGrid.BLOCK_SIZE * GameGrid.BLOCK_SIZE;
	
	public static final int CONTENT_CENTER_X = CONTENT_WIDTH / 2;
	public static final int CONTENT_CENTER_Y = CONTENT_HEIGHT / 2;
	
	/** The central x coordinate of the grid */
	public static int CENTER_X = CONTENT_WIDTH / 2;	
	/** The central y coordinate of the grid */
	public static int CENTER_Y = CONTENT_HEIGHT / 2;
	
	public static int WIDTH, HEIGHT;
	
	/** The amount of frames per second */
	private static final int FPS = 60;
	
	/** The number of milliseconds per frame */
	private static final long FRAME_TIME = 1000L / FPS;
	
	/** The window the game is nested in */
	private Window window;
	
	/** The game grid keeping track of and all tiles */
	public GameGrid grid;
	/** The HUD showing health, tool-bar, inventory etc */
	private HUD hud;
	
	/** Whether or not the game is paused */
	private GameState state;
	
	/** The clock that handles all the update logic */
	private Clock logicTimer;
	
	/** The larger font to draw */
	static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);
	/** The smaller font to draw */
	static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 16);
	
	public Game() {
		grid = new GameGrid(this);
		hud = new HUD(this);
		
		// Initialize the Window,
		// then resize the Window to match wanted content size
		window = new Window(CONTENT_WIDTH, CONTENT_HEIGHT, "MarioMiner", this);
		int extraWidth = CONTENT_WIDTH - window.getContentPane().getSize().width;
		int extraHeight = CONTENT_HEIGHT - window.getContentPane().getSize().height;
		window.setSize(CONTENT_WIDTH + extraWidth, CONTENT_HEIGHT + extraHeight);
		
		CENTER_X = (CONTENT_WIDTH + extraWidth) / 2;
		CENTER_Y = (CONTENT_HEIGHT + extraHeight) / 2;
		WIDTH = CONTENT_WIDTH + extraWidth;
		HEIGHT = CONTENT_HEIGHT + extraHeight;
		
		addKeyListener(new MenuKeyHandler(this));
		addKeyListener(new IngameKeyHandler(this, grid, grid.player));
		
		this.requestFocus();
	}
	
	/**
	 * Starts the game running. Initializes everything and enters loop
	 */
	public void start() {
		/* Initialize any uninitialized game variables */
		this.state = GameState.NEW_GAME;
		this.hud.reset();
		this.grid.reset();
		
		/* Set up the timer to keep the game from running before the user
		 * presses enter to start it (KeyInput handles that)
		 */
		this.logicTimer = new Clock(FPS);
		logicTimer.setPaused(true);
		
		/* Run the game loop */
		run();
	}
	
	private void run() {
		while(true) {
			long start = System.nanoTime();
			
			// Update the timer
			logicTimer.update();
			
			// If a tick has elapsed in the timer, the game can be updated
			if (logicTimer.hasElapsedTick())
				tick();
			
			// TODO: Do some other stuff if necessary
			
			// Render the game to the screen
			render();
			
			// Sleep to cap the frame-rate
			long delta = (System.nanoTime() - start) / 1000000L;
			if (delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void tick() {
		if (grid.player.getHealth() <= 0)
			gameover();
		grid.tick();
		hud.tick();
	}
	
	public void render() {
		repaint();
	}
	
	/**
	 * Resets all game variables to their default values. Should be used when
	 * game is restarted or loaded from a file
	 */
	public void reset() {
		logicTimer.reset();
		grid.reset();
		hud.reset();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw Background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, CONTENT_WIDTH, CONTENT_HEIGHT);
		
		if (state == GameState.NEW_GAME || state == GameState.GAME_OVER) {
			String msg1 = (state == GameState.NEW_GAME) ?
					"MarioMiner" : "Game Over!";
			String msg2 = (state == GameState.NEW_GAME) ?
					"Press ENTER to play!" : "Press ENTER to play again!";
			
			g.setColor(Color.WHITE);
			g.setFont(LARGE_FONT);
			g.drawString(msg1, CENTER_X - g.getFontMetrics().stringWidth(msg1) / 2,  150);
			
			g.setFont(SMALL_FONT);
			g.drawString(msg2, CENTER_X - g.getFontMetrics().stringWidth(msg2) / 2, CENTER_Y);
		}
		
		else {
		grid.render(g);
		hud.render(g);
		}
	}
	
	public void gameover() {
		state = GameState.GAME_OVER;
		logicTimer.setPaused(true);
	}
	
	public void pause() {
		state = GameState.PAUSED;
		logicTimer.setPaused(true);
	}
	
	public void resume() {
		this.state = GameState.INGAME;
		this.logicTimer.setPaused(false);
	}
	
	public GameState getGameState() {
		return this.state;
	}
}
