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
    private ArrayList<Integer> list;
    private ArrayList<Integer> pressedKeys;

    private CustomNumpadView customNumpadView;
    private CustomOnKeyboardActionListener keyListener;

    private Keypad k;
    private Keyboard keyboard = null;
    private List<Keyboard.Key> keyList;

    public CustomNumpadView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        keyboard = new Keyboard(context, R.xml.qwerty);

        customNumpadView = this;
        list = new ArrayList<Integer> (10);
        pressedKeys = new ArrayList<Integer>();
        keyList =  keyboard.getKeys();

        for(int i=0; i<10; i++) {
            list.add(new Integer(i));                                // 키 랜덤 배치 하기 위해서 리스트에 0~9 추가
        }

        this.setRandomKeypad();                                      // 키 랜덤 배치
        this.setPreviewEnabled(false);                              // 미리보기 제거
    }

    public void setKeypad(Keypad k) {
        this.k = k;
    }

    public void setActionListenerActivity(Activity act) {
        keyListener = new CustomOnKeyboardActionListener(act);
        this.setOnKeyboardActionListener(keyListener);
        this.setKeyboard(keyboard);
    }

    public void setRandomKeypad() {
        Collections.shuffle(list);                                  // 리스트 섞기

        for(int i=0; i<9; i++) {                                    // 9개의 숫자를 랜덤 배치
            keyList.get(i).codes[0] = list.get(i)+7;
            keyList.get(i).label = list.get(i) + "";
        }
        keyList.get(10).codes[0] = list.get(9)+7;                  // 10번째 숫자 배치
        keyList.get(10).label = list.get(9) + "";
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
                k.onKey(primaryCode);                                            // 나머지 키 클릭하면 keypad의 onkey 함수 실행

                customNumpadView.setRandomKeypad();                // 키 클릭시 랜덤 배치 기능
            }
            if(primaryCode == 66)                                    // OK 키 클릭하면 keypad의 onEnter 함수 실행
                k.onEnter();
        }

        @Override
        public void onPress(int primaryCode) {                     // 키 클릭 시 여러개 키(총 4개의 키) 눌리는 기능
            int index = 10;
            for(int i=0; i<9; i++) {                                // 클릭된 키 위치 찾기
                if(keyList.get(i).codes[0] == primaryCode) {
                    index = i;
                    break;
                }
            }
            for(int i=0; i<9; i++) {                                // 클릭된 키 제외하고 리스트 생성
                if(i == index)
                    continue;
                pressedKeys.add(i);
            }
            if(index != 10)
                pressedKeys.add(10);

            Collections.shuffle(pressedKeys);

            for(int i=0, j; i<3; i++) {                             // 랜덤으로 3개의 키 추가적으로 눌림
                j = pressedKeys.get(i);
                keyList.get(j).onReleased(true);
            }
            customNumpadView.invalidateAllKeys();                 // 키 배치 갱신
        }

        @Override
        public void onRelease(int primaryCode) {                   // 클릭 되지 않은 상태로 돌리기
            for(int i=0,j; i<3; i++) {
                j = pressedKeys.get(i);
                keyList.get(j).onReleased(true);
            }
            for(int i=0; i<9; i++)                                  // 클릭된 키 제외하고 생성된 리스트를 삭제
                pressedKeys.remove(0);
            customNumpadView.invalidateAllKeys();
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
