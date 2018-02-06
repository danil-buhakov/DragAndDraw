package com.book.dan.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        super(context);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    public BoxDrawingView(Context context, AttributeSet attributeset) {
        super(context,attributeset);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(),event.getY());
        String action = "";
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if(mCurrentBox!=null)
                    mCurrentBox.setCurrent(current);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }
        Log.i(TAG,action+" at x="+current.x+", y="+current.y);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for(Box box:mBoxen){
            float left = Math.min(box.getOrigin().x,box.getCurrent().x);
            float right = Math.max(box.getOrigin().x,box.getCurrent().x);
            float top = Math.min(box.getOrigin().y,box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y,box.getCurrent().y);
            canvas.drawRect(left,top,right,bottom,mBoxPaint);
        }
    }
}
