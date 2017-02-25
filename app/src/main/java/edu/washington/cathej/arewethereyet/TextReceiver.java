package edu.washington.cathej.arewethereyet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by catherinejohnson on 2/18/17.
 */

public class TextReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        // This will be where we update the TopicReference with new data
        // But for now we will just send Toast notifications when it's done.

        Log.i("debug", "message sent to phone number: " + intent.getStringExtra("phoneNumber"));
        CharSequence text = intent.getStringExtra("phoneNumber") + ": Are we there yet?";

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(intent.getStringExtra("phoneNumber"), null,
                intent.getStringExtra("message"), null, null);

        //Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        //toast.show();
    }
}
