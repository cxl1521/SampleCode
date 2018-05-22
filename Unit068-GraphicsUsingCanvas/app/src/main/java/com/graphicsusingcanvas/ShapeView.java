package com.graphicsusingcanvas;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class ShapeView extends View {

    private Context mContext;
    private ShapeDrawable mShapeDraw;
    private Paint mPaint;

    public ShapeView(Context context) {
        super(context);

        mContext = context;

        mShapeDraw = new ShapeDrawable(new OvalShape());
        mShapeDraw.getPaint().setColor(0xffffff00);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.CYAN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mShapeDraw.setBounds(10, 10,
                canvas.getWidth()/2 - 10, canvas.getHeight()/2 - 20);
        mShapeDraw.draw(canvas);

        canvas.drawOval(new RectF(canvas.getWidth()/2 + 10, 10,
                canvas.getWidth() - 10, canvas.getHeight()/2 - 20), mPaint);
        canvas.drawText(this.getContext().getString(R.string.app_name),
                10, canvas.getHeight()/2, mPaint);
        canvas.drawLine(canvas.getWidth()/2 + 10, canvas.getHeight()/2 -10 ,
                canvas.getWidth() - 10, canvas.getHeight()/2, mPaint);

        Drawable drawImg = ContextCompat.getDrawable(
                mContext, R.drawable.img01);
        drawImg.setBounds(10, canvas.getHeight()/2 + 10,
                canvas.getWidth()/2 - 10, canvas.getHeight()*3/4);
        drawImg.draw(canvas);

        RotateDrawable drawRotate =
                (RotateDrawable) ContextCompat.getDrawable(
                        mContext,R.drawable.rotate_drawable);
        drawRotate.setLevel(1000);
        drawRotate.setBounds(canvas.getWidth()/2 + 30, canvas.getHeight()/2,
                canvas.getWidth() + 10, canvas.getHeight()*3/4);
        drawRotate.draw(canvas);
    }
}
