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
package de.nmo.eclipse.ui.games.coffeehunter.entities.players;

import java.io.IOException;

import javax.imageio.ImageIO;

import de.nmo.eclipse.ui.games.coffeehunter.entities.PlayerStats;

/**
 * 
 *
 * @author Bergen, Marco (I-EA-25, extern)
 * @since 16.07.2017
 */
public class PlayerTinyDragon extends PlayerStats {

  public PlayerTinyDragon() {
    width = 30;
    height = 30;
    cwidth = 20;
    cheight = 20;

    moveSpeed = 0.3;
    maxSpeed = 1.6;
    stopSpeed = 0.4;
    fallSpeed = 0.15;
    maxFallSpeed = 4.0;
    jumpStart = -4.8;
    stopJumpSpeed = 0.3;

    health = maxHealth = 5;
    fire = maxFire = 2500;

    fireCost = 200;
    fireBallDamage = 5;
    //fireBalls = new ArrayList<FireBall>();

    scratchDamage = 8;
    scratchRange = 40;

    numFrames = new int[] { 2, 8, 1, 2, 4, 2, 5 };

    try {
      image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
