package de.nmo.eclipse.ui.filterflyoutpalette.internal;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class FilterLabel extends Label {

  /**
   * @param string
   *
   * @author TJNKXAO
   * @since 07.11.2016
   */
  public FilterLabel(String string) {
    super(string, PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_SYNCED));
    setBorder(new LineBorder(new Color(Display.getCurrent(), 204, 230, 255)));
  }

}