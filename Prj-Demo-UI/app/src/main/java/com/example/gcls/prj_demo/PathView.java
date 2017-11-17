package com.example.gcls.prj_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blue on 2016/12/25.
 */

public class PathView extends View {
    private Path path;
    private Path pathCar;
    private Paint paint;
    private Paint paintCar;
    private int COUNT = 0;
    private List<Integer> args = new ArrayList<Integer>();
    private List<Integer> distances = new ArrayList<Integer>();
    private List<Integer> xs = new ArrayList<Integer>();
    private List<Integer> ys = new ArrayList<Integer>();
    private int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    private int arg = 0, a = 0, b = 0, l = 0;
    private int delay = 0;

    public PathView(Context context, AttributeSet attrs){
        super(context, attrs);
//		this.setFocusable(true);
        path = new Path();
        pathCar = new Path();
        paint = new Paint();
        paintCar = new Paint();
        paint.setColor(Color.BLUE);
        paintCar.setColor(Color.RED);
        paint.setAntiAlias(true);
        paintCar.setAntiAlias(true);
        paint.setStrokeWidth(12);
        paintCar.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paintCar.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paintCar.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        canvas.drawPath(pathCar, paintCar);
    }


    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent e){
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
                args.clear();
                distances.clear();
                xs.clear();
                ys.clear();
                args.add(0);
                distances.add(0);
                path.reset();
                pathCar.reset();
                x1 = (int) e.getX();
                y1 = (int) e.getY();
                path.moveTo(x1, y1);
                pathCar.moveTo(x1, y1);
                xs.add(x1);
                ys.add(y1);
                COUNT = delay = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = (int) e.getX();
                y2 = (int) e.getY();
                path.lineTo(x2, y2);
                a = x1 - x2;
                b = y1 - y2;
                l = (int) (Math.sqrt(a * a + b * b));
                arg = (int) (Math.acos(((double) b) / ((double) l)) / Math.PI * 180);
                if(a < 0) arg = -arg;
                args.add(arg);
                distances.add(l);
                xs.add(x2);
                ys.add(y2);
                Log.d("path", "arg: " + arg + " l: " + l);
                ++COUNT;
                x1 = x2;
                y1 = y2;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                new Thread(new Runnable(){
                    public void run(){
                        for(int i = 1; i <= COUNT; ++i){
                            pathCar.lineTo(xs.get(i), ys.get(i));
                            //pathCar.lineTo(i*100, i*100);
                            Log.d("actual", "x: " + xs.get(i) + " y: " + ys.get(i));
                            arg = args.get(i) - args.get(i-1);
                            if(arg > 180) arg = arg - 360;
                            else if(arg < -180) arg = arg + 360;

                            // left
                            if(arg > 0) {
                                MainActivity.blueTooth.sendInformation("2");
                            }
                            // right
                            else if(arg < 0) {
                                MainActivity.blueTooth.sendInformation("3");
                            }
                            delay = Math.abs(arg) * 6;
                            try{
                                Thread.sleep(delay);
                            } catch(InterruptedException e){
                                e.printStackTrace();
                            }
                            l = distances.get(i) * 5;
                            delay = l;
                            // forward
                            MainActivity.blueTooth.sendInformation("1");
                            try{
                                Thread.sleep(delay);
                            } catch(InterruptedException e){
                                e.printStackTrace();
                            }
                            Log.d("actual", "arg: " + arg + " l: " + l);
                        }
                        // stop
                        MainActivity.blueTooth.sendInformation("5");
                    }
                }).start();
                break;
        }
        return true;
    }
}
