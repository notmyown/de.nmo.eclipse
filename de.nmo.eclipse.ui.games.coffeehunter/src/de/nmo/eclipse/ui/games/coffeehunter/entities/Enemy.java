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

import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

/**
 * 
 *
 * @author Bergen, Marco (I-EA-25, extern)
 * @since  19.07.2017
 */
public abstract class Enemy extends MapObject {

  protected int     health;
  protected int     maxHealth;
  protected boolean dead;
  protected int     damage;

  protected boolean remove;

  protected long    flinchCount;

  protected boolean flinching;
  protected long    flinchTimer;

  protected double  range = -1;

  protected double  startX;

  private Trigger   finishTrigger;

  /**
   * @param tm
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 19.07.2017
   */
  public Enemy(TileMap tm) {
    super(tm);
  }

  public boolean isDead() {
    return dead;
  }

  public int getDamage() {
    return damage;
  }

  public void hit(int damage) {
    if (dead || flinching) {
      return;
    }
    health -= damage;
    if (health < 0) {
      health = 0;
    }
    if (health == 0) {
      dead = true;
    }
    flinching = true;
    flinchTimer = System.nanoTime();
  }

  abstract public void update();

  public boolean shouldRemove() {
    return remove;
  }

  /**
   * @return
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 28.07.2017
   */
  public double[] checkPlayerCollision(Player p) {
    return null;
  }

  public void setStartPosition(double x, double y) {
    this.startX = x;
  }

  @Override
  protected void getNextPosition() {
    super.getNextPosition();
    if (right && this.range >= 0) {
      if ((this.startX + this.range) < this.x) {
        this.dx = 0;
      }
    } else if (left && range >= 0) {
      if ((this.startX) > this.x) {
        this.dx = 0;
      }
    }
  }

  public void setRange(double r) {
    this.range = r;
  }

  public void setFinishTrigger(Trigger t) {
    this.finishTrigger = t;
  }

  public void finishEvent() {
    if (this.finishTrigger != null) {
      this.finishTrigger.run(this);
    }
  }

}
