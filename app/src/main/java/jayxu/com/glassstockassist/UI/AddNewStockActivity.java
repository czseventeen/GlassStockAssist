package jayxu.com.glassstockassist.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.List;

import jayxu.com.glassstockassist.Model.Stocks;
import jayxu.com.glassstockassist.R;

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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please say the name of the stock" );
        startActivityForResult(intent, SPEECH_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Log.d(TAG, "Retrieved speech!!!!!!!!!!");

        if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            SpokenText = results.get(0);
            Log.d(TAG, "The SPOKENTEXT was---------> " +SpokenText);
            //Initialize a new Stocks obj based on SpokenText, and a randomly generated Price.
            Stocks stock=new Stocks(SpokenText, Stocks.KEY_GENERATE_RANDOM_STOCK);
            LiveStockService.addStockItem(stock);
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
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
