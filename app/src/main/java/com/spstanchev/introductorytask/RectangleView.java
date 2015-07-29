package com.spstanchev.introductorytask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;


/**
 * Created by Stefan on 7/29/2015.
 */
public class RectangleView extends View {

    // Class constants defining state of the thread
    private static final int DONE = 0;
    private static final int RUNNING = 1;

    // Following declared static so we can access from the anonymous inner class
    // running the animation loop.
    private static int mState;
    private Paint mPaintOne, mPaintTwo;
    private Random mRandomX1, mRandomY1, mRandomX2, mRandomY2;
    private float mX1 = 0.0f, mY1 = 0.0f, mX2 = 0.0f, mY2 = 300.0f;
    // Handlers to implement updates from the background thread to views
    // on the main UI
    private Handler handler1 = new Handler();
    private Handler handler2 = new Handler();

    private int viewWidth = 0, viewHeight = 0;

    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Set up the Paint objects that will control format of screen draws

        mPaintOne = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOne.setColor(Color.BLUE);
        mPaintTwo = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTwo.setColor(Color.RED);
        mRandomX1 = new Random();
        mRandomY1 = new Random();
        mRandomX2 = new Random();
        mRandomY2 = new Random();

        mState = RUNNING;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mState == RUNNING) {
                    //make calculations for x and y
                    if (viewWidth != 0 && viewHeight != 0){
                        mX1 = (float) mRandomX1.nextInt(viewWidth*3/4);
                        mY1 = (float) mRandomY1.nextInt(viewHeight-viewWidth/8);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler1.post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mState == RUNNING) {
                    //make calculations for x and y
                    if (viewWidth != 0 && viewHeight != 0){
                        mX2 = (float) mRandomX2.nextInt(viewWidth*3/4);
                        mY2 = (float) mRandomY2.nextInt(viewHeight-viewWidth/8);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler2.post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(mX1, mY1, mX1 + canvas.getWidth()/4, mY1 + canvas.getWidth()/8, mPaintOne);
        canvas.drawRect(mX2, mY2, mX2 + canvas.getWidth()/4, mY2 + canvas.getWidth()/8, mPaintTwo);
    }

    // Stop the thread loop
    public void stopLooper(){
        mState = DONE;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }
}
