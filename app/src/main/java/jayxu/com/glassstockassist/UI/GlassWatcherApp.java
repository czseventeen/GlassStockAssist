package jayxu.com.glassstockassist.UI;

import android.app.Application;
import android.content.Context;

/**
 * Created by Yuchen on 12/21/2015.
 */
public class GlassWatcherApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        GlassWatcherApp.context=getApplicationContext();
    }

    public static Context getAppContext(){
        return GlassWatcherApp.context;
    }
}
