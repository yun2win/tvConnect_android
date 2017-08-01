/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.liyueyun.talkdemo.ui.weight.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;


public class PhotoView extends ImageView implements IPhotoView {

    private final PhotoViewAttacher mAttacher;

    private ScaleType mPendingScaleType;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PhotoView(Context context, AttributeSet attr, int defStyle) {

           super(context, attr, defStyle);
           super.setScaleType(ScaleType.MATRIX);
           mAttacher = new PhotoViewAttacher(this);
        try {
           if (null != mPendingScaleType) {
               setScaleType(mPendingScaleType);
               mPendingScaleType = null;
           }
       }catch (Exception e){}
    }

    /**
     * @deprecated use {@link #setRotationTo(float)}
     */
    @Override
    public void setPhotoViewRotation(float rotationDegree) {
       try {
        mAttacher.setRotationTo(rotationDegree);
       }catch (Exception e){}
    }
    
    @Override
    public void setRotationTo(float rotationDegree) {
        try {
        mAttacher.setRotationTo(rotationDegree);
        }catch (Exception e){}
    }

    @Override
    public void setRotationBy(float rotationDegree) {
        try {
        mAttacher.setRotationBy(rotationDegree);
        }catch (Exception e){}
    }

    @Override
    public boolean canZoom() {
        try {
        return mAttacher.canZoom();
        }catch (Exception e){
            return  false;
        }
    }

    @Override
    public RectF getDisplayRect() {
        return mAttacher.getDisplayRect();
    }

    @Override
    public Matrix getDisplayMatrix() {
        return mAttacher.getDrawMatrix();
    }

    @Override
    public boolean setDisplayMatrix(Matrix finalRectangle) {
        return mAttacher.setDisplayMatrix(finalRectangle);
    }

    @Override
    @Deprecated
    public float getMinScale() {
        return getMinimumScale();
    }

    @Override
    public float getMinimumScale() {
        return mAttacher.getMinimumScale();
    }

    @Override
    @Deprecated
    public float getMidScale() {
        return getMediumScale();
    }

    @Override
    public float getMediumScale() {
        return mAttacher.getMediumScale();
    }

    @Override
    @Deprecated
    public float getMaxScale() {
        return getMaximumScale();
    }

    @Override
    public float getMaximumScale() {
        return mAttacher.getMaximumScale();
    }

    @Override
    public float getScale() {
        return mAttacher.getScale();
    }

    @Override
    public ScaleType getScaleType() {
        return mAttacher.getScaleType();
    }

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        try {
        mAttacher.setAllowParentInterceptOnEdge(allow);
        }catch (Exception e){}
    }

    @Override
    @Deprecated
    public void setMinScale(float minScale) {
        try {
        setMinimumScale(minScale);
        }catch (Exception e){}
    }

    @Override
    public void setMinimumScale(float minimumScale) {
        try {
        mAttacher.setMinimumScale(minimumScale);
        }catch (Exception e){}
    }

    @Override
    @Deprecated
    public void setMidScale(float midScale) {
        try {
        setMediumScale(midScale);
        }catch (Exception e){}
    }

    @Override
    public void setMediumScale(float mediumScale) {
        try {
        mAttacher.setMediumScale(mediumScale);
        }catch (Exception e){}
    }

    @Override
    @Deprecated
    public void setMaxScale(float maxScale) {
        try {
        setMaximumScale(maxScale);
        }catch (Exception e){}
    }

    @Override
    public void setMaximumScale(float maximumScale) {
        try {
        mAttacher.setMaximumScale(maximumScale);
        }catch (Exception e){}
    }

    @Override
    // setImageBitmap calls through to this method
    public void setImageDrawable(Drawable drawable) {
        try {
        super.setImageDrawable(drawable);
        if (null != mAttacher) {
            mAttacher.update();
        }
        }catch (Exception e){}
    }

    @Override
    public void setImageResource(int resId) {
        try {
        super.setImageResource(resId);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }catch (Exception e){}
    }

    @Override
    public void setImageURI(Uri uri) {
        try {
        super.setImageURI(uri);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }catch (Exception e){}
    }

    @Override
    public void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener) {
        try {
        mAttacher.setOnMatrixChangeListener(listener);
        }catch (Exception e){}
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        try {
        mAttacher.setOnLongClickListener(l);
        }catch (Exception e){}
    }

    @Override
    public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener) {
        try {
        mAttacher.setOnPhotoTapListener(listener);
        }catch (Exception e){}
    }

    @Override
    public PhotoViewAttacher.OnPhotoTapListener getOnPhotoTapListener() {
        return mAttacher.getOnPhotoTapListener();
    }

    @Override
    public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener) {
        try {
        mAttacher.setOnViewTapListener(listener);
        }catch (Exception e){}
    }

    @Override
    public PhotoViewAttacher.OnViewTapListener getOnViewTapListener() {
        return mAttacher.getOnViewTapListener();
    }

    @Override
    public void setScale(float scale) {
        try {
        mAttacher.setScale(scale);
        }catch (Exception e){}
    }

    @Override
    public void setScale(float scale, boolean animate) {
        try {
        mAttacher.setScale(scale, animate);
        }catch (Exception e){}
    }

    @Override
    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        try {
        mAttacher.setScale(scale, focalX, focalY, animate);
        }catch (Exception e){}
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        try {
        if (null != mAttacher) {
            mAttacher.setScaleType(scaleType);
        } else {
            mPendingScaleType = scaleType;
        }
        }catch (Exception e){}
    }

    @Override
    public void setZoomable(boolean zoomable) {
        try {
        mAttacher.setZoomable(zoomable);
        }catch (Exception e){}
    }

    @Override
    public Bitmap getVisibleRectangleBitmap() {
        return mAttacher.getVisibleRectangleBitmap();
    }

    @Override
    public void setZoomTransitionDuration(int milliseconds) {
        try {
        mAttacher.setZoomTransitionDuration(milliseconds);
        }catch (Exception e){}
    }

    @Override
    public IPhotoView getIPhotoViewImplementation() {
        return mAttacher;
    }

    @Override
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
        try {
        mAttacher.setOnDoubleTapListener(newOnDoubleTapListener);
    }catch (Exception e){}
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttacher.cleanup();
        super.onDetachedFromWindow();
    }

    public PhotoViewAttacher getPhotoViewAttacher(){
        return mAttacher;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try{
            return  super.onTouchEvent(event);
        } catch(Exception ex) {
            return false;
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public float getViewX() {
        float x = 0;
        if(mAttacher!=null){
            x =mAttacher.getViewX();
        }
        return x;
    }

    public float getViewY() {
        float y = 0;
        if(mAttacher!=null){
            y =mAttacher.getViewY();
        }
        return y;
    }


}