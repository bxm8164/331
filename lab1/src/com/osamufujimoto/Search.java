package com.osamufujimoto;

import java.util.*;

import static com.osamufujimoto.Util.*;

/**
 * Search Class.
 * The optimal path from the starting node to the goal node is computed here
 * @author Osamu Fujimoto
 */
public class Search {

    /**
     * The starting node
     */
    private Node _start;

    /**
     * The goal node
     */
    private Node _goal = null;

    /**
     * Used to keep track how we reached our goal node
     */
    public HashMap<Node, Node> cameFrom = new HashMap<>();

    /**
     * The cost from the starting node to the current node.
     */
    private HashMap<Node, Double> g = new HashMap<>();

    /**
     * f(Node) = g(Node) + h(Node)
     * h is an admissible heuristic function that estimates the cost form the Node to the goal.
     */
    private HashMap<Node, Double> f = new HashMap<>();

    /**
     * The nodes that are lake, swap and marsh and its edges.
     */
    private Set<Node> _edges;

    /**
     * The nodes already evaluated
     */
    private ArrayList<Node> closed = new ArrayList<>();

    /**
     * All the nodes in the map
     */
    private Node[][] _all;

    /**
     * The nodes that are discovered but not yet evaluated
     */
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


    /**
     * Search construct
     * @param start the starting node
     * @param goal the goal node
     * @param all all the nodes in the map
     * @param edges the nodes that are lake, swap or marsh and its edges
     */
    public Search(Node start, Node goal, Node[][] all, Set<Node> edges ) {

        _start = start;

        _goal = goal;

        _all = all;

        _edges = edges;

    }

    /**
     * Run the A* Search
     */
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

            if (current == _goal) {

                rebuildPath(current);

                return;
            }

            closed.add(current);

            Main.successors(current, _all);

            for (Node edge : current.successors) {

                // already evaluated
                if (closed.contains(edge)) {
                    continue;
                }

                // distance from the start to the neighbor
                double tG = g.get(current) + distance(current, edge);

                // discover a new node
                if (!open.contains(edge)) {
                    open.add(edge);

                }

                // this is not a better path
                else if (tG >= g.get(edge)) {
                    continue;
                }

                // this is the best path until now
                cameFrom.put(edge, current);

                g.put(edge, tG);

                double estimated = g.get(edge) + heuristic(edge, _goal);

                f.put(edge, estimated);

                // horrible way to force an update in the priority queue.
                open.remove(edge);
                open.add(edge);

            }

        }

    }

    /**
     * Heuristic function.
     * @param s the successor of the node
     * @param g the goal node
     * @return the cost from the successor to the goal
     */
    public double heuristic(Node s, Node g) {


        return  distance(s, g) * getTerrainCost(s.t) * Math.pow(s.e, 2);


    }

    /**
     * Heuristic function. The function heuristic(s, g) was used for the starting node and this was used for all its
     * successors. It seems to produce better results because it looks the node ahead of the succesor, but it has the
     * drawback that it runs pretty slow (using the simple heuristic it takes form 3 to 5 seconds and using this
     * heuristic it increases the time to 22-25 seconds.
     * @param s the successor of the current node
     * @param g the goal node
     * @param current the current node
     * @return the cost from the successor to the goal
     */
    public  double heuristic(Node s, Node g, Node current) {

        if (g.t == Terrain.OUT_OF_BOUNDS || s.t == Terrain.LAKE_SWAP_MARSH) {
            return Double.MAX_VALUE;
        }

        // get the successor direction
        Node next = null;

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

