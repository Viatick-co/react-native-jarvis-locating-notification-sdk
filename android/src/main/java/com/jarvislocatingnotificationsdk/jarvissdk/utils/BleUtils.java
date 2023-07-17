package com.jarvislocatingnotificationsdk.jarvissdk.utils;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;

import com.jarvislocatingnotificationsdk.jarvissdk.model.PeripheralDetail;

public class BleUtils {
  private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
  private static final int MANUFACTURER_ID = 76;

  public static PeripheralDetail getBeaconFromScanResult(ScanResult scanResult) {
    byte[] scanRecord = scanResult.getScanRecord().getBytes();
    int rssi = scanResult.getRssi();

    StringBuilder sb = new StringBuilder(2 * scanRecord.length);
    for (byte b : scanRecord) {
      sb.append(HEX_DIGITS[(b >> 4 & 0xF)]).append(HEX_DIGITS[(b & 0xF)]);
    }
    String scanRecordAsHex = sb.toString();

    for (int i = 0; i < scanRecord.length; i++) {
      int payloadLength = unsignedByteToInt(scanRecord[i]);
      if ((payloadLength == 0) || (i + 1 >= scanRecord.length)) {
        break;
      }

      if (unsignedByteToInt(scanRecord[(i + 1)]) != 255) {
        i += payloadLength;
      } else {
        if (payloadLength == 26 || payloadLength == 27) {

          if ((unsignedByteToInt(scanRecord[(i + 2)]) == 76) &&
            (unsignedByteToInt(scanRecord[(i + 3)]) == 0) &&
            (unsignedByteToInt(scanRecord[(i + 4)]) == 2) &&
            (unsignedByteToInt(scanRecord[(i + 5)]) == 21)) {
            int startIndex = (i + 6) * 2;
            String proximityUUID = String.format("%s-%s-%s-%s-%s",
              new Object[]{scanRecordAsHex.substring(startIndex, startIndex + 8),
                scanRecordAsHex.substring(startIndex + 8, startIndex + 12),
                scanRecordAsHex.substring(startIndex + 12, startIndex + 16),
                scanRecordAsHex.substring(startIndex + 16, startIndex + 20),
                scanRecordAsHex.substring(startIndex + 20, startIndex + 32)});

            int major = unsignedByteToInt(scanRecord[(i + 22)]) * 256 + unsignedByteToInt(scanRecord[(i + 23)]);
            int minor = unsignedByteToInt(scanRecord[(i + 24)]) * 256 + unsignedByteToInt(scanRecord[(i + 25)]);
            int measuredPower = (int) scanRecord[(i + 26)];
            double accuracy = calculateAccuracy(rssi, measuredPower);

            String mac = scanResult.getDevice().getAddress();
            return new PeripheralDetail(mac, proximityUUID, major, minor, accuracy);
          }

//            Log.d(TAG, "Manufacturer specific data does not start with 0x4C000215");
          break;
        }

//          Log.d(TAG, "Manufacturer specific data should have 26 bytes length");
        break;
      }
    }

    return null;
  }

//  public static ScanFilter getScanFilterUUID(final String uuid) {
//    final ScanFilter.Builder builder = new ScanFilter.Builder();
//
//    // the manufacturer data byte is the filter!
//    final byte[] manufacturerData = new byte[]
//      {
//        0, 0,
//
//        // uuid
//        0, 0, 0, 0,
//        0, 0,
//        0, 0,
//        0, 0, 0, 0, 0, 0, 0, 0,
//
//        // major
//        0, 0,
//
//        // minor
//        0, 0,
//
//        0
//      };
//
//    // the mask tells what bytes in the filter need to match, 1 if it has to match, 0 if not
//    final byte[] manufacturerDataMask = new byte[]
//      {
//        0, 0,
//
//        // uuid
//        1, 1, 1, 1,
//        1, 1,
//        1, 1,
//        1, 1, 1, 1, 1, 1, 1, 1,
//
//        // major
//        0, 0,
//
//        // minor
//        0, 0,
//
//        0
//      };
//
//    // copy UUID (with no dashes) into data array
//    System.arraycopy(hexStringToByteArray(uuid.replaceAll("-", "")), 0, manufacturerData, 2, 16);
//
//
//    builder.setManufacturerData(
//      MANUFACTURER_ID,
//      manufacturerData,
//      manufacturerDataMask);
//
//    return builder.build();
//  }

  public static ScanFilter getScanFilter(String uuidStr, int major, int minor)
  {
    final ScanFilter.Builder builder = new ScanFilter.Builder();

    // the manufacturer data byte is the filter!
    final byte[] manufacturerData = new byte[]
      {
        0,0,

        // uuid
        0,0,0,0,
        0,0,
        0,0,
        0,0,0,0,0,0,0,0,

        // major
        0,0,

        // minor
        0,0,

        0
      };

    // the mask tells what bytes in the filter need to match, 1 if it has to match, 0 if not
    final byte[] manufacturerDataMask = new byte[]
      {
        0,0,

        // uuid
        1,1,1,1,
        1,1,
        1,1,
        1,1,1,1,1,1,1,1,

        // major
        1,1,

        // minor
        0,0,

        0
      };

    // copy UUID (with no dashes) into data array
    System.arraycopy(hexStringToByteArray(uuidStr.replaceAll("-","")), 0, manufacturerData, 2, 16);

    // copy major into data array

    System.arraycopy(integerToByteArray(major), 0, manufacturerData, 18, 2);

    // copy minor into data array
//    System.arraycopy(integerToByteArray(minor), 0, manufacturerData, 20, 2);

    builder.setManufacturerData(
      MANUFACTURER_ID,
      manufacturerData,
      manufacturerDataMask);

//    ScanFilter.Builder mBuilder = new ScanFilter.Builder();
//    ByteBuffer mManufacturerData = ByteBuffer.allocate(23);
//    ByteBuffer mManufacturerDataMask = ByteBuffer.allocate(24);
//    byte[] uuidByte = hexStringToByteArray("F7826DA64FA24E988024BC5B71E0893E");
//    mManufacturerData.put(0, (byte)0xBE);
//    mManufacturerData.put(1, (byte)0xAC);
//    for (int i=2; i<=17; i++) {
//      mManufacturerData.put(i, uuidByte[i-2]);
//    }
//    for (int i=0; i<=17; i++) {
//      mManufacturerDataMask.put((byte)0x01);
//    }
//    mBuilder.setManufacturerData(224, mManufacturerData.array(), mManufacturerDataMask.array());
//
    return builder.build();
  }

  private static int unsignedByteToInt(byte value) {
    return value & 0xFF;
  }

  private static double calculateAccuracy(int rssi, int measuredPower) {
    int RSSI = Math.abs(rssi);

    if (RSSI == 0.0D) {
      return -1.0D;
    }


    double ratio = RSSI * 1.0D / measuredPower;
    if (ratio < 1.0D) {
      return Math.pow(ratio, 8.0D);
    }

    double accuracy = 0.69976D * Math.pow(ratio, 7.7095D) + 0.111D;
    return accuracy;
  }

  private static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
        + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

  /**
   * Convert major or minor to hex byte[]. This is used to create a {@link ScanFilter}.
   *
   * @param value major or minor to convert to byte[]
   * @return byte[]
   */
  private static byte[] integerToByteArray(final int value) {
    final byte[] result = new byte[2];
    result[0] = (byte) (value / 256);
    result[1] = (byte) (value % 256);

    return result;
  }
}
