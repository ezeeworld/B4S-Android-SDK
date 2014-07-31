B4S-Android-SDK
===============

Beacon4Store SDK for Android, by Ezeeworld, see www.beacon4store.com

## Requirements

The B4S SDK uses **Bluetooth 4.0 BLE**, also called Low Energy or Bluetooth Smart Ready. It is available on most new Android devices, with a minimum of **Android 4.3**. Almost all popular newer devices are supported, such as Samsung's Galaxy S3/4/5 and variants, Note 2/3/10.1, LG G2/G3/Nexus 4/Nexus5, Moto G/E/X, HTC One and variants, Sony Z/Z1/Z2 and variants and many more.

The B4S SDK is a compiled JAR that can be dropped (together with its dependencies) directly into the `libs` folder in Eclipse, Andorid Studio or compiled with Ant. The SDK is currently targetting API level 19 (Android 4.4) with minimal API level 18 (Android 4.3) for Bluetooth BLE support.

The SDK depends on the Jackson, Linear Algebra for Java libraries and EventBus libraries, which are all Apache License 2.0-licensed. Finally, it depends on the Google Play Services for its Location Services support. See instructions below how to obtain and add this, as it is not included in the SDK package.

## Installation

### Add jar libraries
1. Open your application in Eclipse
2. Drop the jar files found in `sdk/libs` directly into the `libs` fodler of your existing Android project.

### Add Google Play Services
1. Import the Google Play Services library project. If not installed yet, use the Adroid SDK Manager to install this (currently at version 18). It is advised to copy the library project to your local directory from `android-sdk/extras/google/google_play_services/libproject`. Import the `google-play-services_lib` directory using File -> Import... -> Existing Android Code Into Workspace wizard. Make sure it is marked as Library Project in the Android properties.
2. Open the properties page of your Android project and add teh for the google-play-services_lib as library project dependency. The jar files in `libs` should already be adopted into your Android Private Libraries build..

## Usage

### Configure the SDK

1. Open your project AndroidManifest.xml
2. Add the required permissions for access to internet, Bluetooth BLE, location and the boot receiver:
```xml
	<uses-permission android:name="android.permission.BLUETOOTH" />
	<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
	<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
	<uses-feature android:name="android.hardware.bluetooth_le" android:required="true"/>
```
3. For location updates the Google Play Services requires some meta data in the `<application>` tag.
```xml
		<meta-data android:name="com.google.android.gms.version"
           android:value="@integer/google_play_services_version" />
```

4. Add the B4S `MonitoringService` to the `<application>` element to allow background scanning for beacons.
```xml
		<service
			android:name="com.ezeeworld.b4s.android.sdk.monitor.MonitoringService"
			android:exported="false" />
```
5. It is recommended to add the B4S `SystemEventReceiver` to ensure the background scanning is started when the device is rebooted.
```xml
		<receiver android:name="com.ezeeworld.b4s.android.sdk.monitor.SystemEventReceiver" >
			<intent-filter>
				<action android:name="android.intent.action.ACTION_USER_PRESENT" />
				<action android:name="android.intent.action.BOOT_COMPLETED" />
			</intent-filter>
		</receiver>
```
6. Your application will be send broadcast messages when the B4S SDK registers a matched interaction. Add to an existing broadcast receiver or create a new one to catch these. An example is given below that will show user notifications.
```xml
		<receiver
			android:name="com.ezeeworld.b4s.android.sdk.sample.B4SNotificationReceiver"
			android:exported="false" >
			<intent-filter>
				<action android:name="com.ezeeworld.b4s.android.sdk.monitor.B4S_NOTIFICATION" />
			</intent-filter>
		</receiver>
```
7. The B4S SDK required initialization of the library with the unique application ID (provided by Ezeeworld). It is suggested to do so in the application object. Make sure the application object refers to the `Application` object first.
```xml
	<application
		android:name="com.ezeeworld.b4s.android.sdk.sample.SampleApp"
		android:label="@string/app_name" >
```
and in the `Application` instance (here `SampleApp`) `onCreate` method call `init` and make sure the `MonitoringService` runs upon the first app start. Replace `MY-APP-ID` with your unique application ID.
```java
		B4SSettings.init(this, "MY-APP-ID", "1");
		MonitoringManager.ensureMonitoringService(this);
```

### Receive notifications

The SDK sends broadcats to yur application when a beacon is matched, such that your application can itself generate the proper notification for the user.

As specified above in the `AndroidManifest.xml` we define a `B4SNotificationReceiver` which parses the notifications. This extends from `BroadcastReceiver` directly and on the `onReceive` method the broadcasted messages are received as `Intent`s. At the moment every `Intent` will contain:

- `MonitoringManager.INTENT_SHOW` - Unique ID of the interaction that was matched
- `MonitoringManager.INTENT_TITLE` - Interaction name
- `MonitoringManager.INTENT_MESSAGE` - Interaction message, in which customer name, beacon name, shop name, etc. were already substituted
- `MonitoringManager.INTENT_DATA` - Possibly a data string, often a piece of JSON encoded data

To simply show a user notification every time a message is broadcasted, implement in your `onReceive`:
```java
		NotificationManager notifications = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		int id = intent.getIntExtra(MonitoringManager.INTENT_SHOW, -1);
		String title = intent.getStringExtra(MonitoringManager.INTENT_TITLE);
		String message = intent.getStringExtra(MonitoringManager.INTENT_MESSAGE);
		notifications.notify(id, new Notification.Builder(context)
				.setSmallIcon(R.drawable.ic_notification)
				.setContentTitle(title)
				.setContentText(message)
				.setAutoCancel(true)
				.build());
```
As normal, the notifications can be extended with sounds, vibrations, etc.

## Sample application

A minimal sample application is included in the `sample` directory of this repo. It implements the notification system described above and includes some optional optimalizations, such as the changing of scan frequency settings (in `SampleApplication`) and a richer notification (in the `B4SNotificationReceiver`). Note that it will not run correctly until the application ID has been replaced from `MY-APP-ID` to your unique ID as provided by Ezeeworld.

## Copyright

Copyright 2014 Ezeeworld. Redistributes compiled versions of Jackson, Linear Algebra for Java libraries and EventBus libraries under the Apache License 2.0.