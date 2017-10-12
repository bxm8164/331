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

    static Season _season = Season.SUMMER; // default
    /**
     * Main program
     * @param args the arguments
     */
    public static void main(String[] args) {

        Timer timer = new Timer().start();

        int[][] water = new int[500][395];

        Node[][] n = IO.read(image, elevations, water);

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

        if (_season == Season.SPRING) {
            Set<Node> result = new LinkedHashSet<>();
            Set<Node> _borders = bound.get(1);

            for (int i = 0; i < 7; i++) {
                Set<Node> _result = expandOutside(_borders, n);
                result.addAll(_result);
                _borders = _result;
            }

            for (Node _result : result) {
                n[_result.y][_result.x].t = Terrain.LAKE_SWAP_MARSH;
            }
        }


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


        Printer.plotNodes(fullPath, new File("terrain.png"), output, n);

        Printer.printHumanReadableOutput(nodes, fullPathList);

        timer.stop().print();

    }

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

    Set <Node> mud = new LinkedHashSet<>();

    public static Set<Node> isNowUnderwater(Node[][] n, List<Node> current, Node from) {
        Set<Node> isReachable = new LinkedHashSet<>();
        for (Node _successor : current) {
            if (n[_successor.y][_successor.x].t != Terrain.LAKE_SWAP_MARSH) {
                if (Math.abs(_successor.e - from.e) > 1.0) {
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
