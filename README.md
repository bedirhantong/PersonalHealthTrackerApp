# Customized Personal Health Tracker

## Table of Contents

* [About](#about)
* [Features](#features)
* [Protoype](#prototype)
* [Installing Epona](#setup)



<a href='https://www.linkpicture.com/view.php?img=LPic645b73f12ea98836219032'><img src='https://www.linkpicture.com/q/Epona_Temp.png' type='image'></a>

## About
- EPONA is a personal health tracker Android application. The application allows the user to monitor the performance for various activities such as running, walking, cycling, swimming.

- A personal health tracker app build with [Kotlin](https://kotlinlang.org/) and [Firebase](https://firebase.google.com/).

## Features

1. Allows users to login and gain access to the application.
   - If the user has forgotten his/her password, he/she can access his/her account by using only one of the following information: email, phone number or username.
2. Activity tracking for running, hiking, cycling, and swimming.
   - EPONA is a customised health tracker application for different activities. It gives freedom to the user by offering flexible preference options.
3. Map tracking during activities.
   - Running activity : EPONA will show the user which paths the user has followed on the map to the current location, how many steps the user has taken in total, average speed, total distance travelled (km), energy consumption and time elapsed while the user is doing the activity.
4. Log storage for past activities.
   - Before you start any activity in EPONA, EPONA asks you whether you want to keep a record of the activity for history. You can access the statistical data of the activities you have saved days, weeks or even months later.
5. Detailed user profile with activity statistics.
   - The profile includes a table of the number of steps taken in the current day by hours and a brief summary of the different activities performed during the day.




## Prototype
- [Protoype](https://www.figma.com/proto/4Os1gplcwPt1cogi9XLT4Y/20200808033_EPONA?page-id=0%3A1&type=design&node-id=3-6&viewport=862%2C395%2C0.22&scaling=scale-down&starting-point-node-id=3%3A6)	is the interactive version of EPONA on Figma.

## Setup
- The first thing you need to do is open the project in Android Studio on your own computer using the project's Github link.
- Then you need an unique Google Maps API key. You can get it from [this](https://developers.google.com/maps/documentation/javascript/get-api-key?hl=tr) website.
- If you don't know how to get **Google Maps API key** for this project then you can also use the same link.
   - If you get your unique Google Maps API key then you should change the value of **google_maps_key** on ___string.xml___ file.
- If you are all set then you can run the EPONA on your device.
- But you have to restrict your **Google Maps API key** from Google Cloud Platform. [Here](https://developers.google.com/maps/api-security-best-practices?hl=tr) you can learn how to restrict your Map API key for android applications.
