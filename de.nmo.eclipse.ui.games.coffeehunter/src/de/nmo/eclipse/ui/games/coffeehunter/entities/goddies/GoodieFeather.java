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
package de.nmo.eclipse.ui.games.coffeehunter.entities.goddies;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import de.nmo.eclipse.ui.games.coffeehunter.entities.Animation;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Player;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

/**
 * 
 *
 * @author TJNKXAO
 * @since 28.08.2017
 */
public class GoodieFeather extends Goodie {

  public GoodieFeather(TileMap tm) {
    super(tm);
    this.moveSpeed = 0.5;
    this.maxSpeed = 0.5;
    this.fallSpeed = 0.2;

    this.maxFallSpeed = 10.0;
    this.width = 16;
    this.height = 16;
    this.cheight = 16;
    this.cwidth = 16;

    try {
      BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Goddies/feather.gif"));
      this.sprites = new BufferedImage[2];
      for (int i = 0; i < this.sprites.length; i++) {
        this.sprites[i] = spritesheet.getSubimage(i * this.width, 0, this.width, this.height);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.animation = new Animation();
    this.animation.setFrames(this.sprites);
    this.animation.setDelay(200);

    this.right = true;
  }

  @Override
  protected void start() {
    this.started = true;

  }

  protected void getNextPosition() {
    if (this.started) {
      if (this.currentinit) {
        this.dy -= 1;
        return;
      }
      super.getNextPosition();
      // falling
      if (this.falling) {
        this.dy += this.fallSpeed;
        if (this.dy > this.maxFallSpeed) {
          this.dy = this.maxFallSpeed;
        }
      }
    }
  }

  public void checkTileMapCollision() {
    if (this.currentinit && this.y > this.startY - 40) {
      this.currCol = (int) this.x / this.tileSize;
      this.currRow = (int) this.y / this.tileSize;

      this.xdest = this.x + this.dx;
      this.ydest = this.y + this.dy;

      this.xtemp = this.x;
      this.ytemp = this.y;
      //calculateCorners(this.x, this.ydest);
      if (this.dy < 0) {
        this.ytemp += this.dy;
        this.dy = 0;
      }
    } else {
      this.currentinit = false;
      super.checkTileMapCollision();
    }
  }

  @Override
  public void update() {
    if (this.started) {
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
  }

  @Override
  public void intersectEvent(Player p) {
    if (this.started && !this.currentinit) {
    p.setGlid(true);
    p.setHealth(p.getHealth() + 1);
    p.hit(1);
    this.consumed = true;
    }
  }

}
