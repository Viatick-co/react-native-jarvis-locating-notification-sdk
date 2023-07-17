package com.jarvislocatingnotificationsdk.jarvissdk.apis.response;

import java.io.Serializable;
import java.util.Map;

public class ApplicationDetail implements Serializable {

  private long id;
  private String name;
  private String email;
  private Map<String, DeviceFilter> deviceFilterMap;

  public ApplicationDetail(long id, String name, String email, Map<String, DeviceFilter> deviceFilterMap) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.deviceFilterMap = deviceFilterMap;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Map<String, DeviceFilter> getDeviceFilterMap() {
    return deviceFilterMap;
  }

  public void setDeviceFilterMap(Map<String, DeviceFilter> deviceFilterMap) {
    this.deviceFilterMap = deviceFilterMap;
  }

}
