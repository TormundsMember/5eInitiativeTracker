package io.github.tormundsmember.a5einitiativetracker.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.HideKeyboardEvent;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.OpenFragmentEvent;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Combatant;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Fight;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.AddCombatantFragmentResponse;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.EditCombatantEvent;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.EncounterSavedEvent;
import io.github.tormundsmember.a5einitiativetracker.Logic.Factories.BusFactory;
import io.github.tormundsmember.a5einitiativetracker.Logic.Factories.RealmFactory;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.github.tormundsmember.a5einitiativetracker.UI.Fragments.AddCombatantFragment;
import io.github.tormundsmember.a5einitiativetracker.UI.Fragments.AllEncountersFragment;
import io.github.tormundsmember.a5einitiativetracker.UI.Fragments.InitiativeListFragment;
import io.github.tormundsmember.a5einitiativetracker.UI.Fragments.SaveEncounterFragment;
import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nvView)
    NavigationView mNavigationView;


    private Bus mBus;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private InputMethodManager mInputMethodManager;
    private Realm mRealm;
    private Fight mFight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setTitle(R.string.nav_runEncounter);
        setTitle(R.string.nav_runEncounter);
        setupDrawerContent(mNavigationView);
        mRealm = RealmFactory.getInstance(this);

        mRealm.beginTransaction();
        mRealm.delete(Fight.class);
//        mRealm.delete(Encounter.class);
        mFight = mRealm.createObject(Fight.class);
        mFight.setFighters(new RealmList<Combatant>());
        mRealm.commitTransaction();

        mFragmentManager = getSupportFragmentManager();
        loadFragment(InitiativeListFragment.newInstance(mRealm), false);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mBus = BusFactory.getInstance();
        mBus.register(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mRealm = RealmFactory.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRealm = RealmFactory.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        super.onBackPressed();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        mFragment = null;
        String title = (String) menuItem.getTitle();
        switch (menuItem.getItemId()) {
            case R.id.combat:
                mFragment = InitiativeListFragment.newInstance(mRealm);
                break;
            case R.id.players:
                title = getString(R.string.title_players);
                mFragment = AllEncountersFragment.newInstance(mRealm);
                break;
            case R.id.monsters:
                title = getString(R.string.title_encounters);
                break;
        }
        if (mFragment != null)
            loadFragment(mFragment, false);
        menuItem.setChecked(true);
        setTitle(title);
        mDrawerLayout.closeDrawers();
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack)
            mFragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("x").commit();
        else
            mFragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void hideKeyboard(HideKeyboardEvent e) {
        View view = this.getCurrentFocus();
        if (view != null) {
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Subscribe
    public void editCombatant(EditCombatantEvent event) {
        loadFragment(AddCombatantFragment.newInstance(event.getCombatant()), false);
    }

    @Subscribe
    public void combatantAddingDone(AddCombatantFragmentResponse event) {

        if (event.isCanceled())
            mFragmentManager.popBackStack();
        RealmList<Combatant> fighters = mFight.getFighters();

        List<Combatant> list = new LinkedList<>();
        mRealm.beginTransaction();
        for (int i = 0; i < fighters.size(); i++) {
            list.add(fighters.get(i));
        }
        Collections.sort(list, new Comparator<Combatant>() {
            @Override
            public int compare(Combatant combatant, Combatant t1) {
                return t1.getInitiative() - combatant.getInitiative();
            }
        });
        fighters = new RealmList<>();
        for (int i = 0; i < list.size(); i++) {
            fighters.add(list.get(i));
        }
        mFight.setFighters(fighters);
        mRealm.commitTransaction();
        loadFragment(InitiativeListFragment.newInstance(mRealm), false);
    }

    @Subscribe
    public void encounterSaved(EncounterSavedEvent event) {
        loadFragment(AllEncountersFragment.newInstance(mRealm),false);
    }

    @Subscribe
    public void openFragment(OpenFragmentEvent event){
        loadFragment(event.getFragment(), event.isAddtoBackStack());
    }
}
