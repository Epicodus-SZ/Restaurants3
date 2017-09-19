## How to reproduce your API key to run this app

1. ADD YELP TOKEN TO GRADLE.PROPERTIES: Put your Yelp Token in the file gradle.properties looking like this `YelpToken = "o50T7xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxcx"`


Tracking API Key strings

2. ADD GRADLE.PROPERTIES TO .GITIGNORE: Make sure this file is in your .gitignore file.
*You might want to email yourself a copy of the gradle.properties file, so you can reproduce the app.*
3. EDIT BUILD.GRADLE FILE: Then add a line in your build.gradle file (Module:app) that looks like this

```
    android {
        buildTypes.each {
            it.buildConfigField 'String', 'STEVES_LAST_NAME', StevesLastName
        ```
 This tells gradle to auto generate these Static Strings in the ***BuildConfig.java*** file.

4. CREATE A CONSTANTS.JAVA CLASS. Create a new class called Constants.java and add a line like:
`public static final String STEVES_LAST_NAME = BuildConfig.STEVES_LAST_NAME;`
5. NOW YOU CAN ACCESS THE STRING VIA CONSTANT: Now you can access your strings in your code using this format = `Constants.STEVE_LAST_NAME`




 10. Add instructions to your README.md file to explain to others how to re-create your gradle.properties

 # Yelp calls this client ID
 YelpConsumerKey = "73_1234567890"
 YelpConsumerSecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxTb3OaUDlKRVxxxxx"
 YelpToken = "o50T7xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxcx"
 YelpTokenSecret = "Bearer"
 StevesLastName = "Zaske"

 ##