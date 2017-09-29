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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * 
 *
 * @author Bergen, Marco (I-EA-25, extern)
 * @since  19.07.2017
 */
public class HUD {

  private Player        player;
  private BufferedImage image;
  private Font          font;

  public HUD(Player p) {
    this.player = p;
    try {
      image = ImageIO.read(getClass().getResourceAsStream("/HUD/hud.png"));
      font = new Font("Arial", Font.PLAIN, 10);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void draw(Graphics2D g) {
    g.drawImage(image, 200, 3, null);
    g.setFont(font);
    g.drawString(player.getHealth() + " / " + player.getMaxHealth(), 265, 13);
    g.drawString(player.getFire() / 100 + " / " + player.getMaxFire() / 100, 265, 23);
  }

}
