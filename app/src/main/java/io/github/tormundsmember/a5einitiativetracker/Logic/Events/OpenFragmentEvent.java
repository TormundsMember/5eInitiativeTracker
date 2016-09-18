package io.github.tormundsmember.a5einitiativetracker.Logic.Events;

import android.support.v4.app.Fragment;

/**
 * Created by Tormund on 18.09.2016.
 */
public class OpenFragmentEvent {

    Fragment fragment;
    boolean addtoBackStack;

    public OpenFragmentEvent(Fragment fragment, boolean addtoBackStack) {
        this.fragment = fragment;
        this.addtoBackStack = addtoBackStack;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public boolean isAddtoBackStack() {
        return addtoBackStack;
    }
}
