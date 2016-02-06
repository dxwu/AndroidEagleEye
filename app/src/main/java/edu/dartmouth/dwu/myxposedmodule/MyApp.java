package edu.dartmouth.dwu.myxposedmodule;

import android.app.Application;
import android.content.Context;

/**
 * Created by dwu on 2/3/16.
 */
public class MyApp extends Application {
    private static Context mContext;

    public void onCreate(){
        mContext = this.getApplicationContext();
        super.onCreate();
    }

    public static Context getAppContext(){
        return mContext;
    }
}

