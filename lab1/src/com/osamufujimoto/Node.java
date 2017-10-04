package com.osamufujimoto;

public class Node {

    /**
     *  The x coordinatae
     */
    public final int x;

    /**
     * The y coordinate
     */
    public final int y;

    /**
     * THe type of terrain
     */
    public final Terrain t;

    /**
     * The elevation
     */
    public final double e;

    /**
     * Node constructor
     * @param x the x coordinate
     * @param y the y coordinate
     * @param t the terrain type
     * @param e the elevation
     */
    public Node(int x, int y, Terrain t, double e ) {

        this.x = x;

        this.y = y;

        this.t = t;

        this.e = e;

    }
}
