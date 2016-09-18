package io.github.tormundsmember.a5einitiativetracker.Logic.Events;

/**
 * Created by Tormund on 10.09.2016.
 */
public class AddCombatantFragmentResponse {

    boolean cancel;

    public AddCombatantFragmentResponse(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCanceled() {
        return cancel;
    }
}
