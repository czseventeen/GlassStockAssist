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
    protected static RemoteViews mRemoteViews;
    protected static final int MAX_NUM_STOCKS=4;

    public static ArrayList<Stocks> StockList=new ArrayList<>();
    private static ArrayList<Integer> PriceTextViewIDList=new ArrayList<>();
    private static ArrayList<Integer> SymbolTextViewIDList=new ArrayList<>();



    //Below are code used to indicate whether to initiliaize the remoteview or just update the Prices;
    private static final int KEY_USING_NEW_STOCKS =0;
    private static final int KEY_UPDATE_PRICE=1;
    private static final int KEY_REMOVE_ITEM =2 ;
    private static final int KEY_ADD_STOCK = 3;


    //below is the timer for interrupt
    private static final long UPDATE_RATE = 10000;
    //Below are the updater used to update the remoteView every second
    private static final Handler mHandler = new Handler();
    private final UpdateLiveCardRunnable mUpdateLiveCardRunnable =
            new UpdateLiveCardRunnable();

    //below is used for checking whether the user is removing an item
    public static boolean isAddingOrRemoving =false;

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
            menuIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(this, 1, menuIntent, 0));
            //mLiveCard.setVoiceActionEnabled(true);
            mLiveCard.publish(PublishMode.REVEAL);

            // Queue the update text runnable
            mHandler.post(mUpdateLiveCardRunnable);
            // The startForeground was suppose to keep the Service from being killed, don't have to now
//           startForeground(startId,new Notification());
        } else {
            mLiveCard.navigate();
        }
        return START_STICKY;
    }

    private static void updateRemoteView(int code) {
        int i=0;
        if(StockList.size()==0){
            //There are no stocks in the watchlist , tell the user so.
            mRemoteViews.setTextViewText(R.id.CardTitle, "You aren't watching any stocks");
        }

        for (Stocks stock:StockList) {
           //Only allowing maximum of 4 items to be displayed.
           if(i>=4){
               continue;
           }
            //store the old price value for comparison
            double OldPrice = stock.getmLastPrice();
            //Randomly generate a new price
            double NewPrice = (Math.round(stock.SetAndGetNewRandomStockPrice()* 100.0)) / 100.0;
            //print the new Price
            mRemoteViews.setTextViewText(PriceTextViewIDList.get(i), "" + NewPrice);
            //Change the color of the Price based on Price change
            if (NewPrice >= OldPrice) {
                mRemoteViews.setTextColor(PriceTextViewIDList.get(i), Color.GREEN);
            } else {
                mRemoteViews.setTextColor(PriceTextViewIDList.get(i), Color.RED);
            }
            //If adding new/removing stock from the list, repopulate the list
            if(code==KEY_USING_NEW_STOCKS || code==KEY_ADD_STOCK || code ==KEY_REMOVE_ITEM){
                mRemoteViews.setTextViewText(SymbolTextViewIDList.get(i), stock.getmSymbol());
            }

            i++;
        }

    }

    private static void initializeTextViewIDLists() {
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
            // use the random constructor to randomly generate a stock
            Stocks stock=new Stocks(null,Stocks.KEY_GENERATE_RANDOM_STOCK);
            StockList.add(stock);
        }
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished() && !isAddingOrRemoving) {
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
        // update the mRemoteView with the latest StockList items
        updateRemoteView(KEY_REMOVE_ITEM);
        //Set the last item in the stocklist to be null
        mRemoteViews.setTextViewText(PriceTextViewIDList.get(StockList.size()), "");
        mRemoteViews.setTextViewText(SymbolTextViewIDList.get(StockList.size()), "");
        // Always call setViews() to update the live card's RemoteViews.
        mLiveCard.setViews(mRemoteViews);

    }
    public static void addStockItem(Stocks stock){
        StockList.add(stock);
        // update the mRemoteView with the latest StockList items
        updateRemoteView(KEY_ADD_STOCK);
        // Always call setViews() to update the live card's RemoteViews.
        mLiveCard.setViews(mRemoteViews);

    }

    public static void AddingOrRemovingItem(){
        isAddingOrRemoving =true;
    }
    public static void doneAddingOrRemovingItem(){
        isAddingOrRemoving =false;
    }
}
