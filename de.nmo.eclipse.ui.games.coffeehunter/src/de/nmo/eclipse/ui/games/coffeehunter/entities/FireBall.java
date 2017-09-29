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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

public class FireBall extends MapObject {
  private boolean         hit;
  private boolean         remove;

  private BufferedImage[] sprites;
  private BufferedImage[] hitSprites;

  public FireBall(TileMap tm, boolean right) {
    super(tm);
    moveSpeed = 3.8;
    dx = right ? moveSpeed : -moveSpeed;

    facingRight = right;

    height = 30;
    width = 30;
    cheight = 14;
    cwidth = 14;

    try {
      BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fireball.gif"));
      sprites = new BufferedImage[4];
      for (int i = 0; i < sprites.length; i++) {
        sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
      }
      hitSprites = new BufferedImage[3];
      for (int i = 0; i < hitSprites.length; i++) {
        hitSprites[i] = spritesheet.getSubimage(i * width, height, width, height);
      }


    } catch (Exception e) {
      e.printStackTrace();
    }
    animation = new Animation();
    animation.setFrames(sprites);
    animation.setDelay(70);
  }

  public void setHit() {
    if (hit) {
      return;
    }
    hit = true;
    animation.setFrames(hitSprites);
    animation.setDelay(70);
    dx = 0;
  }

  public boolean shouldRemove() {
    return remove;
  }

  public void update() {
    checkTileMapCollision();
    setPosition(xtemp, ytemp);

    if (dx == 0 && !hit) {
      setHit();
    }

    animation.update();

    if (hit && animation.hasPlayedOnce()) {
      remove = true;
    }
  }

  public void draw(Graphics2D g) {
    setMapPosition();

    super.draw(g);
  }

}
