package com.example.ms.work;

/**
 * Created by ms on 2018-01-02.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class keypad extends Activity {

    private EditText editText;
    private CustomNumpadView customNumpadView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_keypad);

        editText = findViewById(R.id.editText);
        editText.setInputType(0);

        customNumpadView = (CustomNumpadView) findViewById(R.id.numpadView);
        customNumpadView.setActionListenerActivity(keypad.this);
        customNumpadView.setKeypad(this);
    }

    public void onKey() {                                                                   // OK 키 외의 나머지 키 클릭 시 실행
        Intent intent = new Intent();
        intent.setAction("test.com.action.TEST");
        sendBroadcast(intent.putExtra("result", editText.getText().toString()));      // editText 내용을 브로드케스트에 전달
    }

    public void onEnter() {                                                                 // OK 키 클릭 시 실행
        Intent intent = new Intent();                                                        // intent 로 화면 전환
        intent.putExtra("result", editText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void OnClose(View v) {                                                           // 닫기 버튼 클릭 시 실행
        Intent intent = new Intent();
        intent.getType();
        setResult(RESULT_OK, intent);
        finish();
    }
}
