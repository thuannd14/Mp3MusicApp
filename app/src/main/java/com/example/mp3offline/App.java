package com.example.mp3offline;

import android.app.Application;

public class App extends Application {
    private static App instance;
    private Storage storage;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        storage = new Storage();
    }

    public static App getInstance() {
        return instance;
    }

    public Storage getStorage() {
        return storage;
    }
}
