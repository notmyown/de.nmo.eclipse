package de.nmo.eclipse.ui.games.tilemapeditor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Block {
	
	BufferedImage image;
	int x;
	int y;
	
	public Block(BufferedImage b) {
		image = b;
	}
	
	public void setPosition(int i1, int i2) {
		x = i1;
		y = i2;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, x, y, null);
	}
	
}