package com.nathb.swiftshop;

import android.app.Application;

import com.nathb.swiftshop.data.PopulateRealm;
import com.nathb.swiftshop.log.LogTree;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class SwiftShopApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new LogTree());
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                                        .initialData(new PopulateRealm(getResources()))
                                        .build();
        Realm.setDefaultConfiguration(config);
    }
}
