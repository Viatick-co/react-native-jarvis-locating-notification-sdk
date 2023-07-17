import * as React from 'react';

import {
  StyleSheet,
  View,
  Text,
  useColorScheme,
  SafeAreaView,
  Platform,
  PermissionsAndroid,
  StatusBar,
  ScrollView,
  Button,
} from 'react-native';
import {
  startScanService,
  stopScanService,
  getServiceStatus,
  BeaconInfo,
  NotifcationInfo,
} from 'react-native-jarvis-locating-notification-sdk';
import { Colors } from 'react-native/Libraries/NewAppScreen';

import LearnMoreLinkList from './LearnMoreLinkList';
import Header from './Header';

// PLEASE DON'T EXPOSE SDK KEY AS BELOW. THIS IS JUST FOR EXAMPLE.
// MAKE SURE YOU GET SDK_KEY THROUGH YOUR OWN API WHICH REQUIRES AUTHENTICATION
const SDK_KEY = 'a82c113e3422eb33d0577c545fecaab3';

const Section: React.FC<
  React.PropsWithChildren<{
    title: string;
  }>
> = ({ children, title }) => {
  const isDarkMode = useColorScheme() === 'dark';
  return (
    <View style={styles.sectionContainer}>
      <Text
        style={[
          styles.sectionTitle,
          { color: isDarkMode ? Colors.light : Colors.dark },
        ]}
      >
        {title}
      </Text>
      <Text
        style={[
          styles.sectionDescription,
          { color: isDarkMode ? Colors.light : Colors.dark },
        ]}
      >
        {children}
      </Text>
    </View>
  );
};

export default function App() {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const [beaconSignalSet, setBeaconSignalSet] = React.useState<
    Record<string, { beacon: BeaconInfo; time: string }>
  >({});

  const requestPermission = async (): Promise<void> => {
    if (Platform.OS === 'android') {
      if (Platform.Version >= 31) {
        const bleScanGranted = await PermissionsAndroid.check(
          PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN!
        );
        if (!bleScanGranted) {
          await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN!,
            {
              title: 'Bluetooth Scan Permission',
              message: 'To Scan BLE',
              buttonNeutral: 'Ask Me Later',
              buttonNegative: 'Cancel',
              buttonPositive: 'OK',
            }
          );
        }
      }

      if (
        !(await PermissionsAndroid.check(
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION!
        ))
      ) {
        await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION!,
          {
            title: 'Access Fine Location',
            message: 'To Scan BLE',
            buttonNeutral: 'Ask Me Later',
            buttonNegative: 'Cancel',
            buttonPositive: 'OK',
          }
        );
      }

      if (
        !(await PermissionsAndroid.check(
          PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION!
        ))
      ) {
        await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION!,
          {
            title: 'Access Coarse Location',
            message: 'To Scan BLE',
            buttonNeutral: 'Ask Me Later',
            buttonNegative: 'Cancel',
            buttonPositive: 'OK',
          }
        );
      }

      if (
        !(await PermissionsAndroid.check(
          PermissionsAndroid.PERMISSIONS.ACCESS_BACKGROUND_LOCATION!
        ))
      ) {
        await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.ACCESS_BACKGROUND_LOCATION!,
          {
            title: 'Access Background Location',
            message: 'To Scan BLE',
            buttonNeutral: 'Ask Me Later',
            buttonNegative: 'Cancel',
            buttonPositive: 'OK',
          }
        );
      }

      const locgranted = await PermissionsAndroid.check(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION!
      );
      console.log('ACCESS_FINE_LOCATION', locgranted);
      const blegranted = await PermissionsAndroid.check(
        PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN!
      );
      console.log('BLUETOOTH_SCAN', blegranted);
    } else {
      // if running ios
      // make sure your user set always allow location to allow app can run in background
    }
  };

  const onProximityPush = (
    device: BeaconInfo,
    noti: NotifcationInfo,
    time: number
  ) => {
    console.log(device, noti, time);
  };

  const onBeaconSignal = (device: BeaconInfo, time: number) => {
    console.log('signal', device, time);

    setBeaconSignalSet((prevState) => {
      const { uuid, minor, major } = device;

      const dateTime = new Date(time);
      const key = `${uuid}-${major}-${minor}`;

      return {
        ...prevState,
        [key]: {
          beacon: device,
          time: dateTime.toTimeString(),
        },
      };
    });
  };

  const startJarvisSdk = async (): Promise<void> => {
    setBeaconSignalSet({});

    const success = await startScanService(
      SDK_KEY,
      3,
      'ic_launcher_round',
      'Jarvis Example',
      'We are running foreground service...',
      onProximityPush,
      onBeaconSignal
    );
    console.log('startJarvisSdk', success);
  };

  const getJarvisServiceStatus = async (): Promise<void> => {
    const status = await getServiceStatus();
    console.log('status', status);
  };

  React.useEffect(() => {
    requestPermission();
  }, []);

  const beacons = Object.values(beaconSignalSet);

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}
      >
        <Header />
        <View>
          {/*<Section title="Usage">*/}
          {/*  Visit <Text style={styles.highlight}>App.tsx</Text> in folder{' '}*/}
          {/*  <Text style={styles.highlight}>example</Text> to see example code*/}
          {/*</Section>*/}
          {/*<Section title="Learn More">*/}
          {/*  Read the <Text style={styles.highlight}>README</Text> to discover*/}
          {/*  all functions and it's prerequisite:*/}
          {/*</Section>*/}
          {/*<LearnMoreLinkList />*/}
          {/*<Section title="Example">*/}
          {/*  Press Button <Text style={styles.highlight}>START</Text> or{' '}*/}
          {/*  <Text style={styles.highlight}>STOP</Text> to start and stop SDK*/}
          {/*</Section>*/}

          <View style={{ flexDirection: 'row', padding: 10 }}>
            <View style={{ flex: 0.33, padding: 10 }}>
              <Button title="START" onPress={startJarvisSdk} />
            </View>
            <View style={{ flex: 0.33, padding: 10 }}>
              <Button title="STOP" onPress={stopScanService} />
            </View>
            <View style={{ flex: 0.33, padding: 10 }}>
              <Button title="STATUS" onPress={getJarvisServiceStatus} />
            </View>
          </View>

          <View>
            <View
              style={{ flexDirection: 'column', padding: 10, width: '100%' }}
            >
              {beacons.map((aBeacon) => {
                const { beacon: data, time } = aBeacon;

                const { uuid, major, minor, distance } = data;

                const key = `${uuid}-${major}-${minor}`;
                return (
                  <View key={key} style={styles.beaconRow}>
                    <View>
                      <Text>
                        {major} - {minor}
                      </Text>
                    </View>
                    <View>
                      <Text>{distance}m</Text>
                    </View>
                    <View>
                      <Text>{time}</Text>
                    </View>
                  </View>
                );
              })}
            </View>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    marginTop: 0,
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  beaconRow: {
    borderStyle: 'solid',
    borderBottomWidth: 2,
    borderColor: 'blue',
    width: '100%',
    paddingBottom: 5,
  },
  highlight: { fontWeight: '700' },
});
