package com.book.dan.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private static final String POINTS_ARRAY_LIST_STRING = "PointsArrayListString";

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
        super(context, attributeset);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getAction()) {
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
                if (mCurrentBox != null)
                    mCurrentBox.setCurrent(current);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }
        Log.i(TAG, action + " at x=" + current.x + ", y=" + current.y);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("savedState",super.onSaveInstanceState());
        StringBuilder boxPointers = new StringBuilder();
        ArrayList<String> pointers = new ArrayList<>();
        for(Box box:mBoxen){
            boxPointers.append(box.getOrigin().x+";"+box.getOrigin().y+"`");
            boxPointers.append(box.getCurrent().x+";"+box.getCurrent().y);
            pointers.add(boxPointers.toString());
            boxPointers = new StringBuilder();
        }
        bundle.putStringArrayList(POINTS_ARRAY_LIST_STRING,pointers);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            List<String> pointers = ((Bundle) state).getStringArrayList(POINTS_ARRAY_LIST_STRING);
            if(pointers!=null) {
                for(String s:pointers){
                    putBoxFromString(s);
                }
            }
            state = ((Bundle) state).getParcelable("savedState");
        }
        super.onRestoreInstanceState(state);
    }

    private void putBoxFromString(String boxString){
        String[] strings = boxString.split("`");
        String origin = strings[0];
        String current = strings[1];
        float origin_x = Float.parseFloat(origin.split(";")[0]);
        float origin_y = Float.parseFloat(origin.split(";")[1]);
        float current_x = Float.parseFloat(current.split(";")[0]);
        float current_y = Float.parseFloat(current.split(";")[1]);
        Box box = new Box(new PointF(origin_x,origin_y));
        box.setCurrent(new PointF(current_x,current_y));
        mBoxen.add(box);
    }
}
