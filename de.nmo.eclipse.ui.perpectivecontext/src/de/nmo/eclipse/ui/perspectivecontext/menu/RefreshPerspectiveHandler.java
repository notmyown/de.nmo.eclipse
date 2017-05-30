package de.nmo.eclipse.ui.perspectivecontext.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import de.nmo.eclipse.ui.perspectivecontext.utils.Utils;

/**
 * reset the current perspective to its default
 *
 */
public class RefreshPerspectiveHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().resetPerspective();
    return null;
  }

  @Override
  public boolean isEnabled() {
    return Utils.getActivePerspective() != null;
  }

  @Override
  public boolean isHandled() {
    return Utils.getActivePerspective() != null;
  }

}
