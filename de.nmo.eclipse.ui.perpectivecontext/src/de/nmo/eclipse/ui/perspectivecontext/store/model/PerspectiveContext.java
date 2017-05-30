package de.nmo.eclipse.ui.perspectivecontext.store.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 */
public class PerspectiveContext {

  List<Perspective> perspectives = new ArrayList<>();
  String       name;
  Perspective       defaultperspective;

  /**
   * @param string Name
   *
   */
  public PerspectiveContext(String string) {
    setName(string);
  }

  /**
   * @return Liefert den Wert von perspectives.
   */
  public List<Perspective> getPerspectives() {
    return this.perspectives;
  }

  /**
   * @param perspectives Setzt den Wert von perspectives.
   */
  public void setPerspectives(List<Perspective> perspectives) {
    this.perspectives = perspectives;
  }

  /**
   * @return Liefert den Wert von name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param name Setzt den Wert von name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return Liefert den Wert von defaultperspective.
   */
  public Perspective getDefaultperspective() {
    return this.defaultperspective;
  }

  /**
   * @param defaultperspective Setzt den Wert von defaultperspective.
   */
  public void setDefaultperspective(Perspective defaultperspective) {
    this.defaultperspective = defaultperspective;
  }

}
