B4S-Android-SDK
===============

Beacon4Store SDK for Android, by Ezeeworld, see www.beacon4store.com

## Requirements

The B4S SDK uses **Bluetooth 4.0 BLE**, also called Low Energy or Bluetooth Smart Ready. It is available on most new Android devices, with a minimum of **Android 4.3**. 

The B4S SDK is a compiled JAR that can be dropped (together with its dependencies) directly into the `libs` folder in Eclipse, Android Studio or compiled with Ant. The SDK is currently targetting API level 19 (Android 4.4) with minimal API level 18 (Android 4.3) for Bluetooth BLE support.

The SDK depends on the Jackson and EventBus libraries, which are all Apache License 2.0-licensed. Finally, it depends on the Google Play Services for its Location Services, Advertiser ID and Cloud Messaging support. See instructions below how to obtain and add this, as it is not included in the B4S SDK package.

## Installation

### Add jar libraries
1. Open your application project
2. Drop the jar files found in `sdk/libs` directly into the `libs` folder of your existing Android project. When using Gradle to build you may use the following Maven dependencies instead of the separate jar files:

   ```gradle
   dependencies {
      compile 'de.greenrobot:eventbus:2.3.0'
      compile 'com.squareup.retrofit:retrofit:2.0.0-beta1'
      compile 'com.squareup.retrofit:converter-jackson:2.0.0-beta1'
      compile 'com.google.android.gms:play-services-location:8.1.0'
      compile 'com.google.android.gms:play-services-ads:8.1.0'
      compile "com.google.android.gms:play-services-gcm:8.1.0"
      compile fileTree(dir: 'libs', include: ['*.jar'])
   }
   ```

### Add Google Play Services (Eclipse/Ant)
1. Import the Google Play Services library project. If not installed yet, use the Adroid SDK Manager to install this (currently at version 21). It is advised to copy the library project to your local directory from `android-sdk/extras/google/google_play_services/libproject`. Import the `google-play-services_lib` directory using File -> Import... -> Existing Android Code Into Workspace wizard. Make sure it is marked as Library Project in the Android properties.
2. Open the properties page of your Android project and add the for the google-play-services_lib as library project dependency. The jar files in `libs` should already be adopted into your Android Private Libraries build.

## Usage

### Configure the SDK

1. Open your project AndroidManifest.xml
2. Add the required permissions for access to internet, Bluetooth LE, location and the boot receiver:

   ```xml
   <uses-permission android:name="android.permission.BLUETOOTH" />
   <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
   <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
   <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   <uses-feature android:name="android.hardware.bluetooth_le" android:required="false" />
   ```

3. For location updates, the Google Play Services requires some meta data in the `<application>` tag.

   ```xml
         <meta-data android:name="com.google.android.gms.version"
              android:value="@integer/google_play_services_version" />
   ```

4. Add the B4S services to the `<application>` element to allow background scanning for and interacting with beacons.

   ```xml
      <!-- Background monitoring for beacons -->
      <service
         android:name="com.ezeeworld.b4s.android.sdk.monitor.MonitoringManager"
         android:exported="true"
         tools:ignore="ExportedService">
         <intent-filter>
            <action android:name="com.ezeeworld.b4s.android.sdk.monitor.B4S_ENSURE_SCANNING" />
            <action android:name="com.ezeeworld.b4s.android.sdk.monitor.B4S_QUERY_SCHEDULE" />
            <action android:name="com.ezeeworld.b4s.android.sdk.monitor.B4S_SCHEDULE_RESULT" />
         </intent-filter>
      </service>
      <service
         android:name="com.ezeeworld.b4s.android.sdk.monitor.ScanService"
         android:exported="false" />
      <service
         android:name="com.ezeeworld.b4s.android.sdk.monitor.InteractionService"
         android:exported="true"
         tools:ignore="ExportedService">
         <intent-filter>
            <action android:name="com.ezeeworld.b4s.android.sdk.monitor.B4S_OBSERVATIONS" />
         </intent-filter>
      </service>
      <service
         android:name="com.ezeeworld.b4s.android.sdk.notifications.NotificationService"
         android:exported="false" />

      <activity
         android:name="com.ezeeworld.b4s.android.sdk.monitor.WebViewInteractionActivity"
         android:configChanges="orientation|keyboardHidden"
         android:exported="true"
         android:noHistory="true" />

      <receiver android:name="com.ezeeworld.b4s.android.sdk.monitor.SystemEventReceiver">
         <intent-filter>
            <action android:name="android.intent.action.ACTION_USER_PRESENT" />
            <action android:name="android.intent.action.BOOT_COMPLETED" />
         </intent-filter>
      </receiver>
   ```
5. If you use the push messaging feature of the B4S SDK, also add the push services. Outside of the `<application>` you need additional permissions:

   ```xml
   <uses-permission android:name="android.permission.WAKE_LOCK" />
   <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
   <permission android:name="com.ezeeworld.b4s.android.sdk.gcm.permission.C2D_MESSAGE" android:protectionLevel="signature" />
   ```

   and in the `<application>` tag the push services registration:

   ```xml
      <!-- Push messaging -->
      <receiver
         android:name="com.google.android.gms.gcm.GcmReceiver"
         android:exported="true"
         android:permission="com.google.android.c2dm.permission.SEND">
         <intent-filter>
            <action android:name="com.google.android.c2dm.intent.RECEIVE" />
            <category android:name="com.ezeeworld.b4s.android.sdk.gcm" />
         </intent-filter>
      </receiver>

      <service
         android:name="com.ezeeworld.b4s.android.sdk.push.GcmListenerService"
         android:exported="false">
         <intent-filter>
            <action android:name="com.google.android.c2dm.intent.RECEIVE" />
         </intent-filter>
      </service>
      <service
         android:name="com.ezeeworld.b4s.android.sdk.push.InstanceIDListenerService"
         android:exported="false">
         <intent-filter>
            <action android:name="com.google.android.gms.iid.InstanceID" />
         </intent-filter>
      </service>
   ```

