package de.nmo.eclipse.ui.perspectivecontext.preferences;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Internationalization
 */
public class Messages {
  private static final String         BUNDLE_NAME     = "de.nmo.eclipse.ui.perspectivecontext.preferences.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  private Messages() {
  }

  /**
   * @param key
   * @return
   *
   */
  public static String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }
}
