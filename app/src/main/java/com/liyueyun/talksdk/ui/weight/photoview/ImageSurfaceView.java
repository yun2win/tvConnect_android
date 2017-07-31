package com.liyueyun.talksdk.ui.weight.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by hejie on 2017/7/4.
 */

public class ImageSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, View.OnTouchListener {

    // 一根手指在触碰
    private int ONE_TOUCH = 1;
    // 多根手指在触碰
    private int MORE_TOUCH = 2;
    // 当前触碰的手指模拟
    private int currentTouch = ONE_TOUCH;
    private static final float MAX_ZOOM_SCALE = 4.0f;
    private static final float MIN_ZOOM_SCALE = 1.0f;
    private float default_scale = MIN_ZOOM_SCALE;
    private static final float FLOAT_TYPE = 1.0f;
    private float mCurrentMaxScale = MAX_ZOOM_SCALE;
    private float mCurrentScale = 1.0f;

    private Rect mRectSrc = new Rect(); // used for render image.
    private Rect mRectDes = new Rect(); // used for store size of monitor.

    private int mCenterX, mCenterY;
    int mSurfaceHeight, mSurfaceWidth, mImageHeight, mImageWidth;

    private PointF mStartPoint = new PointF();

    private PointF startPointRect = new PointF();
    private boolean isStartArea = false;//单指操作起点是否在图片内
    private PointF finishPointRect = new PointF();
    private float mStartDistance = 0f;

    private SurfaceHolder mSurHolder = null;
    private Bitmap mBitmap;

    float finishX=0,finishY =0;
    float startX=0,startY =0;
    public ImageSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurHolder = getHolder();
        mSurHolder.addCallback(this);
        this.setOnTouchListener(this);
        setZOrderOnTop(true);//使surfaceview放到最顶层
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    private void init(float default_scale) {
        mCurrentMaxScale = Math.max(
                MIN_ZOOM_SCALE,
                4 * Math.min(FLOAT_TYPE * mImageHeight / mSurfaceHeight, 1.0f
                        * mImageWidth / mSurfaceWidth));
        mCurrentScale = default_scale;
        mCenterX = mImageWidth / 2;
        mCenterY = mImageHeight / 2;
        calcRect();

    }

    private void adjustCenter() {
        int w = mRectSrc.right - mRectSrc.left;
        int h = mRectSrc.bottom - mRectSrc.top;

        if (mCenterX - w / 2 < 0) {
            mCenterX = w / 2;
            mRectSrc.left = 0;
            mRectSrc.right = w;
        } else if (mCenterX + w / 2 >= mImageWidth) {
            mCenterX = mImageWidth - w / 2;
            mRectSrc.right = mImageWidth;
            mRectSrc.left = mRectSrc.right - w;

        } else {
            mRectSrc.left = mCenterX - w / 2;
            mRectSrc.right = mRectSrc.left + w;
        }

        if (mCenterY - h / 2 < 0) {
            mCenterY = h / 2;
            mRectSrc.top = 0;
            mRectSrc.bottom = h;
        } else if (mCenterY + h / 2 >= mImageHeight) {
            mCenterY = mImageHeight - h / 2;
            mRectSrc.bottom = mImageHeight;
            mRectSrc.top = mRectSrc.bottom - h;
        } else {
            mRectSrc.top = mCenterY - h / 2;
            mRectSrc.bottom = mRectSrc.top + h;
        }

    }

    private void calcRect() {
        int w, h;
        float imageRatio, surfaceRatio;
        imageRatio = FLOAT_TYPE * mImageWidth / mImageHeight;
        surfaceRatio = FLOAT_TYPE * mSurfaceWidth / mSurfaceHeight;

        if (imageRatio < surfaceRatio) {
            h = mSurfaceHeight;
            w = (int) (h * imageRatio);
        } else {
            w = mSurfaceWidth;
            h = (int) (w / imageRatio);
        }

        if (mCurrentScale > MIN_ZOOM_SCALE) {
            w = Math.min(mSurfaceWidth, (int) (w * mCurrentScale));
            h = Math.min(mSurfaceHeight, (int) (h * mCurrentScale));
        } else {
            mCurrentScale = MIN_ZOOM_SCALE;
        }

        mRectDes.left = (mSurfaceWidth - w) / 2;
        mRectDes.top = (mSurfaceHeight - h) / 2;
        mRectDes.right = mRectDes.left + w;
        mRectDes.bottom = mRectDes.top + h;

        float curImageRatio = FLOAT_TYPE * w / h;
        int h2, w2;
        if (curImageRatio > imageRatio) {
            h2 = (int) (mImageHeight / mCurrentScale);
            w2 = (int) (h2 * curImageRatio);
        } else {

            w2 = (int) (mImageWidth / mCurrentScale);
            h2 = (int) (w2 / curImageRatio);
        }
        mRectSrc.left = mCenterX - w2 / 2;
        mRectSrc.top = mCenterY - h2 / 2;
        mRectSrc.right = mRectSrc.left + w2;
        mRectSrc.bottom = mRectSrc.top + h2;
    }

