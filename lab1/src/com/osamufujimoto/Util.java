package com.osamufujimoto;

import java.util.List;

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

    public static void LOGI(String s) {
        if (!NOP) {
            System.out.println(YELLOW + s + RESET);
        }
    }

    /**
     * Calculate the euclidean distance between two points
     * @param x the first x coordinate
     * @param y the first y coordinate
     * @param _x the second x coordinate
     * @param _y the second y coordinate
     * @return the distance between (x, y) and (_x, _y)
     */
    public static double distance(int x, int y, int _x, int _y) {

        return Math.sqrt(Math.pow(_x - x, 2) + Math.pow(_y - y, 2));
    }

    /**
     * Calculate the euclidean distance between two nodes
     * @param o1 the first node
     * @param o2 the second node
     * @return the distance between the first and the second node.
     */
    public static double distance(Node o1, Node o2) {

        return distance(o1.x, o1.y, o2.x, o2.y);
    }

    /**
     * Calculate the manhattan distance between two points
     * @param x the first x coordinate
     * @param y the first y coordinate
     * @param _x the second x coordinate
     * @param _y the second y coordinate
     * @return the distance between (x, y) and (_x, _y)
     */
    public static double manhattanDistance(int x, int y, int _x, int _y) {

        double dx = Math.abs(x - _x);

        double dy = Math.abs(y - _y);

        return 1.0 * (dx + dy);

    }

    /**
     * Calculate the manhattan distance between two nodes
     * @param o1 the first node
     * @param o2 the second node
     * @return the distance between the first and the second node.
     */
    public static double manhattanDistance(Node o1, Node o2) {

        return manhattanDistance(o1.x, o1.y, o2.x, o2.y);
    }

    public static String terrainType(Terrain terrain) {

        return terrain.toString();

    }

    public static void printTerrainType(List<Node> nodes) {

        for (Node node : nodes) {

//            Test.testBrownPath(node);

            PRINT(node.toString() + ": " + terrainType(node.t));

        }
    }


}
