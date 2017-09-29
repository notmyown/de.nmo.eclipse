package de.nmo.eclipse.ui.games.coffeehunter.rcp;

import javax.swing.JPanel;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import de.nmo.eclipse.ui.games.coffeehunter.GamePanel;

public class JaCoPDialog extends Dialog {

  public JaCoPDialog(Shell parentShell) {
    super(parentShell);
    setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
    setBlockOnOpen(false);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    //Composite container = (Composite) super.createDialogArea(parent);
    //container.setLayout(new GridLayout(1, true));
    Composite swtAwtComponent = new Composite(parent, SWT.EMBEDDED);
    GridData data = new GridData(SWT.FILL, SWT.NONE, true, false);
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.heightHint = 500;
    swtAwtComponent.setLayoutData(data);
    java.awt.Frame frame = SWT_AWT.new_Frame(swtAwtComponent);
    JPanel panel = new GamePanel();
    frame.add(panel);
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
    return new Point(640, 480);
  }

}