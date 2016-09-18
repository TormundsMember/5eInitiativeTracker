package io.github.tormundsmember.a5einitiativetracker.Logic.Factories;

import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Encounter;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Tormund on 11.09.2016.
 */
public class KeyFactory {

    private int latestKey = -1;

    public int getKey(){
        return ++latestKey;
    }

    public void restoreKey(Realm realm){
        RealmResults<Encounter> encounters = realm.where(Encounter.class).findAll();
        for (int i = 0; i < encounters.size(); i++) {
            if(latestKey < encounters.get(i).getKey())
                latestKey = encounters.get(i).getKey();
        }
    }
}
