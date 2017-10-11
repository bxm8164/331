package com.osamufujimoto;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.osamufujimoto.Util.LOGD;

/**
 * A node represent a single pixel of an image
 * @author Osamu Fujimoto
 */
public class Node extends Object {

    /**
     *  The x coordinate
     */
    public final int x;

    /**
     * The y coordinate
     */
    public final int y;

    /**
     * THe type of terrain
     */
    public Terrain t;

    /**
     * The elevation
     */
    public final double e;

    /**
     * The color of this node
     */
    public Color c;

    /**
     * The successors of this node
     */
    public List<Node> successors = new ArrayList<>();

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
     * Node constructor
     * @param x the x coordinate
     * @param y the y coordinate
     * @param t the terrain type
     * @param e the elevation at this point
     * @param c the color of the node
     */
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
            successors.add(node);
        } else {
            LOGD("Node skipped (Reason: out of bounds)");
        }
    }

    /**
     * The string representation of the node
     * @return the string representation.
     */
    public String toString() {

        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        Node other = (Node) obj;

        if (other.x == this.x && other.y == this.y) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }


}
