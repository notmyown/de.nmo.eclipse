package de.nmo.eclipse.ui.filterflyoutpalette.internal;

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.palette.ToolEntry;

public class FilterToolEntry extends ToolEntry {

    public static final Object PALETTE_TYPE_FILTER = "$Palette Filter"; //$NON-NLS-1$

    public FilterToolEntry() {
      this(null);
    }

    public FilterToolEntry(String label) {
      this(label, null);
    }

    public FilterToolEntry(String label, String shortDesc) {
      super(label, shortDesc, SharedImages.DESC_SELECTION_TOOL_16, SharedImages.DESC_SELECTION_TOOL_24,
          null);
      if (label == null || label.length() == 0) {
        setLabel("Filter...");
      }
      setUserModificationPermission(PERMISSION_NO_MODIFICATION);
      setType(PALETTE_TYPE_FILTER);
    }

  }