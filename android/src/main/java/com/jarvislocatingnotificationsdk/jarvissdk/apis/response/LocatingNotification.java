package com.jarvislocatingnotificationsdk.jarvissdk.apis.response;

import java.io.Serializable;

public class LocatingNotification implements Serializable {

  private long id;
  private String title;
  private String description;

  public LocatingNotification(long id, String title, String description) {
    this.id = id;
    this.title = title;
    this.description = description;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}