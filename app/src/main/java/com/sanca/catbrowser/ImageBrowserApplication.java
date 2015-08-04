package com.sanca.catbrowser;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by sanca on 04.08.15.
 */
public class ImageBrowserApplication extends Application {

    private static final String TAG = ImageBrowserApplication.class.getSimpleName();

    private static ImageBrowserApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Initializing the Application");
        // initialize the singleton
        sInstance = this;
    }


    public static synchronized Context getAppContext() {
        return sInstance.getApplicationContext();
    }

}
