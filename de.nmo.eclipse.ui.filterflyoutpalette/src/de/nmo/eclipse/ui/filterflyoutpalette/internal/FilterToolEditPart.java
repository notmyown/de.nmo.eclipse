package de.nmo.eclipse.ui.filterflyoutpalette.internal;

import java.lang.reflect.Method;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.viewers.TextCellEditor;

public class FilterToolEditPart extends PaletteEditPart {

  final PaletteViewer   viewer;
  final GraphicalEditor editor1;

  final FilterLabel label;

  public FilterToolEditPart(PaletteEntry model, GraphicalEditor theeditor, PaletteViewer view) {
    super(model);
    this.label = new FilterLabel(model.getLabel());
    this.viewer = view;
    this.editor1 = theeditor;
  }

  @Override
  public EditPartViewer getViewer() {
    EditPartViewer view = super.getViewer();
    return view == null ? this.viewer : view;
  }

  @Override
  protected IFigure createFigure() {
    this.label.setLabelAlignment(PositionConstants.LEFT);
    return this.label;
  }

  @Override
  public void createEditPolicies() {
    super.createEditPolicies();
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new FilterDirectEditPolicy(this.editor1));
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new FilterSelectionEditPolicy());
  }

  @Override
  public void performRequest(Request req) {
    if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
      performDirectEditing();
    }
  }

  void performDirectEditing() {
    FilterDirectEditManager manager = new FilterDirectEditManager(this, TextCellEditor.class,
        new FilterCellEditorLocator(this.label), this.label);
    manager.show();
  }

  private class FilterDirectEditManager extends DirectEditManager {

    Label label1;

    public FilterDirectEditManager(GraphicalEditPart source, Class<?> editorType, CellEditorLocator locator,
        Label label) {
      super(source, editorType, locator);
      this.label1 = label;
    }

    @Override
    protected void initCellEditor() {
      String initialLabelText = this.label1.getText();
      getCellEditor().setValue(initialLabelText);
    }

    @Override
    public void showFeedback() {
      getEditPart().showSourceFeedback(getDirectEditRequest());
    }

  }

  private class FilterSelectionEditPolicy extends SelectionEditPolicy {

    public FilterSelectionEditPolicy() {
      super();
    }

    @Override
    protected void showSelection() {
      performDirectEditing();
    }

    @Override
    protected void hideSelection() {
      // empty
    }

  }

  private class FilterDirectEditPolicy extends DirectEditPolicy {

    final GraphicalEditor editor11;

    public FilterDirectEditPolicy(GraphicalEditor theeditor) {
      super();
      this.editor11 = theeditor;
    }

    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
      for (Object obj : getGraphicalViewer().getEditDomain().getPaletteViewer().getPaletteRoot().getChildren()) {
        if (obj instanceof PaletteDrawer) {
          filter((String) request.getCellEditor().getValue(), (PaletteDrawer) obj);
        }
      }
      if (FilterToolEditPart.this.label.getText().length() == 0) {
        FilterToolEditPart.this.label.setText("Filter...");
      }
      return null;
    }

    private void filter(String filter, PaletteDrawer drawer) {
      String f = filter.toLowerCase();
      if (drawer == null) {
        return;
      }
      if (f == null) {
        f = "";
      }
      if (drawer.getLabel().toLowerCase().contains(f)) {
        drawer.setVisible(true);
        for (Object obj : drawer.getChildren()) {
          if (obj instanceof PaletteEntry) {
            PaletteEntry entry = (PaletteEntry) obj;
            entry.setVisible(true);
          }
        }
      } else {
        boolean foundAtLeastOne = false;
        if (drawer.getChildren() != null) {
          for (Object obj : drawer.getChildren()) {
            if (obj instanceof PaletteEntry) {
              PaletteEntry entry = (PaletteEntry) obj;
              if (entry.getLabel() != null && entry.getLabel().toLowerCase().contains(f)) {
                entry.setVisible(true);
                foundAtLeastOne = true;
              } else {
                entry.setVisible(false);
              }
            }
          }
        }
        drawer.setVisible(foundAtLeastOne);
      }
    }

    private GraphicalViewer getGraphicalViewer() {
      try {
        Method m = GraphicalEditor.class.getDeclaredMethod("getGraphicalViewer");
        m.setAccessible(true);
        GraphicalViewer gv = (GraphicalViewer) m.invoke(this.editor11);
        m.setAccessible(false);
        return gv;
      } catch (Exception e) {
        return null;
      }
    }

    @Override
    protected void showCurrentEditValue(DirectEditRequest request) {
      String value = (String) request.getCellEditor().getValue();
      ((Label) getHostFigure()).setText(value);

    }
  }

}