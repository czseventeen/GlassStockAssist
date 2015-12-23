package jayxu.com.glassstockassist.UI;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

import jayxu.com.glassstockassist.Model.StockConstants;
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

    private static ArrayList<Stocks> StockList=new ArrayList<>();
    private static ArrayList<Integer> PriceTextViewIDList=new ArrayList<>();
    private static ArrayList<Integer> SymbolTextViewIDList=new ArrayList<>();


    //Below are the updater used to update the remoteView every 5 second
    private static final Handler mHandler = new Handler();
    private final UpdateLiveCardRunnable mUpdateLiveCardRunnable =new UpdateLiveCardRunnable();

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
            //initializeRandomStockList();
            initializeTextViewIDLists();
            //Set the text for stock symbols and prices;
            updateRemoteView(StockConstants.KEY_USING_NEW_STOCKS);
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
            mRemoteViews.setTextViewText(R.id.CardTitle, StockConstants.NoStocks);
        }else{
            mRemoteViews.setTextViewText(R.id.CardTitle, StockConstants.WelcomeTitle);
        }

        for (Stocks stock:StockList) {
           //Only allowing maximum of 4 items to be displayed.
           if(i>=StockConstants.MAX_NUM_STOCKS){
               continue;
           }
            //store the old price value for comparison
            double Price = (Math.round(stock.getmCurrentPrice()*100.0)/100.0);
            //print the new Price
            mRemoteViews.setTextViewText(PriceTextViewIDList.get(i), "" + Price);
            //Change the color of the Price based on Price change
            if (stock.getmPercentageChanges()>0) {
                //increasing
                mRemoteViews.setTextColor(PriceTextViewIDList.get(i), Color.GREEN);
            } else if(stock.getmPercentageChanges()<0) {
                //decreasing price
                mRemoteViews.setTextColor(PriceTextViewIDList.get(i), Color.RED);
            }else{
                //Price didn't change
                mRemoteViews.setTextColor(PriceTextViewIDList.get(i), Color.WHITE);
            }
            //If adding new/removing stock from the list, repopulate the list
            if(code== StockConstants.KEY_USING_NEW_STOCKS || code== StockConstants.KEY_ADD_STOCK || code == StockConstants.KEY_REMOVE_ITEM){
                mRemoteViews.setTextViewText(SymbolTextViewIDList.get(i), stock.getmSymbol());
            }

            i++;
        }

    }

    private static void initializeTextViewIDLists() {
        //Re-initialize things
        PriceTextViewIDList=new ArrayList<>();
        PriceTextViewIDList.add(R.id.PriceText1);
        PriceTextViewIDList.add(R.id.PriceText2);
        PriceTextViewIDList.add(R.id.PriceText3);
        PriceTextViewIDList.add(R.id.PriceText4);

        SymbolTextViewIDList=new ArrayList<>();
        SymbolTextViewIDList.add(R.id.StockNameText1);
        SymbolTextViewIDList.add(R.id.StockNameText2);
        SymbolTextViewIDList.add(R.id.StockNameText3);
        SymbolTextViewIDList.add(R.id.StockNameText4);
    }


    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished() && !isAddingOrRemoving) {
            mUpdateLiveCardRunnable.setIsRunning(true);
            mLiveCard.unpublish();
            mLiveCard = null;
            Log.d(TAG, "LOW on memory, killing Service");
        }
        super.onDestroy();
    }



    private class UpdateLiveCardRunnable implements Runnable{

        private boolean mIsRunning = true;
        /*
        Update the card every 5 second
         */
        public void run(){
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn=pm.isScreenOn();
            //Log.d(TAG, "The UpdateLiveCardRunnable has been called, the screen on is:"+isScreenOn);
            if(isRunning()) {
                if(isScreenOn) {
                    // Update the remote view with the new Prices;
                    new Thread(new UpdateStockListRunnable()).start();
                }
                // Queue another score update in 30 seconds.
                mHandler.postDelayed(mUpdateLiveCardRunnable, StockConstants.UPDATE_RATE);
            }

        }

        public boolean isRunning() {
            return mIsRunning;
        }

        public void setIsRunning(boolean isRunning) {
            this.mIsRunning = isRunning;
        }
    }

    public class UpdateStockListRunnable implements Runnable{
        @Override
        public void run() {
            for (int i = 0; i < StockList.size(); i++) {
                Stocks NewStock = FindRealTimeData.findPriceBySymbol(StockList.get(i).getmSymbol());
                if(NewStock.getmCurrentPrice()!=StockConstants.PRICE_NOT_SET) {
                    //Only update when the request comeback OK
                    Log.d(TAG, "Updating stock price for :"+NewStock.getmName()+" to "+NewStock.getmCurrentPrice());
                    StockList.set(i, NewStock);
                }
                else{
                    Log.d(TAG, "Find Price Query for "+StockList.get(i).getmSymbol()+" has Failed !+!+!+!+!+!+!+!+!+!");
                }
                try {
                    //Adding this delay to prevent queries to API from happening too fast
                    Thread.sleep(200);
                    //Log.d(TAG, "Waiting for 0.2 second to prevent querying too fast for data");
                } catch (InterruptedException e) {
                    Log.d(TAG, "Couldn't wait!!!!!!!!!!!!!!!!");
                }
            }
            updateRemoteView(StockConstants.KEY_UPDATE_PRICE);
            // Always call setViews() to update the live card's RemoteViews.
            mLiveCard.setViews(mRemoteViews);
        }
    }


    public static void removeStockItem(int i){
           StockList.remove(i);
        // update the mRemoteView with the latest StockList items
        updateRemoteView(StockConstants.KEY_REMOVE_ITEM);
        //Set the last item in the stocklist to be null
        mRemoteViews.setTextViewText(PriceTextViewIDList.get(StockList.size()), "");
        mRemoteViews.setTextViewText(SymbolTextViewIDList.get(StockList.size()), "");
        // Always call setViews() to update the live card's RemoteViews.
        mLiveCard.setViews(mRemoteViews);

    }
    public static void addStockItem(Stocks stock){
        StockList.add(stock);
        // update the mRemoteView with the latest StockList items
        updateRemoteView(StockConstants.KEY_ADD_STOCK);
        // Always call setViews() to update the live card's RemoteViews.
        mLiveCard.setViews(mRemoteViews);

    }

    public static ArrayList<Stocks> getStockList(){
        return StockList;
    }

    public static void AddingOrRemovingItem(){
        isAddingOrRemoving =true;
    }
    public static void doneAddingOrRemovingItem(){
        isAddingOrRemoving =false;
    }


/*  Used for prototype to simulate a list of stocks watching
    private void initializeRandomStockList() {
        StockList=new ArrayList<>();
        for(int i=0; i< StockConstants.MAX_NUM_STOCKS; i++){
            // use the random constructor to randomly generate a stock
            Stocks stock=new Stocks(null, StockConstants.KEY_GENERATE_RANDOM_STOCK);
            StockList.add(stock);
        }
    }*/

}
