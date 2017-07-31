package com.liyueyun.talksdk.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by hejie on 2017/7/4.
 */

public class RectTotvView extends SurfaceView implements
        SurfaceHolder.Callback {

    // 一根手指在触碰
    private int ONE_TOUCH = 1;
    // 多根手指在触碰
    private int MORE_TOUCH = 2;
    // 当前触碰的手指模拟
    private int currentTouch = ONE_TOUCH;
    int mSurfaceHeight, mSurfaceWidth;

    private PointF mStartPoint = new PointF();

    private PointF startPointRect = new PointF();
    private boolean isStartArea = false;//单指操作起点是否在图片内
    private PointF finishPointRect = new PointF();

    private SurfaceHolder mSurHolder = null;
    float finishX=0,finishY =0;
    float startX=0,startY =0;
    private RectF imageRectF = null;

    public RectTotvView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurHolder = getHolder();
        mSurHolder.addCallback(this);

        setZOrderOnTop(true);//使surfaceview放到最顶层
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    ListenSizeBack listenSizeBack;
    public void setListenSizeBack(ListenSizeBack listenSizeBack){
        this.listenSizeBack = listenSizeBack;
    }
    private void drawClean(){
        synchronized (RectTotvView.class) {
            Canvas c = getHolder().lockCanvas();
            if(c!=null) {
                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
                getHolder().unlockCanvasAndPost(c);
            }
        }
    }
    private void drawRect(){
        synchronized (RectTotvView.class) {
            Canvas c = getHolder().lockCanvas();

            if (c != null && imageRectF != null) {
                boolean paintRect = false;
               //c.drawColor(Color.parseColor("#f3f5f7"));
               // c.drawBitmap(mBitmap, mRectSrc, imageRectF, null);//mRectSrc 图片进行裁截 imageRectF
                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
                if(isStartArea){//起点在区域内
                    if(finishPointRect.x>=imageRectF.left && finishPointRect.x<=imageRectF.right && finishPointRect.y>=imageRectF.top && finishPointRect.y<=imageRectF.bottom){//终点在区域内
                        finishX = finishPointRect.x;
                        finishY = finishPointRect.y;
                        paintRect = true;
                    }else{
                        if(finishPointRect.x>=imageRectF.left && finishPointRect.x<=imageRectF.right){
                            finishX = finishPointRect.x;
                        }else if(finishPointRect.x<imageRectF.left){
                            finishX = imageRectF.left;
                        }else if(finishPointRect.x>imageRectF.right){
                            finishX = imageRectF.right;
                        }
                       if(finishPointRect.y>=imageRectF.top && finishPointRect.y<=imageRectF.bottom){
                           finishY = finishPointRect.y;
                       }else if(finishPointRect.y<imageRectF.top){
                           finishY = imageRectF.top;
                       }else if(finishPointRect.y>imageRectF.bottom){
                           finishY = imageRectF.bottom;
                       }
                        paintRect = true;
                    }
                }else{
                    if(finishPointRect.x>=imageRectF.left && finishPointRect.x<=imageRectF.right && finishPointRect.y>=imageRectF.top && finishPointRect.y<=imageRectF.bottom){//终点在区域内
                        finishX = finishPointRect.x;
                        finishY = finishPointRect.y;
                        paintRect = true;
                    }else{
                        if((startPointRect.x<imageRectF.left &&finishPointRect.x>imageRectF.right)){//x直接跨过图片区域
                            startX = imageRectF.left;
                            finishX = imageRectF.right;
                            startY = startPointRect.y;
                            finishY = finishPointRect.y;
                            paintRect = true;
                        }else if((startPointRect.x>imageRectF.right &&finishPointRect.x<imageRectF.left)){
                            finishX = imageRectF.left;
                            startX = imageRectF.right;
                            startY = startPointRect.y;
                            finishY = finishPointRect.y;
                            paintRect = true;
                        }else if((startPointRect.y<imageRectF.top &&finishPointRect.y>imageRectF.bottom)){//y直接跨过图片区域
                            startY = imageRectF.top;
                            finishY = imageRectF.bottom;
                            startX = startPointRect.x;
                            finishX = finishPointRect.x;
                            paintRect = true;
                        }else if((startPointRect.y>imageRectF.bottom &&finishPointRect.y<imageRectF.top)){
                            finishY = imageRectF.top;
                            startY = imageRectF.bottom;
                            startX = startPointRect.x;
                            finishX = finishPointRect.x;
                            paintRect = true;
                        }
                    }
                }
               if(paintRect) {
                   Paint p = new Paint();
                   p.setColor(Color.parseColor("#4c55a7f8"));
                   p.setStyle(Paint.Style.FILL);//实心矩形框
                   c.drawRect(startX, startY, finishX, finishY, p);
                   p.setStyle(Paint.Style.STROKE);//设置空心
                   p.setStrokeWidth(1);
                   p.setColor(Color.parseColor("#55a7f8"));
                   c.drawRect(startX, startY, finishX, finishY, p);
               }
                getHolder().unlockCanvasAndPost(c);
            }
        }
    }


    public boolean setonTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                currentTouch = MORE_TOUCH;
                drawClean();
                return false;
            case MotionEvent.ACTION_DOWN:
                //获取 图片的位置 imageRectF
                imageRectF = listenSizeBack.getImageRectF();
                if(imageRectF==null)
                    return false;
                currentTouch = ONE_TOUCH;
                //mStartPoint.set(event.getX(), event.getY());
                startPointRect.set(event.getX(),event.getY());
                 if(startPointRect.x>=imageRectF.left && startPointRect.x<=imageRectF.right && startPointRect.y>=imageRectF.top && startPointRect.y<=imageRectF.bottom){
                     isStartArea = true;
                     startX = startPointRect.x;
                     startY = startPointRect.y;
                 }else{
                     isStartArea = false;
                     if(startPointRect.x>=imageRectF.left && startPointRect.x<=imageRectF.right){
                         startX = startPointRect.x;
                     }else if(startPointRect.x<imageRectF.left){
                         startX = imageRectF.left;
                     }else if(startPointRect.x>imageRectF.right){
                         startX = imageRectF.right;
                     }
                     if(startPointRect.y>=imageRectF.top && startPointRect.y<=imageRectF.bottom){
                         startY = startPointRect.y;
                     }else if(startPointRect.y<imageRectF.top){
                         startY = imageRectF.top;
                     }else if(startPointRect.y>imageRectF.bottom){
                         startY = imageRectF.bottom;
                     }
                 }
                 finishX=0;
                 finishY =0;
                 return false;
            case MotionEvent.ACTION_MOVE:
                if(currentTouch == ONE_TOUCH &&imageRectF!=null && event.getPointerCount() == 1){
                    finishPointRect.set(event.getX(), event.getY());
                    drawRect();
                    return true;
                }else{
                    return false;
                }
            case MotionEvent.ACTION_UP:
              if(currentTouch == ONE_TOUCH&&imageRectF!=null){
                  float minWidth = 0,minHeight =0;
                  float diffWidth =0,diffHeight =0;//差值
                  if(startX<finishX){
                      minWidth = startX;
                      diffWidth = finishX - startX;
                  }else{
                      minWidth = finishX;
                      diffWidth = startX - finishX;
                  }
                  if(startY<finishY){
                      minHeight = startY;
                      diffHeight = finishY - startY;
                  }else{
                      minHeight = finishY;
                      diffHeight = startY - finishY;
                  }
                  minWidth = minWidth -imageRectF.left;
                  minHeight = minHeight - imageRectF.top;
                  float imageWidth = imageRectF.right -imageRectF.left;
                  float imageHeight = imageRectF.bottom - imageRectF.top;

                  listenSizeBack.onListenBack(minWidth/imageWidth,minHeight/imageHeight,diffWidth/imageWidth,diffHeight/imageHeight);
                  imageRectF = null;
                  return true;
              }
                imageRectF = null;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    // 初始化
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        synchronized (RectTotvView.class) {
            mSurfaceHeight = height;
            mSurfaceWidth = width;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public abstract static class ListenSizeBack extends Object {
        public  abstract void onListenBack(float x,float y,float width,float height);
        public  abstract RectF getImageRectF();
    }
}
