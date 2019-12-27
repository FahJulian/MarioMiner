package org.mining.managers;

import org.mining.display.GameGrid;
import org.mining.game.Block;
import org.mining.game.BlockType;

public class MapBuilder {
	
	public static Block[][][] buildFlatMap(GameGrid grid, int rows, int cols, int[] intervals, BlockType[] types, int[] layer2Intervals, BlockType[] layer2Types){
		if (intervals[intervals.length - 1] != rows)
			throw new IllegalArgumentException("Last index of intervals must be equal to rows!");
		if (layer2Intervals[layer2Intervals.length - 1] != rows)
			throw new IllegalArgumentException("Last index of layer2Intervals must be equal to rows!");
		
		Block[][][] blocks = new Block[2][rows][cols];
		for (int i = 0; i < intervals.length; i++) {
			int start = (i == 0) ? 0 : intervals[i - 1];
			
			for (int row = start; row < intervals[i]; row++) {
				for(int col = 0; col < blocks[0][row].length; col++)
					blocks[0][row][col] = new Block(types[i], row, col, grid);
			}
		}
		for (int i = 0; i < layer2Intervals.length; i++) {
			int start = (i == 0) ? 0 : layer2Intervals[i - 1];
			
			for (int row = start; row < layer2Intervals[i]; row++) {
				for(int col = 0; col < blocks[0][row].length; col++)
					blocks[1][row][col] = new Block(layer2Types[i], row, col, grid);
			}
		}
		
		return blocks;
	}
}	
