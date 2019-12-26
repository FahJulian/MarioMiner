package org.mining.display;

import java.awt.BorderLayout;

import org.mining.Clock;
import org.mining.managers.IngameKeyHandler;
import org.mining.managers.MenuKeyHandler;

public class Mining extends javax.swing.JFrame{

	/** The Serial Version UID */
	private static final long serialVersionUID = 5905864191282833136L;
	
	/** The amount of frames per second */
	private static final int FPS = 60;
	
	/** The number of milliseconds per frame */
	private static final long FRAME_TIME = 1000L / FPS;
	
	/** The game grid keeping track of and all tiles */
	public GameGrid grid;
	
	/** Whether or not the game is paused */
	private GameState state;
	
	/** The clock that handles all the update logic */
	private Clock logicTimer;
	
	/**
	 * Creates a new instance of the Game. Sets up window's properties,
	 * adds a controller listener
	 */
	public Mining() {
		/* Set basic window properties */
		super("Mining");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		/* TODO: Initialize Panel/Display instances, e.g. HUD and add them
		 * to the window 
		*/
		this.grid = new GameGrid(this);
		add(grid);
		
		addKeyListener(new MenuKeyHandler(this));
		addKeyListener(new IngameKeyHandler(this, grid, grid.player));
		
		/* resize the frame to hold Panel instances, center and show it */
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		requestFocus();
	}
	
	/**
	 * Starts the game running. Initializes everything and enters loop
	 */
	public void start() {
		/* Initialize any uninitialized game variables */
		this.state = GameState.NEW_GAME;
		
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
			
			// Sleep to cap the framerate
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
	
	/**
	 * Updates the game and handles its logic 
	 */
	private void tick() {
		grid.tick();
	}
	
	/** Renders the game to the screen */
	private void render() {
		this.grid.render();
	}
	
	/**
	 * Resets all game variables to their default values. Should be used
	 * when the user restarts the game or loads a saved one or on gameOver
	 */
	public void reset() {
		this.logicTimer.reset();
	}
	
	/** Pauses the game */
	public void pause() {
		this.state = GameState.PAUSED;
		this.logicTimer.setPaused(true);
	}
	
	/** Resumes the game */
	public void resume() {
		this.state = GameState.INGAME;
		this.logicTimer.setPaused(false);
	}
	
	/**
	 * Checks in which state the game is currently in
	 * @return The State version the game is in
	 */
	public GameState getGameState() {
		return this.state;
	}
	
}
