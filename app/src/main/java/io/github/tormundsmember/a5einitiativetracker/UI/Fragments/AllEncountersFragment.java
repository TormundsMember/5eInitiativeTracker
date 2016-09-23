package io.github.tormundsmember.a5einitiativetracker.UI.Fragments;


import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.squareup.otto.Bus;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tormundsmember.a5einitiativetracker.Logic.Factories.KeyFactory;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Encounter;
import io.github.tormundsmember.a5einitiativetracker.Logic.Factories.BusFactory;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.SavedCombatant;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.github.tormundsmember.a5einitiativetracker.UI.Adapters.EncountersAdapter;
import io.github.tormundsmember.a5einitiativetracker.UI.Adapters.SaveEncounterAdapter;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllEncountersFragment extends Fragment {


    @BindView(R.id.liste)
    RecyclerView mRecyclerView;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorLayout;

    Realm mRealm;
    Bus mBus;
    private EncountersAdapter mAdapter;
    private RealmList<Encounter> encounters;

    public AllEncountersFragment() {
        // Required empty public constructor
    }


    public static AllEncountersFragment newInstance(Realm realm) {

        AllEncountersFragment fragment = new AllEncountersFragment();
        fragment.mBus = BusFactory.getInstance();
        fragment.mRealm = realm;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_encounters, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (encounters.size() != 0) {
            inflater.inflate(R.menu.menu_allencounters_searchable, menu);

            MenuItem menuItem = menu.findItem(R.id.search);
            SearchView searchView =
                    (SearchView) menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ((EncountersAdapter)mRecyclerView.getAdapter()).setFilter(newText);
                    return false;
                }
            });
        }
        else {
            inflater.inflate(R.menu.menu_addbuttononly, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RealmResults<Encounter> realmResults = mRealm.where(Encounter.class).findAll();
        encounters = new RealmList<>();
        for (int i = 0; i < realmResults.size(); i++) {
            encounters.add(realmResults.get(i));
        }
        mAdapter = new EncountersAdapter(getContext(), mBus, mRealm, encounters);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                removeEncounter(reverseSortedPositions);
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                removeEncounter(reverseSortedPositions);
                            }
                        });
        mRecyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    private void removeEncounter(int[] reverseSortedPositions) {
        for (final int position : reverseSortedPositions) {
            mRealm.beginTransaction();
            final List<SavedCombatant> combatants = cloneEncounter(encounters.get(position));
            final String title = encounters.get(position).getTitle();
            final boolean isPlayersEnconter = encounters.get(position).isPlayerEncounter();
            encounters.get(position).deleteFromRealm();
            encounters.remove(position);
            mRealm.commitTransaction();
            mAdapter.notifyItemRemoved(position);
            Snackbar.make(coordinatorLayout, R.string.AllEncounters_Delete, Snackbar.LENGTH_SHORT).setAction(R.string.global_undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRealm.beginTransaction();
                    Encounter e = new Encounter();
                    e.setTitle(title);
                    e.setPlayerEncounter(isPlayersEnconter);
                    RealmList<SavedCombatant> combatantRealmList = new RealmList<SavedCombatant>();
                    for (int i = 0; i < combatants.size(); i++) {
                        combatantRealmList.add(combatants.get(i));
                        mRealm.copyToRealm(combatants.get(i));
                    }
                    e.setKey(KeyFactory.getKey());
                    mRealm.copyToRealm(e);
                    e.setCombatants(combatantRealmList);
                    mRealm.commitTransaction();
                    mAdapter.notifyItemInserted(position);
                    mAdapter.notifyDataSetChanged();
                }
            }).setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);

                }
            }).show();
        }
        mAdapter.notifyDataSetChanged();
    }

    private List<SavedCombatant> cloneEncounter(Encounter encounter) {

        List<SavedCombatant> combatantList = new LinkedList<>();
        for (int i = 0; i < encounter.getCombatants().size(); i++) {
            combatantList.add(encounter.getCombatants().get(i));
            combatantList.get(i).deleteFromRealm();
        }
        return combatantList;
    }
}
