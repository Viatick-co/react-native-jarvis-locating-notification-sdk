package com.jarvislocatingnotificationsdk.jarvissdk.services;

import com.jarvislocatingnotificationsdk.jarvissdk.model.PeripheralDetail;

public interface BleScannerServiceCallback {

  void onStarted(boolean success);

  void onBeaconSignalDetected(PeripheralDetail ble, long dateTime);

  void onProximityPush(PeripheralDetail ble, String notificationTitle, String notificationDescription, long dateTime);

  void onDestroyed();

}
