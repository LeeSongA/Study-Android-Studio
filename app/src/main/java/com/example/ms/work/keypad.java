package com.example.ms.work;

/**
 * Created by ms on 2018-01-02.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public class keypad extends Activity {

    private InputMethodManager inputMethodManager;
    private CustomNumpadView customNumpadView;
    private LinearLayout numpadLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_keypad);

        numpadLayout = findViewById(R.id.numpadLayout);
        customNumpadView = (CustomNumpadView) findViewById(R.id.numpadView);
        customNumpadView.setActionListenerActivity(keypad.this);
        numpadLayout.setVisibility(View.VISIBLE);
    }

    public void OnClose(View v) {
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        finish();
    }
}
