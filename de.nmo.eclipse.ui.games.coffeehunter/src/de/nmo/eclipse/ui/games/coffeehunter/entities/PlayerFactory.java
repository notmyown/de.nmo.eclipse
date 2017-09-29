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

import de.nmo.eclipse.ui.games.coffeehunter.entities.players.PlayerOne;
import de.nmo.eclipse.ui.games.coffeehunter.entities.players.PlayerTinyDragon;

/**
 * 
 *
 * @author Bergen, Marco (I-EA-25, extern)
 * @since  16.07.2017
 */
public class PlayerFactory {

  public static PlayerStats getPlayer(String id) {

    if (id == "playerone") {
      return new PlayerOne();
    }
    return new PlayerTinyDragon();
  }

}
