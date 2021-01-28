package com.gtafe.competitionstudent;

import android.content.Context;
import android.content.SharedPreferences;

import static com.gtafe.competitionstudent.Constant.PACKGE;

/**
 * Created by ZhouJF on 2021-01-24.
 */
public class SharePrefrenceUtils {

    public static void setString() {


    }

    public static String getString(Context context, String Key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACKGE, Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(Key, "");
        return string;
    }

    public static void putString(Context context, String Key, String value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PACKGE, Context.MODE_PRIVATE).edit();
        edit.putString(Key, value).apply();

    }

    public static int getInt(Context context, String Key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACKGE, Context.MODE_PRIVATE);
        int string = sharedPreferences.getInt(Key, 0);
        return string;
    }

    public static void putInt(Context context, String Key, int value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PACKGE, Context.MODE_PRIVATE).edit();
        edit.putInt(Key, value).apply();

    }


    public static boolean getBoolean(Context context, String Key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACKGE, Context.MODE_PRIVATE);
        boolean string = sharedPreferences.getBoolean(Key, false);
        return string;
    }

    public static void putBoolean(Context context, String Key, boolean value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PACKGE, Context.MODE_PRIVATE).edit();
        edit.putBoolean(Key, value).apply();

    }
}
