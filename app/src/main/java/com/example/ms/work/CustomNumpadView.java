package com.example.ms.work;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 2018-01-03.
 */

public class CustomNumpadView extends KeyboardView {
    CustomOnKeyboardActionListener keyListener;
    Keyboard keyboard = null;
    List<Keyboard.Key> keyList;
    ArrayList<Integer> list;

    private keypad k;

    public CustomNumpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        keyboard = new Keyboard(context, R.xml.qwerty);

<<<<<<< HEAD
        keyList =  keyboard.getKeys();

        list = new ArrayList<Integer> (10);
=======
        List<Keyboard.Key> keyList =  keyboard.getKeys();

        ArrayList<Integer> list = new ArrayList<Integer> (10);
>>>>>>> 32f3a8337ed2e0920a5371ad3c888178ed578ec0
        for(int i=0; i<10; i++) {
            list.add(new Integer(i));
        }

        Collections.shuffle(list);

        for(int i=0; i<9; i++) {
            keyList.get(i).codes[0] = list.get(i)+7;
            keyList.get(i).label = list.get(i) + "";
        }
        keyList.get(10).codes[0] = list.get(9)+7;
        keyList.get(10).label = list.get(9) + "";
    }

    public void setKeypad(keypad k) {
        this.k = k;
    }

    public void setActionListenerActivity(Activity act) {
        keyListener = new CustomOnKeyboardActionListener(act);
        this.setOnKeyboardActionListener(keyListener);
        this.setKeyboard(keyboard);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    };

    private class CustomOnKeyboardActionListener implements OnKeyboardActionListener {
        Activity owner;

        public CustomOnKeyboardActionListener(Activity activity) {
            owner = activity;
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            long eventTime = System.currentTimeMillis();
            KeyEvent event = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, primaryCode, 0, 0, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE);
            owner.dispatchKeyEvent(event);
            if((primaryCode >= 7 && primaryCode <= 16) || primaryCode == 67) {
                k.onKey();
                Collections.shuffle(list);

                for(int i=0; i<9; i++) {
                    keyList.get(i).codes[0] = list.get(i)+7;
                    keyList.get(i).label = list.get(i) + "";
                }
                keyList.get(10).codes[0] = list.get(9)+7;
                keyList.get(10).label = list.get(9) + "";

                // 숫자 클릭 시 랜덤 배치 되도록,
                // 랜덤 배치 되지만, 클릭된 숫자만 바뀐 숫자로 보여지고 나머지는 그대로 보여지고 클릭하면 변경된 숫자로 입력됨.
                // 바뀐 배치 보여주기 필요.

                // 여러개 눌리는 것처럼 보이도록,
                // dispatchKeyEvent 사용?
            }

            if(primaryCode == 66)
                k.onEnter();
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeUp() {
        }
    }
}
