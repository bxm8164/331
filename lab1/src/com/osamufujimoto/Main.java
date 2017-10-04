package com.osamufujimoto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static com.osamufujimoto.Util.PRINT;

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
    static File output = new File("output.png");

    /**
     * Main program
     * @param args the arguments
     */
    public static void main(String[] args) {

        Node[][] n = read(image, elevations);

        // plotCoursePoints(brown /* course */,  image);

        successors(n[2][2], n);

        _printSuccessors(n[2][2]);
    }


    /**
     * Plot the points from a file to an image
     * @param _course the file containing the points
     * @param _image the image
     */
    public static void plotCoursePoints(File _course, File _image) {

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
    private static void successors(Node node, Node[][] all) {


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

        final double[][] elevation = readFile(_elevation);

        Node[][] node = new Node[500][395];

        try {

            BufferedImage image = ImageIO.read(_image);

            for (int i = 0; i < 500; i++) {

                for (int j = 0; j < 395; j++) {

                    Color color = new Color(image.getRGB(j, i));

                    Terrain t = getTerrainType(color.getRed(), color.getGreen(), color.getBlue());

                    //
                    // Throw an Exception if the terrain type is not defined in
                    // https://www.cs.rit.edu/~zjb/courses/331/proj1/
                    //
                    if (Terrain.UNKNOWN == t) {

                        throw new Exception();
                    }

                    node[i][j] = new Node(i, j, t, elevation[i][j]);

                }

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return node;
    }

    /**
     * Get the type of terrain depending on the RGB values.
     * @param r the red channel
     * @param g the green channel
     * @param b the blue channel
     * @return the type of terrain
     */
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


    /**
     * Read the elevations values from a file.
     * @param _text The text file
     * @return an array containing the elevations
     */
    private static double[][] readFile(File _text) {

        double[][] result = new double[500][495];

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

                    result[i][j] = Double.parseDouble(sp[j]);
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }
        return result;
    }



}
