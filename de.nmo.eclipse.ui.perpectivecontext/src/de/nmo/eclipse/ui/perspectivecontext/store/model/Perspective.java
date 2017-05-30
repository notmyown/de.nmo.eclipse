package de.nmo.eclipse.ui.perspectivecontext.store.model;

import org.eclipse.swt.graphics.Image;

/**
 * internal representation of a perspective
 *
 */
public class Perspective {

  String id;
  String name;
  Image  image;

  /**
   * @param id
   * @param n
   * @param i
   * 
   */
  public Perspective(String id, String n, Image i) {
    setId(id);
    setName(n);
    setImage(i);
  }

  /**
   * @return Liefert den Wert von id.
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id Setzt den Wert von id.
   */
  public void setId(String id) {
    this.id = id;
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
   * @return Liefert den Wert von image.
   */
  public Image getImage() {
    return this.image;
  }

  /**
   * @param image Setzt den Wert von image.
   */
  public void setImage(Image image) {
    this.image = image;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Perspective other = (Perspective) obj;
    if (this.name == null) {
      if (other.name != null)
        return false;
    } else if (!this.name.equals(other.name))
      return false;
    return true;
  }

}
