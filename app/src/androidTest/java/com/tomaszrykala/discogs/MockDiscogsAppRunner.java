package com.tomaszrykala.discogs;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

public class MockDiscogsAppRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        final String name = MockDiscogsApp.class.getCanonicalName();
        return super.newApplication(cl, name, context);
    }
}
