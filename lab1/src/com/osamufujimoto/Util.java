package com.osamufujimoto;

/**
 * @author Osamu Fujimoto
 */
public class Util {

    /**
     * Colors for the terminal
     */
    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    /**
     * Disables the logging statements.
     */
    private static boolean NOP = false;


    /**
     * Print debug message
     * @param s the message
     */
    public static void LOGD(String s) {
        if (!NOP) {
            System.out.println(GREEN + s + RESET);
        }
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
        if (!NOP) {
            System.out.println(RED + s + RESET);
        }
    }

    /**
     * Calculate the distance between two points
     * @param x the first x coordinate
     * @param y the first y coordinate
     * @param _x the second x coordinate
     * @param _y the second y coordinate
     * @return the distance between (x, y) and (_x, _y)
     */
    public static double distance(int x, int y, int _x, int _y) {

        return Math.sqrt(Math.pow(_x - x, 2) + Math.pow(_y - y, 2));
    }


}