    public void setMaxZoom(float value) {
        mCurrentMaxScale = value;
    }
    ListenSizeBack listenSizeBack;
    public void setListenSizeBack(ListenSizeBack listenSizeBack){
        this.listenSizeBack = listenSizeBack;
    }

    public void setBitmap(Bitmap b, float default_scale) {

        if (b == null) {
            return;
        }
        synchronized (ImageSurfaceView.class) {
            mBitmap = b;
            if (mImageHeight != mBitmap.getHeight()
                    || mImageWidth != mBitmap.getWidth()) {
                mImageHeight = mBitmap.getHeight();
                mImageWidth = mBitmap.getWidth();
                this.default_scale = default_scale;
                init(default_scale);
            }
            adjustCenter();
            showBitmap();
        }

    }

    private void showBitmap() {
        synchronized (ImageSurfaceView.class) {
            Canvas c = getHolder().lockCanvas();
            if (c != null && mBitmap != null) {
                //c.drawColor(Color.parseColor("#f3f5f7"));
                //c.drawBitmap(mBitmap, mRectSrc, mRectDes, null);
                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
                getHolder().unlockCanvasAndPost(c);
            }
        }
    }
    private void drawRect(){
        synchronized (ImageSurfaceView.class) {
            Canvas c = getHolder().lockCanvas();
            Paint p = new Paint();
            p.setColor(Color.RED);// 设置红色
            p.setStyle(Paint.Style.STROKE);//设置空心
            p.setStrokeWidth(5);
            if (c != null && mBitmap != null) {

               //c.drawColor(Color.parseColor("#f3f5f7"));
               // c.drawBitmap(mBitmap, mRectSrc, mRectDes, null);//mRectSrc 图片进行裁截 mRectDes图片在Canvas画布中显示的区域
                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
                if(isStartArea){//起点在区域内
                    if(finishPointRect.x>=mRectDes.left && finishPointRect.x<=mRectDes.right && finishPointRect.y>=mRectDes.top && finishPointRect.y<=mRectDes.bottom){//终点在区域内
                        finishX = finishPointRect.x;
                        finishY = finishPointRect.y;
                        c.drawRect(startX, startY, finishX, finishY, p);// 长方形
                    }else{
                        if(finishPointRect.x>=mRectDes.left && finishPointRect.x<=mRectDes.right){
                            finishX = finishPointRect.x;
                        }else if(finishPointRect.x<mRectDes.left){
                            finishX = mRectDes.left;
                        }else if(finishPointRect.x>mRectDes.right){
                            finishX = mRectDes.right;
                        }
                       if(finishPointRect.y>=mRectDes.top && finishPointRect.y<=mRectDes.bottom){
                           finishY = finishPointRect.y;
                       }else if(finishPointRect.y<mRectDes.top){
                           finishY = mRectDes.top;
                       }else if(finishPointRect.y>mRectDes.bottom){
                           finishY = mRectDes.bottom;
                       }
                        c.drawRect(startX, startY, finishX, finishY, p);// 长方形
                    }
                }else{
                    if(finishPointRect.x>=mRectDes.left && finishPointRect.x<=mRectDes.right && finishPointRect.y>=mRectDes.top && finishPointRect.y<=mRectDes.bottom){//终点在区域内
                        finishX = finishPointRect.x;
                        finishY = finishPointRect.y;
                        c.drawRect(startX, startY, finishX, finishY, p);
                    }else{
                        if((startPointRect.x<mRectDes.left &&finishPointRect.x>mRectDes.right)){//x直接跨过图片区域
                            startX = mRectDes.left;
                            finishX = mRectDes.right;
                            startY = startPointRect.y;
                            finishY = finishPointRect.y;
                            c.drawRect(startX, startY, finishX, finishY, p);
                        }else if((startPointRect.x>mRectDes.right &&finishPointRect.x<mRectDes.left)){
                            finishX = mRectDes.left;
                            startX = mRectDes.right;
                            startY = startPointRect.y;
                            finishY = finishPointRect.y;
                            c.drawRect(startX, startY, finishX, finishY, p);
                        }else if((startPointRect.y<mRectDes.top &&finishPointRect.y>mRectDes.bottom)){//y直接跨过图片区域
                            startY = mRectDes.top;
                            finishY = mRectDes.bottom;
                            startX = startPointRect.x;
                            finishX = finishPointRect.x;
                            c.drawRect(startX, startY, finishX, finishY, p);
                        }else if((startPointRect.y>mRectDes.bottom &&finishPointRect.y<mRectDes.top)){
                            finishY = mRectDes.top;
                            startY = mRectDes.bottom;
                            startX = startPointRect.x;
                            finishX = finishPointRect.x;
                            c.drawRect(startX, startY, finishX, finishY, p);
                        }
                    }
                }
                getHolder().unlockCanvasAndPost(c);
            }
        }
    }

