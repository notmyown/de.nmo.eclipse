package de.nmo.eclipse.ui.perspectivecontext.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * Open the config window for perspective contexts
 *
 */
public class CreateContextHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
        "de.nmo.eclipse.ui.perpectivecontext.contextpage", null, null);
    if (pref != null)
      pref.open();
    return null;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean isHandled() {
    return true;
  }

}
