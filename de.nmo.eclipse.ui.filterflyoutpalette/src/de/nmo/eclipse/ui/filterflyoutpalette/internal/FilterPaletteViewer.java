package de.nmo.eclipse.ui.filterflyoutpalette.internal;

import java.lang.reflect.Field;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;

public class FilterPaletteViewer extends PaletteViewer {
  final GraphicalEditor editor1;

  public FilterPaletteViewer(GraphicalEditor theeditor) {
    super();
    this.editor1 = theeditor;
    setEditPartFactory(null);
  }

  @Override
  public void setEditPartFactory(EditPartFactory factory) {
    super.setEditPartFactory(new FilterEditPartFactory(this.editor1, this));
  }

  @Override
  public void setActiveTool(ToolEntry newMode) {
    ToolEntry activeEntry = getActiveTool();
    Object active = getEditPartRegistry().get(activeEntry);
    if (active == null) {
      Field activeField;
      try {
        activeField = PaletteViewer.class.getDeclaredField("activeEntry");
        activeField.setAccessible(true);
        activeField.set(this, null);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    super.setActiveTool(newMode);
  }

}