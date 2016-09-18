package io.github.tormundsmember.a5einitiativetracker.UI.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.squareup.otto.Bus;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.HideKeyboardEvent;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Combatant;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Encounter;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Fight;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.SavedCombatant;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.EncounterSavedEvent;
import io.github.tormundsmember.a5einitiativetracker.Logic.Factories.BusFactory;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.github.tormundsmember.a5einitiativetracker.UI.Activities.MainActivity;
import io.github.tormundsmember.a5einitiativetracker.UI.Adapters.SaveEncounterAdapter;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveEncounterFragment extends Fragment {


    private SaveEncounterAdapter mAdapter;

    public SaveEncounterFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.liste)
    RecyclerView liste;

    @BindView(R.id.txtTitle)
    EditText txtTitle;

    @BindView(R.id.container)
    RelativeLayout container;


    private Realm mRealm;
    private Bus mBus;

    public static SaveEncounterFragment newInstance(Realm realm) {

        SaveEncounterFragment fragment = new SaveEncounterFragment();
        fragment.mRealm = realm;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_save_encounter, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBus = BusFactory.getInstance();
        RealmList<Combatant> fighters = mRealm.where(Fight.class).findFirst().getFighters();
        List<SavedCombatant> combatants = new LinkedList<>();
        for (int i = 0; i < fighters.size(); i++) {
            mRealm.beginTransaction();
            SavedCombatant combatant = mRealm.createObject(SavedCombatant.class);
            combatant.setAc(fighters.get(i).getAc());
            combatant.setName(fighters.get(i).getName());
            combatant.setModifier(0);
            combatant.setPlayer(false);
            combatants.add(combatant);
            mRealm.commitTransaction();
        }

        liste.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SaveEncounterAdapter(getContext(), mRealm, mBus, combatants);
        liste.setAdapter(mAdapter);
    }


    @OnClick(R.id.btnSave)
    public void saveEncounter(){
        if(!txtTitle.getText().toString().trim().equals("")){
            int key = 0;
            if(mRealm.where(Encounter.class).count() != 0) {
                RealmResults<Encounter> encounters = mRealm.where(Encounter.class).findAll();
                Encounter encounter = encounters.last();
                key = encounter != null ? encounter.getKey() + 1 : 0;
            }
            List<SavedCombatant> combatants = mAdapter.getCombatants();
            mRealm.beginTransaction();
            Encounter e = mRealm.createObject(Encounter.class);
            e.setKey(key);
            e.setTitle(txtTitle.getText().toString());
            RealmList<SavedCombatant> combatantRealmList = new RealmList<>();
            for (int i = 0; i < combatants.size(); i++) {
                combatantRealmList.add(combatants.get(i));
            }
            e.setCombatants(combatantRealmList);
            mRealm.copyToRealm(e);
            mRealm.commitTransaction();
            mBus.post(new EncounterSavedEvent());
            mBus.post(new HideKeyboardEvent());
        }else{
            Snackbar.make(container,R.string.SaveEncounter_noTitle, Snackbar.LENGTH_SHORT).show();
        }
    }
}
