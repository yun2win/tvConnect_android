package com.liyueyun.talkdemo.ui.weight.photoview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by hejie on 2017/7/5.
 */

public class PhotoImage extends ImageView {

    public PhotoImage(Context context) {
        super(context);
    }

    public PhotoImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
