package com.jarvislocatingnotificationsdk.jarvissdk.model;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

public class PeripheralDetail {

  private String uuid;
  private int major;
  private int minor;
  private double distance;
  private long lastSignalTime;

  private String mac;

  private String personnelId;

  public PeripheralDetail() {}

  public PeripheralDetail(String mac, String uuid, int major, int minor, double distance) {
    this.mac = mac;
    this.uuid = uuid;
    this.major = major;
    this.minor = minor;
    this.distance = distance;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
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

  public int getMinor() {
    return minor;
  }

  public void setMinor(int minor) {
    this.minor = minor;
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public long getLastSignalTime() {
    return lastSignalTime;
  }

  public void setLastSignalTime(long lastSignalTime) {
    this.lastSignalTime = lastSignalTime;
  }

  public String  getKey() {
    return this.uuid.toLowerCase().replaceAll("-", "") + "-" + this.major + "-" + this.minor;
  }

  public WritableMap toWritableMap() {
    WritableMap map = new WritableNativeMap();
    map.putString("uuid", this.uuid);
    map.putInt("major", this.major);
    map.putInt("minor", this.minor);
    map.putDouble("distance", this.distance);
    map.putDouble("lastSignalTime", this.lastSignalTime);

    return map;
  }

  public String getPersonnelId() {
    return personnelId;
  }

  public void setPersonnelId(String personnelId) {
    this.personnelId = personnelId;
  }

}
