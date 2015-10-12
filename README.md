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
       compile 'de.greenrobot:eventbus:2.2.1'
       compile 'org.codehaus.jackson:jackson-core-asl:1.9.13'
       compile 'org.codehaus.jackson:jackson-mapper-asl:1.9.13'
       compile 'org.la4j:la4j:0.4.9'
       compile 'com.google.android.gms:play-services-location:6.5.87'
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
      <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
      <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
      <uses-feature android:name="android.hardware.bluetooth_le" android:required="false" />
   ```

3. For location updates, the Google Play Services requires some meta data in the `<application>` tag.

   ```xml
         <meta-data android:name="com.google.android.gms.version"
              android:value="@integer/google_play_services_version" />
   ```

4. Add the B4S services to the `<application>` element to allow background scanning for beacons.

   ```xml
         <service
            android:name="com.ezeeworld.b4s.android.sdk.monitor.MonitoringManager"
            android:exported="true">
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
            android:exported="true">
            <intent-filter>
               <action android:name="com.ezeeworld.b4s.android.sdk.monitor.B4S_OBSERVATIONS" />
            </intent-filter>
         </service>
   ```
   
   Note that the `MonitoringManager` and `InteractionService` are exported services (and since B4S SDK version 1.1.23 do not require a custom permission).
5. It is recommended to add the B4S `SystemEventReceiver` to ensure the background scanning is started when the device is rebooted.

   ```xml
         <receiver android:name="com.ezeeworld.b4s.android.sdk.monitor.SystemEventReceiver" >
            <intent-filter>
               <action android:name="android.intent.action.ACTION_USER_PRESENT" />
               <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
         </receiver>
   ```

6. If you use the layer-style notifications for interactions where a web view is opened as in-app activity, also add the web view SDK activity to your manifest.

   ```xml
         <activity
            android:name="com.ezeeworld.b4s.android.sdk.monitor.WebViewInteractionActivity"
            android:configChanges="orientation|keyboardHidden"
            android:noHistory="true" />
   ```

7. The B4S SDK required initialization of the library with the unique application ID (provided by Ezeeworld). It is suggested to do so in the application object. Make sure the application object refers to the `Application` object first.

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
   settings.storeCustomerFields(this, 
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

Special care needs to be taken if the SDK-generated notifications should be shown as pop-up when using the application in the foreground, rather than as Android notification. To implement this behaviour, **every** `Activity` in which notification popups are allowed should extend from the `B4SNotificationActivity` base class. Alternatively, if your activity already extends another class, you may instantiate a `B4SNotificationPopup` instance in the `onCreate` method and call through its `onResume` and `onPause` methods appropriatedly.

## Sample application

A sample application is included in the `sample-eclipse` and `sample-gradle` directories of this repo. They show, as described above, how to set up the SDK and they include some optional optimalizations, such as enabling debuggin (in `SampleApplication`) and foreground notification popup support (in `LaunchActivity`). Note that it will not run correctly until the application ID has been replaced from `MY-APP-ID` to your unique application ID. You can generate an application ID for each of your applications with 'B4S Manager' application.

## Copyright

Copyright 2014-2015 Ezeeworld. Redistributes compiled versions of Jackson and EventBus libraries under the Apache License 2.0.
