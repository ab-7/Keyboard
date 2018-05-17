package com.example.adrija.keyboard;


import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.util.List;

public class CustomKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard symbolKeyboard;
    boolean isSymbol=false;

    private boolean isCaps = false;


    //Press Ctrl+O

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_layout, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        symbolKeyboard = new Keyboard(this, R.xml.int_char);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        isSymbol=false;
        return kv;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {

        InputConnection ic = getCurrentInputConnection();
        playClick(i);
        switch (i) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                if(isCaps)
            {
                Keyboard.Key shiftedKey = keyboard.getKeys().get(keyboard.getShiftKeyIndex());
                shiftedKey.icon=getResources().getDrawable(R.drawable.sym_keyboard_shift_2);
            }
            else
                {
                    Keyboard.Key shiftedKey = keyboard.getKeys().get(keyboard.getShiftKeyIndex());
                    shiftedKey.icon=getResources().getDrawable(R.drawable.sym_keyboard_shift);
                }
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                {
                    if(isSymbol==false)
                    {
                        kv.setKeyboard(symbolKeyboard);
                    }
                    else {
                        kv.setKeyboard(keyboard);
                    }
                    kv.setOnKeyboardActionListener(this);
                    isSymbol=!isSymbol;
                }
                break;
            default:
                char code = (char) i;
                if (Character.isLetter(code) && isCaps)
                code = Character.toUpperCase(code);
                ic.commitText(String.valueOf(code), 1);
        }

    }

    private void playClick(int i) {

        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (i) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

}

