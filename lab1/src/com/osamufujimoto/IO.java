package com.osamufujimoto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class IO {

    /**
     * Read a text file containing the elevation at each point of the map
     * @param _text the text file
     * @return an array that contains the elevation at each point
     */
    private static double[][] readTextFile(File _text) {

        double[][] result = new double[500][395];

        try {

            BufferedReader reader = new BufferedReader(new FileReader(_text));

            String current;

            for (int y = 0; y < 500; y++) {

                current = reader.readLine();

                String[] sp = current.split("   ");

                // The first (numeric) value of sp is at index 1. Index 0 is empty.
                // Skips the last four lines.
                for (int x = 1; x < 396; x++) {

                    result[y][x - 1] = Double.parseDouble(sp[x]);

                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return result;

    }

    /**
     * Read the each pixel value and create a Node containing its position, terrain type and elevation
     * @param _image the image file
     * @param _elevation the text file
     * @return
     */
    public static Node[][] read(final File _image, final File _elevation, int[][] water) {

        final double[][] elevation = readTextFile(_elevation);

        Node[][] node = new Node[500][395];

        // ArrayList<ArrayList<Node>> nds = new ArrayList<>();

        try {

            BufferedImage image = ImageIO.read(_image);

            for (int y = 0; y < 500; y++) {

                for (int x = 0; x < 395; x++) {

                    Color color = new Color(image.getRGB(x, y));

                    Terrain t = Util.getTerrainType(color.getRed(), color.getGreen(), color.getBlue());

                    node[y][x] = new Node(x, y, t, elevation[y][x], color);

                    if (t == Terrain.LAKE_SWAP_MARSH) {

                        water[y][x] = 1;

                    }

                }

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return node;
    }

    /**
     * Find the nodes that are lake, swamp or marsh, or its edges
     * @param water the nodes that are water
     * @param all all the nodes in the map
     * @return a set containing all the water nodes or its edges.
     */
    public static Set<Node> findEdges(int[][] water, Node[][] all) {

        Set<Node> set = new LinkedHashSet<>();

        for (int y = 0; y < 500; y++) {

            for (int x = 0; x < 395; x++) {

                if (water[y][x] == 1) {

                    Main.successors(all[y][x], all);

                    for (Node scs : all[y][x].successors) {

                        if (scs.t != Terrain.OUT_OF_BOUNDS) {

                            set.add(scs);

                        }
                    }
                }

            }
        }

        return set;
    }

    /**
     * Read file containing the control points we need to pass
     * @param _input the input file
     * @param nodes the nodes from the image
     * @return the of control points
     */
    public static List<Node> readInputFile(File _input, Node[][] nodes) {

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




}
