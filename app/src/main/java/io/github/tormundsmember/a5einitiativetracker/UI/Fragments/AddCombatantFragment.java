package io.github.tormundsmember.a5einitiativetracker.UI.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.HideKeyboardEvent;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Combatant;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Fight;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.AddCombatantFragmentResponse;
import io.github.tormundsmember.a5einitiativetracker.Logic.Factories.BusFactory;
import io.github.tormundsmember.a5einitiativetracker.Logic.Factories.RealmFactory;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.github.tormundsmember.a5einitiativetracker.UI.Activities.MainActivity;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCombatantFragment extends Fragment {

    private final static int NO_INIT = 1;
    private final static int NO_NAME = 2;
    private final static int NO_AC = 4;

    @BindView(R.id.txtInit)
    TextView txtInit;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.txtAc)
    TextView txtAc;

    @BindView(R.id.btnCancel)
    AppCompatButton btnCancel;

    @BindView(R.id.btnAdd)
    AppCompatButton btnAdd;

    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorLayout;

    private Bus mBus;
    private Combatant mCombatant;

    public AddCombatantFragment() {
    }

    public static AddCombatantFragment newInstance(Combatant combatant) {

        AddCombatantFragment fragment = new AddCombatantFragment();
        if (combatant != null)
            fragment.mCombatant = combatant;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_combatant, container, false);
        ButterKnife.bind(this, view);
        txtAc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    add();
                }
                return false;
            }
        });

        if (mCombatant != null) {
            txtAc.setText(String.valueOf(mCombatant.getAc()));
            txtInit.setText(String.valueOf(mCombatant.getInitiative()));
            txtName.setText(mCombatant.getName());
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBus = BusFactory.getInstance();
        mBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBus.unregister(this);
    }

    @OnClick(R.id.btnCancel)
    public void cancel() {

        mBus.post(new HideKeyboardEvent());
        mBus.post(new AddCombatantFragmentResponse(true));
    }

    @OnClick(R.id.btnAdd)
    public void add() {

        int flags = 0;

        if (!txtInit.getText().toString().matches("^\\d+$"))
            flags += NO_INIT;
        if (txtName.getText().toString().matches("^$"))
            flags += NO_NAME;
        if (!txtAc.getText().toString().matches("^\\d+$"))
            flags += NO_AC;

        if (flags == 0) {

            mBus.post(new HideKeyboardEvent());
            Realm realm = RealmFactory.getInstance(getActivity());
            realm.beginTransaction();

            Combatant c = mCombatant == null ? realm.createObject(Combatant.class) : mCombatant;
            c.setInitiative(Integer.parseInt(txtInit.getText().toString()));
            c.setAc(Integer.parseInt(txtAc.getText().toString()));
            c.setName(txtName.getText().toString());

            if (mCombatant == null) {
                realm.where(Fight.class).findAll().get(0).getFighters().add(c);
            }
            realm.commitTransaction();
            mBus.post(new AddCombatantFragmentResponse(false));
        } else {
            //private final static int NO_INIT = 1;
//            private final static int NO_NAME = 2;
//            private final static int NO_AC = 4;

            String errorText = getString(R.string.AddCombatant_Error);
            String txt = "";
            switch (flags) {
                case 1:
                    txt = getString(R.string.AddCombatant_Initiative_Error);
                    break;
                case 2:
                    txt = getString(R.string.AddCombatant_Name_Error);
                    break;
                case 3:
                    txt = getString(R.string.AddCombatant_Initiative_Error) + " and " + getString(R.string.AddCombatant_Name_Error);
                    break;
                case 4:
                    txt = getString(R.string.AddCombatant_AC_Error);
                    break;
                case 5:
                    txt = getString(R.string.AddCombatant_Initiative_Error) + " and " + getString(R.string.AddCombatant_Name_Error);
                    break;
                case 6:
                    txt = getString(R.string.AddCombatant_AC_Error) + " and " + getString(R.string.AddCombatant_Name_Error);
                    break;
                case 7:
                    txt = getString(R.string.AddCombatant_Initiative_Error) + ", " + getString(R.string.AddCombatant_AC_Error) + " and " + getString(R.string.AddCombatant_Name_Error);
                    break;
            }

            Snackbar.make(coordinatorLayout,String.format(errorText, txt), Snackbar.LENGTH_LONG).show();
        }
    }
}
