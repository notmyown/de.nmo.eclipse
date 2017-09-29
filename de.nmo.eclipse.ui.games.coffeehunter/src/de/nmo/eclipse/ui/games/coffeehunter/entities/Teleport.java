/**
 *
 ******************************************************************
 *     Copyright VW AG, Germany     *
 ******************************************************************
 *
 ******************************************************************
 *Administrative Information (automatically filled in by MKS)
 ******************************************************************
 *
 * $ProjectName: $
 * $Author: $
 * $Date: $
 * $Name:  $
 * $ProjectRevision: 1.81 $
 * $Revision: 1.141 $
 * $Source: central.mak $
 ******************************************************************
**/
package de.nmo.eclipse.ui.games.coffeehunter.entities;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

/**
 * 
 *
 * @author TJNKXAO
 * @since  01.09.2017
 */
public class Teleport extends TriggerPoint {

  public Teleport(TileMap tm, Player p, int x, int y, int xto, int yto) {
    super(tm, e -> {
      p.setPosition(xto, yto);
    }, x, y);
    this.width = 40;
    this.height = 40;
    this.cheight = 40;
    this.cwidth = 10;
    BufferedImage[] sprites = new BufferedImage[9];
    try {
      BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Goddies/Teleport.gif"));

      for (int i = 0; i < sprites.length; i++) {
        sprites[i] = spritesheet.getSubimage(i * this.width, 0, this.width, this.height);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.animation = new Animation();
    this.animation.setFrames(sprites);
    this.animation.setDelay(50);

    this.right = true;
  }

  public Rectangle getRectangle() {
    int xoffset = cwidth / 2;
    int yoffset = cheight / 2;
    return new Rectangle((int) x - cwidth + xoffset, (int) y - cheight + yoffset, cwidth, cheight);
  }

}
