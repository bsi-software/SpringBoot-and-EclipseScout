package org.eclipse.scout.tasks.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Document extends Model<UUID> {

  /**
   * Hard size limit for documents: 100MB.
   */
  public static final int CONTENT_SIZE_MAX = 100 * 1048576;
  public static final String CONTENT_ERROR_SIZE = "ContentErrorSize";

  public static final int TYPE_OTHER = 0;
  public static final int TYPE_PICTURE = 1;

  @NotNull
  private String name;

  @Size(max = CONTENT_SIZE_MAX, message = CONTENT_ERROR_SIZE)
  private byte[] data;
  private int type;

  public Document() {
    setId(UUID.randomUUID());
  }

  public Document(String name, byte[] data, int type) {
    setId(UUID.randomUUID());

    this.name = name;
    this.type = type;
    this.data = data;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public long getSize() {
    if (data == null) {
      return 0;
    }

    return data.length;
  }

}
