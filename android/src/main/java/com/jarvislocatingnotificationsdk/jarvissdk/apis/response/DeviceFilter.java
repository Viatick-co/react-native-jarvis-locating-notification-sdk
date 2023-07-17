package com.jarvislocatingnotificationsdk.jarvissdk.apis.response;

public class DeviceFilter {

  private String uuid;
  private int major;

  public DeviceFilter(String uuid, int major) {
    this.uuid = uuid;
    this.major = major;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public int getMajor() {
    return major;
  }

  public void setMajor(int major) {
    this.major = major;
  }

}
