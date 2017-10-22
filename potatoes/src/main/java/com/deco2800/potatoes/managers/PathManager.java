package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.collisions.*;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.enemies.EnemyGate;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.worlds.World;

import java.util.*;

import static com.deco2800.potatoes.util.MathUtil.compareFloat;


/**
 * Object to manage the creation and allocation of paths for enemies to follow.
 * TODO assumes that the object is a circle
 */
public class PathManager extends Manager {
    /* The PathManager stores a minimum spanning tree, representing all the internode connections that can be used to
     * create paths. This is represented as a hashmap in memory, where the keys are nodes, and the values are the
     * parent nodes of the keys within the minimum spanning tree.
     *
     * This tree is centered around the node storing the player's location for now, though in future that will be
     * expanded so that multiple different goals can be set.
     */

    private Set<Integer> targetIDs = new HashSet<>();
    private Map<Integer, Point2D> targetCentres = new HashMap<>();
    private Map<Integer, Map<Float, TargetPath>> targetPaths = new HashMap<>();


    private static float nodeOffsetFudge = (float) 0.01;
    private static float edgeWidthFudge = (float) 0.001;



    /**
     */
    public PathManager() {
        // Hi sonar!
    }


    // ----------     Debug Drawing    ---------- //
    // only should be used in render God/Debug

    private boolean nodesNeedsUpdateFlag = false;
    private Set<Point2D> nodes = new HashSet<>();

    private boolean edgesNeedsUpdateFlag = false;
    private Set<Line2D> edges = new HashSet<>();

    //TODO comm
    public Set<Point2D> getNodes() {
        if (nodesNeedsUpdateFlag) {

            nodes.clear();

            for (Map<Float, TargetPath> target : targetPaths.values()) {
                for (TargetPath targPath : target.values()) {
                    nodes.addAll(targPath.nodes);
                }
            }
        }
        return nodes;
    }

    //TODO comm
    public Set<Line2D> getEdges() {
        if (edgesNeedsUpdateFlag) {

            edges.clear();

            for (Map<Float, TargetPath> target : targetPaths.values()) {
                for (TargetPath targPath : target.values()) {
                    edges.addAll(targPath.edges);
                }
            }
        }

        return edges;
    }




    // ----------     on Tick    ---------- //

    //TODO comm removes any targets that have been removed from entities
    //Checks entities that have moved, sets a flag for their TargetPath's to be updated on next request
    public void onTick() {
        World world = GameManager.get().getWorld();

        Set<Integer> removeTargets = new HashSet<>();

        // check targets moved, update trees
        for (Integer targetID: this.targetIDs) {

            AbstractEntity target = world.getEntities().getOrDefault(targetID, null);

            // remove target if it is no longer valid
            if (target == null) {
                // Messes up this for loop if we remove here
                removeTargets.add(targetID);
                continue;
            }

            Point2D newCentre = target.getMask().getCentre();

            // if the target has moved significantly set the flag for it's paths to be recalculated
            if (this.targetCentres.get(targetID).distance(newCentre) > 0.2f) { //TODO dist sqrd methd
                //set recalc flag, do not recalc yet in case no entity requests path
                this.targetCentres.put(targetID, newCentre);
                for(TargetPath targPath : this.targetPaths.get(targetID).values()) {
                    targPath.needsUpdateFlag = true;
                }
            }
        }

        // remove targets that are no longer valid
        for (Integer targetID: removeTargets) {
            this.targetIDs.remove(targetID);
            this.targetCentres.remove(targetID);
            this.targetPaths.remove(targetID);
        }
    }




    // ----------     Requesting a path    ---------- //

