package de.nmo.eclipse.ui.perspectivecontext.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.nmo.eclipse.ui.perspectivecontext.store.ContextStore;
import de.nmo.eclipse.ui.perspectivecontext.store.model.Perspective;
import de.nmo.eclipse.ui.perspectivecontext.store.model.PerspectiveContext;
import de.nmo.eclipse.ui.perspectivecontext.utils.Utils;

/**
 * Create Menuentries for every context
 *
 * @author not_my_own
 */
public class DynamicContributionItem extends ContributionItem {

  static final Map<String, List<Perspective>> perspectivecontexts = new HashMap<>();
  static final Map<String, Image>        displayicons        = new HashMap<>();
  static final Map<String, Perspective>       defaultperspectives = new HashMap<>();

  static {
    getContextFromExtensionPoint();
  }

  /**
   * Default
   */
  public DynamicContributionItem() {
    super();
  }

  /**
   * Default
   * 
   * @param id
   */
  public DynamicContributionItem(String id) {
    super(id);
  }

  @Override
  public void fill(Menu menu, int index) {
    getContextFromConfig();
    for (final String key : perspectivecontexts.keySet()) {
      MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index);
      menuItem.setText(key);
      menuItem.setImage(displayicons.get(key));
      menuItem.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e) {
          activateContext(key);
        }
      });
    }

  }

  /**
   * reset all perspectives and reopen the configured
   * 
   * @param key - Name of the context within the e.p.
   *
   */
  void activateContext(String key) {
    if (perspectivecontexts.get(key) == null) {
      return;
    }
    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    if (page != null && page.getPerspective() != null) {
      page.closeAllPerspectives(true, true);
    }
    List<Perspective> list = perspectivecontexts.get(key);

    ListIterator<Perspective> iter = list.listIterator(list.size());
    while (iter.hasPrevious()) {
      Perspective perspective = iter.previous();
      try {
        PlatformUI.getWorkbench().showPerspective(perspective.getId(),
            PlatformUI.getWorkbench().getActiveWorkbenchWindow());
      } catch (SecurityException | IllegalArgumentException | WorkbenchException e) {
        //swallow
      }
    }
    Perspective def = defaultperspectives.get(key);
    if (def != null && def.getId() != null && !def.getId().isEmpty()) {
      try {
        PlatformUI.getWorkbench().showPerspective(def.getId(), PlatformUI.getWorkbench().getActiveWorkbenchWindow());
      } catch (SecurityException | IllegalArgumentException | WorkbenchException e) {
        //swallow
      }
    }

  }

  private static void getContextFromConfig() {
    List<PerspectiveContext> list = ContextStore.readXML(ContextStore.getConfigFile());
    for (PerspectiveContext context : list) {
      perspectivecontexts.put(context.getName(), context.getPerspectives());
      defaultperspectives.put(context.getName(), context.getDefaultperspective());
    }
  }

  @Override
  public void update() {
    System.err.println("update");
  }


  private static void getContextFromExtensionPoint() {
    final IExtensionPoint extensionPoint = RegistryFactory.getRegistry()
        .getExtensionPoint("de.nmo.eclipse.ui.perspectivecontext.context");
    if (extensionPoint == null) {
      return;
    }

    final IExtension[] extensions = extensionPoint.getExtensions();

    for (final IExtension extension : extensions) {
      String namespace = extension.getNamespaceIdentifier();
      IConfigurationElement[] configurationElements = extension.getConfigurationElements();
      for (IConfigurationElement configElement : configurationElements) {

        String name = configElement.getAttribute("name");
        String icon = configElement.getAttribute("icon");
        Perspective isdefault = Utils.getFullPerspectiveById(configElement.getAttribute("default"));
        if (isdefault != null) {
          defaultperspectives.put(name, isdefault);
        }
        ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(namespace, icon);
        if (desc != null) {
          Image iconD = desc.createImage();
          displayicons.put(name, iconD);
        }

        List<Perspective> perspectives = perspectivecontexts.get(name);
        if (perspectives == null) {
          perspectives = new ArrayList<>();
        }

        for (IConfigurationElement child : configElement.getChildren("perspective")) {
          String id = child.getAttribute("id");
          Perspective p = Utils.getFullPerspectiveById(id);
          if (!perspectives.contains(p)) {
            perspectives.add(p);
          }
        }
        perspectivecontexts.put(name, perspectives);

      }
    }

  }

  @Override
  public boolean isDynamic() {
    return true;
  }

}
