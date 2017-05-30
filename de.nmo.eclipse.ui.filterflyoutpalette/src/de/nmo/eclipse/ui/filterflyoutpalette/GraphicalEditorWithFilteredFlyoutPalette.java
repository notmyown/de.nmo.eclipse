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
package de.nmo.eclipse.ui.filterflyoutpalette;

import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;

import de.nmo.eclipse.ui.filterflyoutpalette.internal.FilterPaletteViewerProvider;

/**
 * Editor with FlyoutPalette and Filter field
 */
public abstract class GraphicalEditorWithFilteredFlyoutPalette extends GraphicalEditorWithFlyoutPalette {

  @Override
  protected PaletteViewerProvider createPaletteViewerProvider() {
    return new FilterPaletteViewerProvider(this, getEditDomain());
  }

}
