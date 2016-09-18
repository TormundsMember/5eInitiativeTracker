package io.github.tormundsmember.a5einitiativetracker.Logic.Models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Fight extends RealmObject {

    RealmList<Combatant> fighters;

    public Fight() {
    }

    public RealmList<Combatant> getFighters() {
        return fighters;
    }

    public void setFighters(RealmList<Combatant> fighters) {
        this.fighters = fighters;
    }
}
