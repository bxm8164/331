package com.osamufujimoto;

import java.util.*;

import static com.osamufujimoto.Util.*;
import static com.osamufujimoto.Util.manhattanDistance;

public class Search {


    private Node _start;

    private Node _goal = null;

    public HashMap<Node, Node> cameFrom = new HashMap<>();

    private HashMap<Node, Double> g = new HashMap<>();

    private HashMap<Node, Double> f = new HashMap<>();

    private Set<Node> edges;


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

    public Search(Node start, Node goal, Node[][] all, Set<Node> edges ) {

        _start = start;

        _goal = goal;

        _all = all;

        this.edges = edges;

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

            // LOGI("Getting the node with the lowest f value: " + current.toString());

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

                double tG = g.get(current) + distance(current, edge);


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

                // LOGI("(f) Edge " + edge.toString() + " is " + edge.t.toString() + " with cost " + estimated);

                f.put(edge, estimated);

                open.remove(edge);
                open.add(edge);



            }



            //
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
            return Double.MAX_VALUE;
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

            return (distance(s, g) * s.e * getTerrainCost(s.t)) + distance(next, g) * next.e * getTerrainCost(next.t);





    }


    List<Node> all = new ArrayList<>();

    /**
     * Rebuild the path
     * @param current the current node
     */
    public void rebuildPath(Node current) {

        LOGD("Rebuilding path for " + current.toString());

        ArrayList<Node> path = new ArrayList<>();

        path.add(current);

        while (cameFrom.containsKey(current)) {

            current = cameFrom.get(current);

            path.add(current);

            all.add(current);
        }

        cameFrom.clear();

    }

}

