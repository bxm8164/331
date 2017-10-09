package com.osamufujimoto;

import java.util.List;

/**
 * Utilities used by program.
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
     * Print information message
     * @param s the mesage
     */
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

    /**
     * Print the string representation of terrain
     * @param terrain
     * @return
     */
    public static String terrainType(Terrain terrain) {

        return terrain.toString();

    }

    /**
     * Print the terrain type of a list of nodes
     * @param nodes the list of nodes
     */
    public static void printTerrainType(List<Node> nodes) {

        for (Node node : nodes) {

            PRINT(node.toString() + ": " + terrainType(node.t));

        }
    }

    /**
     * Get the terrain type given its color.
     * @param r the red channel
     * @param g the green channel
     * @param b the blue channel
     * @return the type of terrain.
     */
    public static Terrain getTerrainType(int r, int g, int b) {

        if (r == 248 && g == 148 && b == 18) {
            return Terrain.OPEN_LAND;
        }
        if (r == 255 && g == 192 && b == 0) {
            return Terrain.ROUGH_MEADOW;
        }
        if (r == 255 && g == 255 && b == 255) {
            return Terrain.EASY_MOVEMENT_FOREST;
        }
        if (r == 2 && g == 208 && b == 60) {
            return Terrain.SLOW_RUN_FOREST;
        }
        if (r == 2 && g == 136 && b == 40) {
            return Terrain.WALK_FOREST;
        }
        if (r == 5 && g == 73 && b == 24) {
            return Terrain.IMPASSIBLE_VEGETATION;
        }
        if (r == 0 && g == 0 && b == 255) {
            return Terrain.LAKE_SWAP_MARSH;
        }
        if (r == 71 && g == 51 && b == 3) {
            return Terrain.PAVED_ROAD;
        }
        if (r == 0 && g == 0 && b == 0) {
            return Terrain.FOOTPATH;
        }
        if (r == 205 && g == 0 && b == 101) {
            return Terrain.OUT_OF_BOUNDS;
        }

        return Terrain.UNKNOWN;
    }

    /**
     * Get the terrain cost
     * @param terrain the terrain
     * @return the expected cost.
     */
    public static double getTerrainCost(Terrain terrain) {

        if (Terrain.OPEN_LAND == terrain) {                 // orange
            return 1.025;
        }
        else if (Terrain.ROUGH_MEADOW == terrain) {         // light orange
            return 1.50;
        }
        else if (Terrain.EASY_MOVEMENT_FOREST == terrain) { // white
            return 1.10;
        }
        else if (Terrain.SLOW_RUN_FOREST == terrain) {      // light green
            return 1.20;
        }
        else if (Terrain.WALK_FOREST == terrain) {          // green
            return 1.15;
        }
        else if (Terrain.IMPASSIBLE_VEGETATION == terrain) {// dark green
            return 999;
        }
        else  if (Terrain.PAVED_ROAD == terrain) {          // brown
            return 1.0;
        }
        else if (Terrain.FOOTPATH == terrain) {             // black
            return 1.025;
        }
        // blue, dark pink
        else if (Terrain.LAKE_SWAP_MARSH == terrain || Terrain.OUT_OF_BOUNDS == terrain) {
            return Double.MAX_VALUE;
        } else {
            return Double.MAX_VALUE;

        }
    }


    /**
     * Get the direction of the successor of a node
     * @param current the current node
     * @param next the next node
     * @return the direction of the next node
     */
    public static Direction getNodeDirection(Node current, Node next) {

        if (current == null || next == null) {
            return null;
        }

        int dx = next.x - current.x;
        int dy = next.y - current.y;


        if (dy == 1) {
            return Direction.TOP;
        } else if (dy == -1) {
            return Direction.BOTTOM;
        } else if (dx == 1) {
            return Direction.RIGHT;
        } else if (dx == -1) {
            return Direction.LEFT;
        } else {
            return null;
        }


    }


}
