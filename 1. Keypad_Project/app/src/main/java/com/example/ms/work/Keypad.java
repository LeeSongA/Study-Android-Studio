package com.example.ms.work;

/**
 * Created by ms on 2018-01-02.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Keypad extends Activity {

    private CustomNumpadView customNumpadView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_keypad);

        customNumpadView = (CustomNumpadView) findViewById(R.id.numpadView);
        customNumpadView.setActionListenerActivity(Keypad.this);
        customNumpadView.setKeypad(this);
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

    /**
    public void OnClose(View v) {                                                           // 닫기 버튼 클릭 시 실행
        Intent intent = new Intent();
        intent.getType();
        setResult(RESULT_OK, intent);
        finish();
    }
     **/
}
