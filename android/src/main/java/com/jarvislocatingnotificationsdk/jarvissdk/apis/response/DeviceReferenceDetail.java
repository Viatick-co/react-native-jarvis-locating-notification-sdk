package com.jarvislocatingnotificationsdk.jarvissdk.apis.response;

public class DeviceReferenceDetail {
  private String id;
  private String name;

  public DeviceReferenceDetail(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public DeviceReferenceDetail() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
