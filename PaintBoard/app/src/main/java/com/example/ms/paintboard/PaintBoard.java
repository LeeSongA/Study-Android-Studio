package com.example.ms.paintboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ms on 2018-01-16.
 */

public class PaintBoard extends View {
    Context mContext;
    Paint paint;

    Bitmap bitmap;
    Canvas canvas;

    float curX, curY;
    float oldX, oldY;

    public PaintBoard(Context context) {
        super(context);
        init(context);
    }

    public PaintBoard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        paint = new Paint();
        paint.setStrokeWidth(10f); // 선의 굵기
    }

    // 화면에서 사이즈가 정해지면 호출됨.
    @Override
    protected  void  onSizeChanged(int w, int h, int oldw, int oldh) {
        if(w >0 && h>0) {
            // Bitmap.createBitmap 일반적으로 Bitmap 객체를 생성 시 사용
            // Bitmap.Config.ARGB_888 일반적인 색상적용
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas();
            canvas.setBitmap(bitmap);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected  void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(bitmap != null) {
            canvas.drawBitmap(bitmap, 0,0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {      // 입력 이벤트 발생
        int action = motionEvent.getAction();
        curX = motionEvent.getX();
        curY = motionEvent.getY();

        if(action == MotionEvent.ACTION_DOWN) {

        } else if(action == MotionEvent.ACTION_MOVE) {
            canvas.drawLine(oldX, oldY, curX, curY, paint);
        } else if(action == MotionEvent.ACTION_UP) {

        }

        // 메모리에 있는 정보들을 뷰에 다시 그려 줌.
        // invalidate() 호출되면 그려지게 됨.
        invalidate();

        oldX = curX;
        oldY = curY;

        return true;
    }
}
