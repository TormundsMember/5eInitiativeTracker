package io.github.tormundsmember.a5einitiativetracker.Logic.Factories;

import android.app.Activity;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.internal.Context;

/**
 * Created by Tormund on 10.09.2016.
 */
public class RealmFactory {
    private static Realm ourInstance;

    public static Realm getInstance(Activity context) {
        if(ourInstance == null){
            ourInstance = Realm.getInstance(new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build());
        }
        return ourInstance;
    }

}
