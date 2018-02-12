package com.example.ms.work;

/**
 * Created by ms on 2018-01-02.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Keypad extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width= displayMetrics.widthPixels * 3 / 5;
        int height = displayMetrics.heightPixels * 3 / 10;
        wmlp.y = displayMetrics.heightPixels * 1 / 20;

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        String BackColor = "#eeeeee";
        linearLayout.setBackgroundColor(Color.parseColor((BackColor)));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        // LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(50*dp));
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
        textView.setText("KeyPad");
        String BackgroundColor = "#00000000";
        textView.setBackgroundColor(Color.parseColor(BackgroundColor));
        textView.setGravity(Gravity.CENTER);
        String TextColor = "#888888";
        textView.setTextColor(Color.parseColor(TextColor));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        linearLayout.addView(textView, textViewParams);

        CustomNumpadView customNumpadView = new CustomNumpadView(this,null);
        customNumpadView.setActionListenerActivity(this);
        customNumpadView.setKeypad(this);
        LinearLayout.LayoutParams customNumpadViewParams = new LinearLayout.LayoutParams(width, height);
        linearLayout.addView(customNumpadView, customNumpadViewParams);

        Button button_Clear = new Button(this);
        LinearLayout.LayoutParams button_SaveParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);   // Util.covertDptoPx(getApplicationContext(), 60)
        button_Clear.setText("Clear");
        String BackgroundColor1 = "#00000000";
        textView.setBackgroundColor(Color.parseColor(BackgroundColor1));
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(Util.covertDptoPx(getApplicationContext(), 16), Util.covertDptoPx(getApplicationContext(), 16), Util.covertDptoPx(getApplicationContext(), 16), Util.covertDptoPx(getApplicationContext(), 16));
        String TextColor1 = "#888888";
        textView.setTextColor(Color.parseColor(TextColor1));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        // button_Clear.setLayoutParams(button_SaveParams);
        button_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("test.com.action.TEST");
                intent.putExtra("result", 100);
                sendBroadcast(intent);
            }
        });
        linearLayout.addView(button_Clear, button_SaveParams);

        setContentView(linearLayout);
    }

    public void onKey(int primaryCode) {                                                    // OK 키 외의 나머지 키 클릭 시 실행
        Intent intent = new Intent("test.com.action.TEST");
        intent.putExtra("result", primaryCode - 7);
        sendBroadcast(intent);    // 브로드케스트에 전달
    }

    public void onEnter() {                                                                 // OK 키 클릭 시 실행
        Intent intent = new Intent();                                                        // intent 로 화면 전환
        setResult(RESULT_OK, intent);
        finish();
    }
}
