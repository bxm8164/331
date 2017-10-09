package com.osamufujimoto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.osamufujimoto.Util.*;

public class Main {

    /**
     * The image file
     */
    static final File image=  new File("terrain.png");

    /**
     * The elevation file
     */
    static final File elevations = new File("mpp.txt");

    /**
     * THe brown course
     */
    static final File brown = new File("brown.txt");

    /**
     * The red course
     */
    static final File red = new File("red.txt");

    /**
     * THe white course
     */
    static final File white = new File("white.txt");

    /**
     * THe output file
     */
    static File output = new File("output_all_.png");

    /**
     * Main program
     * @param args the arguments
     */
    public static void main(String[] args) {

        int[][] water = new int[500][395];

        Node[][] n = IO.read(image, elevations, water);

        Set<Node> bound = IO.findEdges(water, n);

        LOGD("Read elevations: OK");

        List<Node> nodes = IO.readInputFile(new File("brown.txt"), n);

        LOGD("Read image: OK");

        LOGI ("Number of checkpoints: " + nodes.size());

        List<Node> fullPath = new ArrayList<>();

        Search s;

        for (int i = 0; i < nodes.size() - 1 ; i++) {

            s = new Search(nodes.get(i), nodes.get(i + 1), n, bound);

            s.find();

            fullPath.addAll(s.all);

            s.all.clear();

        }

        LOGI("Number of nodes: " + fullPath.size());

        Printer.plotNodes(fullPath, new File("terrain.png"), output);

    }

    /**
     * Compute the node successors
     * @param node the node we want the successors
     * @param all all the nodes.
     */
    public static void successors(Node node, Node[][] all) {


        int y = node.y;
        int x = node.x;

                if (y == 0) {

                    node.addSuccessor(all[y + 1][x]);

                } else if (y == 499) {

                    node.addSuccessor(all[y - 1][x]);
                } else {

                    node.addSuccessor(all[y + 1][x]);
                    node.addSuccessor(all[y - 1][x]);
                }

                if (x == 0) {

                    node.addSuccessor(all[y][x + 1]);

                } else if (x == 394) {
                    node.addSuccessor(all[y][x - 1]);

                } else {
                    node.addSuccessor(all[y][x + 1]);
                    node.addSuccessor(all[y][x - 1]);
                }

    }

}
