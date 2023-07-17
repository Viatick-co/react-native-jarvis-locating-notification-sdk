package com.jarvislocatingnotificationsdk.jarvissdk.apis.response;

public class JarvisDevice {

  private long id;
  private String name;
  private String mac;

  private DeviceReferenceDetail referenceDetail;

  private String deviceType;

  public JarvisDevice() {
  }

  public JarvisDevice(long id, String name, String mac, DeviceReferenceDetail referenceDetail, String deviceType) {
    this.id = id;
    this.name = name;
    this.mac = mac;
    this.referenceDetail = referenceDetail;
    this.deviceType = deviceType;
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

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public DeviceReferenceDetail getReferenceDetail() {
    return referenceDetail;
  }

  public void setReferenceDetail(DeviceReferenceDetail referenceDetail) {
    this.referenceDetail = referenceDetail;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }
}
