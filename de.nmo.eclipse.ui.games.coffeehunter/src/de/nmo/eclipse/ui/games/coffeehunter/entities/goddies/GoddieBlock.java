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
 * @since 28.07.2017
 */
public class GoddieBlock extends Enemy {

  private BufferedImage[] sprites;

  boolean                 active = true;

  Goodie                  goodie = null;

  /**
   * @param tm
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 28.07.2017
   */
  public GoddieBlock(TileMap tm) {
    super(tm);
    this.moveSpeed = 0;
    this.maxSpeed = 0;
    this.fallSpeed = 00;

    this.maxFallSpeed = 0;
    this.width = 30;
    this.height = 30;
    this.cheight = 20;
    this.cwidth = 20;
    this.health = this.maxHealth = 50;
    this.damage = 0;

    try {
      BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Goddies/questionblock.gif"));
      this.sprites = new BufferedImage[2];
      for (int i = 0; i < this.sprites.length; i++) {
        this.sprites[i] = spritesheet.getSubimage(i * this.width, 0, this.width, this.height);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.animation = new Animation();
    this.animation.setFrames(this.sprites);
    this.animation.setDelay(1000);

    this.left = true;
    this.facingRight = true;
  }

  public GoddieBlock(TileMap tm, int x, int y) {
    this(tm);
    setPosition(x, y);
  }

  public GoddieBlock(TileMap tm, int x, int y, Goodie good) {
    this(tm);
    setPosition(x, y);
    this.goodie = good;
    this.goodie.setPosition(x, y);
    this.goodie.startY = y;
  }

  @Override
  public void update() {
    if (this.goodie != null) {
      if (this.goodie.isConsumed()) {
        this.goodie = null;
      } else {
        this.goodie.update();
      }
    }
  }

  @Override
  public void draw(Graphics2D g) {
    setMapPosition();
    g.drawImage(this.sprites[this.active ? 0 : 1], (int) (this.x + this.xmap - this.width),
        (int) (this.y + this.ymap - this.height), null);
    if (this.goodie != null) {
      this.goodie.draw(g);
    }
    debugDraw(g);
  }

  @Override
  public Rectangle getRectangle() {
    int xoffset = -5;
    int yoffset = 0;
    return new Rectangle((int) x - cwidth + xoffset, (int) y - cheight + yoffset, cwidth, cheight);
  }

  @Override
  public void intersectEvent(Player p) {
    if (this.active) {
      int maxhealth = p.getMaxHealth();
      double[] vector = p.getVector();
      if (vector[1] > 0 && vector[0] == 0) {
        this.active = false;
        this.goodie.start();
      }
    } else {
      //  this.goodie.setPosition(this.x, this.y);
    }
  }

  @Override
  public double[] checkPlayerCollision(Player p) {
    double[] v = p.getVector();
    double y = v[1];
    double x = v[0];

    return new double[] { p.getx(), p.gety() + y };
  }

  public Goodie getGoodie() {
    return this.goodie;
  }

}
