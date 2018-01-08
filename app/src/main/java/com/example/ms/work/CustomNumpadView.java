package com.example.ms.work;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by user on 2018-01-03.
 */

public class CustomNumpadView extends KeyboardView {
    CustomOnKeyboardActionListener keyListener;
    Keyboard keyboard = null;
    private keypad k;

    public CustomNumpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        keyboard = new Keyboard(context, R.xml.qwerty);
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
