package com.bot.jojo;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListener implements NativeKeyListener {
    private Spaghetti sp = Spaghetti.getInstance();
    private boolean ctrlPressed;

    // https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {}

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        if (nke.getKeyCode() >= NativeKeyEvent.VC_1 && nke.getKeyCode() <= NativeKeyEvent.VC_4)
            sp.setLastPotionTime(System.currentTimeMillis());
        else if (nke.getKeyCode() >= NativeKeyEvent.VC_F1 && nke.getKeyCode() <= NativeKeyEvent.VC_F9)
            sp.setLastSpellTime(System.currentTimeMillis());
        else if (ctrlPressed && nke.getKeyCode() == NativeKeyEvent.VC_SLASH)
            sp.toggleHpSpell();
        else if (ctrlPressed && nke.getKeyCode() == NativeKeyEvent.VC_PERIOD)
            sp.toggleHpPotion();
        else if (ctrlPressed && nke.getKeyCode() == NativeKeyEvent.VC_COMMA)
            sp.toggleManaPotion();
        else if (nke.getKeyCode() == NativeKeyEvent.VC_CONTROL)
            ctrlPressed = true;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        if (nke.getKeyCode() == NativeKeyEvent.VC_CONTROL)
            ctrlPressed = false;
    }
}
