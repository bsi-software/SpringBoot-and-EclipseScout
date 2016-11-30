package org.eclipse.scout.tasks.model;

import java.util.UUID;

public class Document extends Model<UUID> {

  public static final int TYPE_OTHER = 0;
  public static final int TYPE_PICTURE = 1;

  private String name;
  private byte[] data;
  private int type;
  private long size;

  public Document() {
    setId(UUID.randomUUID());
  }

  public Document(String name, byte[] data, int type) {
    setId(UUID.randomUUID());

    this.name = name;
    this.type = type;

    setData(data);
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

    if (this.data == null) {
      size = 0;
    }
    else {
      size = this.data.length;
    }
  }

  public long getSize() {
    return size;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

}
