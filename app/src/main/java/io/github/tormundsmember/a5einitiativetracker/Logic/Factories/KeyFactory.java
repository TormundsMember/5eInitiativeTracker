package io.github.tormundsmember.a5einitiativetracker.Logic.Factories;

import android.util.Log;

import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Encounter;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Tormund on 11.09.2016.
 */
public class KeyFactory {

    private static int latestKey = -1;
    private static final String TAG = KeyFactory.class.getSimpleName();

    public static int getKey() {
        return ++latestKey;
    }

    public static void restoreKey(Realm realm) {
        RealmResults<Encounter> encounters = realm.where(Encounter.class).findAll();
        for (int i = 0; i < encounters.size(); i++) {
            if (latestKey < encounters.get(i).getKey())
                latestKey = encounters.get(i).getKey();
        }
        Log.d(TAG, "key restored: " + String.valueOf(latestKey));
    }
}
