package io.github.tormundsmember.a5einitiativetracker.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.SavedCombatant;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.realm.Realm;

/**
 * Created by Tormund on 18.09.2016.
 */
public class AddEncountersAdapter extends RecyclerView.Adapter<AddEncountersAdapter.ViewHolder> {

    List<SavedCombatant> mCombatants;
    Context mContext;
    Random r;
    Realm mRealm;

    public AddEncountersAdapter(Context context, List<SavedCombatant> combatants, Realm realm) {
        this.mContext = context;
        this.mCombatants = combatants;
        this.r = new Random();
        this.mRealm = realm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_setcombatantbeforefight, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SavedCombatant combatant = mCombatants.get(position);
        holder.txtModifier.setText((combatant.getModifier() > 0 ? "+" : "") + String.valueOf(combatant.getModifier()));
        holder.txtName.setText(combatant.getName());
        holder.txtInit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().matches("^-?\\d+$")) {
                    combatant.setInit(Integer.parseInt(charSequence.toString()));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        if(combatant.getInit() != Integer.MIN_VALUE)
            holder.txtInit.setText(String.valueOf(combatant.getInit()));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.beginTransaction();
                mCombatants.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                mRealm.commitTransaction();
            }
        });

        holder.btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.beginTransaction();
                int mod = combatant.getModifier();
                combatant.setInit(r.nextInt(20) + mod + 1);
                holder.txtInit.setText(String.valueOf(combatant.getInit()));
                notifyDataSetChanged();
                mRealm.commitTransaction();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCombatants.size();
    }

    public void setAllRandom() {
        int mod;
        for (int i = 0; i < mCombatants.size(); i++) {
            SavedCombatant combatant = mCombatants.get(i);
            mod = combatant.getModifier();
            combatant.setInit(r.nextInt(20) + mod + 1);
        }
        notifyItemRangeChanged(0,getItemCount());
        notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtModifier)
        TextView txtModifier;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtInit)
        EditText txtInit;

        @BindView(R.id.btnRandom)
        ImageButton btnRandom;
        @BindView(R.id.btnDelete)
        ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
