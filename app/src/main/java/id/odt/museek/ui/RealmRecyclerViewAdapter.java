package id.odt.museek.ui;

/**
 * Created by arrival on 4/29/17.
 */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.RealmObject;
import io.realm.RealmResults;

public abstract class RealmRecyclerViewAdapter<T extends RealmObject, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected RealmResults<T> realmResults;
    protected Context context;

    public RealmRecyclerViewAdapter(Context context, RealmResults<T> realmResults) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (realmResults == null) {
            throw new IllegalArgumentException("RealmResults cannot be null");
        }

        this.realmResults = realmResults;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }
}
