package io.github.tormundsmember.a5einitiativetracker.UI.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Combatant;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Fight;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.EditCombatantEvent;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.realm.Realm;

/**
 * Created by Tormund on 10.09.2016.
 */
public class InitiativeListAdapter extends RecyclerView.Adapter<InitiativeListAdapter.ViewHolder>{

    Context mContext;
    Realm mRealm;
    Bus mBus;

    public InitiativeListAdapter(Context context, Realm mRealm, Bus bus) {
        this.mContext = context;
        this.mRealm = mRealm;
        this.mBus = bus;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_combatant,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Combatant c = mRealm.where(Fight.class).findAll().get(0).getFighters().get(position);

        holder.txtName.setText(c.getName());
        holder.txtAC.setText(String.valueOf(c.getAc()));
        holder.txtInit.setText(String.valueOf(c.getInitiative()));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.beginTransaction();
                mRealm.where(Fight.class).findAll().get(0).getFighters().remove(c);
                mRealm.commitTransaction();
                InitiativeListAdapter.this.notifyItemRemoved(position);
                InitiativeListAdapter.this.notifyDataSetChanged();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBus.post(new EditCombatantEvent(c));
            }
        });

        if(c.isCurrentFighter()){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.txtInit.setTextColor(Color.WHITE);
            holder.txtName.setTextColor(Color.WHITE);
            holder.txtAC.setTextColor(Color.WHITE);
            holder.btnDelete.getDrawable().setTint(Color.WHITE);
            holder.btnEdit.getDrawable().setTint(Color.WHITE);
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.txtInit.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.txtName.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.txtAC.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.btnDelete.getDrawable().setTint(ContextCompat.getColor(mContext,R.color.colorPrimaryDark));
            holder.btnEdit.getDrawable().setTint(ContextCompat.getColor(mContext,R.color.colorPrimaryDark));
        }
    }

    @Override
    public int getItemCount() {
        Fight fight = mRealm.where(Fight.class).findAll().get(0);
        return fight.getFighters().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txtName)
        TextView txtName;

        @BindView(R.id.txtAC)
        TextView txtAC;

        @BindView(R.id.txtInit)
        TextView txtInit;

        @BindView(R.id.btnEdit)
        ImageButton btnEdit;

        @BindView(R.id.btnDelete)
        ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
