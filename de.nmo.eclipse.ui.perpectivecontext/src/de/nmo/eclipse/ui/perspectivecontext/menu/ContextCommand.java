package de.nmo.eclipse.ui.perspectivecontext.menu;

/**
 * 
 * empty Command as placeholder for the menu within the toolbar
 */
public class ContextCommand extends AbstractHandler {
  // inherit

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean isHandled() {
    return true;
  }

}
