## How to reproduce your API key to run this app

ADD YELP TOKEN TO GRADLE.PROPERTIES: Put your Yelp Token in the file gradle.properties looking like this `YelpToken = "o50T7xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxcx"`


## Reference on how to securely add API keys to your Android App

1. ADD GRADLE.PROPERTIES TO .GITIGNORE: Make sure this file is in your .gitignore file *BEFORE* your first git commit, or it will not be ignored.
*You might want to email yourself a copy of the gradle.properties file, so you can reproduce the app.*
2. EDIT BUILD.GRADLE FILE: Then add a line in your build.gradle file (Module:app) that looks like this

```
    android {
        buildTypes.each {
            it.buildConfigField 'String', 'YELP_TOKEN', YelpToken
        }
        ```
 This tells gradle to auto generate these Static Strings in the ***BuildConfig.java*** file.

3. CREATE A CONSTANTS.JAVA CLASS. Create a new class called Constants.java and add a line like:
`public static final String YELP_TOKEN = BuildConfig.YELP_TOKEN;`
4. NOW YOU CAN ACCESS THE STRING VIA CONSTANT: Now you can access your strings in your code using this format = `Constants.STEVE_LAST_NAME`
5. Add instructions to your README.md file to explain to others how to re-create your gradle.properties


## GoogleFirebase API keys are stored in app/google-services.json, and apparently it's okay to leave in your repo.