package com.weatherupdate.glare.activities;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    private static final int SCHEMA_V_PREV = 1;// previous schema version
    private static final int SCHEMA_V_NOW = 2;// change schema version if any change happened in schema


    public static int getSchemaVNow() {
        return SCHEMA_V_NOW;
    }
    @Override
    public void onCreate() {
        super.onCreate ( );
        Realm.init(this);
        RealmConfiguration config=new RealmConfiguration.Builder ().schemaVersion(SCHEMA_V_NOW)
                .deleteRealmIfMigrationNeeded().build ();
        Realm.setDefaultConfiguration (config);

    }
}