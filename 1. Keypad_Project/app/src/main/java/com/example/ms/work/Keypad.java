package com.example.ms.work;

/**
 * Created by ms on 2018-01-02.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Keypad extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Log.d("DEBUG", dp+"");

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        String BackColor = "#eeeeee";
        linearLayout.setBackgroundColor(Color.parseColor((BackColor)));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        // LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(50*dp));
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.covertDptoPx(getApplicationContext(), 50));
        textView.setLayoutParams(textViewParams);
        textView.setText("KeyPad");
        String BackgroundColor = "#00000000";
        textView.setBackgroundColor(Color.parseColor(BackgroundColor));
        textView.setGravity(Gravity.CENTER);
        String TextColor = "#888888";
        textView.setTextColor(Color.parseColor(TextColor));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        linearLayout.addView(textView);

        CustomNumpadView customNumpadView = new CustomNumpadView(this,null);
        customNumpadView.setActionListenerActivity(this);
        customNumpadView.setKeypad(this);
        LinearLayout.LayoutParams customNumpadViewParams = new LinearLayout.LayoutParams(Util.covertDptoPx(getApplicationContext(), 250), Util.covertDptoPx(getApplicationContext(), 100));
        customNumpadView.setLayoutParams(customNumpadViewParams);
        linearLayout.addView(customNumpadView);

        Button button_Clear = new Button(this);
        LinearLayout.LayoutParams button_SaveParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button_Clear.setText("Clear");
        // button_Clear.setLayoutParams(button_SaveParams);
        button_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("result", 0);
                finish();
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
