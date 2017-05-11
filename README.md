# Landfill e-Forms Application Project (Mobile Application)

The Landfill e-Forms Mobile Application is a data entry application for the City of Los Angeles, Department of Sanitation inspectors to use out on the field while performing land surveys.

## Table of Contents
* [Prerequisites](https://github.com/landfill-eforms/landfill-android-app/blob/master/README.md#prerequisites)
	* [Mobile Application Development](https://github.com/landfill-eforms/landfill-android-app/blob/master/README.md#mobile-application-development)
	* [Mobile Application Database](https://github.com/landfill-eforms/landfill-android-app/blob/master/README.md#mobile-application-database)
* [Environment Setup](https://github.com/landfill-eforms/landfill-android-app/blob/master/README.md#environment-setup)
* [Application Installation](https://github.com/landfill-eforms/landfill-android-app/blob/master/README.md#application-installation)
	* [Install through Android Studio](https://github.com/landfill-eforms/landfill-android-app/blob/master/README.md#install-android-studio)
	* [Install through File Download](https://github.com/landfill-eforms/landfill-android-app/blob/master/README.md#install-file-download)

## Prerequisites
This sections lists the prerequisites for each component of the mobile application separately.

### Mobile Application Development
* Java 8 (JDK recommended). Java 8 can downloaded [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
* Android Studio 2.3.x can be downloaded [here](https://developer.android.com/studio/index.html). The Android Studio setup wizard will install the latest Android SDK release and was the preferred environment for this project. The use of another IDE, such as Eclipse or IntelliJ, for development will require the Android SDK to be installed separately.

### Mobile Application Database
The Android SDK and Android emulators (such as HAXM) both include the sqlite3 database tool. In development, 
```import android.database.sqlite``` would import the library. More documentation on sqlite3 can be found [here](https://developer.android.com/reference/android/database/sqlite/package-summary.html).

## Environment Setup
To open the project, start Android Studio and select:

```
Open an existing Android Studio project
```

In the directory, go to the folder in which the repository was cloned, and select:

```
landfill-android-app
```

NOTE: The first time the project is opened, Gradle will take a bit to build the project.

The ```app``` folder will contain three folders: manifests, java, and res.
```manifests``` will contain the xml file with all of the application activities.
```java``` will contain the Java code used to develop the application.
```res``` will contain the xml files used to layout each application activity.

## Application Installation
For application installation, a phone running Android 5.1 (Lollipop) or higher is required. 

NOTE: If there are any installation issues, you may have to first go into the device's security setting and enable ```Unknown sources``` which allows the installation of applications from sources other than the Google Play Store.

### Install through Android Studio

Open Android Studio, and open the ```landfill-android-app``` project. If you have not set up your Android development environment, please see the [Environment Setup](https://github.com/landfill-eforms/landfill-android-app/blob/master/README.md#environment-setup) instructions above.

Connect phone to computer running Android Studio and make sure it recognizes the device.

With device connected, in the menus go to ```Build > Build APK``` and application will be installed on device. 

NOTE: If there has been a change in the database, go to the Application Manager on device and Clear All application data to avoid application crashing. The application will be installed but will crash if there are conflicts in the database. Be sure to clear data for clean install.

### Install through File Download

With no device connected, in the menus go to ```Build > Build APK``` and application .apk file will be downloaded inside the project folder. To retrieve .apk file go to:

```
landfill-android-app > app > build > outputs > apk
```

Send .apk file to selected device and open .apk file for immediate installation.

NOTE: If there has been a change in the database, go to the Application Manager on device and Clear All application data to avoid application crashing. The application will be installed but will crash if there are conflicts in the database. Be sure to clear data for clean install.

