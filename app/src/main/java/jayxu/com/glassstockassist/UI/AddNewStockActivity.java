package jayxu.com.glassstockassist.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;


import com.google.android.glass.touchpad.Gesture;

import java.util.ArrayList;
import java.util.List;

import jayxu.com.glassstockassist.Model.StockConstants;
import jayxu.com.glassstockassist.Model.Stocks;
import jayxu.com.glassstockassist.R;


/**
 * Created by Yuchen on 12/13/2015.
 */
public class AddNewStockActivity extends Activity{
    private static final int SPEECH_REQUEST = 0;
    private static final String TAG =AddNewStockActivity.class.getSimpleName() ;
    protected static String SpokenText=null;
    private static boolean DisplayingErrorMessage=false;
//    private GestureDetector mGestureDetector;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//Do not add any flags for this, cause this will kill this AddNewStockAcitivty, and the vocie recognition will end immaturely.
//        SpeechRecognitionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        SpeechRecognitionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent SpeechRecognitionIntent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SpeechRecognitionIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please say the name of the stock you wish to add:");
        startActivityForResult(SpeechRecognitionIntent, SPEECH_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        //Log.d(TAG, "onActivityResult triggered!!!!!!!!!!");
        context=this;
        if (requestCode == SPEECH_REQUEST ) {
            //Log.d(TAG, "SPEECH_REQUEST Verified!!!!!!!!!!");
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                SpokenText = results.get(0);
                Log.d(TAG, "The SPOKENTEXT was---------> " + SpokenText);
                //removing spaces to fit the HTTP protocol
                SpokenText=SpokenText.replace(" ","");
                findSymbolsByName_AsynTask finder= new findSymbolsByName_AsynTask();
                finder.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SpokenText);
                //finder.execute(SpokenText);
                //Kill the SPEECH_REQUEST activity

            }else{
                finish();
                //Log.d(TAG, "RESULT NOT OK!!!!!!!!!!! result code is: "+resultCode);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        if(!DisplayingErrorMessage) {
            Log.d(TAG, "AddNewStockActivity is getting killed!!!!!!!!, The DisplayErrorMessage is:"+DisplayingErrorMessage);
            finish();
            super.onDestroy();
        }
    }

    public class findSymbolsByName_AsynTask extends AsyncTask<String, Void, ArrayList<Stocks>> {

        @Override
        protected ArrayList<Stocks> doInBackground(String... params) {
          //  android.os.Debug.waitForDebugger();
            return FindRealTimeData.findSymbolByName(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Stocks> stocks) {
          //  android.os.Debug.waitForDebugger();
            if(stocks.size()==0){
//          Alert the user the name was not found and keep speech recognition running.

                GlassStockWatcherAlertDialog alert=new GlassStockWatcherAlertDialog(context,
                        R.drawable.ic_warning_150,
                        R.string.no_stock_symbol_found,
                        R.string.no_stock_symbol_found_footer);
                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //Finishing the Speech Recognition Activity;
                        finish();

//                        Intent SpeechRecognitionIntent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                        SpeechRecognitionIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please say the name of the stock you wish to add:");
//                        startActivityForResult(SpeechRecognitionIntent, SPEECH_REQUEST);
                        Intent AddIntent = new Intent(getApplicationContext(), AddNewStockActivity.class);
                        AddIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        AddIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    am.playSoundEffect(soundEffect);
                        startActivity(AddIntent);
                        DisplayingErrorMessage=false;
                    }
                });

                alert.show();
                DisplayingErrorMessage=true;

            }else if(stocks.size()>1){
                //Finishing the Speech Recognition Activity;
                finish();
                //more than one stocks found, start a new activity that asks the user to select
                Intent intent=new Intent(getApplicationContext(), AddSelectionScreenActivity.class);
                intent.putExtra(StockConstants.KEY_STOCK_PARCELABLE,stocks);
                startActivity(intent);
            }else if(stocks.size()==1){
                //Finishing the Speech Recognition Activity;
                finish();
                //There was only 1 match, update the StockList
                AddNewStockToList_AsynTask addStockToList_AsynTask= new AddNewStockToList_AsynTask();
                addStockToList_AsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, stocks.get(0));
                //addStockToList_AsynTask.execute(stocks.get(0));
            }
//            this.cancel(true);
        }
    }

    /*
    * This AsyncTask is used by findSymbolByName to do the following:
    * 1. Using the Stocks obj passed to it(This is assumed that the Stock Symbol exists), use the FindRealTimeData.findPriceBySymbol method to
    * query the API to grab the JSON data of the lastest Price Volumn etc..
    * 2. The FindRealTimeData.findPriceBySymbol method will return a new Stock object with all the latest Price/Volume info.
    * 3. Using that info, add the new Stock to the LiveStockService.StockList
    * */
    public static class AddNewStockToList_AsynTask extends AsyncTask<Stocks, Void, Stocks>{
        @Override
        protected Stocks doInBackground(Stocks... params) {
         //   android.os.Debug.waitForDebugger();
            return FindRealTimeData.findPriceBySymbol(params[0].getmSymbol());
        }

        @Override
        protected void onPostExecute(Stocks stock) {
         //   android.os.Debug.waitForDebugger();
            LiveStockService.addStockItem(stock);
//            this.cancel(true);
        }
    }




//    private GestureDetector createGestureDetector(Context context) {
//        GestureDetector gestureDetector = new GestureDetector(context);
//        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
//            @Override
//            public boolean onGesture(Gesture gesture) {
//            if(gesture==Gesture.TAP)
//            {
//                return true;
//            }
//            if(gesture==Gesture.SWIPE_DOWN){
//                 onDestroy();
//            }
//            return false;
//            }
//        });
//
//        return gestureDetector;
//    }

}
