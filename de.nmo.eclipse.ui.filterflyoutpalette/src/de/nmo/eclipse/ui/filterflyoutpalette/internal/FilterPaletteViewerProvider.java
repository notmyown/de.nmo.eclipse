package de.nmo.eclipse.ui.filterflyoutpalette.internal;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author TJNKXAO
 *
 */
public class FilterPaletteViewerProvider extends PaletteViewerProvider {

  final GraphicalEditor editor;

  /**
   * @param theeditor
   * @param graphicalViewerDomain
   *
   * @author TJNKXAO
   * @since 04.11.2016
   */
  public FilterPaletteViewerProvider(GraphicalEditor theeditor, EditDomain graphicalViewerDomain) {
    super(graphicalViewerDomain);
    this.editor = theeditor;
  }

  @Override
  public PaletteViewer createPaletteViewer(Composite parent) {
    FilterPaletteViewer pViewer = new FilterPaletteViewer(this.editor);
    pViewer.createControl(parent);
    configurePaletteViewer(pViewer);
    hookPaletteViewer(pViewer);
    pViewer.getPaletteRoot().add(0, new FilterToolEntry());
    return pViewer;
  }





}
