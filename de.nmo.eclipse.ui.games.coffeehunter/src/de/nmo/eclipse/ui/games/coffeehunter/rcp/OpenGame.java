package de.nmo.eclipse.ui.games.coffeehunter.rcp;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.widgets.Display;

import de.nmo.eclipse.ui.games.coffeehunter.GamePanel;
import de.nmo.eclipse.ui.games.coffeehunter.gamestate.IExit;

public class OpenGame implements IHandler {

  @Override
  public void addHandlerListener(IHandlerListener handlerListener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void dispose() {

  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    JaCoPDialog dialog = new JaCoPDialog(Display.getCurrent().getActiveShell());
    GamePanel.exit = new IExit() {
      @Override
      public void exit() {
        Display.getDefault().asyncExec(new Runnable() {

          @Override
          public void run() {
            dialog.close();
          }
        });
      }
    };
    dialog.open();
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

  @Override
  public void removeHandlerListener(IHandlerListener handlerListener) {

  }

}
