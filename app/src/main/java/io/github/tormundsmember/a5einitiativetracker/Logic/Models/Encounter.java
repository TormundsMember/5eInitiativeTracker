package io.github.tormundsmember.a5einitiativetracker.Logic.Models;

import java.util.HashMap;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;


public class Encounter extends RealmObject {

    @PrimaryKey
    private int key;
    private String title;
    private boolean isPlayerEncounter;
    private RealmList<SavedCombatant> combatants;

    @Ignore
    private String contents;

    public Encounter() {
    }

    public Encounter(String title, RealmList<SavedCombatant> combatants, boolean isPlayerEncounter) {
        this.title = title;
        setCombatants(combatants);
        this.isPlayerEncounter = isPlayerEncounter;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPlayerEncounter() {
        return isPlayerEncounter;
    }

    public void setPlayerEncounter(boolean playerEncounter) {
        isPlayerEncounter = playerEncounter;
    }

    public RealmList<SavedCombatant> getCombatants() {
        return combatants;
    }

    public void setCombatants(RealmList<SavedCombatant> combatants) {
        this.combatants = combatants;
        calculateContents();
    }

    public void calculateContents() {
        HashMap<String, Integer> countMap = new HashMap<>();
        for (int i = 0; i < combatants.size(); ++i) {
            String name = combatants.get(i).getName();
            int countUp = 1;
            if (countMap.containsKey(name)) {
                countUp = countMap.get(name) + 1;
                countMap.remove(name);
            }
            countMap.put(name, countUp);
        }
        contents = "";
        Object[] keys = countMap.keySet().toArray();
        for (int i = 0; i < keys.length; ++i) {
            contents += countMap.get(keys[i]) + "x " + keys[i];
            if (i + 1 < keys.length)
                contents += "\r\n";
        }
    }

    @Override
    public String toString() {
        return contents;
    }



}
