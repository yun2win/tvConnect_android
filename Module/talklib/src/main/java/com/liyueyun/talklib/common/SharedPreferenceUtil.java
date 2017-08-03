package com.liyueyun.talklib.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hejie on 2017/7/20.
 */

public class SharedPreferenceUtil {

     private static String shareName ="talksdk_info";

    public static void setTalkTvList(Context context,String tvlist){
        SharedPreferences preferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1=preferences.edit();
        editor1.putString("tvlist", tvlist);
        editor1.commit();
    }

    public static String getTalkTvList(Context context){
        SharedPreferences preferences = context.getSharedPreferences(shareName,Context.MODE_PRIVATE);
        return preferences.getString("tvlist","");
    }
}
