package com.example.ms.signpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by ms on 2018-01-18.
 */

public class SignPad extends Activity {

    private CustomSignPad customSignPad;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 없애기
        setContentView(R.layout.activity_signpad);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        customSignPad = new CustomSignPad(SignPad.this);
        linearLayout.addView(customSignPad);

    }

    public void btn_OK_event(View v) {          // Screen 에 있는 서명 html canvas 로 전달
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void btn_Clear_event(View v) {
        // 지우기
    }


    public void btn_Cancel_event(View v) {      // Screen 화면 닫기
        Intent intent = new Intent();
        intent.getType();
        setResult(RESULT_OK, intent);
        finish();
    }

}

