package com.jarvislocatingnotificationsdk.jarvissdk.apis;

import android.util.Log;

import com.jarvislocatingnotificationsdk.jarvissdk.apis.response.ApplicationDetail;
import com.jarvislocatingnotificationsdk.jarvissdk.apis.response.DeviceFilter;
import com.jarvislocatingnotificationsdk.jarvissdk.apis.response.DeviceReferenceDetail;
import com.jarvislocatingnotificationsdk.jarvissdk.apis.response.JarvisDevice;
import com.jarvislocatingnotificationsdk.jarvissdk.apis.response.LocatingNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JarvisApi {

  private final String apiHost = "https://jarvis.viatick.com/apis";
  private final OkHttpClient httpClient = new OkHttpClient.Builder().writeTimeout(20, TimeUnit.SECONDS).build();

  private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

  private JarvisApi() {
  }

  public ApplicationDetail getApplicationDetail(String sdkKey) {
    Request request = new Request.Builder()
        .header("Access-Token", sdkKey)
        .url(apiHost + "/account/application/detail")
        .build();

    try {
      Response rp = this.httpClient.newCall(request).execute();

      if (rp.isSuccessful()) {
        String bodyResponse = rp.body().string();
        JSONObject responseObject = new JSONObject(bodyResponse);

        long id = responseObject.getLong("id");
        String name = responseObject.getString("name");
        String email = responseObject.getString("email");
        JSONObject deviceFilterJson = responseObject.getJSONObject("deviceFilter");
        JSONObject beaconFilterJson = deviceFilterJson.getJSONObject("attendance_beacon");
        String uuid = beaconFilterJson.getString("uuid");
        int major = beaconFilterJson.getInt("major");

        Map<String, DeviceFilter> deviceFilterMap = new HashMap<>();
        deviceFilterMap.put("attendance_beacon", new DeviceFilter(uuid, major));

        return new ApplicationDetail(id, name, email, deviceFilterMap);
      } else {
        Log.d("getApplicationDetail", "code" + rp.code());
      }
    } catch (IOException | JSONException e) {
      Log.e("JarvisApi", e.getLocalizedMessage());
    }

    return null;
  }

  public JarvisDevice findDevice(String sdkKey, String bleKey) {
    Request request = new Request.Builder()
        .header("Access-Token", sdkKey)
        .url(apiHost + "/device/get-device?bleKey=" + bleKey)
        .build();

    try {
      Response rp = this.httpClient.newCall(request).execute();

      if (rp.isSuccessful()) {
        String bodyResponse = rp.body().string();
        JSONObject responseObject = new JSONObject(bodyResponse);

        long id = responseObject.getLong("id");
        String name = responseObject.getString("name");
        String mac = responseObject.getString("mac");
        String deviceType = responseObject.getString("deviceType");

        JSONObject referenceObject = responseObject.getJSONObject("referenceDetail");
        String referenceId = referenceObject.getString("id");
        String referenceName = referenceObject.getString("name");
        DeviceReferenceDetail referenceDetail = new DeviceReferenceDetail(referenceId, referenceName);

        return new JarvisDevice(id, name, mac, referenceDetail, deviceType);
      } else {
        Log.d("JarvisApi", "findDevice code " + rp.code());
      }
    } catch (IOException | JSONException e) {
      Log.e("JarvisApi", e.getLocalizedMessage());
    }

    return null;
  }

  public LocatingNotification findNotificationByDevice(String sdkKey, String uuid, int major, int minor) {
    JSONObject bodyJson = new JSONObject();
    try {
      bodyJson.put("uuid", uuid);
      bodyJson.put("major", major);
      bodyJson.put("minor", minor);
    } catch (JSONException e) {
      Log.e("JarvisApi", e.getLocalizedMessage());
    }

    RequestBody body = RequestBody.create(bodyJson.toString(), JSON_MEDIA_TYPE);
    Request request = new Request.Builder()
      .header("Access-Token", sdkKey)
      .url(apiHost + "/resource/locating-notification/find-by-device")
      .post(body)
      .build();

    try {
      Response rp = this.httpClient.newCall(request).execute();

      if (rp.isSuccessful()) {
        String bodyResponse = rp.body().string();
        JSONObject responseObject = new JSONObject(bodyResponse);

        long notificationId = responseObject.getLong("id");
        String title = responseObject.getString("title");
        String description = responseObject.getString("description");

        return new LocatingNotification(notificationId, title, description);
      } else {
        Log.d("findByDevice", "code" + rp.code());
      }
    } catch (IOException | JSONException e) {
      Log.e("JarvisApi", e.getLocalizedMessage());
    }

    return null;
  }

  public void updatePersonnelGps(String sdkKey, String personnelId, String lat, String lng) {
    JSONObject bodyJson = new JSONObject();
    try {
      bodyJson.put("id", personnelId);
      bodyJson.put("gpsLat", lat);
      bodyJson.put("gpsLng", lng);
    } catch (JSONException e) {
      Log.e("JarvisApi", e.getLocalizedMessage());
    }

    RequestBody body = RequestBody.create(bodyJson.toString(), JSON_MEDIA_TYPE);
    Request request = new Request.Builder()
      .header("Access-Token", sdkKey)
      .url(apiHost + "/resource/personnel/edit")
      .put(body)
      .build();

    try {
      Response rp = this.httpClient.newCall(request).execute();
      if (rp.isSuccessful()) {
        Log.d("JarvisApi", "update personnel gps successfully - "  + personnelId);
      } else {
        Log.d("JarvisApi", "Update personnel gps code" + rp.code());
      }
    } catch (IOException e) {
      Log.e("JarvisApi", e.getLocalizedMessage());
    }
  }

  public static JarvisApi getInstance() {
    return SingletonHelper.INSTANCE;
  }

  private static class SingletonHelper {
    private static final JarvisApi INSTANCE = new JarvisApi();
  }

}
