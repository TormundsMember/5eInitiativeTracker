package io.github.tormundsmember.a5einitiativetracker.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tormundsmember.a5einitiativetracker.Logic.Models.Encounter;
import io.github.tormundsmember.a5einitiativetracker.Logic.Events.AddEncounterToFightEvent;
import io.github.tormundsmember.a5einitiativetracker.R;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Tormund on 11.09.2016.
 */
public class EncountersAdapter extends RecyclerView.Adapter<EncountersAdapter.ViewHolder> {

    Context mContext;
    Bus mBus;
    Realm mRealm;
    RealmList<Encounter> mEncounters;
    String filter = "";

    public EncountersAdapter(Context mContext, Bus mBus, Realm mRealm, RealmList<Encounter> mEncounters) {
        this.mContext = mContext;
        this.mBus = mBus;
        this.mRealm = mRealm;
        this.mEncounters = mEncounters;
        mBus.register(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listitem_encounter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Encounter encounter = mEncounters.get(position);

        if (filter.equals("") || encounter.getTitle().toLowerCase().contains(filter.toLowerCase())) {
            holder.view.setVisibility(View.VISIBLE);
            mRealm.beginTransaction();
            encounter.calculateContents();
            mRealm.commitTransaction();
            holder.txtContents.setText(encounter.toString());
            holder.txtTitle.setText(encounter.getTitle());
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBus.post(new AddEncounterToFightEvent(encounter));
                }
            });
        } else {
            holder.view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mEncounters.size();
    }

    public void setFilter(String filter) {
        this.filter = filter;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageButton)
        ImageButton imageButton;

        @BindView(R.id.txtContents)
        TextView txtContents;

        @BindView(R.id.txtTitle)
        TextView txtTitle;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }
}
