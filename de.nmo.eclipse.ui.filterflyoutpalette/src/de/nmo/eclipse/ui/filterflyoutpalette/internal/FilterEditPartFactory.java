package de.nmo.eclipse.ui.filterflyoutpalette.internal;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.PaletteEditPartFactory;
import org.eclipse.gef.ui.parts.GraphicalEditor;

public class FilterEditPartFactory extends PaletteEditPartFactory {

    final FilterPaletteViewer viewer;
    final GraphicalEditor     editor1;

    public FilterEditPartFactory(GraphicalEditor theeditor, FilterPaletteViewer bpmnPaletteViewer) {
      super();
      this.viewer = bpmnPaletteViewer;
      this.editor1 = theeditor;
    }

    @Override
    public EditPart createEditPart(EditPart context, Object model) {
      EditPart part = null;
      if (model instanceof FilterToolEntry) {
        part = new FilterToolEditPart((PaletteEntry) model, this.editor1, this.viewer);
      } else {
        part = super.createEditPart(context, model);
      }
      return part;
    }

  }