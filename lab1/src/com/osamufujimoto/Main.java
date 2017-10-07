package com.osamufujimoto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

        Node[][] n = read(image, elevations);

        LOGD("Read elevations: OK");

        List<Node> nodes = _readInputFile(new File("brown.txt"), n);

        LOGD("Read image: OK");

        LOGI ("Number of checkpoints: " + nodes.size());

        List<Node> fullPath;

        Search search = new Search(n);


        for (int i = 0; i < nodes.size() - 1 && false; i++) {

            Search s = new Search(nodes.get(i), nodes.get(i + 1), n);

            // search.setStart(nodes.get(i));

            // search.setGoal(nodes.get(i + 1));

            s.find();

            PRINT("Working with " +  nodes.get(i).toString() + " and " + nodes.get(i+1).toString());

            fullPath.addAll(s.all);

            s.all.clear();

            if (i == 4) break;
        }

        printTerrainType(nodes);

        _plotCoursePoints(nodes, new File("terrain.png"));

        //_plotCoursePoints(nodes, new File("terrain.png"), new File("terrain_white_locations"));
    }

    public static List<Node> _readInputFile(File _input, Node[][] nodes) {

        List<Node> all = new ArrayList<>();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(_input));

            String line;


            while( (line = reader.readLine()) != null) {

                String[] s = line.split(" ");

                int x = Integer.parseInt(s[0]);

                int y = Integer.parseInt(s[1]);

                Node node = nodes[y][x];

                all.add(node);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return all;

    }


    public static void _plotCoursePoints(List<Node> nodes, File _image, File _output) {

        File _save = output; /* save the original file to a temp variable */

        output = _output;

        _plotCoursePoints(nodes, _image);

        output = _save; /* restore the file */

    }
    public static void _plotCoursePoints(List<Node> nodes, File _image) {
        try {

            // System.out.println("Here...!");

            BufferedImage image = ImageIO.read(_image);


            PRINT("Number of nodes: " + nodes.size());
            for (Node node : nodes) {


                int color = new Color(255, 0 , 0).getRGB();

                //
                // image.setRGB(x, y,  node.c.getRGB()); // red

                image.setRGB(node.x, node.y, color);
                //
                // Make the marker bigger
                //



            }

            ImageIO.write(image, "png", output);

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    /**
     * Plot the points from a file to an image
     * @param _course the file containing the points
     * @param _image the image
     */
    public static void _plotCoursePoints(File _course, File _image) {

        try {

            BufferedImage image = ImageIO.read(_image);

            BufferedReader reader = new BufferedReader(new FileReader(_course));

            String line;

            while ( (line = reader.readLine()) != null ) {

                String[] points = line.split(" ");

                int x = Integer.parseInt(points[0]);

                int y = Integer.parseInt(points[1]);

                int color = new Color(255, 0 , 0).getRGB();

                image.setRGB(x, y, new Color(255, 0 , 0).getRGB()); // red

                //
                // Make the marker bigger
                //

                image.setRGB(x - 1, y, color);
                image.setRGB(x + 1, y, color);

                image.setRGB(x - 1, y + 1, color);
                image.setRGB(    x   ,y + 1, color);
                image.setRGB(x + 1, y + 1, color);

                image.setRGB(x - 1, y - 1, color);
                image.setRGB(    x   ,y - 1, color);
                image.setRGB(x + 1, y - 1, color);


            }

            ImageIO.write(image, "png", output);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

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



    /**
     * Print the successors of a node
     * @param node the node we want the successors
     */
    private static void _printSuccessors(Node node) {

        for (Node successors : node.sucessors) {

            PRINT(successors.toString());

        }
    }



    /**
     * Read the each pixel value and create a Node containing its position, terrain type and elevation
     * @param _image the image file
     * @param _elevation the text file
     * @return
     */
    public static Node[][] read(File _image, File _elevation) {

        // final double[][] elevation = _readFile(_elevation);

        Node[][] node = new Node[500][395];

        // ArrayList<ArrayList<Node>> nds = new ArrayList<>();

        try {

            BufferedImage image = ImageIO.read(_image);

            for (int y = 0; y < 500; y++) {

                // ArrayList<Node> nodesX = new ArrayList<>();

                for (int x = 0; x < 395; x++) {

                    Color color = new Color(image.getRGB(x, y));

                    Terrain t = getTerrainType(color.getRed(), color.getGreen(), color.getBlue());

                    node[y][x] = new Node(x, y, t, 0.0, color);

                    // nodesX.add(new Node(x, y, t, 0.0));

                }

                // nds.add(nodesX);

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return node;
    }

    /** Get the terran type **/
    private static Terrain getTerrainType(int r, int g, int b) {

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


    /** Implement later **/
    private static double[][] _readFile(File _text) {

        double[][] result = new double[395][500];

        /*
        try {

            BufferedReader reader = new BufferedReader(new FileReader(_text));

            String current;

            //
            // Read the 500 lines
            //
            for (int i = 0; i < 500; i++) {

                current = reader.readLine();

                String[] sp = current.split("   ");

                //
                // The first (numeric) value of sp is at index 1. Index 0 is empty.
                //
                for (int j = 1; j < 396; j++) {

                    result[i][j - 1] = Double.parseDouble(sp[j]);
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }
        */
        return result;
    }



}
