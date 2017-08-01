package com.liyueyun.talkdemo.common;

import android.app.Application;

import com.yun2win.talksdk.Back;
import com.yun2win.talksdk.TalkManage;


/**
 * Created by hejie on 2017/7/24.
 */

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TalkManage.getInstance().initTalk(this, "106", "就是我", "", "pcDeF3PT63XYAlvm", "CsFJameMf3eQUCzT4ERbOzCh", new Back.Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(int code, String error) {
            }
        });

    }
}
