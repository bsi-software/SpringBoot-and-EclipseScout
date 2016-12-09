package org.eclipse.scout.tasks.model;

import javax.validation.constraints.NotNull;

public class Model<ID> {

  /**
   * Represents the ID of this object. Must not be null.
   */
  @NotNull
  protected ID id;

  public Model() {
  }

  public Model(ID id) {
    this.id = id;
  }

  /**
   * Returns the ID of this object.
   */
  public ID getId() {
    return id;
  }

  /**
   * Sets the ID of this object.
   */
  public void setId(ID id) {
    this.id = id;
  }

  /**
   * Returns true if the two objects share the same id, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    @SuppressWarnings("unchecked")
    ID objId = (ID) ((Model) obj).getId();

    if (objId == null) {
      return false;
    }

    return objId.equals(id);
  }

  /**
   * Returns the hash code based only of the id of this object.
   */
  @Override
  public int hashCode() {
    return id == null ? 0 : id.hashCode();
  }
}
