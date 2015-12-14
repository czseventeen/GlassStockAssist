package jayxu.com.glassstockassist.UI;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import jayxu.com.glassstockassist.R;

/**
 * A transparent {@link Activity} displaying a "Stop" options menu to remove the {@link LiveCard}.
 */
public class LiveCardMenuActivity extends Activity {

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Open the options menu right away.
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.live_stock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add:{
                Intent AddIntent=new Intent(this,AddNewStockActivity.class);
                startActivity(AddIntent);
            }

            case R.id.action_delete:{
                Intent DeleteIntent = new Intent(this, RemoveActivity.class);
                DeleteIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                DeleteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(DeleteIntent);
            }
            case R.id.action_stop:
                // Stop the service which will unpublish the live card.
                stopService(new Intent(this, LiveStockService.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        // Nothing else to do, finish the Activity.
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
