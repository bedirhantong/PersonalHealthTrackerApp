# Customized Personal Health Tracker

## Table of Contents

* [About](#about)
* [Features](#features)
* [Protoype](#prototype)
* [Presentation](#presentation)
* [Managing](#managing)
* [Libraries](#libraries)
* [Installing Epona](#setup)



<a href='https://www.linkpicture.com/view.php?img=LPic645b73f12ea98836219032'><img src='https://www.linkpicture.com/q/Epona_Temp.png' type='image'></a>

## About
- EPONA is a personal health tracker Android application. The application allows the user to monitor the performance for various activities such as running, step counting, diving, breath taking.

- A personal health tracker app build with [Kotlin](https://kotlinlang.org/) and [Firebase](https://firebase.google.com/).

## Features

1. `Allows users to login and gain access to the application.`
   - If the user has forgotten his/her password, he/she can access his/her account by using only one of the following information: email, phone number or username.
2. `Activity tracking for running, hiking, cycling, and swimming.`
   - EPONA is a customised health tracker application for different activities. It gives freedom to the user by offering flexible preference options.
3. `Map tracking during activities.`
   - Running activity : EPONA will show the user which paths the user has followed on the map to the current location, how many steps the user has taken in total, average speed, total distance travelled (km), energy consumption and time elapsed while the user is doing the activity.
4. `Log storage for past activities.`
   - Before you start any activity in EPONA, EPONA asks you whether you want to keep a record of the activity for history. You can access the statistical data of the activities you have saved days, weeks or even months later.
5. `Detailed user profile with activity statistics.`
   - The profile includes a table of the number of steps taken in the current day by hours and a brief summary of the different activities performed during the day.




## Prototype
- [Protoype](https://www.figma.com/proto/4Os1gplcwPt1cogi9XLT4Y/20200808033_EPONA?page-id=0%3A1&type=design&node-id=3-6&viewport=862%2C395%2C0.22&scaling=scale-down&starting-point-node-id=3%3A6)	is the interactive version of EPONA on Figma.


## Presentation
- You can access the presentation video of EPONA from [here](https://youtu.be/-CcyrmyBTVE)


## Managing
- When you finish any healthy activity and save it to the history, you will skipping to the [`AddActivitiesAndShowToUser.kt`](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/data/AddActivitiesAndShowToUser.kt) to see the statistics of your healthy activity and has an option to save it to the history.
- If you click save button, you will be skipping to the [`ProfileFragment.kt`](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/ui/profile/ProfileFragment.kt). In the background I will add it to the `Firestore` with a query. That lists the healthy activities based on their activity type on `Firestore`.
- When you save the activity to the history and skip to the [`ProfileFragment.kt`](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/ui/profile/ProfileFragment.kt) you will see that the latest healthy activity that you have done will be show up at the top of previous activities. Same thing happens in  [`MainScreenFragment.kt`](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/ui/mainPage/MainScreenFragment.kt). How is that possible?
   - Skipping to the  [`ProfileFragment.kt`](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/ui/profile/ProfileFragment.kt) and healthy activity statistic pulling from `Firestore` is happening simultaneously. First I get the statistics of current user. And create an object of [HealthyActivity.kt](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/data/HealthyActivity.kt), after than that I add this healthy activity to the my healthy activity list. So that I can update [`ProfileFragment.kt`](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/ui/profile/ProfileFragment.kt) and [HealthyActivity.kt](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/data/HealthyActivity.kt) with using [HealthyActivityAdapter.kt](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/adapter/HealthyActivityAdapter.kt).
   - In [HealthyActivityAdapter.kt](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/adapter/HealthyActivityAdapter.kt) you can manage the [recycler_view_activity.xml](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/res/layout/recycler_view_activity.xml) for [`ProfileFragment.kt`](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/ui/profile/ProfileFragment.kt) and [`MainScreenFragment.kt`](https://github.com/bedirhantong/PersonalHealthTrackerApp/blob/master/app/src/main/java/com/example/personalhealthtracker/ui/mainPage/MainScreenFragment.kt).



## Libraries

1. `WilliamChart` : is an Android Library to rapidly implement attractive and insightful charts in android applications. You can see its details [here](https://github.com/diogobernardino/williamchart)
2. `Firebase-firestore` : Implemented it so that I can manage Epona's database without any complex query and database implementation.
3. `Easy Permission` : is a wrapper library to simplify basic system permissions logic when targeting Android M or higher. You can see its details [here](https://github.com/googlesamples/easypermissions)
4. `Circular Progress Bar` : allowing to realize a circular ProgressBar in the simplest way possible. You can see the details [here](https://github.com/lopspower/CircularProgressBar)
5. `Google maps location servies` : To get the current user's current location on the map on live.

## Setup
- The first thing you need to do is open the project in Android Studio on your own computer using the project's Github link.
- Then you need an unique Google Maps API key. You can get it from [this](https://developers.google.com/maps/documentation/javascript/get-api-key?hl=tr) website.
- If you don't know how to get **Google Maps API key** for this project then you can also use the same link.
   - If you get your unique Google Maps API key then you should change the value of **google_maps_key** on ___string.xml___ file.
- If you are all set then you can run the EPONA on your device.
- But you have to restrict your **Google Maps API key** from Google Cloud Platform. [Here](https://developers.google.com/maps/api-security-best-practices?hl=tr) you can learn how to restrict your Map API key for android applications.
