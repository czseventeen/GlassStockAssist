package jayxu.com.glassstockassist.UI;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.lang.reflect.Array;
import java.util.ArrayList;

import jayxu.com.glassstockassist.Model.Stocks;
import jayxu.com.glassstockassist.R;

/**
 * A {@link Service} that publishes a {@link LiveCard} in the timeline.
 */
public class LiveStockService extends Service {
    private static final String TAG= LiveStockService.class.getSimpleName();
    private static final String LIVE_CARD_TAG = "LiveStockService";

    public static LiveCard mLiveCard;
    RemoteViews mRemoteViews;
    private static final int MAX_NUM_STOCKS=4;

    public static ArrayList<Stocks> StockList=new ArrayList<>();
    private ArrayList<Integer> PriceTextViewIDList=new ArrayList<>();
    private ArrayList<Integer> SymbolTextViewIDList=new ArrayList<>();



    //Below are code used to indicate whether to initiliaize the remoteview or just update the Prices;
    private static final int KEY_USING_NEW_STOCKS =0;
    private static final int KEY_UPDATE_PRICE=1;

    //below is the timer for interrupt
    private static final long UPDATE_RATE = 1000;
    //Below are the updater used to update the remoteView every second
    private final Handler mHandler = new Handler();
    private final UpdateLiveCardRunnable mUpdateLiveCardRunnable =
            new UpdateLiveCardRunnable();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mLiveCard == null) {
            mLiveCard = new LiveCard(this, LIVE_CARD_TAG);
            mRemoteViews= new RemoteViews(getPackageName(), R.layout.live_stock);
            initializeRandomStockList();
            initializeTextViewIDLists();
            //Set the text for stock symbols and prices;
            updateRemoteView(KEY_USING_NEW_STOCKS);
            mLiveCard.setViews(mRemoteViews);

            // Display the options menu when the live card is tapped.
            Intent menuIntent = new Intent(this, LiveCardMenuActivity.class);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
            mLiveCard.publish(PublishMode.REVEAL);

            // Queue the update text runnable
            mHandler.post(mUpdateLiveCardRunnable);
            startForeground(startId,new Notification());
        } else {
            mLiveCard.navigate();
        }
        return START_STICKY;
    }

    private void updateRemoteView(int code) {
        int i=0;
        if(code== KEY_USING_NEW_STOCKS) {
            //Initialize new stock objects
            for (Stocks stock:StockList) {

                double LastPrice = (Math.round(stock.getmLastPrice() * 100)) / 100;
                mRemoteViews.setTextViewText(PriceTextViewIDList.get(i), "" + LastPrice);
                //Mocking the Raise/Down color scheme
                if (LastPrice >= 20) {
                    mRemoteViews.setTextColor(PriceTextViewIDList.get(i), Color.RED);
                } else {
                    mRemoteViews.setTextColor(PriceTextViewIDList.get(i), Color.GREEN);
                }
                mRemoteViews.setTextViewText(SymbolTextViewIDList.get(i), stock.getmSymbol());
                i++;
            }

        }else if(code == KEY_UPDATE_PRICE){
            //just update the existing stocks' price
            for (Stocks stock:StockList) {
                double OldPrice = stock.getmLastPrice();
                double NewPrice = (Math.round(stock.SetAndGetNewRandomStockPrice()* 100.0)) / 100.0;
                mRemoteViews.setTextViewText(PriceTextViewIDList.get(i), "" + NewPrice);
                //Mocking the Raise/Down color scheme
                if (NewPrice >= OldPrice) {
                    mRemoteViews.setTextColor(PriceTextViewIDList.get(i), Color.GREEN);
                } else {
                    mRemoteViews.setTextColor(PriceTextViewIDList.get(i), Color.RED);
                }
                i++;
            }
        }
    }

    private void initializeTextViewIDLists() {
        PriceTextViewIDList.add(R.id.PriceText1);
        PriceTextViewIDList.add(R.id.PriceText2);
        PriceTextViewIDList.add(R.id.PriceText3);
        PriceTextViewIDList.add(R.id.PriceText4);

        SymbolTextViewIDList.add(R.id.StockNameText1);
        SymbolTextViewIDList.add(R.id.StockNameText2);
        SymbolTextViewIDList.add(R.id.StockNameText3);
        SymbolTextViewIDList.add(R.id.StockNameText4);



    }

    private void initializeRandomStockList() {
        for(int i=0; i<MAX_NUM_STOCKS; i++){
            Stocks stock=new Stocks(-1);
            StockList.add(stock);
        }
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mUpdateLiveCardRunnable.setStop(true);
            mLiveCard.unpublish();
            mLiveCard = null;
            Log.d(TAG, "LOW on memory, killing Service");
        }
        super.onDestroy();
    }



    private class UpdateLiveCardRunnable implements Runnable{

        private boolean mIsStopped = false;
        /*
        Update the card every 1 second
         */
        public void run(){
            if(!isStopped()){
                // Update the remote view with the new Prices;
                try {
                    updateRemoteView(KEY_UPDATE_PRICE);
                // Always call setViews() to update the live card's RemoteViews.
                    mLiveCard.setViews(mRemoteViews);

                // Queue another score update in 30 seconds.
                mHandler.postDelayed(mUpdateLiveCardRunnable, UPDATE_RATE);
                }catch (NullPointerException e){
                    Log.w(TAG,e);
                }
            }
        }

        public boolean isStopped() {
            return mIsStopped;
        }

        public void setStop(boolean isStopped) {
            this.mIsStopped = isStopped;
        }
    }

    public static void removeStockItem(int i){
           StockList.remove(i);
    }
}
