package io.github.tormundsmember.a5einitiativetracker.Logic.Models;

import io.realm.RealmObject;

/**
 * Created by Tormund on 27.08.2016.
 */
public class Combatant extends RealmObject {


    private boolean isPlayer;
    private boolean currentFighter;
    private int ac;
    private int initiative;
    private String name;


    public Combatant() {
    }

    public Combatant(boolean isPlayer, int initiative, int ac, String name, boolean currentFighter) {
        this.isPlayer = isPlayer;
        this.initiative = initiative;
        this.ac = ac;
        this.name = name;
        this.currentFighter = currentFighter;
    }

    public Combatant(int initiative, int ac, String name, boolean currentFighter) {
        this.initiative = initiative;
        this.ac = ac;
        this.name = name;
        this.currentFighter = currentFighter;
    }

    public Combatant(int ac, int initiative, String name) {
        this.ac = ac;
        this.initiative = initiative;
        this.name = name;
        this.currentFighter = false;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    public boolean isCurrentFighter() {
        return currentFighter;
    }

    public void setCurrentFighter(boolean currentFighter) {
        this.currentFighter = currentFighter;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
