package jayxu.com.glassstockassist.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;

/**
 * Created by Yuchen on 12/22/2015.
 */
public class GlassStockWatcherAlertDialog extends Dialog {

//    private final DialogInterface.OnClickListener mOnClickListener;
    private final AudioManager mAudioManager;


    public GlassStockWatcherAlertDialog(Context context, int iconResId,
                       int textResId, int footnoteResId
                       //DialogInterface.OnClickListener onClickListener
                       ) {
        super(context);

//        mOnClickListener = onClickListener;
        mAudioManager =(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        setContentView(new CardBuilder(context, CardBuilder.Layout.ALERT)
                .setIcon(iconResId)
                .setText(textResId)
                .setFootnote(footnoteResId)
                .getView());
    }


}