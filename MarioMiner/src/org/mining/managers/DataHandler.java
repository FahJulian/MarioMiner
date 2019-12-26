package org.mining.managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The {@code DataHandler} class handles all loading and writing of extern
 * files/images/...
 * @author julian
 */
public class DataHandler { 

	/**
	 * Loads an image from a given sourcepath - Care: if sourcepath is not
	 * found, returns null
	 * @param src The sourcepath of the image
	 * @return BufferedImage from the sourcepath, if path not found: null
	 */
	public static BufferedImage loadImage(String src) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(src));
		} catch (IOException e) {
			System.err.println("Image could not be loaded from " + src);
		}
		return img;
	}
}
