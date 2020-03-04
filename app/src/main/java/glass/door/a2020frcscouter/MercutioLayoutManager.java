package glass.door.a2020frcscouter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class MercutioLayoutManager extends LinearLayoutManager {

    public MercutioLayoutManager(Context c) {
        super(c);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.v("Error", "IndexOutOfBoundsException in RecyclerView happens");
        }
    }
}
