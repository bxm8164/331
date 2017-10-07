package com.osamufujimoto;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.osamufujimoto.Util.LOGD;

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
     * The cost of this node
     */
    public final double cost;

    public Color c;

    public List<Node> sucessors = new ArrayList<>();

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

        this.cost = e; /* to be changed later */


    }

    public Node(int x, int y, Terrain t, double e, Color c) {

        this(x, y, t, e);

        this.c = c;


    }

    /**
     * Add a node to the list of successors
     * @param node the node to be added
     */
    public void addSuccessor(Node node) {

        if (node.t != Terrain.OUT_OF_BOUNDS || node.t != Terrain.LAKE_SWAP_MARSH) {
            sucessors.add(node);
        } else {
            LOGD("Node skipped (Reason: out of bounds)");
        }
    }

    /**
     * The string representation of the node
     * @return the string representation.
     */
    public String toString() {

        return String.format("Node: (%d, %d)", x, y);
    }
}
