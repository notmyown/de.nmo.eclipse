package de.nmo.eclipse.ui.games.tilemapeditor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;

import de.nmo.eclipse.ui.games.Activator;


public class TileMapEditorDialog extends Dialog {

  MyPanel panel;

  public TileMapEditorDialog(Shell parentShell) {
    super(parentShell);
    setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE);
    setBlockOnOpen(false);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    //Composite container = (Composite) super.createDialogArea(parent);
    //container.setLayout(new GridLayout(1, true));
    Composite swtAwtComponent = new Composite(parent, SWT.EMBEDDED);
    GridData data = new GridData(SWT.FILL, SWT.NONE, true, true);
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    swtAwtComponent.setLayoutData(data);
    java.awt.Frame frame = SWT_AWT.new_Frame(swtAwtComponent);
    this.panel = new MyPanel();
    frame.add(this.panel);
    //frame.setMinimumSize(new Dimension(640, 480));
    return swtAwtComponent;
  }

  // overriding this methods allows you to set the
  // title of the custom dialog
  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("Selection dialog");
  }

  @Override
  protected Point getInitialSize() {
    return new Point(1600, 1000);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    super.okPressed();
    try {
      this.panel.saveMap(Activator.getPrefs().get(MyPanel.TILEMAPEDITOR_MAP, null));
      Activator.getPrefs().flush();
    } catch (BackingStoreException e) {
      e.printStackTrace();
    }
  }

}