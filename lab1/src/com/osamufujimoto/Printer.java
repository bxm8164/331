package com.osamufujimoto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.osamufujimoto.Util.*;

/**
 * Printer functions.
 * @author Osamu F
 */

class Path {

}
public class Printer {

    public static void printHumanReadableOutput(List<Node> controlPoints, List<List<Node>> fullPath) {

        int controlPoint = 0;
        int distance = 0;
        Direction direction;
        LOGI("Number of control points: " + controlPoints.size() );
        LOGI("Number of paths: " + fullPath.size() );


        for (int i = 0; i < fullPath.size(); i++) {

            System.out.println("-- On control point #" + i);

            List<Node> currentPath = fullPath.get(i);

            List<List<Node>> nodes;


            for (int j = 0; j < currentPath.size() - 1; j++) {

                // Node start = currentPath.get(0);

                // Node end = currentPath.get(currentPath.size() - 1);

                // LOGI("  Starting node: " + start.toString());

                // LOGI("  End node: " + end.toString());

                // break;

                Node current = currentPath.get(j);

                Node _current = new Node(current.x, current.y, current.t, current.e, current.c);

                Node next = currentPath.get(j+1);



                if (current.t != next.t) {

                    double _distance = manhattanDistancePixel(current, next);

                    System.out.println("Move for " + _distance + "m in " + current.t.toString());

                    continue;
                }

                double _distance = 0.0;

                while (current.t == next.t && j < currentPath.size() ) {

                    _distance += manhattanDistancePixel(current, next);

                    current = next;

                    next = currentPath.get(currentPath.indexOf(current) + 1);

                    j = currentPath.indexOf(next) + 1;

                }

                double m = _current.y - current.x / _current.x - current.x;

                System.out.println("Follow for " + (int) Math.floor(_distance)+ "m.");

            }

            System.out.println();

        }

        System.out.println("-- On control point #" + controlPoints.size());

        System.out.println();
        System.out.println();
        System.out.println();

        // PRINT(fullPath.get(fullPath.size() - 1).toString());
    }

    /**
     * Plot the lake, swamp, marsh area with expanded edges
     * @param nodes the nodes (lake, swap, marsh) and its edges
     * @param img the output file
     */
    public static void plotEdges(Set<Node> nodes, File img) {

        try {

            BufferedImage image = new BufferedImage(395, 500, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < 500; y++)
                for (int x = 0; x < 395; x++) {
                    image.setRGB(x, y, new Color(255, 255, 255).getRGB());
                }

            for (Node nds : nodes) {

                image.setRGB(nds.x, nds.y, new Color(0, 0, 0).getRGB());

            }

            ImageIO.write(image, "png", img);

        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }


    /**
     * Plot a list of nodes
     * @param nodes the list of node
     * @param _image the original map
     * @param output the output file
     */
    public static void plotNodes(List<Node> nodes, File _image, File output) {

        try {

            BufferedImage image = ImageIO.read(_image);

            for (Node node : nodes) {


                int color = new Color(255, 0 , 0).getRGB();

                image.setRGB(node.x, node.y, color);

            }

            ImageIO.write(image, "png", output);

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    /**
     * Plot the points from a file to an image
     * @param _course the file containing the points
     * @param _image the map
     * @param output the output file
     */
    public static void plotInputFile(File _course, File _image, File output) {

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
     * Print the successors of a node
     * @param node the node we want the successors
     */
    public static void printSuccesors(Node node) {

        for (Node successors : node.successors) {

            PRINT(successors.toString());

        }
    }

}
