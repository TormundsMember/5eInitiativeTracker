package io.github.tormundsmember.a5einitiativetracker.Logic.Events;

import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Combatant;

/**
 * Created by Tormund on 10.09.2016.
 */
public class EditCombatantEvent {

    Combatant combatant;

    public EditCombatantEvent(Combatant combatant) {
        this.combatant = combatant;
    }

    public Combatant getCombatant() {
        return combatant;
    }
}
