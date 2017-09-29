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
package de.nmo.eclipse.ui.games.coffeehunter.entities.enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import de.nmo.eclipse.ui.games.coffeehunter.entities.Animation;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Enemy;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

/**
 * 
 *
 * @author Bergen, Marco (I-EA-25, extern)
 * @since 19.07.2017
 */
public class EnemyDebugger extends Enemy {

  private BufferedImage[] sprites;

  /**
   * @param tm
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 19.07.2017
   */
  public EnemyDebugger(TileMap tm) {
    super(tm);
    this.moveSpeed = 0.3;
    this.maxSpeed = 0.3;
    this.fallSpeed = 0.2;

    this.maxFallSpeed = 10.0;
    this.width = 30;
    this.height = 30;
    this.cheight = 20;
    this.cwidth = 20;
    this.health = this.maxHealth = 2;
    this.damage = 1;

    try {
      BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/debugger.png"));
      this.sprites = new BufferedImage[3];
      for (int i = 0; i < this.sprites.length; i++) {
        this.sprites[i] = spritesheet.getSubimage(i * this.width, 0, this.width, this.height);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.animation = new Animation();
    this.animation.setFrames(this.sprites);
    this.animation.setDelay(100);

    this.right = true;
  }

  public EnemyDebugger(TileMap tm, int x, int y) {
    this(tm);
    setPosition(x, y);
    setStartPosition(x, y);
  }

  public EnemyDebugger(TileMap tm, int x, int y, int range) {
    this(tm, x, y);
    this.setRange(range);
  }

  protected void getNextPosition() {
    super.getNextPosition();
    if (this.falling) {
      this.dy += this.fallSpeed;
    }
  }

  public Rectangle getRectangle() {
    int xoffset = cwidth / 4;
    int yoffset = cheight / 2;
    return new Rectangle((int) x - cwidth + xoffset, (int) y - cheight + yoffset, cwidth, cheight);
  }

  public void update() {
    getNextPosition();
    checkTileMapCollision();
    setPosition(this.xtemp, this.ytemp);

    if (this.flinching) {
      long elapsed = (System.nanoTime() - this.flinchTimer) / 1000000;
      if (elapsed > 4000) {
        this.flinching = false;
      }
    }

    if (this.right && this.dx == 0) {
      this.right = false;
      this.left = true;
      facingRight = true;
    }
    else if (this.left && this.dx == 0) {
      this.left = false;
      this.right = true;
      facingRight = false;
    }

    this.animation.update();

  }

  public void draw(Graphics2D g) {
    setMapPosition();
    super.draw(g);
  }

}
