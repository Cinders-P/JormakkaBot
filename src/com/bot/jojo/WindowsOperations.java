package com.bot.jojo;

import com.sun.istack.internal.Nullable;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import java.awt.Robot;
import java.awt.Color;
import java.awt.AWTException;
import java.awt.event.KeyEvent;

public class WindowsOperations {
    private final int MAX_TITLE_LENGTH;
    private final int REQ_COLOR_VALUE; // out of 255

    public WindowsOperations() {
        MAX_TITLE_LENGTH = 1024;
        REQ_COLOR_VALUE = 100;
    }

    public boolean isRed(Color c) {
        // makes sure its not dark grey which will also have red > REQ_COLOR_VALUE
        return c.getRed() > REQ_COLOR_VALUE && c.getBlue() < 20 && c.getGreen() < 20;
    }

    public boolean isBlue(Color c) {
        return c.getBlue() > REQ_COLOR_VALUE && c.getRed() < 20 && c.getGreen() < 20;
    }

    // http://stackoverflow.com/questions/6391439/getting-active-window-information-in-java
    public boolean checkZezFocused() {
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowText(hwnd, buffer, MAX_TITLE_LENGTH);
        // ex. "Zezenia Online (name) [FPS: 57]"
        return Native.toString(buffer).matches("Zezenia Online.*?FPS.*");
    }

    public WinDef.RECT getZezArea() {
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        WinDef.RECT rect = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect(hwnd, rect);
        // ex. [(-8,-8)(1928,1058)]
        return rect;
    }

    // https://kodejava.org/how-do-i-get-the-colour-of-a-screen-pixel/
    @Nullable
    public Color getColorOf(int x, int y) {
        try {
            return new Robot().getPixelColor(x, y);
        } catch (AWTException e) {
            e.printStackTrace();
            throw new RuntimeException("Failure initiating AWT robot :(", e);
        }
    }

    // http://stackoverflow.com/questions/7745959/how-to-simulate-keyboard-presses-in-java
    public void pressKey(int ke) {
        try {
            Robot bot = new Robot();
            bot.keyPress(ke);
            bot.keyRelease(ke);
        } catch (AWTException e) {
            System.out.println("Exception encountered while simulating " +
                               KeyEvent.getKeyText(ke));
            e.printStackTrace();
        }
    }
}
