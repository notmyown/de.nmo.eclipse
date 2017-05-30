package de.nmo.eclipse.ui.perspectivecontext.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import de.nmo.eclipse.ui.perspectivecontext.store.ContextStore;
import de.nmo.eclipse.ui.perspectivecontext.store.model.Perspective;
import de.nmo.eclipse.ui.perspectivecontext.store.model.PerspectiveContext;

/**
 * Configure Contexts
 */
public class PerspectiveContextPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

  Text                     fContextName;
  CheckboxFilteredTree     fTreePerspectives;
  CheckboxFilteredTree     fTreeContext;
  List<PerspectiveContext> contextlist;
  Combo                    fcontextcombo;
  private Combo            fdefaultcombo;

  @Override
  public void init(IWorkbench workbench) {
    this.contextlist = ContextStore.readXML(ContextStore.getConfigFile());
  }

  @Override
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout());
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));

    Label label = new Label(composite, SWT.WRAP);
    label.setText(Messages.getString("perspectivecontext.page.headline")); //$NON-NLS-1$
    label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    Composite newName = new Composite(composite, SWT.NONE);
    newName.setLayout(new GridLayout(2, false));
    newName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    this.fContextName = new Text(newName, SWT.SINGLE | SWT.BORDER);
    this.fContextName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    this.fContextName.setFocus();

    Button newB = new Button(newName, SWT.PUSH);
    newB.setText(Messages.getString("perspectivecontext.page.new")); //$NON-NLS-1$
    newB.addSelectionListener(new SelectionAdapter()
    {

      @Override
      public void widgetSelected(SelectionEvent e) {
        PerspectiveContext selected = null;
        if (PerspectiveContextPreferencePage.this.fContextName.getText() != null
            && !PerspectiveContextPreferencePage.this.fContextName.getText().trim().isEmpty()) {
          for (PerspectiveContext oldc : PerspectiveContextPreferencePage.this.contextlist) {
            if (oldc.getName().equals(PerspectiveContextPreferencePage.this.fContextName.getText().trim())) {
              selected = oldc;
            }
          }
          if (selected == null) {
            selected = new PerspectiveContext(PerspectiveContextPreferencePage.this.fContextName.getText());
          }
        }
        if (selected != null) {
          PerspectiveContextPreferencePage.this.contextlist.add(selected);

          PerspectiveContextPreferencePage.this.fcontextcombo.setItems(getContextNames());
          for (int i = 0; i < PerspectiveContextPreferencePage.this.fcontextcombo.getItemCount(); i++) {
            if (PerspectiveContextPreferencePage.this.fcontextcombo.getItem(i).toString().equals(selected.getName())) {
              PerspectiveContextPreferencePage.this.fcontextcombo.select(i);
              break;
            }
          }
          PerspectiveContextPreferencePage.this.fTreePerspectives.getCheckboxTreeViewer()
              .setInput(getAvailablePerspectives(selected));
          PerspectiveContextPreferencePage.this.fTreeContext.getCheckboxTreeViewer()
              .setInput(selected.getPerspectives());
          updateDefault();
        }
      }

    });
    newB.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

    this.fcontextcombo = new Combo(newName, SWT.NONE);
    this.fcontextcombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    this.fcontextcombo.setItems(getContextNames());

    this.fcontextcombo.addSelectionListener(new SelectionAdapter()
    {
      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
       */
      @Override
      public void widgetSelected(SelectionEvent e) {
        PerspectiveContext pc = null;
        for (PerspectiveContext oldc : PerspectiveContextPreferencePage.this.contextlist) {
          if (oldc.getName().equals(PerspectiveContextPreferencePage.this.fcontextcombo
              .getItem(PerspectiveContextPreferencePage.this.fcontextcombo.getSelectionIndex()).toString())) {
            PerspectiveContextPreferencePage.this.fTreePerspectives.getCheckboxTreeViewer()
                .setInput(getAvailablePerspectives(oldc));
            pc = oldc;
            break;
          }
        }
        if (pc != null) {
          PerspectiveContextPreferencePage.this.fTreeContext.getCheckboxTreeViewer().setInput(pc.getPerspectives());
          updateDefault();
        }
      }
    });

    Button delB = new Button(newName, SWT.PUSH);
    delB.setText(Messages.getString("perspectivecontext.page.delete")); //$NON-NLS-1$
    delB.addSelectionListener(new SelectionAdapter()
    {

      @Override
      public void widgetSelected(SelectionEvent e) {
        PerspectiveContext context = getCurrentContext();
        PerspectiveContextPreferencePage.this.contextlist.remove(context);
        PerspectiveContextPreferencePage.this.fcontextcombo.setItems(getContextNames());
          PerspectiveContextPreferencePage.this.fTreePerspectives.getCheckboxTreeViewer()
            .setInput(getAvailablePerspectives(null));
          PerspectiveContextPreferencePage.this.fTreeContext.getCheckboxTreeViewer()
            .setInput(null);
        updateDefault();
      }

    });
    delB.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

    Composite table = new Composite(composite, SWT.NONE);
    table.setLayout(new GridLayout(3, true));
    table.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    label = new Label(table, SWT.WRAP);
    label.setText(Messages.getString("perspectivecontext.page.available_perspectives")); //$NON-NLS-1$
    label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    label = new Label(table, SWT.WRAP);
    label.setText(""); //$NON-NLS-1$
    label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    label = new Label(table, SWT.WRAP);
    label.setText(Messages.getString("perspectivecontext.page.current_perspectives")); //$NON-NLS-1$
    label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    this.fTreePerspectives = new CheckboxFilteredTree(table, SWT.BORDER, new PatternFilter());
    GridData gd = new GridData(GridData.FILL_BOTH);
    gd.heightHint = 250;
    this.fTreePerspectives.getViewer().getControl().setLayoutData(gd);

    final IStructuredContentProvider fTableContentProvider = new ContentProvider();
    this.fTreePerspectives.getCheckboxTreeViewer().setContentProvider(fTableContentProvider);
    this.fTreePerspectives.getCheckboxTreeViewer().setUseHashlookup(true);
    this.fTreePerspectives.getCheckboxTreeViewer().setLabelProvider(new PerspectiveContextLabelProvider());
    this.fTreePerspectives.getCheckboxTreeViewer().setUseHashlookup(true);
    this.fTreePerspectives.getCheckboxTreeViewer().setInput(getAvailablePerspectives(null));

    // Add select / deselect all buttons for bug 46669
    Composite buttonComposite = new Composite(table, SWT.NONE);
    FillLayout fillLayout = new FillLayout();
    fillLayout.type = SWT.VERTICAL;

    buttonComposite.setLayout(fillLayout);
    buttonComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

    Button addButton = new Button(buttonComposite, SWT.PUSH);

    addButton.setText(Messages.getString("perspectivecontext.page.add")); //$NON-NLS-1$
    addButton.setToolTipText(Messages.getString("perspectivecontext.page.add.tooltip")); //$NON-NLS-1$
    addButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent selectionEvent) {
        PerspectiveContext context = getCurrentContext();
        if (context != null) {
          for (Object x : PerspectiveContextPreferencePage.this.fTreePerspectives.getCheckboxTreeViewer()
              .getCheckedElements()) {
            if (x instanceof Perspective && !context.getPerspectives().contains(x)) {
              context.getPerspectives().add((Perspective) x);
            }
          }
          PerspectiveContextPreferencePage.this.fTreePerspectives.getCheckboxTreeViewer()
              .setInput(getAvailablePerspectives(context));
          PerspectiveContextPreferencePage.this.fTreeContext.getCheckboxTreeViewer()
              .setInput(context.getPerspectives());
          updateDefault();
        }

      }
    });

    Button addAllButton = new Button(buttonComposite, SWT.PUSH);

    addAllButton.setText(Messages.getString("perspectivecontext.page.addall")); //$NON-NLS-1$
    addAllButton.setToolTipText(Messages.getString("perspectivecontext.page.addall.tooltip")); //$NON-NLS-1$
    addAllButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent selectionEvent) {
        PerspectiveContext context = getCurrentContext();
        if (context != null) {
          for (Object x : (List<?>) PerspectiveContextPreferencePage.this.fTreePerspectives.getCheckboxTreeViewer()
              .getInput()) {
            if (x instanceof Perspective && !context.getPerspectives().contains(x)) {
              context.getPerspectives().add((Perspective) x);
            }
          }
          PerspectiveContextPreferencePage.this.fTreePerspectives.getCheckboxTreeViewer()
              .setInput(getAvailablePerspectives(context));
          PerspectiveContextPreferencePage.this.fTreeContext.getCheckboxTreeViewer()
              .setInput(context.getPerspectives());
          updateDefault();
        }
      }
    });

    Button removeButton = new Button(buttonComposite, SWT.PUSH);
    removeButton.setText(Messages.getString("perspectivecontext.page.remove")); //$NON-NLS-1$
    removeButton.setToolTipText(Messages.getString("perspectivecontext.page.remove.tooltip")); //$NON-NLS-1$
    removeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent selectionEvent) {
        PerspectiveContext context = getCurrentContext();
        if (context != null) {
          for (Object x : PerspectiveContextPreferencePage.this.fTreeContext.getCheckboxTreeViewer()
              .getCheckedElements()) {
            if (x instanceof Perspective && context.getPerspectives().contains(x)) {
              context.getPerspectives().remove(x);
            }
          }
          PerspectiveContextPreferencePage.this.fTreePerspectives.getCheckboxTreeViewer()
              .setInput(getAvailablePerspectives(context));
          PerspectiveContextPreferencePage.this.fTreeContext.getCheckboxTreeViewer()
              .setInput(context.getPerspectives());
          updateDefault();
        }
      }
    });

    Button removeAllButton = new Button(buttonComposite, SWT.PUSH);
    removeAllButton.setText(Messages.getString("perspectivecontext.page.removeall")); //$NON-NLS-1$
    removeAllButton.setToolTipText(Messages.getString("perspectivecontext.page.removeall.tooltip")); //$NON-NLS-1$
    removeAllButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent selectionEvent) {
        PerspectiveContext context = getCurrentContext();
        if (context != null) {
          context.getPerspectives().clear();
          PerspectiveContextPreferencePage.this.fTreePerspectives.getCheckboxTreeViewer()
              .setInput(getAvailablePerspectives(context));
          PerspectiveContextPreferencePage.this.fTreeContext.getCheckboxTreeViewer()
              .setInput(context.getPerspectives());
          updateDefault();
        }
      }
    });

    this.fTreeContext = new CheckboxFilteredTree(table, SWT.BORDER, new PatternFilter());
    GridData gd2 = new GridData(GridData.FILL_BOTH);
    gd2.heightHint = 250;
    this.fTreeContext.getViewer().getControl().setLayoutData(gd2);
    this.fTreeContext.getCheckboxTreeViewer().setContentProvider(fTableContentProvider);
    this.fTreeContext.getCheckboxTreeViewer().setUseHashlookup(true);
    this.fTreeContext.getCheckboxTreeViewer().setLabelProvider(new PerspectiveContextLabelProvider());
    this.fTreeContext.getCheckboxTreeViewer().setUseHashlookup(true);
    this.fTreeContext.getCheckboxTreeViewer().setInput(null);

    Composite defName = new Composite(composite, SWT.NONE);
    defName.setLayout(new GridLayout(2, false));
    defName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    label = new Label(defName, SWT.WRAP);
    label.setText(Messages.getString("perspectivecontext.page.default_perspective")); //$NON-NLS-1$
    //label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    this.fdefaultcombo = new Combo(defName, SWT.NONE);
    this.fdefaultcombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    //this.fdefaultcombo.setItems(null);


    return composite;
  }

  List<Perspective> getAvailablePerspectives(PerspectiveContext activecontext) {
    List<Perspective> list = new ArrayList<>();
    IPerspectiveRegistry perspectives = PlatformUI.getWorkbench().getPerspectiveRegistry();
    for (IPerspectiveDescriptor desc : perspectives.getPerspectives()) {
      Perspective p = new Perspective(desc.getId(), desc.getLabel(), null);
      if (activecontext == null || !activecontext.getPerspectives().contains(p)) {
        p.setImage(desc.getImageDescriptor().createImage());
        list.add(p);
      }
    }
    return list;
  }

  void updateDefault() {
    PerspectiveContext context = getCurrentContext();
    if (context == null) {
      this.fdefaultcombo.setItems((String[]) null);
      return;
    }
    String sel = null;
    if (this.fdefaultcombo.getSelectionIndex() >= 0) {
      this.fdefaultcombo.getItem(this.fdefaultcombo.getSelectionIndex());
    }
    this.fdefaultcombo.select(0);
    List<String> pers = new ArrayList<>();
    for (Perspective p : context.getPerspectives()) {
      pers.add(p.getName());
    }
    this.fdefaultcombo.setItems(pers.toArray(new String[pers.size()]));
    if (sel != null && !sel.isEmpty()) {
      for (int i = 0; i < this.fdefaultcombo.getItemCount(); i++) {
        if (sel.equals(fdefaultcombo.getItem(i))) {
          fdefaultcombo.select(i);
          break;
        }
      }
    }
  }

  PerspectiveContext getCurrentContext() {
    PerspectiveContext pc = null;
    for (PerspectiveContext oldc : PerspectiveContextPreferencePage.this.contextlist) {
      if (PerspectiveContextPreferencePage.this.fcontextcombo.getSelectionIndex() >= 0
          && oldc.getName().equals(PerspectiveContextPreferencePage.this.fcontextcombo
              .getItem(PerspectiveContextPreferencePage.this.fcontextcombo.getSelectionIndex()).toString())) {
        pc = oldc;
        break;
      }
    }
    return pc;
  }

  String[] getContextNames() {
    List<String> array = new ArrayList<>();
    for (PerspectiveContext c : this.contextlist) {
      array.add(c.getName());
    }
    return array.toArray(new String[array.size()]);
  }

  @Override
  public boolean performOk() {
    ContextStore.writeXML(ContextStore.getConfigFile(), this.contextlist);
    return true;
  }

  class ContentProvider implements ITreeContentProvider {
    @Override
    public Object[] getElements(Object inputElement) {
      if (inputElement instanceof List) {
        return ((List<?>) inputElement).toArray();
      }
      return PluginRegistry.getAllModels();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
      return null;
    }

    @Override
    public Object getParent(Object element) {
      return null;
    }

    @Override
    public boolean hasChildren(Object element) {
      return false;
    }
  }

  class PerspectiveContextLabelProvider extends LabelProvider {

    public PerspectiveContextLabelProvider() {
    }

    @Override
    public String getText(Object element) {
      if (element instanceof PerspectiveContext) {
        PerspectiveContext context = (PerspectiveContext) element;
        return context.getName();
      }
      if (element instanceof Perspective) {
        Perspective context = (Perspective) element;
        return context.getName();
      }
      return super.getText(element);
    }

    @Override
    public Image getImage(Object element) {
      if (element instanceof Perspective) {
        return ((Perspective) element).getImage();
      }
      return null;
    }

    @Override
    public void dispose() {
      super.dispose();
    }

  }

  class CheckboxFilteredTree extends FilteredTree {

    public CheckboxFilteredTree(Composite parent, int treeStyle, PatternFilter filter) {
      super(parent, treeStyle, filter, true);
    }

    @Override
    protected TreeViewer doCreateTreeViewer(Composite parent1, int style) {
      return new CheckboxTreeViewer(parent1, style);
    }

    public CheckboxTreeViewer getCheckboxTreeViewer() {
      return (CheckboxTreeViewer) getViewer();
    }

  }

}
