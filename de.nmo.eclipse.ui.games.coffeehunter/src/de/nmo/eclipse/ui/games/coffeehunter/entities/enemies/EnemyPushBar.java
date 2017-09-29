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
import de.nmo.eclipse.ui.games.coffeehunter.entities.Player;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

/**
 * 
 *
 * @author Bergen, Marco (I-EA-25, extern)
 * @since 19.07.2017
 */
public class EnemyPushBar extends Enemy {

  private BufferedImage[] sprites;

  /**
   * @param tm
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 19.07.2017
   */
  public EnemyPushBar(TileMap tm) {
    super(tm);
    this.moveSpeed = 0;
    this.maxSpeed = 0;
    this.fallSpeed = 0;

    this.maxFallSpeed = 0.0;
    this.width = 60;
    this.height = 30;
    this.cwidth = 30;
    this.cheight = 30;
    this.health = this.maxHealth = Integer.MAX_VALUE;
    this.damage = 2;
    this.facingRight = true;

    try {
      BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/pushbar.png"));
      this.sprites = new BufferedImage[4];
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

  public EnemyPushBar(TileMap tm, boolean fr, int x, int y) {
    this(tm);
    setPosition(x, y);
    setStartPosition(x, y);
    this.facingRight = fr;
  }

  @Override
  protected void getNextPosition() {
    super.getNextPosition();
  }

  @Override
  public Rectangle getRectangle() {
    int theight = this.cheight;
    int twidth = this.cwidth;
    int offset = 0;
    switch (this.animation.getFrame()) {
      case 3: {
        theight = 30;
        twidth = 30;
        break;
      }
      //$FALL-THROUGH$
      default: {
        theight = 0;
        twidth = 0;
        break;
      }
    }

    return new Rectangle((int) this.x - this.cwidth + offset, (int) this.y - this.cheight, twidth, theight);
  }

  @Override
  public void update() {
    getNextPosition();
    checkTileMapCollision();
    if (this.animation.getFrame() == 3) {
      this.animation.setDelay(1000);
    } else {
      this.animation.setDelay(500);
    }
    this.animation.update();

  }

  @Override
  public void draw(Graphics2D g) {
    setMapPosition();
    if (this.facingRight) {
      g.drawImage(this.animation.getImage(), (int) (this.x + this.xmap - this.width),
          (int) (this.y + this.ymap - this.height), null);
    } else {
      g.drawImage(this.animation.getImage(), (int) (this.x + this.xmap - this.width / 2 + this.width),
          (int) (this.y + this.ymap - this.height), -this.width, this.height, null);
    }
    debugDraw(g);
    //super.draw(g);
  }

  @Override
  public void intersectEvent(Player p) {
    if (this.facingRight) {
      p.setPosition(p.getx() + 1, p.gety());
    } else {
      p.setPosition(p.getx() - 1, p.gety());
    }
  }

}
