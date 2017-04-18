package com.bot.jojo;

import com.sun.jna.platform.win32.WinDef.RECT;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Spaghetti {
    private static Spaghetti spaghetti;
    private WindowsOperations util = new WindowsOperations();
    private RECT windowRect;

    private int spellDelay; // milliseconds
    private int potionDelay;
    private int healthColumn;
    private int manaColumn;
    private int yEnd;
    private int yStart;
    private long lastSpellTime;
    private long lastPotionTime;

    private boolean useHpSpell = true;
    private boolean useHpPotion = true;
    private boolean useManaPotion = true;
    private boolean isZezeniaFocused;


    public static Spaghetti getInstance() {
        if (spaghetti == null)
            spaghetti = new Spaghetti();
        return spaghetti;
    }

    public void startMonitor() throws InterruptedException {
        while (true) {
            if (isZezeniaFocused && yEnd != 0) {

                if (isHealthLow()) {
                    if (useHpSpell && enoughSpellDelay()) {
                        util.pressKey(KeyEvent.VK_F12);
                        System.out.println("Casting HP Spell (F12)...");
                        lastSpellTime = System.currentTimeMillis();
                    }
                    if (useHpPotion && enoughPotionDelay()) {
                        util.pressKey(KeyEvent.VK_F11);
                        System.out.println("Using HP Potion (F11)...");
                        lastPotionTime = System.currentTimeMillis();
                    }
                }

                if (isManaLow()) {
                    if (useManaPotion && enoughPotionDelay()) {
                        util.pressKey(KeyEvent.VK_F10);
                        System.out.println("Using Mana Potion (F10)...");
                        lastPotionTime = System.currentTimeMillis();
                    }
                }

            }
            Thread.sleep(80);
        }
    }

    // https://github.com/kwhat/jnativehook
    public boolean startListener() {
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            return false;
        }

        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
        return true;
    }

    // http://stackoverflow.com/questions/11520819/java-create-background-thread-which-does-something-periodically
    public void startTimer() {
        Timer t = new Timer();
        Random rand = new Random();

        t.scheduleAtFixedRate( new TimerTask() {
              public void run() {
                  isZezeniaFocused = util.checkZezFocused();
                  spellDelay = rand.nextInt(100) + 700;
                  potionDelay = rand.nextInt(100) + 700;
                  if (isZezeniaFocused && (windowRect == null || !windowRect
                          .equals(util.getZezArea()))) {
                      updateRect();
                  }
              }
          },
          0,
          500); // run every second afterwards
    }

    public void toggleHpSpell() {
        System.out.print("F12: ");
        System.out
                .println((useHpSpell = !useHpSpell) ? "HP Spell ON" : "HP Spell OFF");
    }

    public void toggleHpPotion() {
        System.out.print("F11: ");
        System.out
                .println((useHpPotion = !useHpPotion) ? "HP Potion ON" : "HP Potion OFF");
    }

    public void toggleManaPotion() {
        System.out.print("F10: ");
        System.out
                .println((useManaPotion = !useManaPotion) ? "Mana Potion ON" : "Mana Potion OFF");
    }

    public void setLastSpellTime(long time) {
        lastSpellTime = time;
    }

    public void setLastPotionTime(long time) {
        lastPotionTime = time;
    }

    // PRIVATE METHODS =========================================================

    // http://stackoverflow.com/questions/351565/system-currenttimemillis-vs-system-nanotime
    private boolean enoughSpellDelay() {
        return System.currentTimeMillis() - lastSpellTime >= spellDelay;
    }

    private boolean enoughPotionDelay() {
        return System.currentTimeMillis() - lastPotionTime >= potionDelay;
    }

    private boolean isHealthLow() {
        for (int y = yStart; y <= yEnd; y++) {
            if (util.isRed(util.getColorOf(healthColumn, y)))
                    return false;
        }
        return true;
    }

    private boolean isManaLow() {
        for (int y = yStart; y <= yEnd; y++) {
            if (util.isBlue(util.getColorOf(manaColumn, y)))
                return false;
        }
        return true;
    }

    private int findHealthBar() {
        RECT rect = windowRect;
        int y = rect.bottom - 120;
        int half = (rect.right - rect.left) / 2;

        while (true) {
            for (int x = half/2; x < half; x += 150) {
                if (util.isRed(util.getColorOf(x, y))) {
                    return y;
                }
            }

            y = y - 10;
            if (y <= rect.top && yEnd == 0)
                throw new RuntimeException("Failure identifying health bar.");
        }
    }

    private void updateRect() {
        windowRect = util.getZezArea();
        yEnd = findHealthBar();
        yStart = yEnd - 30;
        int halfWidth = (windowRect.right - windowRect.left) / 2;
        healthColumn = windowRect.left + halfWidth - halfWidth / 8;
        manaColumn = windowRect.left + halfWidth + halfWidth / 3;
    }

}
