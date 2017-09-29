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
import java.awt.image.BufferedImage;

import de.nmo.eclipse.ui.games.coffeehunter.entities.MapObject;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

/**
 * 
 *
 * @author TJNKXAO
 * @since  28.08.2017
 */
abstract public class Goodie extends MapObject {

  protected double          startY;

  protected boolean started = false;
  protected boolean         currentinit = true;

  protected boolean         consumed    = false;

  protected BufferedImage[] sprites;

  /**
   * @param tm
   *
   * @author TJNKXAO
   * @since 28.08.2017
   */
  public Goodie(TileMap tm) {
    super(tm);
  }

  abstract public void update();

  abstract protected void start();

  public void setPosition(double x, double y) {
    super.setPosition(x, y);
  }

  @Override
  protected void getNextPosition() {
    super.getNextPosition();
  }

  @Override
  public void draw(Graphics2D g) {
    if (this.started) {
      setMapPosition();
      super.draw(g);
    }
  }

  public boolean isConsumed() {
    return consumed;
  }

}
