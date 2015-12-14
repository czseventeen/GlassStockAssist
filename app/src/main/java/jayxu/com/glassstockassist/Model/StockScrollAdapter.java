package jayxu.com.glassstockassist.Model;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuchen on 12/13/2015.
 */
public class StockScrollAdapter extends CardScrollAdapter {
    private ArrayList<CardBuilder> mCards;
    public StockScrollAdapter(ArrayList<CardBuilder> cards){
        this.mCards=cards;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public Object getItem(int i) {
        return mCards.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return mCards.get(i).getView();
    }

    @Override
    public int getPosition(Object o) {
        return this.mCards.indexOf(o);
    }
}
