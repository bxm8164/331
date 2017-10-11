package com.osamufujimoto;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.osamufujimoto.Util.*;

/**
 * The main program
 * @author Osamu Fujimoto
 */
public class Main {

    /**
     * The image file
     */
    static final File image
            =  new File("terrain.png");

    /**
     * The elevation file
     */
    static final File elevations
            = new File("mpp.txt");

    /**
     * THe output file
     */
    static File output
            = new File("output_all_.png");

    static Season _season = Season.SUMMER; // default
    /**
     * Main program
     * @param args the arguments
     */
    public static void main(String[] args) {

        Timer timer = new Timer().start();

        int[][] water = new int[500][395];

        Node[][] n = IO.read(image, elevations, water);

        Set<Node> bound = IO.findEdges(water, n);

        File in = parseArguments(args);

        LOGI("Working with " + in.getName() + " course in " + _season.toString() + " season.");

        LOGD("Read elevations: OK");

        // List<Node> nodes = IO.readInputFile(new File("brown.txt"), n);

        List<Node> nodes = IO.readInputFile(in, n);

        LOGD("Read image: OK");

        // LOGI ("Number of checkpoints: " + nodes.size());

        List<Node> fullPath = new ArrayList<>();

        List<List<Node>> fullPathList = new ArrayList<>();


        Search s;

        for (int i = 0; i < nodes.size() - 1 ; i++) {

            s = new Search(nodes.get(i), nodes.get(i + 1), n, bound);

            s.find();

            fullPath.addAll(s.all);

            Collections.reverse(s.all);
            fullPathList.add(s.all);

            // s.all.clear();

        }

        LOGI("Number of nodes: " + fullPath.size());

        Printer.plotNodes(fullPath, new File("terrain.png"), output);

        Printer.printHumanReadableOutput(nodes, fullPathList);

        timer.stop().print();

    }

    /**
     * Compute the node successors
     * @param node the node we want the successors
     * @param all all the nodes.
     */
    public static void successors(Node node, Node[][] all) {


        int y = node.y;
        int x = node.x;

        try {

            node.addSuccessor(all[y - 1][x]);
            node.addSuccessor(all[y + 1][x]);
            node.addSuccessor(all[y][x + 1]);
            node.addSuccessor(all[y][x - 1]);
            node.addSuccessor(all[y + 1][x - 1]);
            node.addSuccessor(all[y + 1][x + 1]);
            node.addSuccessor(all[y - 1][x - 1]);
            node.addSuccessor(all[y - 1][x + 1]);


        } catch (Exception ex) {
            // LOGD("Node is out of bounds. SKipping");
        }


    }

    public static File parseArguments(String[] args) {

        File file = null;
        for (String arg : args) {
            String[] split = arg.split(":");
            if (split[0].equals("c")) {
                if (split[1].equals("white") || split[1].equals("brown") || split[1].equals("red")
                        || split[1].equals("test")) {
                    String course = split[1];
                    if (course.equals("brown")) {
                        file = new File("brown.txt");
                    }
                    if (course.equals("red")) {
                        file = new File("red.txt");
                    }
                    if (course.equals("white")) {
                        file = new File("white.txt");
                    }
                    if (course.equals("test")) {
                        file = new File("one.txt");
                    }
                } else {
                    LOGI("Course not valid. Exiting");
                    System.exit(-1);
                }
            }
            if (split[0].equals("s")) {
                if (split[1].equals("winter") || split[1].equals("fall") || split[1].equals("spring") ||
                        split[1].equals("summer")) {
                    String season = split[1];
                    if (season.equals("winter")) {
                        _season = Season.WINTER;
                    }
                    if (season.equals("fall")) {
                        _season = Season.FALL;
                    }
                    if (season.equals("spring")) {
                        _season = Season.SPRING;
                    }
                    if (season.equals("summer")) {
                        _season = Season.SUMMER;
                    }
                } else {
                    LOGI("Season not valid. Exiting");
                    System.exit(-1);
                }
            }

        }
        if (file == null) {
            LOGI("No course elected. Exiting");
            System.exit(-1);
        } else {
            return file;
        }
        return null;
    }

}
