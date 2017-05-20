package com.grantsome.videoplayer.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Grantsome on 2017/4/27.
 * ͼƬԲ�λ�����
 */

public class CircleImage extends ImageView {

    private Paint mPaint = new Paint();

    public CircleImage(Context context) {
        super(context);
    }

    public CircleImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Bitmap scaleBitmap(Bitmap bitmap){
        float scale = (float) getWidth()/bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Drawable drawable = getDrawable();
        if(drawable!=null){
            Bitmap rawBitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap newBitmap = scaleBitmap(rawBitmap);
            Bitmap circleBitmap = getCircleBitmap(newBitmap);
            mPaint.reset();
            canvas.drawBitmap(circleBitmap,0,0,mPaint);
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap){
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(circleBitmap);
        mPaint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        int radius = bitmap.getWidth()/2;
        canvas.drawCircle(radius,radius,radius,mPaint);
        //SRC_IN ���²㶼��ʾ,�²������ʾ��
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,0,0,mPaint);
        return circleBitmap;
    }
}