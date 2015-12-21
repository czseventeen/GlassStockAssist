package jayxu.com.glassstockassist.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import jayxu.com.glassstockassist.Model.Stocks;


/**
 * Created by Yuchen on 12/13/2015.
 */
public class AddNewStockActivity extends Activity{
    private static final int SPEECH_REQUEST = 0;
    private static final String TAG =AddNewStockActivity.class.getSimpleName() ;
    protected static String SpokenText=null;

//    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//Do not add any flags for this, cause this will kill this AddNewStockAcitivty, and the vocie recognition will end immaturely.
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please say the name of the stock you wish to add:" );
        startActivityForResult(intent, SPEECH_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Log.d(TAG, "onActivityResult triggered!!!!!!!!!!");

        if (requestCode == SPEECH_REQUEST ) {
            Log.d(TAG, "SPEECH_REQUEST Verified!!!!!!!!!!");

            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                SpokenText = results.get(0);
                Log.d(TAG, "The SPOKENTEXT was---------> " + SpokenText);

                Asyn_findSymbolsByName finder= new Asyn_findSymbolsByName();
                finder.execute(SpokenText);
                //Kill the SPEECH_REQUEST activity
                finish();

            }else{
                Log.d(TAG, "RESULT NOT OK!!!!!!!!!!! result code is: "+resultCode);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onDestroy() {
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
                //Start a new activity indicating that there was no match found
            }if(stocks.size()>1){
                //more than one stocks found, start a new activity that asks the user to select
            }if(stocks.size()==1){
                //There was only 1 match, update the StockList
                Asyn_AddNewStockToList price_finder= new Asyn_AddNewStockToList();
                price_finder.execute(stocks.get(0));
            }
        }
    }

    public class Asyn_AddNewStockToList extends AsyncTask<Stocks, Void, Stocks>{
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
