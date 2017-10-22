package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.collisions.Line2D;

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

    //private Map< AbstractEntity, Map<Point2D, Point2D>> spanningPaths = new HashMap<>(); TODO allow targets & width
    private Map<Point2D, Point2D> spanningPath = new HashMap<>();
    // for each target there is a spanning tree of nodes, showing the shortest path to that target

    private Set<Point2D> nodes = new HashSet<>();               // all nodes of the graph
    private Map<Point2D, Boolean> nodeChecked = new HashMap<>();// nodes which have a direct line of sight
    private Map<Point2D, Float> nodeCost = new HashMap<>();     // the distance to the target from this node

    private Set<Line2D> edges = new HashSet<>();                    // all the edges of the graph
    private Map<Point2D, Set<Line2D>> nodeEdges = new HashMap<>();  // the edges that each point is a part of
    private Map<Line2D, Float> edgeCost = new HashMap<>();          // the cost of the edge (at the moment distance^2)
    private Map<Line2D, Float> edgeWidth = new HashMap<>();         // the max width an entity following this edge can be

    private AbstractEntity target;

    //TODO Check target static
    //TODO Check obstacles added removed
    //TODO Check target mobile && moved
    //TODO different set of nodes based on entity size

    private Line2D currEdge = null;


    private static final int NUMBER_OF_RANDOM_NODES = 0;
    private static float nodeOffset = (float) 0.7;

    /**
     */
    public PathManager() {
        // Hi sonar!
    }



    public Set<Point2D> getNodes() {
        return nodes;
    }

    public Set<Line2D> getEdges() {
        return edges;
    }

    /**
     * Populates the internal graph representation of the path manager, based on the initial world state.
     * Should be run after loading the map
     */
    public void initialise() {
        if (! nodes.isEmpty()) return;

        nodes.clear();
        edges.clear();
        //TODO clear the other stuff

        World world = GameManager.get().getWorld();


        //add points in corners of map
        nodes.add(new Point2D(0, 0));
        nodes.add(new Point2D(world.getWidth(), 0));
        nodes.add(new Point2D(0, world.getLength()));
        nodes.add(new Point2D(world.getWidth(), world.getLength()));


        //get portal
        AbstractEntity portal = null;
        for (AbstractEntity entity : world.getEntities().values()) {
            if (entity instanceof EnemyGate) {
                portal = entity;
                break;
            }
        }

        //add points around corners of of portal
        float portX = portal.getPosX();
        float portY = portal.getPosY();

        nodes.add( new Point2D(portX - nodeOffset, portY - nodeOffset));
        nodes.add( new Point2D(portX - nodeOffset, portY + nodeOffset));
        nodes.add( new Point2D(portX + nodeOffset, portY - nodeOffset));
        nodes.add( new Point2D(portX + nodeOffset, portY + nodeOffset));


        // put nodes on the corners of our static entities
        for (AbstractEntity entity : world.getEntities().values()) {
            if (entity.isStatic() && entity.isSolid()) {
                //TODO make less crummy & make 8 points for circles
                nodes.add( new Point2D(entity.getPosX() - (nodeOffset + 0.5f), entity.getPosY() - (nodeOffset + 0.5f)));
                nodes.add( new Point2D(entity.getPosX() - (nodeOffset + 0.5f), entity.getPosY() + (nodeOffset + 0.5f)));
                nodes.add( new Point2D(entity.getPosX() + (nodeOffset + 0.5f), entity.getPosY() - (nodeOffset + 0.5f)));
                nodes.add( new Point2D(entity.getPosX() + (nodeOffset + 0.5f), entity.getPosY() + (nodeOffset + 0.5f)));
            }
        }


        // Initialise random points on the map
        for (int i = 0; i < NUMBER_OF_RANDOM_NODES; i++) {
            nodes.add(new Point2D((float) (Math.random() * world.getWidth()),
                    (float) (Math.random() * world.getLength())));
        }

        //loop through all nodes and all entities, removing any nodes that intersect with the entity
        Set<Point2D> removedNodes = new HashSet<>();
        for (Point2D node : this.nodes) {
            for (AbstractEntity entity : world.getEntities().values()) {
                if (entity.isStatic() && entity.isSolid() && entity.getMask().overlaps(node)) {
                    removedNodes.add(node);
                }
            }
        }

        for (Point2D node : removedNodes) {
            this.nodes.remove(node);
        }


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
                if(width > 0.68f ) { //TODO

                    float distSqr = edge.getLenSqr();

                    this.edges.add(edge);
                    this.edgeCost.put(edge, distSqr);
                    this.edgeWidth.put(edge, width);
                    this.nodeEdges.get(node1).add(edge);
                    this.nodeEdges.get(node2).add(edge);

                }
            }
        }

        // build the minimum spanning tree from the graph - and set the spanningTree variable.
        //optimiseGraph(lastPlayerPosition, nodes, edges);
        initialiseGraph();
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


    private void initialiseGraph() {

        //clear data structures
        this.nodeCost.clear();

        //set target entity to the player
        World world = GameManager.get().getWorld();

        for (AbstractEntity entity : world.getEntities().values()) { //TODO allow custom target
            if (entity instanceof Player) {					//(entity.getClass().isAssignableFrom(goal)) {
                this.target = entity;
                break;
            }
        }

        // get a point at the centre of the target
        Point2D targetNode = new Point2D(target.getPosX(), target.getPosY());


        // setup queue for dijkstra's algorithm
        Queue<Point2D> nodeQ = new PriorityQueue<>(20, (Point2D p1, Point2D p2) -> {
            // order nodes by nodeCost
                int val = (int) (nodeCost.get(p1) - nodeCost.get(p2));
                return val;
            // NOTE: this is kinda unstable, updating the value in nodeCost will not update the queue
            // the node will have to be removed, nodeCost updated, node reinserted to queue
            }
        );


        // clear the current spanningPath (spanning tree)
        this.spanningPath.clear();

        // loop through all nodes,
        for (Point2D node : this.nodes) {

            Line2D edge = new Line2D(node, targetNode);


            float width = getClearance(edge);

            if (width >= 0.68f ) { //TODO check width is enough for my movement

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
                        break;
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
                    break;
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

        System.out.println("---------");
        for (Point2D node : this.nodes) {
            System.out.println("n: " + node + " to: " + this.spanningPath.get(node));
        }
    }


    public void onTick() {
        /*
        // AbstractWorld world = GameManager.get().getWorld();


         //if player hasn't moved since last tick, can skip this
         if (!player.equals(lastPlayerPosition)) {
             lastPlayerPosition = player;

             //populates directNode, nodes which have a direct line of sight
             boolean doesCollide;
             for (Box3D node : this.nodes) {
                 doesCollide = false;
                 for (AbstractEntity entity : world.getEntities().values()) {
                     if (entity.isStaticCollideable() &&
                            entity.getBox3D().doesIntersectLine(node.getX(), node.getY(), 0, player.getX(), player.getY(), 0)) {
                         doesCollide = true;
                         break;
                     }
                 }
                 if (!doesCollide) {
                     this.directNode.put(node, true);
                 }
             }
         }
        */
    }

    /**
     * Takes as inputs a representation of the graph of internode connections that can be used to create paths.
     * Calculates the minimum spanning tree of the graph, and then stores this internally so it can be used to generate
     * new paths for enemies.
     *
     * param start The initial vertex within the graph where the search starts, and where each generated path will end.
     * param vertices The vertices of the graph of internode connections.
     * param edges The edges of the graph of internode connections.
     */
    private void optimiseGraph() {          //Point2D start, Set<Point2D> vertices, Map<DoublePoint2D, Float> edges) {
        /*List<Point2D> workQueue = new ArrayList<>();
        Map<Point2D, Float> distances = new HashMap<>();

        spanningTree.clear();
        workQueue.add(start);
        distances.put(start, new Float(0));

        while (workQueue.size() > 0) {
            // find the closest thing within the work queue
            Point2D current = workQueue.get(0);
            for (Point2D other : workQueue) {
                if (distances.get(other) < distances.get(current)) {
                    current = other;
                }
            }
            vertices.remove(current);
            workQueue.remove(current);

            // TODO make this more efficient by improving the way we store the graph
            for (Point2D other : vertices) {

                DoublePoint2D pair = new DoublePoint2D(current, other);

                if (!edges.containsKey(pair)) {
                    continue;
                }

                Float distance = edges.get(pair);

                if (!distances.containsKey(other) || distances.get(other) > distance) {
                    spanningTree.put(other, current);
                    distances.put(other, distance);
                }

                if (!workQueue.contains(other)) {
                    workQueue.add(other);
                }
            }

        }

        */

    }

    //TODO
    public Shape2D getTargetNode(Circle2D currentPos, AbstractEntity target, Shape2D targetNode) {

        this.edges.remove(this.currEdge);

        if (target == null)
            return null;

        float currX = currentPos.getX();
        float currY = currentPos.getY();
        float targX = target.getPosX();
        float targY = target.getPosY();

        float rad = currentPos.getRadius();

        boolean hasNode = (targetNode != null);
        float nodeX = hasNode? targetNode.getX() : -999;
        float nodeY = hasNode? targetNode.getY() : -999;

        // if straight line to target (clear of static solid obstacles)
        Line2D direct = new Line2D(currX, currY, targX, targY);

        this.currEdge = direct;
        this.edges.add(this.currEdge);

        float directWidth = getClearance(direct);
        if ( directWidth > rad) {
            System.out.println("MAN: there's a straight path, with width: " + directWidth);
            return target.getMask();
        }

        // if targetNode reached (roughly centred on)
        if (hasNode && Math.abs(currX - nodeX) < 0.25f && Math.abs(currY - nodeY) < 0.25f) {
            // go to next node
            System.out.println("MAN: returning next node");
            return spanningPath.get(targetNode);
        }

        // if straight line to targetNode (clear of static solid obstacles)
        // (should implicitly be true if no obstacles have been added)
        direct = new Line2D(currX, currY, nodeX, nodeY);
        directWidth = getClearance(direct);
        if (hasNode && directWidth > rad) {
            // targetNode is still valid
            System.out.println("MAN: continuing to last node, with width: " + directWidth);
            return targetNode;

        } else {

            // find new shortest path

            Point2D currNode = new Point2D(currX, currY);

            Point2D newTargNode = null;
            float pathCost = Float.POSITIVE_INFINITY;

            System.out.println("MAN: I'm looking for a new path");

            // loop through all nodes,
            for (Point2D node : this.nodes) {

                // draw a line from here to node
                Line2D edge = new Line2D(currNode, node);

                // check that there is clearance / path is valid
                float width = getClearance(edge);
                if (width < rad)
                    continue;

                // get cost of new valid path
                float newPathCost = this.nodeCost.getOrDefault(node, Float.POSITIVE_INFINITY) + edge.getLenSqr();

                if ( newPathCost < pathCost ) {
                    pathCost = newPathCost;
                    newTargNode = node;
                }
            }
            return newTargNode;
        }
    }

    private class targetPath {

    }
}
