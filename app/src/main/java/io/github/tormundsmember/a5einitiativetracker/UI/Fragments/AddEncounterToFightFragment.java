package io.github.tormundsmember.a5einitiativetracker.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.ActionBar;
import android.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.SavedCombatant;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.github.tormundsmember.a5einitiativetracker.UI.Adapters.AddEncountersAdapter;
import io.realm.Realm;

/**
 * Created by Tormund on 18.09.2016.
 */
public class AddEncounterToFightFragment extends Fragment {


    @BindView(R.id.liste)
    RecyclerView mListe;

    @BindView(R.id.btnRandom)
    ImageButton btnRandom;

    @BindView(R.id.btnAdd)
    Button btnAdd;

    private List<SavedCombatant> combatants;
    private AddEncountersAdapter mAdapter;
    private Realm mRealm;

    public static AddEncounterToFightFragment newInstance(List<SavedCombatant> combatants, Realm realm) {
        AddEncounterToFightFragment fragment = new AddEncounterToFightFragment();
        fragment.combatants = combatants;
        fragment.mRealm = realm;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_encounter_to_fight, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(false);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new AddEncountersAdapter(getContext(), combatants, mRealm);
        mListe.setAdapter(mAdapter);
        mListe.setLayoutManager(new LinearLayoutManager(getContext()));
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getSimpleName(),"setAllRandom");
                mRealm.beginTransaction();
                mAdapter.setAllRandom();
                mRealm.commitTransaction();
            }
        });
    }
}