    //TODO comm
    public Point2D getNextNodeToTarget(Circle2D currentPos, Integer targetID, Point2D targetPathNode) {

        World world = GameManager.get().getWorld();

        // if no targetID don't move
        if (targetID == null)
            return null;

        // add this target if it does not exist
        if (!this.targetIDs.contains(targetID)) {
            this.targetIDs.add(targetID);
            System.out.println("targID Add: " + targetID);
            Point2D targCenter = world.getEntities().get(targetID).getMask().getCentre();
            this.targetCentres.put(targetID, targCenter);
            this.targetPaths.put(targetID, new HashMap<>());
        }

        // get this TargetPath, add this path width if it does not exist
        float rad = currentPos.getRadius();

        TargetPath targPath = null;

        for (TargetPath targPathIter : this.targetPaths.get(targetID).values()){
            if (compareFloat(targPathIter.myWidth,  rad)) {
                targPath = targPathIter;
                break;
            }
        }

        if (targPath == null) {
            targPath = new TargetPath();
            targPath.myWidth = rad;
            targPath.targetID = targetID;
            targPath.initialise();
            this.targetPaths.get(targetID).put(rad, targPath);
        }

        // update
        if (targPath.needsUpdateFlag) {
            targPath.createGraph();
            targPath.needsUpdateFlag = false;
        }


        Point2D target = this.targetCentres.get(targetID);
        Point2D currPos = currentPos.getCentre();


        if (targetPathNode != null) {

            // if straight line to target (clear of static solid obstacles)
            Line2D direct = new Line2D(currPos, target);
            float directWidth = getClearance(direct);
            if (directWidth > rad) {
                System.out.println("MAN: there's a straight path, with width: " + directWidth);
                return target;
            }

            // if targetPathNode is not a valid node, try for a new path
            if (!targPath.nodes.contains(targetPathNode))
                return newPath(targPath, currPos);

            // if targetPathNode reached (roughly centred on), follow to next point
            if (currentPos.getCentre().distance(targetPathNode) < 0.2) { //TODO dist sqrd method
                // go to next node
                System.out.println("MAN: returning next node");
                return targPath.spanningPath.get(targetPathNode);
            }

            // if straight line to targetPathNode (clear of static solid obstacles)
            // (should implicitly be true if no obstacles have been added)
            direct = new Line2D(currPos, targetPathNode); //TODO check if implicitly true
            directWidth = getClearance(direct);
            if (directWidth > rad) {
                // targetNode is still valid
                System.out.println("MAN: continuing to last node, with width: " + directWidth);
                return targetPathNode;

            }
        }

        return newPath(targPath, currPos);
    }

    private Point2D newPath(TargetPath targPath, Point2D currPos) {
        // new shortest path is needed

        Point2D newTargPathNode = null;
        float pathCost = Float.POSITIVE_INFINITY;

        System.out.println("MAN: I'm looking for a new path");

        // loop through all nodes,
        for (Point2D node : targPath.nodes) {

            // draw a line from here to node
            Line2D edge = new Line2D(currPos, node);

            // check that there is clearance / path is valid
            float width = getClearance(edge);
            if (width <= targPath.myWidth)
                continue;

            // get cost of new valid path
            float newPathCost = targPath.nodeCost.getOrDefault(node, Float.POSITIVE_INFINITY) + edge.getLenSqr();

            if (newPathCost < pathCost) {
                pathCost = newPathCost;
                newTargPathNode = node;
            }
        }
        return newTargPathNode;
    }



    //TODO description & use obstacles data structure
    private float getClearance(Line2D edge) {

        World world = GameManager.get().getWorld();

        float width = Float.POSITIVE_INFINITY; //default width

        //limit width by each obstacle
        for (AbstractEntity entity : world.getEntities().values()) {
            if (entity.isStatic() && entity.isSolid()) {

                width = Math.min(width, edge.distance(entity.getMask()));

                if (width < 0 ) { //TODO allow my width?
                    // if overlapping; return  negative value early
                    return width;
                }
            }
        }

        return width;
    }

    //TODO stores nodes in order in X & Y coords seperately?
    private class nodeMap {}

    private class TargetPath {

        private Integer targetID;
        private float myWidth = -1;

        boolean needsUpdateFlag = false;

        private Map<Point2D, Point2D> spanningPath = new HashMap<>();
        // for each target there is a spanning tree of nodes, showing the shortest path to that target

        private Set<Point2D> nodes = new HashSet<>();               // all nodes of the graph
        private Map<Point2D, Boolean> nodeChecked = new HashMap<>();// keeps track of nodes as they are checked off
        private Map<Point2D, Float> nodeCost = new HashMap<>();     // the distance to the target from this node

