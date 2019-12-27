package org.mining.game;

import java.awt.image.BufferedImage;

/**
 * Specifies Objects that can be held in the placers hand
 * and can therefore be in an inventory slot
 * @author julian
 *
 */
public interface Holdable {
	public abstract BufferedImage getTBIcon();
}
