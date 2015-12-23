package jayxu.com.glassstockassist.UI;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;

import jayxu.com.glassstockassist.Model.StockConstants;
import jayxu.com.glassstockassist.Model.StockScrollAdapter;
import jayxu.com.glassstockassist.Model.Stocks;

/**
 * Created by Yuchen on 12/21/2015.
 */
public class AddSelectionScreenActivity extends Activity {
    private static final String TAG= AddSelectionScreenActivity.class.getSimpleName();

    private ArrayList<Stocks> stocklist=new ArrayList<>();
    private CardScrollView mCardScrollerView;
    private ArrayList<CardBuilder> mCards=new ArrayList<>();
    private CardScrollAdapter mScrollAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //Grab the list of stocks passed from AddNewStockActivity:
            stocklist=getIntent().getParcelableArrayListExtra(StockConstants.KEY_STOCK_PARCELABLE);
            //Populate the cards using the ArrayList of stocks retrieved.
            PopulateCardList(this);
            mCardScrollerView =new CardScrollView(this);
            mScrollAdapter=new StockScrollAdapter(mCards);
            mCardScrollerView.setAdapter(mScrollAdapter);
            mCardScrollerView.activate();
            setContentView(mCardScrollerView);
            setCardScrollerListener();
        }


    private void PopulateCardList(Context context){
        for(Stocks stock: stocklist) {
            //Checks if the stock is valid entry.
            if(!stock.getmName().equals("")) {
                int endOfString=stock.getmName().length();
                String StockName=stock.getmName();
//                if(endOfString>10){
//                    endOfString=10;
//                    StockName=StockName.substring(0,endOfString)+"...";
//                }
                Log.d(TAG, "The name of this stock was : +"+ stock.getmName()+"+");
                mCards.add(new CardBuilder(context, CardBuilder.Layout.MENU)
                        .setText(stock.getmSymbol())
                        .setFootnote(StockName+" @ "+stock.getmExchangeMarket()));
            }
        }
    }
    private void setCardScrollerListener() {
        mCardScrollerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked view at position " + position + " Going to Add: " + stocklist.get(position).getmName());
                //Using the AsyncTask AddNewStockToList_AsynTask to query the latest update on the particular stock, then add that stock to our Stock watchlist
                AddNewStockActivity.AddNewStockToList_AsynTask addNewStockTask=new AddNewStockActivity.AddNewStockToList_AsynTask();
                addNewStockTask.execute(stocklist.get(position));

                // Play sound.
                int soundEffect = Sounds.TAP;
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);
                finish();

            }
        });
    }
}

