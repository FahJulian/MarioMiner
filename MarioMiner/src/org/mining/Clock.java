package org.mining;

public class Clock {
		
	/**
	 * Number of milliseconds that make up one tick
	 */
	private float millisPerTick;
	
	/**
	 * The last time the clock was updated (used for calculating delta time)
	 */
	private long lastUpdate;
	
	/**
	 * The number of ticks that have elapsed and have not been requested
	 */
	private int elapsedTicks;
	
	/**
	 * Amount of excess time before the next tick
	 */
	private float excessTime;
	
	/**
	 * Whether or not the clock is paused
	 */
	private boolean isPaused;
	
	
	/**
	 * Creates a new clock and sets it's ticks per second
	 * @param ticksPerSecond The number of ticks that elapse per second
	 */
	public Clock(float ticksPerSecond) {
		setTicksPerSecond(ticksPerSecond);
		reset();
	}
	
	/**
	 * Sets the number of cycles that elapse per second
	 * @param cyclesPerSecond The number fo cycles per second
	 */
	public void setTicksPerSecond(float ticksPerSecond) {
		this.millisPerTick = (1.0f / ticksPerSecond) * 1000;
	}
	
	/**
	 * Resets the clocks stats. Elapsed Ticks and time excess will be reset
	 * to 0, lastUpdate will be set to current time, isPaused will be set false
	 */
	public void reset() {
		this.elapsedTicks = 0;
		this.excessTime = 0.0f;
		this.lastUpdate = getCurrentTime();
		this.isPaused = false;
	}
	
	/**
	 * Updates the clocks stats. If the clock is not paused, excess time and 
	 * elapsed ticks will be calculated. SHOULD BE CALLED EVERY TICK! to avoid 
	 * nasty stuff with the delta time.
	 */
	public void update() {
		// Get the current time and calculate the delta time
		long currUpdate = getCurrentTime();
		float delta = (float)(currUpdate - lastUpdate) + excessTime;
		
		// Update the number of elapsed Ticks and excess time if not paused
		if (!isPaused) {
			this.elapsedTicks += (int)Math.floor(delta / millisPerTick);
			this.excessTime = delta % millisPerTick;
		}
	}
	
	/**
	 * Paused or unpauses the clock. While paused, elapsed ticks and time excess
	 * will not be updated, even if the {@code update} method should be called
	 * every frame anyways to prevent issues.
	 * @param paused Whether or not to pause the clock
	 */
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	/**
	 * Checks to see if the clock is currently paused
	 * @return Whether or not this clock is currently paused
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Checks if a tick has elapsed for this clock. If so, the number of
	 * elapsed ticks will be decremented by one
	 * @return Whether or not a tick has elapsed
	 * @see peekElapsedTick
	 */
	public boolean hasElapsedTick() {
		if (elapsedTicks > 0) {
			this.elapsedTicks--;
			return true;
		}else return false;
	}
	
	/**
	 * Checks if a tick has elapsed for this clock. Unlike 
	 * {@code hasElapsedTick}, the number of ticks will not be decremented.
	 * @return Whether or not a cycle has elapsed
	 * @see hasElapsedTick
	 */
	public boolean peekElapsedTick() {
		return (elapsedTicks > 0);
	}
	
	/**
	 * Calculates the current time in milliseconds using the computer's high
	 * resolution clock. Much more reliable than 
	 * {@code System.currentTimeMillis()}, and faster than 
	 * {@code System.nanoTime()}
	 * @return The current time in milliseconds
	 */
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);
	}
}
