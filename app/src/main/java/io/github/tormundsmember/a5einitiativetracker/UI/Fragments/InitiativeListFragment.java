package io.github.tormundsmember.a5einitiativetracker.UI.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.OpenFragmentEvent;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Combatant;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Fight;
import io.github.tormundsmember.a5einitiativetracker.Logic.Factories.BusFactory;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.github.tormundsmember.a5einitiativetracker.UI.Adapters.InitiativeListAdapter;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InitiativeListFragment extends Fragment {

    @BindView(R.id.liste)
    RecyclerView mRecyclerView;

    @BindView(R.id.btnNext)
    AppCompatImageButton btnNext;

    private Realm mRealm;
    private Bus mBus;

    public InitiativeListFragment() {
    }

    public static InitiativeListFragment newInstance(Realm realm) {

        InitiativeListFragment fragment = new InitiativeListFragment();
        fragment.mRealm = realm;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initiative_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        if(mRealm.where(Fight.class).findAll().get(0).getFighters().size() != 0){
            btnNext.setVisibility(View.VISIBLE);
        }else{
            btnNext.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mRealm.where(Fight.class).findFirst().getFighters().size() != 0)
            inflater.inflate(R.menu.menu_initiativelist_savable, menu);
        else
            inflater.inflate(R.menu.menu_addbuttononly, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addNew:
                mBus.post(new OpenFragmentEvent(AddCombatantFragment.newInstance(null), false));
                return true;
            case R.id.saveEncounter:
                mBus.post(new OpenFragmentEvent(SaveEncounterFragment.newInstance(mRealm), false));
                return true;
        }
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBus = BusFactory.getInstance();
        mRecyclerView.setAdapter(new InitiativeListAdapter(getContext(), mRealm, mBus));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBus.unregister(this);
    }

    @OnClick(R.id.btnNext)
    public void nextCompetitor(){
        RealmList<Combatant> fighters = mRealm.where(Fight.class).findFirst().getFighters();
        boolean found = false;
        int i = 0;
        for(;i < fighters.size() && !found; ++i){
            if(fighters.get(i).isCurrentFighter()){
                mRealm.beginTransaction();
                fighters.get(i).setCurrentFighter(false);
                mRealm.commitTransaction();
                found = true;
            }
        }
        mRealm.beginTransaction();
        if(!found)
            fighters.get(0).setCurrentFighter(true);
        else{
            if(i == fighters.size())
                fighters.get(0).setCurrentFighter(true);
            else
                fighters.get(i).setCurrentFighter(true);
        }
        mRealm.commitTransaction();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}