        private Set<Line2D> edges = new HashSet<>();                    // all the edges of the graph
        private Map<Point2D, Set<Line2D>> nodeEdges = new HashMap<>();  // the edges that each point is a part of
        private Map<Line2D, Float> edgeCost = new HashMap<>();          // the cost of the edge (at the moment distance^2)

        //TODO init
        //TODO add shape
        //TODO remove shape

        public void initialise() {
            initialiseNodes();
            initialiseEdges();
            createGraph();
            needsUpdateFlag = false;
        }

        //assumes width has been set
        public void initialiseNodes() {
            spanningPath.clear();

            nodes.clear();
            nodeChecked.clear();
            nodeCost.clear();

            edges.clear();
            nodeEdges.clear();
            edgeCost.clear();

            World world = GameManager.get().getWorld();

            float offset = nodeOffsetFudge + myWidth;

            // put nodes on the corners of our static entities
            for (AbstractEntity entity : world.getEntities().values()) {
                if (entity.isStatic() && entity.isSolid()) {
                    Shape2D mask = entity.getMask();

                    float xExtent = 0;
                    float yExtent = 0;
                    if (mask instanceof Box2D) {
                        Box2D box = (Box2D) mask;

                        xExtent = box.getXLength() / 2 + offset;
                        yExtent = box.getYLength() / 2 + offset;

                    } else if (mask instanceof Circle2D) {
                        // TODO 8 points around circles / make smoother
                        Circle2D circ = (Circle2D) mask;

                        xExtent = circ.getRadius() + offset;
                        yExtent = xExtent;
                    }

                    float xMin = mask.getX() - xExtent;
                    float xMax = mask.getX() + xExtent;
                    float yMin = mask.getY() - yExtent;
                    float yMax = mask.getY() + yExtent;

                    // add corner nodes
                    nodes.add(new Point2D(xMin, yMin));
                    nodes.add(new Point2D(xMin, yMax));
                    nodes.add(new Point2D(xMax, yMin));
                    nodes.add(new Point2D(xMax, yMax));

                    //NOTE: only Box2D's and Circle2D's have points added
                }
            }

            /*
            // Initialise random points on the map
            for (int i = 0; i < NUMBER_OF_RANDOM_NODES; i++) {
                nodes.add(new Point2D((float) (Math.random() * world.getWidth()),
                        (float) (Math.random() * world.getLength())));
            }
            */

            //loop through all nodes and all entities, removing any nodes that intersect with entities
            Set<Point2D> removedNodes = new HashSet<>();
            for (Point2D node : this.nodes) {
                for (AbstractEntity entity : world.getEntities().values()) { //TODO obstacle data structure
                    if (entity.isStatic() && entity.isSolid() && entity.getMask().overlaps(node)) {
                        removedNodes.add(node);
                    }
                }
            }

            for (Point2D node : removedNodes) {
                this.nodes.remove(node);
            }

            nodesNeedsUpdateFlag = true;
        }

        public void initialiseEdges () {

            float clearance = myWidth + edgeWidthFudge;

            //loop through every combination of 2 nodes & create lines
            for (Point2D node1 : this.nodes) {

                this.nodeEdges.put(node1, new HashSet<>());     //start the list of edges associated with this node

                for (Point2D node2 : this.nodes) {

                    // only get each line once
                    if (node1 == node2)
                        break;

                    Line2D edge = new Line2D(node1, node2);

                    // check the line against each obstacle entity TODO use obstacles data structure
                    float width = getClearance(edge);


                    // if clear of overlaps
                    if(width > clearance ) {

                        float distSqr = edge.getLenSqr();

                        this.edges.add(edge);
                        this.edgeCost.put(edge, distSqr);
                        this.nodeEdges.get(node1).add(edge);
                        this.nodeEdges.get(node2).add(edge);

                    }
                }
            }

            edgesNeedsUpdateFlag = true;
        }

