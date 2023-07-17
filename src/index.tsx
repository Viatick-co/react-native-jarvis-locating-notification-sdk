import { NativeEventEmitter, NativeModules, Platform } from 'react-native';
import { BeaconInfo, NotifcationInfo, JarvisServiceStatus } from './types';

const LINKING_ERROR =
  `The package 'react-native-jarvis-locating-notification-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({
    ios: "- You have run 'pod install'\n",
    default: '',
  }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const JarvisLocatingNotificationSdk =
  NativeModules.JarvisLocatingNotificationSdk
    ? NativeModules.JarvisLocatingNotificationSdk
    : new Proxy(
        {},
        {
          get() {
            throw new Error(LINKING_ERROR);
          },
        }
      );

const eventEmitter = new NativeEventEmitter(NativeModules.JarvisTemplateAppSdk);
let proximityPushCallback: (
  device: BeaconInfo,
  notification: NotifcationInfo,
  time: number
) => void;

let beaconSignalCallback: (device: BeaconInfo, time: number) => void;

eventEmitter.addListener('BeaconInformation', (event) => {
  const { uuid, minor, major, time, title, description } = event;
  const device: BeaconInfo = { uuid, minor, major };
  const notification: NotifcationInfo = { title, description };

  if (proximityPushCallback) {
    proximityPushCallback(device, notification, time);
  }
});
eventEmitter.addListener('BeaconSignal', (event) => {
  const { uuid, minor, major, distance, time } = event;
  const device: BeaconInfo = { uuid, minor, major, distance };
  if (beaconSignalCallback) {
    beaconSignalCallback(device, time);
  }
});

const startScanService = async (
  sdkKey: string,
  locatingRange: number,
  notificationIconName: string,
  notificationTitle: string,
  notificationDescription: string,
  onProximityPush: (
    device: BeaconInfo,
    notification: NotifcationInfo,
    time: number
  ) => void,
  onBeaconSignal?: (device: BeaconInfo, time: number) => void
): Promise<boolean> => {
  proximityPushCallback = onProximityPush;
  if (onBeaconSignal) {
    beaconSignalCallback = onBeaconSignal;
  } else {
    beaconSignalCallback = null;
  }
  return await JarvisLocatingNotificationSdk.startScanService(
    sdkKey,
    locatingRange,
    notificationIconName,
    notificationTitle,
    notificationDescription
  );
};

const stopScanService = async (): Promise<void> => {
  // @ts-ignore
  proximityPushCallback = null;
  beaconSignalCallback = null;
  return await JarvisLocatingNotificationSdk.stopScanService();
};

const getServiceStatus = async (): Promise<JarvisServiceStatus> => {
  return await JarvisLocatingNotificationSdk.getScanServiceStatus();
};

function multiply(a: number, b: number): Promise<number> {
  return JarvisLocatingNotificationSdk.multiply(a, b);
}

export {
  startScanService,
  stopScanService,
  getServiceStatus,
  BeaconInfo,
  NotifcationInfo,
  multiply,
};
