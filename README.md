# ViewMyLog
This is an Android Library to show your logs of your Application in your Android Device. It provides 3 different ways of displaying logs in an Android App.
Furthermore it allows the user to track callbackmethod-calls of Android activities.


## Important
In order to use this library you need to migrate your application to AndroidX.
Check out [here](https://medium.com/androiddevelopers/migrating-to-androidx-tip-tricks-and-guidance-88d5de238876) how to migrate to AndroidX.


## Installation

### Step 1 
Add the JitPack repository to your Project Build File `build.gradle`.

````gradle
allprojects {
   repositories {
      ...
      maven { url 'https://jitpack.io' }
   }
}
````

### Step 2 
Add the dependency to this library in your Module Build File `build.gradle`.

````gradle
dependencies {
    implementation 'com.github.SalihColak:LogApp:1.0'
}

````

### Step 3 (optional)
Add the `SettingsActivity` and the `LogViewerActivity` to your `AndroidManifest.xml` for full functionality. 

````xml
<application ...>
    ....
    <activity android:name="com.thk.viewmylog.activties.LogViewActivity"/>
    <activity android:name="com.thk.viewmylog.activties.SettingsActivity"/>
</application>
````

## Usage

Create an instance of LogViewer in your activity as following.
````java
LogViewer logViewer = new LogViewer(this);
````

Now you can use the following methods on logViewer.
````java
logViewer.startLogViewActivity();       // Starts the LogViewActivity (declaration in AndroidManifest.xml needed)
logViewer.togglePopupWindow();          // Displays or removes a PopupWindow which shows logs.
logViewer.trackActivityLifecycle();     // Registers an LifecycleObserver for the activity , which shows Toast-Messages everytime the activit chnages its lifecycle state.
logViewer.startSettings();              // Starts the SettingsActivity, which allows the user to change preferences. (declaration in AndroidManifest.xml needed)
````

## Recommended usage
Add this predefined OptionsMenu to your activity.
````java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.optionsmenu, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()){
        case R.id.logActivity:
            logViewer.startLogViewActivity();
            break;
        case R.id.logPopup:
            logViewer.togglePopupWindow();
            break;
        case R.id.lifecycle:
            logViewer.trackActivityLifecycle();
            break;
        case R.id.userSettings:
            logViewer.startSettings();
            break;
        default: return super.onOptionsItemSelected(item);
    }
    return false; 
}
````

![OptionsMenu](https://raw.github.com/SalihColak/ViewMyLog/master/images/screen (5).jpg?raw=true "OptionsMenu")
