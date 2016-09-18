package io.github.tormundsmember.a5einitiativetracker.Logic.Events;

import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Encounter;

/**
 * Created by Tormund on 11.09.2016.
 */
public class AddEncounterToFightEvent {

    Encounter encounter;

    public AddEncounterToFightEvent(Encounter encounter) {
        this.encounter = encounter;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }
}