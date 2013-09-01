package appMetro.origDestino;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

public class ImageZoomView extends View implements Observer {
    private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private final Rect mRectSrc = new Rect();
    private final Rect mRectDst = new Rect();
    private Bitmap mBitmap;
    private float mAspectQuotient;
    private ZoomState mState;

    public ImageZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImage(Bitmap bitmap) {
        mBitmap = bitmap;
        calculateAspectQuotient();
        invalidate();
    }

    public void setZoomState(ZoomState state) {
        if (mState != null) {
            mState.deleteObserver(this);
        }
        mState = state;
        mState.addObserver(this);
        invalidate();
    }

    private void calculateAspectQuotient() {
        if (mBitmap != null) {
            mAspectQuotient = (((float)mBitmap.getWidth()) / mBitmap.getHeight())
                    / (((float)getWidth()) / getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null && mState != null) {
            final int viewWidth = getWidth();
            final int viewHeight = getHeight();
            final int bitmapWidth = mBitmap.getWidth();
            final int bitmapHeight = mBitmap.getHeight();

            final float panX = mState.getPanX();
            final float panY = mState.getPanY();
            final float zoomX = mState.getZoomX(mAspectQuotient) * viewWidth / bitmapWidth;
            final float zoomY = mState.getZoomY(mAspectQuotient) * viewHeight / bitmapHeight;

            mRectDst.left = 0;
            mRectDst.top = 0;
            mRectDst.right = viewWidth;
            mRectDst.bottom = viewHeight;
            
            /**
             *  Se delimitan los bordes de la vista.
             * (Enmarcación de las imagenes)
             */

            if (bitmapWidth * zoomX < viewWidth){
            	mRectSrc.left = 0;
        		mRectSrc.right = (int)(mRectSrc.left + viewWidth / zoomX);
            }else{
            	mRectSrc.left = (int)((panX * bitmapWidth) - (viewWidth / (zoomX * 2)));
                mRectSrc.right = (int)(mRectSrc.left + (viewWidth / zoomX));
                if (mRectSrc.left <= 0) {
            		mRectSrc.left = 0;
            		mRectSrc.right = (int)(mRectSrc.left + viewWidth / zoomX);
            	}else{
            		if (mRectSrc.right >= bitmapWidth) {
            			mRectSrc.right = bitmapWidth;
            			mRectSrc.left = (int)(mRectSrc.right - (viewWidth / zoomX));
            		}
            	}
            }
            
            if (bitmapHeight * zoomY < viewHeight){
            	mRectSrc.top = 0;
                mRectSrc.bottom = (int)(mRectSrc.top + viewHeight / zoomY);
            }else{
	            mRectSrc.top = (int)((panY * bitmapHeight) - (viewHeight / (zoomY * 2)));
	            mRectSrc.bottom = (int)(mRectSrc.top + (viewHeight / zoomY));

	            if (mRectSrc.top <= 0) {
	                mRectSrc.top = 0;
	                mRectSrc.bottom = (int)(mRectSrc.top + viewHeight / zoomY);
	            }else{
		            if (mRectSrc.bottom >= bitmapHeight) {
		                mRectSrc.bottom = bitmapHeight;
		                mRectSrc.top = (int)(mRectSrc.bottom - viewHeight / zoomY);
		            }
	            }
            }
            
            float panxTemp = (mRectSrc.left + (viewWidth / (zoomX * 2))) / bitmapWidth;
            mState.setPanX(panxTemp);
            float panyTemp = (mRectSrc.top + viewHeight / (zoomY * 2)) / bitmapHeight;
        	mState.setPanY(panyTemp);
            canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        calculateAspectQuotient();
    }

    public void update(Observable observable, Object data) {
        invalidate();
    }
}
