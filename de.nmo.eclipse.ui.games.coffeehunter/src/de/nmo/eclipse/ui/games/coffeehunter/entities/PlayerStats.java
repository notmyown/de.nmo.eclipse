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

import java.awt.image.BufferedImage;

/**
 * 
 *
 * @author Bergen, Marco (I-EA-25, extern)
 * @since  16.07.2017
 */
public abstract class PlayerStats {

  //player stuff
  protected int    health;
  protected int    maxHealth;
  protected int    fire;
  protected int    maxFire;

  protected int    fireCost;
  protected int    fireBallDamage;

  protected int    scratchDamage;
  protected int    scratchRange;

  // movement attributes
  protected double moveSpeed;
  protected double maxSpeed;
  protected double stopSpeed;
  protected double fallSpeed;
  protected double maxFallSpeed;
  protected double jumpStart;
  protected double stopJumpSpeed;

  protected BufferedImage image;

  // dimensions
  protected int    width;
  protected int    height;

  // collision box
  protected int    cwidth;
  protected int    cheight;
  protected int[]         numFrames;

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getCWidth() {
    return cwidth;
  }

  public int getCHeight() {
    return cheight;
  }

  public double getMoveSpeed() {
    return moveSpeed;
  }

  public double getMaxSpeed() {
    return maxSpeed;
  }

  public double getStopSpeed() {
    return stopSpeed;
  }

  public double getFallSpeed() {
    return fallSpeed;
  }

  public double getMaxFallSpeed() {
    return maxFallSpeed;
  }

  public double getJumpStart() {
    return jumpStart;
  }

  public double getStopJumpSpeed() {
    return stopJumpSpeed;
  }

  public int getHealth() {
    return health;
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public int getFire() {
    return fire;
  }

  public int getMaxFire() {
    return maxFire;
  }

  public int getFireCost() {
    return fireCost;
  }

  public int getFireBallDamage() {
    return fireBallDamage;
  }

  public int getScratchDamage() {
    return scratchDamage;
  }

  public int getScratchRange() {
    return scratchRange;
  }

  public BufferedImage getImage() {
    return image;
  }

  public int[] getNumFrames() {
    return numFrames;
  }

}
