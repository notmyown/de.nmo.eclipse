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

import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

/**
 * 
 *
 * @author TJNKXAO
 * @since  31.08.2017
 */
public class TriggerPoint extends Enemy {

  Trigger trigger;
  /**
   * @param tm
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 19.07.2017
   */
  public TriggerPoint(TileMap tm) {
    super(tm);
    this.moveSpeed = 0;
    this.maxSpeed = 0;
    this.fallSpeed = 0;
    this.maxFallSpeed = 0.0;
    this.width = 20;
    this.height = 20;
    this.cheight = 20;
    this.cwidth = 20;
    this.health = this.maxHealth = Integer.MAX_VALUE;
    this.damage = 0;
  }

  public TriggerPoint(TileMap tm, Trigger t, int x, int y) {
    this(tm);
    setPosition(x, y);
    setStartPosition(x, y);
    this.trigger = t;
  }

  @Override
  public void intersectEvent(Player p) {
    if (this.trigger != null) {
      this.trigger.run(this);
    }
  }

  @Override
  public void update() {
    if (this.animation != null) {
      this.animation.update();
    }
  }

  @Override
  public void draw(Graphics2D g) {
    setMapPosition();
    if (this.animation != null) {
      super.draw(g);
    }
    debugDraw(g);
  }

}
