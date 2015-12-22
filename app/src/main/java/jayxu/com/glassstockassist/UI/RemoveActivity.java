package jayxu.com.glassstockassist.UI;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;

import jayxu.com.glassstockassist.Model.StockScrollAdapter;
import jayxu.com.glassstockassist.Model.Stocks;

/**
 * Created by Yuchen on 12/13/2015.
 */
public class RemoveActivity extends Activity {
    private static final String TAG= RemoveActivity.class.getSimpleName();

    private ArrayList<Stocks> stocklist=LiveStockService.getStockList();
    private CardScrollView mCardScrollerView;
    private ArrayList<CardBuilder> mCards=new ArrayList<>();
//    private GestureDetector mGestureDetector;
    private CardScrollAdapter mScrollAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PopulateCardList(this);
        mCardScrollerView =new CardScrollView(this);
        mScrollAdapter=new StockScrollAdapter(mCards);
        mCardScrollerView.setAdapter(mScrollAdapter);
        mCardScrollerView.activate();
        setContentView(mCardScrollerView);
        setCardScrollerListener();
    }


    private void PopulateCardList(Context context){
        int i=0;
        for(Stocks stock: stocklist) {
            mCards.add(i, new CardBuilder(context, CardBuilder.Layout.MENU)
                    .setText(stock.getmSymbol())
            );
            i++;
        }
    }


//    private GestureDetector createGestureDetector(Context context) {
//        GestureDetector gestureDetector = new GestureDetector(context);
//        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
//            @Override
//            public boolean onGesture(Gesture gesture) {
//            if(gesture==Gesture.TAP)
//            {
//
//                return true;
//            }
//            return false;
//            }
//        });
//
//        return gestureDetector;
//    }

//    @Override
//    public boolean onCreatePanelMenu(int featureId, Menu menu){
//        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS || featureId ==  Window.FEATURE_OPTIONS_PANEL) {
//            getMenuInflater().inflate(R.menu.delete_menu, menu);
//            return true;
//        }
//        return super.onCreatePanelMenu(featureId, menu);
//    }



    private void setCardScrollerListener() {
        mCardScrollerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked view at position " + position + "Removing "+stocklist.get(position).getmSymbol());
                LiveStockService.removeStockItem(position);
                // Play sound.
                int soundEffect = Sounds.TAP;
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);
                Log.d(TAG, "The isAddingOrRemoving variable is "+LiveStockService.isAddingOrRemoving);
                finish();

            }
        });
    }

//
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        // Open the options menu right away.
//        openOptionsMenu();
//    }
//    //Update the menu with current stocks added
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        int order=0;
//        for(Stocks stock: stocklist) {
//            menu.add(0,order,menu.NONE, stock.getmSymbol());
//            order++;
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.delete_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//       super.onOptionsItemSelected(item);
//       LiveStockService.removeStockItem(item.getItemId());
//
//        return false;
//    }
}
