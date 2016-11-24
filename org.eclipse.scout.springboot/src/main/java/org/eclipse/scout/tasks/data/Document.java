package org.eclipse.scout.tasks.data;

import java.util.UUID;

public class Document {

  public static final int TYPE_OTHER = 0;
  public static final int TYPE_PICTURE = 1;

  private UUID id = UUID.randomUUID();
  private String name;
  private byte[] data;
  private int type;
  private long size;

  public Document() {
  }

  public Document(String name, byte[] data, int type) {
    this.name = name;
    this.type = type;

    setData(data);
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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
