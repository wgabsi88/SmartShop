package com.beuth.ebp.smartshop;

import android.app.Application;

/**
 * Created by Stefan Völkel on 26.01.2016.
 */
public class SessionId extends Application
{
    private int sessionId;
    private static SessionId singleInstance = null;

    public static SessionId getInstance()
    {
        return singleInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return this.sessionId;
    }
}
