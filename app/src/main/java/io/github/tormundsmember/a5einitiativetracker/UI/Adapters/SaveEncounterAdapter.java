package io.github.tormundsmember.a5einitiativetracker.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.squareup.otto.Bus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.SavedCombatant;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.realm.Realm;

/**
 * Created by Tormund on 11.09.2016.
 */
public class SaveEncounterAdapter extends RecyclerView.Adapter<SaveEncounterAdapter.ViewHolder>{

    Context mContext;
    Realm mRealm;
    Bus mBus;
    List<SavedCombatant> combatants;


    public SaveEncounterAdapter(Context context, Realm mRealm, Bus bus, List<SavedCombatant> combatants) {
        this.mContext = context;
        this.mRealm = mRealm;
        this.mBus = bus;
        this.combatants = combatants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listitem_savecombatanttoencounter,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SavedCombatant combatant = combatants.get(position);

        holder.txtAC.setText(String.valueOf(combatant.getAc()));
        holder.txtName.setText(combatant.getName());

        holder.txtAC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().matches("^\\d+$")) {
                    mRealm.beginTransaction();
                    combatant.setAc(Integer.parseInt(charSequence.toString()));
                    mRealm.commitTransaction();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.txtModifier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().matches("^-?\\d+$")) {
                    mRealm.beginTransaction();
                    combatant.setModifier(Integer.parseInt(charSequence.toString()));
                    mRealm.commitTransaction();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRealm.beginTransaction();
                combatant.setName(charSequence.toString());
                mRealm.commitTransaction();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.beginTransaction();
                combatants.remove(combatant);
                mRealm.commitTransaction();
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return combatants.size();
    }

    public List<SavedCombatant> getCombatants() {
        return combatants;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txtModifier)
        EditText txtModifier;

        @BindView(R.id.txtName)
        EditText txtName;

        @BindView(R.id.txtAC)
        EditText txtAC;

        @BindView(R.id.btnDelete)
        ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
