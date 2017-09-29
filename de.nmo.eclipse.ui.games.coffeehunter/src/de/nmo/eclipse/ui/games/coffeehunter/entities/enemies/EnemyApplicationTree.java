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
public class EnemyApplicationTree extends Enemy {

  private BufferedImage[] sprites;

  /**
   * @param tm
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 19.07.2017
   */
  public EnemyApplicationTree(TileMap tm) {
    super(tm);
    this.moveSpeed = 0;
    this.maxSpeed = 0;
    this.fallSpeed = 0;

    this.maxFallSpeed = 0.0;
    this.width = 120;
    this.height = 90;
    this.cwidth = 120;
    this.cheight = 90;
    this.health = this.maxHealth = Integer.MAX_VALUE;
    this.damage = 2;
    this.facingRight = true;

    try {
      BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/applicationtree.png"));
      this.sprites = new BufferedImage[4];
      for (int i = 0; i < this.sprites.length; i++) {
        this.sprites[i] = spritesheet.getSubimage(i * this.width, 0, this.width, this.height);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.animation = new Animation();
    this.animation.setFrames(this.sprites);
    this.animation.setDelay(1000);

    this.right = true;
  }

  public EnemyApplicationTree(TileMap tm, int x, int y) {
    this(tm);
    setPosition(x, y);
    setStartPosition(x, y);
  }

  public EnemyApplicationTree(TileMap tm, int x, int y, int range, boolean fD) {
    this(tm, x, y);
    this.setRange(range);
    this.facingDown = fD;
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
      case 1:
      case 3: {
        theight = 36;
        break;
      }
      case 2: {
        theight = 18;
        break;
      }
      case 0: {
        offset = 40;
        twidth = 80;
      }
        //$FALL-THROUGH$
      default: {
        theight = this.cheight;
        break;
      }
    }

    return new Rectangle((int) this.x - this.cwidth + offset, (int) this.y - this.cheight, twidth, theight);
  }

  @Override
  public void update() {
    getNextPosition();
    checkTileMapCollision();

    this.animation.update();

  }

  @Override
  public void draw(Graphics2D g) {
    setMapPosition();

    g.drawImage(this.animation.getImage(), (int) (this.x + this.xmap - this.width),
        (int) (this.y + this.ymap - this.height), null);

    debugDraw(g);
    //super.draw(g);
  }

}
