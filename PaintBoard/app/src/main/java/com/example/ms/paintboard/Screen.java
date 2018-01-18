package com.example.ms.paintboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by ms on 2018-01-16.
 */

public class Screen extends Activity {

    private PaintBoard paintBoard;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_screen);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        paintBoard = new PaintBoard(Screen.this);
        linearLayout.addView(paintBoard);

    }

    public void btn_OK_event(View v) {
    }

    public void btn_Clear_event(View v) {

    }

    public void btn_Cancel_event(View v) {
        Intent intent = new Intent();
        intent.getType();
        setResult(RESULT_OK, intent);
        finish();
    }

}
