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
public class EnemySaw extends Enemy {

  private BufferedImage[] sprites;

  /**
   * @param tm
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 19.07.2017
   */
  public EnemySaw(TileMap tm) {
    super(tm);
    this.moveSpeed = 0.8;
    this.maxSpeed = 1;
    this.fallSpeed = 0;

    this.maxFallSpeed = 0.0;
    this.width = 32;
    this.height = 16;
    this.cheight = 20;
    this.cwidth = 32;
    this.health = this.maxHealth = Integer.MAX_VALUE;
    this.damage = 2;

    try {
      BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/saw.png"));
      this.sprites = new BufferedImage[2];
      for (int i = 0; i < this.sprites.length; i++) {
        this.sprites[i] = spritesheet.getSubimage(i * this.width, 0, this.width, this.height);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.animation = new Animation();
    this.animation.setFrames(this.sprites);
    this.animation.setDelay(50);

    this.right = true;
  }

  public EnemySaw(TileMap tm, int x, int y) {
    this(tm);
    setPosition(x, y);
    setStartPosition(x, y);
  }

  public EnemySaw(TileMap tm, int x, int y, int range, boolean fD) {
    this(tm, x, y);
    this.setRange(range);
    this.facingDown = fD;
  }

  protected void getNextPosition() {
    super.getNextPosition();
  }

  @Override
  public Rectangle getRectangle() {
    int theight = this.cheight;
    int twidth = this.cwidth;
    int xoffset = 0;
    int yoffset = 0;
    if (!this.facingDown) {
      xoffset = this.width / 2;
      yoffset = this.height / 2;
    } else {
      if (this.facingRight) {
        xoffset = this.width;
      } else {
        xoffset = this.width / 2;
      }
      yoffset = this.height / 2;
    }

    return new Rectangle((int) this.x - this.cwidth + xoffset, (int) this.y - this.cheight + yoffset, twidth, theight);
  }

  public void update() {
    getNextPosition();
    checkTileMapCollision();
    setPosition(this.xtemp, this.ytemp);

    if (this.right && this.dx == 0) {
      this.right = false;
      this.left = true;
      facingRight = true;
    } else if (this.left && this.dx == 0) {
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
