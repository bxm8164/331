package com.osamufujimoto;

import java.util.ArrayList;
import java.util.List;

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

    }

    /**
     * Add a node to the list of successors
     * @param node the node to be added
     */
    public void addSuccessor(Node node) {

        sucessors.add(node);
    }

    /**
     * The string representation of the node
     * @return the string representation.
     */
    public String toString() {

        return String.format("Node: (%d, %d)", x, y);
    }
}
