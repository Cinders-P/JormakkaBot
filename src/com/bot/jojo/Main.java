package com.bot.jojo;

public class Main {

    public static void main(String[] args) throws Exception {
        printInstructions();
        Spaghetti sp = Spaghetti.getInstance();

        sp.startTimer();
        if (!sp.startListener()) return;
        sp.startMonitor();
    }

    private static void printInstructions() {
        System.out.println();
        System.out.println(" ▄▄▄▄ ████▄ █▄▄▄▄ █▀▄▀█ ██   █  █▀ █  █▀ ██       ███   ████▄   ▄▄▄▄▀");
        System.out.println("▄▀  █ █   █ █  ▄▀ █ █ █ █ █  █▄█   █▄█   █ █      █  █  █   █ ▀▀▀█    ");
        System.out.println("    █ █   █ █▀▀█  █ ▀ █ █▄▄█ █▀▄   █▀▄   █▄▄█     █ ▀ ▄ █   █    █    ");
        System.out.println(" ▄ █  ▀████ █  █  █   █ █  █ █  █  █  █  █  █     █  ▄▀ ▀████   █     ");
        System.out.println("  ▀           █      █     █   █     █      █     ███          ▀      ");
        System.out.println("             ▀      ▀     █   ▀     ▀      █                           ");
        System.out.println("                         ▀                ▀                            ");
        System.out.println("Defaults:");
        System.out.println("HP Spell ON    (F12)  press ctrl + / to toggle.");
        System.out.println("HP Potion ON   (F11)  press ctrl + . to toggle.");
        System.out.println("Mana Potion ON (F10)  press ctrl + , to toggle.");
        System.out.println();
        System.out.println("Spell Delay: 700-800ms");
        System.out.println("Resets anytime you press a key between F1-F9.");
        System.out.println("Potion Delay: 700-800ms");
        System.out.println("Resets anytime you press a key between 1-4.");
        System.out.println();
        System.out.println("Note: must have hp/mana bars set below game screen.");
        System.out.println("Press ctrl + c to stop the application.");
        System.out.println("Terveisin, Nordoz-K.");
        System.out.println();
    }

}
