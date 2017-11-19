package com.osamufujimoto;

import java.io.*;
import java.util.*;

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

    /**
     * The season (The sumemr season is the default value)
     */
    static Season _season = Season.SUMMER;

    /**
     * Main program
     * @param args the arguments
     */
    public static void main(String[] args) {

        Timer timer = new Timer().start();

        int[][] water = new int[500][395];

        final Node[][] n = IO.read(image, elevations, water);

        List<Set<Node>> bound = IO.findEdges(water, n);

        File in = parseArguments(args);

        LOGI("Working with " + in.getName() + " course in " + _season.toString() + " season.");

        LOGD("Read elevations: OK");

        // List<Node> nodes = IO.readInputFile(new File("brown.txt"), n);

        List<Node> nodes = IO.readInputFile(in, n);

        LOGD("Read image: OK");

        // LOGI ("Number of checkpoints: " + nodes.size());

        List<Node> fullPath = new ArrayList<>();

        List<List<Node>> fullPathList = new ArrayList<>();

        if (_season == Season.WINTER) {
            Set<Node> result = new LinkedHashSet<>();
            Set<Node> _borders = bound.get(1);

            for (int i = 0; i < 7; i++) {
                Set<Node> _result = expandInside(_borders, n);
                result.addAll(_result);
                _borders = _result;
            }

        }


        Node[][] __copy = new Node[500][395];
        for (int y = 0; y < 500; y++) {

            for (int x = 0; x < 395; x++) {

                __copy[y][x] = n[y][x];
            }
        }

        Set<Node> _borders = new LinkedHashSet<>();

        if (_season == Season.SPRING) {
            Set<Node> result = new LinkedHashSet<>();
            _borders = bound.get(1);

            for (int i = 0; i < 15; i++) {
                Set<Node> _result = expandOutside(_borders, __copy);
                result.addAll(_result);
                _borders = _result;
            }

        }


        // Printer.plotNodes(fullPath, new File("terrain.png"), output, _n);

        // System.exit(0);

        Search s;

        for (int i = 0; i < nodes.size() - 1 ; i++) {

            s = new Search(nodes.get(i), nodes.get(i + 1), n, bound.get(1) /* the edges */ );

            s.find();

            fullPath.addAll(s.all);

            Collections.reverse(s.all);
            fullPathList.add(s.all);

            // s.all.clear();

        }

        LOGI("Number of nodes: " + fullPath.size());


        if (_season == Season.SPRING)
            Printer.plotNodes(fullPath, new File("terrain.png"), output, _borders);
        else
            Printer.plotNodes(fullPath, new File("terrain.png"), output, null);

        Printer.printHumanReadableOutput(nodes, fullPathList);

        timer.stop().print();

    }

    /**
     * Any water within seven pixels of non-water is safe to walk-on
     * @param _borders the borders of the areas that contains water
     * @param n all the nodes
     * @return
     */
    public static Set<Node> expandInside(Set<Node> _borders, Node[][] n) {
        Set<Node> safe = new LinkedHashSet<>();
        for (Node node : _borders) {
            if (node.successors.isEmpty()) {
                Main.successors(node, n);
                if (node.successors.isEmpty()) continue;
            }
            List<Node> current = node.successors;
            Set<Node> level = makeSafe(n, current, null);
            safe.addAll(level);
        }
        return safe;
    }

    /**
     * Any pixels within fifteen pixels of water that can be reached from a water pixel without gaining more than
     * one meter of elevation (total) are now underwater
     * @param _borders the borders of the areas that contains water
     * @param n all the nodes
     * @return
     */
    public static Set<Node> expandOutside(Set<Node> _borders, Node[][] n) {
        Set<Node> safe = new LinkedHashSet<>();
        for (Node node : _borders) {
            if (node.successors.isEmpty()) {
                Main.successors(node, n);
                if (node.successors.isEmpty()) continue;
            }
            List<Node> current = node.successors;
            Set<Node> level = isNowUnderwater(n, current, node);
            safe.addAll(level);
        }
        return safe;
    }


    /**
     *
     * @param n
     * @param current
     * @param from
     * @return
     */
    public static Set<Node> makeSafe(Node[][] n, List<Node> current, Node from) {
        Set<Node> isSafeNow = new LinkedHashSet<>();
        for (Node _successor : current) {
            if (n[_successor.y][_successor.x].t == Terrain.LAKE_SWAP_MARSH) {
                n[_successor.y][_successor.x].t = Terrain.ICE;
                isSafeNow.add(n[_successor.y][_successor.x]);
            }
        }

        return isSafeNow;
    }


    /**
     *
     * @param n
     * @param current
     * @param from
     * @return
     */
    public static Set<Node> isNowUnderwater(Node[][] n, List<Node> current, Node from) {
        Set<Node> isReachable = new LinkedHashSet<>();
        for (Node _successor : current) {
            if (n[_successor.y][_successor.x].t != Terrain.LAKE_SWAP_MARSH ||
                    n[_successor.y][_successor.x].t != Terrain.OUT_OF_BOUNDS) {
                if ( _successor.e - from.e > 1) {
                    isReachable.add(n[_successor.y][_successor.x]);
                }
            }
        }
        return isReachable;
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

    /**
     * Parse the command-line arguments.
     * @param args the arguments
     * @return the file we're going to use as a input
     */
    public static File parseArguments(String[] args) {

        File file = null;
        for (String arg : args) {
            String[] split = arg.split(":");
            if (split[0].equals("o")) {
                output = new File(split[1]);
            }
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
