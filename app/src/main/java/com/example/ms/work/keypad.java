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

    private CustomNumpadView customNumpadView;
    private EditText editText;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_keypad);

        customNumpadView = (CustomNumpadView) findViewById(R.id.numpadView);
        customNumpadView.setActionListenerActivity(keypad.this);
        customNumpadView.setKeypad(this);

        editText = findViewById(R.id.editText);
    }

    public void onEnter() {
        Intent intent = new Intent();
        intent.putExtra("result", editText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void OnClose(View v) {
        Intent intent = new Intent();
        intent.getType();
        setResult(RESULT_OK, intent);
        finish();
    }
}
