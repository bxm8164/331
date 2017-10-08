package com.osamufujimoto;

import java.util.*;

import static com.osamufujimoto.Util.*;
import static com.osamufujimoto.Util.manhattanDistance;

public class Search {


    private  Node _start;

    private Node _goal = null;

    public HashMap<Node, Node> cameFrom = new HashMap<>();

    private HashMap<Node, Double> g = new HashMap<>();

    private HashMap<Node, Double> f = new HashMap<>();


    private Node[][] _all;

    private PriorityQueue<Node> open = new PriorityQueue<>(new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            if (f.get(o1) < f.get(o2)) {

                return -1;

            } else if (f.get(o1) > f.get(o2)) {

                return 1;

            } else {

                return 0;

            }
        }
    });

    private ArrayList<Node> closed = new ArrayList<>();

    public Search(Node start, Node goal, Node[][] all) {

        _start = start;

        _goal = goal;

        _all = all;

    }




    public void find() {

        if (_start == null || _goal == null) {

            LOGE("Start/Goal is null. Exiting");

            return;

        }

        open.clear();

        closed.clear();

        f.clear();

        g.clear();

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

            LOGI("Getting the node with the lowest f value: " + current.toString());

            if (current == _goal) {

                rebuildPath(current);

                return;
            }

            closed.add(current);

            Main.successors(current, _all);

            // LOGD(String.format("%s has %d successors", current.toString(), current.successors.size()));

            for (Node edge : current.successors) {

                // already evaluated
                if (closed.contains(edge)) {

                    continue;
                }

                double tG = g.get(current) + manhattanDistance(current, edge);


                if (!open.contains(edge)) {
                    // LOGI("Discovered a new node: " + edge.toString());
                    open.add(edge);

                }

                // Distance from the start to the neighbor

                else if (tG >= g.get(edge)) {
                    // LOGI("This is not a better path");
                    continue;
                }

                // LOGD("Recording path: " + edge.toString());
                cameFrom.put(edge, current);

                g.put(edge, tG);

                double estimated = g.get(edge) + heuristic(edge, _goal, current);

                // LOGI("(F) Edge " + edge.toString() + " with cost " + estimated);

                f.put(edge, estimated);

                open.remove(edge);
                open.add(edge);

            }



            // LOGI("Next: " + open.peek().toString());

            //reak;

        }

    }

    public double heuristic(Node s, Node g) {

        return distance(s, g) * getTerrainCost(s.t);

        // Right
    }

    public  double heuristic(Node s, Node g, Node current) {

        if (g.t == Terrain.OUT_OF_BOUNDS || s.t == Terrain.LAKE_SWAP_MARSH) {
            return 999;
        }

        // get the successor direction
        Node next = null;


            // LOGD("Sucessor: " + s.toString());
            // LOGD("Current: " + current.toString());

            Direction d = getNodeDirection(current, s);

            try {
                if (d == Direction.BOTTOM) {

                    next = _all[s.y - 1][s.x];

                }
                if (d == Direction.TOP) {
                    next = _all[s.y + 1][s.x];

                }
                if (d == Direction.LEFT) {
                    next = _all[s.y][s.x - 1];

                }
                if (d == Direction.RIGHT) {
                    next = _all[s.y - 1][s.x + 1];

                }
            } catch (Exception ex) {
                next = g;
            }

            return (manhattanDistance(s, g) * getTerrainCost(s.t)) + manhattanDistance(next, g) * next.e * getTerrainCost(next.t);



    }

    public HashMap<Direction, List<Node>> twosuccessors(Node node) {

        HashMap<Direction, List<Node>> ss = new HashMap<>();

        final int x = node.x;
        final int y = node.y;
        // left
        List<Node> L = new ArrayList<>();
        L.add(_all[y][x - 1]);
        L.add(_all[y][x-2]);
        ss.put(Direction.LEFT, L);

        // right
        List<Node> R = new ArrayList<>();
        R.add(_all[y][x + 1]);
        R.add(_all[y][x + 2]);
        ss.put(Direction.RIGHT, R);

        // top
        List<Node> T = new ArrayList<>();
        T.add(_all[y + 1][x]);
        T.add(_all[y + 2][x]);
        ss.put(Direction.TOP, T);

        // bottom
        List<Node> B = new ArrayList<>();
        B.add(_all[y - 1][x]);
        B.add(_all[y - 2][x]);
        ss.put(Direction.BOTTOM, B);

        return ss;

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

        cameFrom.clear();

        // Main._plotCoursePoints(path, new File("terrain.png"));

    }

}

