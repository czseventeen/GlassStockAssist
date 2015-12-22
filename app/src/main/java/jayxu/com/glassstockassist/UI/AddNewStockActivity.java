package jayxu.com.glassstockassist.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;


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
    protected static Context context;
//    private GestureDetector mGestureDetector;

    protected static GlassStockWatcherAlertDialog alert;

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
        context=this;
        Log.d(TAG, "onActivityResult triggered!!!!!!!!!!");
        if (requestCode == SPEECH_REQUEST ) {
            //Log.d(TAG, "SPEECH_REQUEST Verified!!!!!!!!!!");
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                SpokenText = results.get(0);
                Log.d(TAG, "The SPOKENTEXT was---------> " + SpokenText);
                Asyn_findSymbolsByName finder= new Asyn_findSymbolsByName();
                finder.execute(SpokenText);
                //Kill the SPEECH_REQUEST activity

            }else{
                Log.d(TAG, "RESULT NOT OK!!!!!!!!!!! result code is: "+resultCode);

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
        Log.d(TAG, "AddNewStockActivity is getting killed!!!!!!!!");
        finish();
        super.onDestroy();
    }

    public class Asyn_findSymbolsByName extends AsyncTask<String, Void, ArrayList<Stocks>> {

        @Override
        protected ArrayList<Stocks> doInBackground(String... params) {
            return FindRealTimeData.findSymbolByName(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Stocks> stocks) {
            if(stocks.size()==0){
//          Alert the user the name was not found and keep speech recognition running.

                DialogInterface.OnClickListener onClickListener=new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "+++++++CLICKED RECIVED!");
                        alert.dismiss();

                    }
                };

                alert=new GlassStockWatcherAlertDialog(context,
                        R.drawable.ic_warning_150,
                        R.string.no_stock_symbol_found,
                        R.string.no_stock_symbol_found_footer,
                        onClickListener);
                alert.show();

            }if(stocks.size()>1){
                finish();
                //more than one stocks found, start a new activity that asks the user to select
                Intent intent=new Intent(AddNewStockActivity.this, AddSelectionScreenActivity.class);
                intent.putExtra(StockConstants.KEY_STOCK_PARCELABLE,stocks);
                startActivity(intent);
            }if(stocks.size()==1){
                finish();
                //There was only 1 match, update the StockList
                Asyn_AddNewStockToList price_finder= new Asyn_AddNewStockToList();
                price_finder.execute(stocks.get(0));
            }
        }
    }

    /*
    * This AsyncTask is used by findSymbolByName to do the following:
    * 1. Using the Stocks obj passed to it(This is assumed that the Stock Symbol exists), use the FindRealTimeData.findPriceBySymbol method to
    * query the API to grab the JSON data of the lastest Price Volumn etc..
    * 2. The FindRealTimeData.findPriceBySymbol method will return a new Stock object with all the latest Price/Volume info.
    * 3. Using that info, add the new Stock to the LiveStockService.StockList
    * */
    public static class Asyn_AddNewStockToList extends AsyncTask<Stocks, Void, Stocks>{
        @Override
        protected Stocks doInBackground(Stocks... params) {
            return FindRealTimeData.findPriceBySymbol(params[0].getmSymbol());
        }

        @Override
        protected void onPostExecute(Stocks stock) {
            LiveStockService.addStockItem(stock);
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
