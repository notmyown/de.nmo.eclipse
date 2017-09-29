package de.nmo.eclipse.ui.games;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IStartup;

/**
 * 
 *
 * @author Bergen, Marco (I-EA-25, extern)
 * @since 03.08.2017
 */
public class KonamiCode extends PropertyTester implements IStartup {

  static boolean      konami = false;

  String              typed = "";
  final static String code  = "Up;Up;Down;Down;Left;Right;Left;Right;B;A;";

  @Override
  public void earlyStartup() {
    Display.getDefault().asyncExec(new Runnable()
    {

      @Override
      public void run() {
        Display display = Display.getDefault();

        display.addFilter(SWT.KeyDown, new Listener()
        {
          @Override
          public void handleEvent(Event event) {
            if (konami) {
              return;
            }
            event.doit = false;
            switch (event.keyCode) {
              case 16777219: {
                KonamiCode.this.typed += "Left;";
                break;
              }
              case 16777220: {
                KonamiCode.this.typed += "Right;";
                break;
              }
              case 16777217: {
                KonamiCode.this.typed += "Up;";
                break;
              }
              case 16777218: {
                KonamiCode.this.typed += "Down;";
                break;
              }
              case 97: {
                KonamiCode.this.typed += "A;";
                break;
              }
              case 98: {
                KonamiCode.this.typed += "B;";
                break;
              }
              default: {
                KonamiCode.this.typed = "";
                break;
              }
            }
            if (code.equals(KonamiCode.this.typed)) {
              konami = true;
              System.setProperty("de.nmo.eclipse.ui.games.systemTest", "konami");
              MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "NMO Games",
                  "Sie haben erfolgreich NMO Games freigeschaltet. Nutzen sie das 'NMO Games' Menu.");
            }
          }
        });
      }
    });


  }

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    return konami;
  }

}
