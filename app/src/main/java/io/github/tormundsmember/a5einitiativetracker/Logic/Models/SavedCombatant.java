package io.github.tormundsmember.a5einitiativetracker.Logic.Models;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Tormund on 27.08.2016.
 */
public class SavedCombatant extends RealmObject {



    private String name;
    private int ac;
    private int modifier;
    private boolean isPlayer;

    @Ignore
    private int init;

    public SavedCombatant() {
        init = Integer.MIN_VALUE;
    }

    public SavedCombatant(String name, int key, int ac, int modifier, boolean isPlayer) {

        this.name = name;
        this.ac = ac;
        this.modifier = modifier;
        this.isPlayer = isPlayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    public int getInit() {
        return init;
    }

    public void setInit(int init) {
        this.init = init;
    }
}
