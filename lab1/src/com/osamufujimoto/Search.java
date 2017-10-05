package com.osamufujimoto;

import java.io.File;
import java.util.*;

import static com.osamufujimoto.Util.LOGD;
import static com.osamufujimoto.Util.PRINT;
import static com.osamufujimoto.Util.manhattanDistance;

public class Search {


    private final Node _start;

    private final Node _goal;

    public HashMap<Node, Node> cameFrom = new HashMap<>();

    private HashMap<Node, Double> g = new HashMap<>();

    private HashMap<Node, Double> f = new HashMap<>();

    public Comparator<Node> comparator = (o1, o2) -> {

        if (f.get(o1) < f.get(o2)) {

            return -1;

        } else if (f.get(o1) > f.get(o2)) {

            return 1;

        } else {

            return 0;

        }

    };

    private Node[][] _all;

    private PriorityQueue<Node> open = new PriorityQueue<>(comparator);

    private ArrayList<Node> closed = new ArrayList<>();

    public Search(Node start, Node goal, Node[][] all) {

        _start = start;

        _goal = goal;

        _all = all;

        /*
        for (int i = 0; i < 500; i++) {

            for (int j = 0; j < 395; j++) {

                //
                // Start node to the current node
                //
                g.put(all[i][j], Util.distance(_start, all[i][j]));

                f.put(all[i][j], Double.MAX_VALUE );


            }
        }
        */


    }

    public void find() {

        open.clear();

        closed.clear();

        open.add(_start);

        g.put(_start, 0.0);

        for (int i = 0; i < 500; i++) {

            for (int j = 0; j < 395; j++) {

                Node node = _all[i][j];

                f.put(node, Double.MAX_VALUE);
            }

        }


        f.put(_start, heuristic(_start, _goal));

        while (!open.isEmpty()) {

            Node current = open.poll();

            if (current == _goal) {

                rebuildPath(current);

                return;
            }

            closed.add(current);

            Main.successors(current, _all);

            for (Node edge : current.sucessors) {

                if (closed.contains(edge)) {
                    continue;
                }

                double tG = g.get(current) + manhattanDistance(current, edge);

                if (!open.contains(edge)) {

                    open.add(edge);

                }

                else if (tG >= g.get(edge)) {

                    continue;
                }

                cameFrom.put(edge, current);

                g.put(edge, tG);

                double estimated = g.get(edge) + heuristic(edge, _goal);

                f.put(edge, estimated);


            }


        }

    }

    public static double heuristic(Node s, Node g) {

        return manhattanDistance(s, g);

        // return 1.0;
    }


    List<Node> all = new ArrayList<>();

    public void rebuildPath(Node current) {

        System.out.println("Rebuilding path...");

        ArrayList<Node> path = new ArrayList<>();

        path.add(current);

        while (cameFrom.containsKey(current)) {

            current = cameFrom.get(current);

            path.add(current);

            all.add(current);
        }

        for (Node node : path) {
            PRINT(node.toString());
        }

        // Main._plotCoursePoints(path, new File("terrain.png"));

    }

}

