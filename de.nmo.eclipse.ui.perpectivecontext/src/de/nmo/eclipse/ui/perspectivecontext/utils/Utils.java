package de.nmo.eclipse.ui.perspectivecontext.utils;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.nmo.eclipse.ui.perspectivecontext.store.model.Perspective;

public class Utils {

  /**
   * @return the current perspective or null if no perspective can be found
   *
   */
  public static IPerspectiveDescriptor getActivePerspective() {
    try {
      IWorkbench wb = PlatformUI.getWorkbench();

      IWorkbenchWindow win = wb.getActiveWorkbenchWindow();

      IWorkbenchPage page = win.getActivePage();

      return page.getPerspective();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 
   * @param id
   * @return a internal representation of an Perspective Object
   */
  public static Perspective getFullPerspectiveById(String id) {
    IPerspectiveRegistry perspectives = PlatformUI.getWorkbench().getPerspectiveRegistry();
    IPerspectiveDescriptor desc = perspectives.findPerspectiveWithId(id);
    Perspective p = null;
    if (desc != null) {
      p = new Perspective(desc.getId(), desc.getLabel(), desc.getImageDescriptor().createImage());
    }
    return p;
  }

}
