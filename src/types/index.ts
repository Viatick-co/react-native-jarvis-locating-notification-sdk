interface BeaconInfo {
  uuid: string;
  minor: number;
  major: number;
  distance?: number;
}

interface NotifcationInfo {
  title: string;
  description: string;
}

type ServiceBeaconInfo = {
  uuid: string;
  major: number;
  minor: number;
  distance: number;
  lastSignalTime: number;
};

type JarvisServiceStatus = {
  lastDetectedSignalDateTime: number;
  serviceRunning: boolean;
  beacons: ServiceBeaconInfo[];
};

export { BeaconInfo, NotifcationInfo, JarvisServiceStatus };
