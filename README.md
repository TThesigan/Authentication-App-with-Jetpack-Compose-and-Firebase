## Sign in & Sign up App with Kotlin Jetpack Compose and Firebase


This project is a simple Android application developed in Kotlin using Jetpack Compose for the UI and that demonstrates Firebase Realtime Database integration and authentication using sign-up and sign-in functionality 



### Built With

* Kotlin: 1.9.0
* Jetpack Compose
* Firebase
* Android SDK minimum: API level 24 or above
* Gradle: 8.6



### Project setup

* Should have a firebase account
* Delete current "google-services" json from "DeltaTestApp/app/" location
* Android studio go to "tools" > Firebase
* Choose "Authentication" & Choose "Authenticate using a custom authentication system"
* In the "Connect your app to firebase" section click "connect" button then its open the firebase console in browser
* Create a project in firebase
* Go to "authorization" enable the email/password sign in method
* Enable the "Real time database" & set the Real time database rules as 
```sh
  {
  "rules": {
    ".read": true,
    ".write": true
    }
  }
  ```
* Go to Android studio again & In "Add the firebase Authentication SDK to your app" section click the add button & accept the changes
* After that "google-services" json automatically added under "DeltaTestApp/app/"
* Finally app is ready to execute !!!


