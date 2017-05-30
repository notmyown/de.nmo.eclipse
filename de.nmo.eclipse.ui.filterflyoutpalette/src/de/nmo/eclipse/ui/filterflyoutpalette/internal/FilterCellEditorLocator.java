package de.nmo.eclipse.ui.filterflyoutpalette.internal;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

public class FilterCellEditorLocator implements CellEditorLocator {

    private Label nameLabel;

    public FilterCellEditorLocator(Label label) {
      this.nameLabel = label;
    }

    @Override
    public void relocate(CellEditor celleditor) {
      org.eclipse.swt.graphics.Rectangle iconBounds = this.nameLabel.getIcon().getBounds();
      Text text = (Text) celleditor.getControl();
      Point pref = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
      Rectangle rect = this.nameLabel.getBounds().getCopy();
      this.nameLabel.translateToAbsolute(rect);
      text.setBounds(rect.x + iconBounds.width, rect.y + 1, pref.x + 1, pref.y + 1);
    }
  }