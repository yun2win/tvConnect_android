package com.liyueyun.talkdemo.ui.weight.photoview;

/**
 * Created by hejie on 2017/7/4.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TouchImageView extends SurfaceView implements Callback {
    private SurfaceHolder holder = null;
    private Bitmap bitmap = null;
    private Matrix matrix = null;

    // 一根手指在触碰
    private int ONE_TOUCH = 1;
    // 多根手指在触碰
    private int MORE_TOUCH = 2;
    // 当前触碰的手指模拟
    private int currentTouch = ONE_TOUCH;

    // 单根手指触碰的开始点
    private Point startPoint;

    // 多根手指滑动的前后距离
    private float dis1;
    private float dis2;
    // 多根手指滑动的中心点
    private Point centerPoint;

    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchImageView(Context context) {
        super(context);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        holder = this.getHolder();
        holder.addCallback(this);

        // 得到一个Bitmap对象
     /*   bitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.img_027)).getBitmap();*/

        matrix = new Matrix();

        startPoint = new Point();
        centerPoint = new Point();
    }

    /**
     * 绘画
     */
    private void draw() {
        // 锁定画布
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            return;
        }

        // 清除画布
        canvas.drawColor(Color.BLACK);

        // 画图片
        canvas.drawBitmap(bitmap, matrix, null);

        // 解锁画布并提交
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 能接受单根和多根手指的触碰
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // 得到单根手指一开始接触的坐标
                startPoint.set((int) event.getX(), (int) event.getY());
                // 设置模式为单根手指
                currentTouch = ONE_TOUCH;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // 得到两根手指一开始触碰的距离
                dis1 = getDistance(event);
                // 得到这两根手指的中心点
                getCenter(event);
                // 设置模式为多根手指触碰
                currentTouch = MORE_TOUCH;
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentTouch == ONE_TOUCH) {
                    // 如果是一根手指,则设置平移,平移的距离是从开始点到现在的移动点
                    matrix.postTranslate((event.getX() - startPoint.x) / 10,
                            (event.getY() - startPoint.y) / 10);
                } else if (currentTouch == MORE_TOUCH) {
                    // 如果是多根手指模式,则设置缩放,缩放的比例是刚接触时两根手指之间的距离与移动后两根手指之间的距离比.
                    // 缩放的中心点是刚接触时两根手指的中心点
                    dis2 = getDistance(event);
                    matrix.postScale((dis2 / dis1), (dis2 / dis1), centerPoint.x,
                            centerPoint.y);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                break;
            default:
                break;
        }

        // 重新画图像
        draw();

        return true;
    }

    /**
     * 得到两根手指之间的距离
     *
     * @param event
     * @return
     */
    private float getDistance(MotionEvent event) {
        float a = event.getX(1) - event.getX(0);
        float b = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    /**
     * 得到两根手指之间的中心点
     *
     * @param event
     */
    private void getCenter(MotionEvent event) {
        float a = event.getX(1) + event.getX(0);
        float b = event.getY(1) + event.getY(0);
        centerPoint.set((int) a / 2, (int) b / 2);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 让这个View在创建的时候就开始画画
        draw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}