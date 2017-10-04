package com.osamufujimoto;

public class Util {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    /**
     * Print debug message
     * @param s the message
     */
    public static void LOGD(String s) {
        System.out.println(ANSI_GREEN + s + ANSI_RESET);
    }


    /**
     * Print message
     * @param s the message
     */
    public static void PRINT(String s) {
        System.out.println(s);
    }

    /**
     * Print error message
     * @param s the message
     */
    public static void LOGE(String s) {
        System.out.println(ANSI_GREEN + s + ANSI_RESET);
    }



}
