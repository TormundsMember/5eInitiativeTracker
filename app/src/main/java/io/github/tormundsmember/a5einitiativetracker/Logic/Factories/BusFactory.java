package io.github.tormundsmember.a5einitiativetracker.Logic.Factories;

import com.squareup.otto.Bus;

/**
 * Created by Tormund on 04.09.2016.
 */
public class BusFactory {

    private static Bus ourInstance = new Bus();
    public static Bus getInstance() {
        return ourInstance;
    }

}
