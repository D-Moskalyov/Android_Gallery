package com.android.gallery.app;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
