package com.gtafe.competitionteacher;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;



public class GtaToast {
    private static final String TAG = "AEEToast";
    static Toast toast;
    private static Toast mToast2;


    public static void toastOne(Context context,String msg) {

        if (toast != null) {
            toast.cancel();
        }
        Log.e(TAG, "toastOne: " );
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setDuration( Toast.LENGTH_SHORT);
        toast.show();

    }

}