    private void dragAction(MotionEvent event) {

        synchronized (ImageSurfaceView.class) {
            PointF currentPoint = new PointF();
            currentPoint.set(event.getX(), event.getY());
            int offsetX = (int) currentPoint.x - (int) mStartPoint.x;
            int offsetY = (int) currentPoint.y - (int) mStartPoint.y;
            mStartPoint = currentPoint;

            mCenterX -= offsetX/4;
            mCenterY -= offsetY/4;

            adjustCenter();
            showBitmap();
        }
    }

    private void zoomAcition(MotionEvent event) {

        synchronized (ImageSurfaceView.class) {

            float newDist = spacing(event);
            float scale = newDist / mStartDistance;
            mStartDistance = newDist;

            mCurrentScale *= scale;
            mCurrentScale = Math.max(FLOAT_TYPE,
                    Math.min(mCurrentScale, mCurrentMaxScale));

            calcRect();
            adjustCenter();
            showBitmap();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                currentTouch = ONE_TOUCH;
                //mStartPoint.set(event.getX(), event.getY());
                startPointRect.set(event.getX(),event.getY());
                 if(startPointRect.x>=mRectDes.left && startPointRect.x<=mRectDes.right && startPointRect.y>=mRectDes.top && startPointRect.y<=mRectDes.bottom){
                     isStartArea = true;
                     startX = startPointRect.x;
                     startY = startPointRect.y;
                 }else{
                     isStartArea = false;
                     if(startPointRect.x>=mRectDes.left && startPointRect.x<=mRectDes.right){
                         startX = startPointRect.x;
                     }else if(startPointRect.x<mRectDes.left){
                         startX = mRectDes.left;
                     }else if(startPointRect.x>mRectDes.right){
                         startX = mRectDes.right;
                     }
                     if(startPointRect.y>=mRectDes.top && startPointRect.y<=mRectDes.bottom){
                         startY = startPointRect.y;
                     }else if(startPointRect.y<mRectDes.top){
                         startY = mRectDes.top;
                     }else if(startPointRect.y>mRectDes.bottom){
                         startY = mRectDes.bottom;
                     }
                 }
                 finishX=0;
                 finishY =0;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                currentTouch = MORE_TOUCH;
                float distance = spacing(event);
                if (distance > 10f) {
                    mStartPoint.set(event.getX(), event.getY());
                    mStartDistance = distance;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(currentTouch == MORE_TOUCH){
                    if (event.getPointerCount() == 1)
                        return true;
                    float newDist = spacing(event);
                    float gapMove = Math.abs(newDist - mStartDistance);
                    if(gapMove<4f){
                        dragAction(event);
                    }else{
                        zoomAcition(event);
                    }
                }else{
                    finishPointRect.set(event.getX(), event.getY());
                    drawRect();
                }

                break;
            case MotionEvent.ACTION_UP:
              if(currentTouch == ONE_TOUCH){
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
                  float initScale =0;
                  if(((float)mSurfaceWidth/mSurfaceHeight)>((float)mImageWidth/mImageHeight)){
                      initScale = (float)mImageHeight/mSurfaceHeight;
                  }else{
                      initScale = (float)mImageWidth/mSurfaceWidth;
                  }
                diffWidth =  diffWidth/mCurrentScale;
                diffHeight = diffHeight/mCurrentScale;
                minWidth = (minWidth-mRectDes.left)/mCurrentScale+mRectSrc.left;
                minHeight =(minHeight - mRectDes.top)/mCurrentScale+mRectSrc.top;
                  listenSizeBack.onListenBack(initScale*minWidth/mImageWidth,initScale*minHeight/mImageHeight,initScale*diffWidth/mImageWidth,initScale*diffHeight/mImageHeight);
              }
            case MotionEvent.ACTION_POINTER_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    // 初始化
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        synchronized (ImageSurfaceView.class) {
            mRectDes.set(0, 0, width, height);
            mSurfaceHeight = height;
            mSurfaceWidth = width;
            init(default_scale);
            if (mBitmap != null) {
                showBitmap();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public abstract static class ListenSizeBack extends Object {
        public  abstract void onListenBack(float x,float y,float width,float height);
    }
}
