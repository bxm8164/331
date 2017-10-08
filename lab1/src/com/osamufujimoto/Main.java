package com.osamufujimoto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

        List<Node> nodes = IO.readInputFile(new File("red.txt"), n);

        LOGD("Read image: OK");

        LOGI ("Number of checkpoints: " + nodes.size());

        List<Node> fullPath = new ArrayList<>();

        // Search search = new Search(n);


        for (int i = 0; i < nodes.size() - 1 ; i++) {

            Search s = new Search(nodes.get(i), nodes.get(i + 1), n, bound);

            // search.setStart(nodes.get(i));

            // search.setGoal(nodes.get(i + 1));

            s.find();

            PRINT("Working with " +  nodes.get(i).toString() + " and " + nodes.get(i+1).toString());

            fullPath.addAll(s.all);

            s.all.clear();

        }

        printTerrainType(nodes);

        /*
        try {
            Printer.plotEdges(bound, new File("edges.png"));
        } catch (Exception ex) { ex.printStackTrace(); }
        */

        _plotCoursePoints(fullPath, new File("terrain.png"));

        //_plotCoursePoints(nodes, new File("terrain.png"), new File("terrain_white_locations"));
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

        for (Node successors : node.successors) {

            PRINT(successors.toString());

        }
    }










}