        // build the minimum spanning tree from the graph - and set the spanningTree variable.
        //optimiseGraph(lastPlayerPosition, nodes, edges);
        private void createGraph() {

            //clear data structures
            this.nodeCost.clear();
            this.spanningPath.clear();

            //set target entity to the player
            World world = GameManager.get().getWorld();

            // get a point at the centre of the target
            Point2D targetNode = world.getEntities().get(targetID).getMask().getCentre();


            // setup queue for dijkstra's algorithm
            Queue<Point2D> nodeQ = new PriorityQueue<>(20, (Point2D p1, Point2D p2) -> {
                // order nodes by nodeCost
                int val = (int) (nodeCost.get(p1) - nodeCost.get(p2));
                return val;
                // NOTE: this is kinda unstable, updating the value in nodeCost will not update the queue
                // the node will have to be removed, nodeCost updated, node reinserted to queue
            }
            );


            float clearance = myWidth + edgeWidthFudge;

            // loop through all nodes,
            for (Point2D node : this.nodes) {

                Line2D edge = new Line2D(node, targetNode);

                float width = getClearance(edge);

                if (width > clearance ) {

                    //this node has a direct line of sight(with given width), it is a direct node

                    float directNodeCost = edge.getLenSqr();    // set the cost of getting from the node to the target
                    this.nodeCost.put(node, directNodeCost);
                    this.spanningPath.put(node, null);      // add a null terminator as the next node in the spanningPath
                    this.nodeChecked.put(node, true);       // Mark this node as explored

                    //start Dijkstra's algorithm here, start the queue
                    for (Line2D nodeEdge : this.nodeEdges.get(node)) {

                        Point2D exploreNode = nodeEdge.getOtherEndPoint(node);

                        // the node might also be a direct node and have already been explored
                        if (this.nodeChecked.getOrDefault(exploreNode, false)) {
                            continue;
                        }

                        // the cost to reach exploreNode from this directNode
                        float exploreNodeCost = directNodeCost + nodeEdge.getLenSqr();

                        // find the current cost to reach exploreNode, if it has any
                        float exploreNodeCurrCost = this.nodeCost.getOrDefault(exploreNode, Float.POSITIVE_INFINITY);

                        // if cost is smaller
                        if (exploreNodeCost < exploreNodeCurrCost) {

                            nodeQ.remove(exploreNode);
                            this.nodeCost.put(exploreNode, exploreNodeCost);        // update cost

                            // add the node that this node should point towards (tentatively)
                            this.spanningPath.put(exploreNode, node);

                            nodeQ.add(exploreNode);                                 // reorder the nodeQ
                        }
                    }

                } else {
                    this.nodeChecked.put(node, false);
                    this.nodeCost.put(node, Float.POSITIVE_INFINITY);
                }
            }

            // Dijkstra's algorithm thing
            while (!nodeQ.isEmpty()) {
                // pop front
                Point2D addedNode = nodeQ.poll();

                // add this on to the spanning path
                this.nodeChecked.put(addedNode, true);

                float addedNodeCost = this.nodeCost.get(addedNode);

                //explore edges
                for (Line2D nodeEdge : this.nodeEdges.get(addedNode)) {

                    Point2D exploreNode = nodeEdge.getOtherEndPoint(addedNode);

                    // the node might also be a part of the path already and have already been explored
                    if (this.nodeChecked.get(exploreNode)) {
                        continue;
                    }

                    // the cost to reach exploreNode from this directNode
                    float exploreNodeCost = addedNodeCost + nodeEdge.getLenSqr();

                    // find the current cost to reach exploreNode, if it has any
                    float exploreNodeCurrCost = this.nodeCost.getOrDefault(exploreNode, Float.POSITIVE_INFINITY);

                    // if cost is smaller
                    if (exploreNodeCost < exploreNodeCurrCost) {

                        nodeQ.remove(exploreNode);
                        this.nodeCost.put(exploreNode, exploreNodeCost);        // update cost

                        // add the node that this node should point towards (tentatively)
                        this.spanningPath.put(exploreNode, addedNode);

                        nodeQ.add(exploreNode);                                 // reorder the nodeQ
                    }
                }
            }
        }

    }
}
