package com.example.ms.paintboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        PaintBoard paintBoard = new PaintBoard(MainActivity.this);

        linearLayout.addView(paintBoard);
        setContentView(R.layout.activity_main);
    }

    public void button1event(View v) {
        // 내용 삭제
    }

    public void button2event(View v) {
        // 내용 전달
    }
}