6. The B4S SDK required initialization of the library with the unique application ID (provided by Ezeeworld). It is suggested to do so in the application object. Make sure the application object refers to the `Application` object first, for example:

   ```xml
      <application
         android:name="com.ezeeworld.b4s.android.sdk.sample.SampleApp"
         android:label="@string/app_name" >
   ```

   and in the `Application` instance (here `SampleApp`) `onCreate` method call `init` and make sure the `MonitoringService` runs upon the first app start. Replace `MY-APP-ID` with your unique application ID.

   ```java
         B4SSettings settings = B4SSettings.init(this, "MY-APP-ID");
         settings.setShouldEnforceBluetooth(true); // Turn on Bluetooth when required for background scanning (true by default)
         MonitoringManager.ensureMonitoringService(this);
   ```

   Logging is turned off by default; use the `B4SSettings` object returned on initialization to enable debugging (using `setShouldLogDebug` and `setShouldLogVerbose`) as well as to change various other settings.
   
7. To allow the SDK to show notifications as pop-up over the application when in the foreground, every `Activity` of your application needs a tie-in to the B4S SDK. Specifically, every `Activity` needs to either extends from `B4SNotificationActivity` or manually call through an instance of `B4SNotificationPopup`. In the latter case, ensure that you call through the `onPause` and `onResume` methods of the `B4SNotificationPopup` isntance.

### Application tagging

You can tag your application with the B4S SDK. You can set two values: The first parameter is the event descriptor and the second the user data asociated to the event.

   ```java
      B4S.event("Launch","No Data");
   ```

You can even set your own data dictionnary

   ```java
      Hashtable tags = new Hashtable();
      tags.put("Key1","String1");
      tags.put("Key2","String2");
      tags.put("Key3",4);
      B4STag.event("Test", tags);
   ```

### Push messaging

To enable support for push messages via the Google Cloud Messaging and B4S servers, call set on the `B4SSettings` instance returned by `B4SSettings.init()` (or use `B4SSettings.getInstance()`). By supplying the GCM Sender ID this way, push messaging support will be enabled. While the token registration is fully managed, you need to call this every time (typically in your `Application` object, right after `init()`).

You can retrieve the required Sender ID from the [Google Developer Console](https://console.developers.google.com/). You also need to note the Server API Key and (let us) register it in the B4S backoffice.

### Customer data

B4S can relate beacon interactions to individual customers. Supplying the customer details is typically done on startup and/or after a user signed in to his/her account. In these cases you can update te SDK with this customer data (which is persisted; no need to call every time).

   ```java
   // Still need to set the customer details
   settings.storeCustomerFields(
      "ClientReference#", 
      "UserID", 
      InteractionsApi.B4SGENDER_FEMALE, 
      "LastName", 
      "FirstName", 
      "Email", 
      "Phone", 
      "Address", 
      "City", 
      "Country", 
      "Zipcode", 
      35); // Age
   ```

### Deep linking

By default the SDK will generate interaction notifications directly, such as web links. Deep links are send to the main activity of your application. The following Intent extras are available:

- `MonitoringManager.INTENT_SHOW` - (int) Hash code of the interaction that was matched
- `MonitoringManager.INTENT_INTERACTION` - (String) Unique ID of the interaction that was matched
- `MonitoringManager.INTENT_TITLE` - (String) Message title, defaults to interaction name if no custom title was supplied
- `MonitoringManager.INTENT_MESSAGE` - (String) Message, in which customer name, beacon name, shop name, etc. were already substituted
- `MonitoringManager.INTENT_DATA` - (String) Possibly a data string, often a piece of JSON encoded data
- `MonitoringManager.INTENT_SHOPNAME` - (String) Name of the interaction's shop
- `MonitoringManager.INTENT_SHOPCLIENTREF` - (String) The free-form string set on the shop as client reference
- `MonitoringManager.INTENT_GROUPNAME` - (String) Name of the interaction's group
- `MonitoringManager.INTENT_BEACONNAME` - (String) Name of the matched beacon as configured in the SDK
- `MonitoringManager.INTENT_BEACONID` - (IBeaconID) Beacon identification, including technical name (B4S:XXXX:XXXX), UDID, major and minor
- `MonitoringManager.INTENT_DISTANCE` - (double) Distance estimate in meters

## Sample application

A sample application is included in the `sample-eclipse` and `sample-gradle` directories of this repo. They show, as described above, how to set up the SDK and they include some optional optimalizations, such as enabling debuggin (in `SampleApplication`) and foreground notification popup support (in `LaunchActivity`). Note that it will not run correctly until the application ID has been replaced from `MY-APP-ID` to your unique application ID. You can generate an application ID for each of your applications with 'B4S Manager' application.

## Copyright

Copyright 2014-2015 Ezeeworld. Redistributes compiled versions of Jackson and EventBus libraries under the Apache License 2.0.
