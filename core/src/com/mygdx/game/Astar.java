/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.IntArray;

/**
 *
 * @author gregclemp
 */
/** @author Nathan Sweet */
public class Astar {
    private final int width, height;
    private final BinaryHeap<PathNode> open;
    private final PathNode[] nodes;
    int runID;
    private final IntArray path = new IntArray();
    private int targetX, targetY;
    private Map map;

    public Astar (Map map, int width, int height) {
            this.width = width;
            this.height = height;
            this.map = map;
            open = new BinaryHeap(width * 4, false);
            nodes = new PathNode[width * height];
    }

    /** Returns x,y pairs that are the path from the target to the start. */
    public IntArray getPath (int startX, int startY, int targetX, int targetY) {
            this.targetX = targetX;
            this.targetY = targetY;

            path.clear();
            open.clear();

            runID++;
            if (runID < 0) runID = 1;

            int index = startY * width + startX;
            PathNode root = nodes[index];
            if (root == null) {
                    root = new PathNode(0);
                    root.x = startX;
                    root.y = startY;
                    nodes[index] = root;
            }
            root.parent = null;
            root.pathCost = 0;
            open.add(root, 0);

            int lastColumn = width - 1, lastRow = height - 1;
            int i = 0;
            while (open.size > 0) {
                    PathNode node = open.pop();
                    if (node.x == targetX && node.y == targetY) {
                            while (node != root) {
                                    path.add(node.x);
                                    path.add(node.y);
                                    node = node.parent;
                            }
                            break;
                    }
                    node.closedID = runID;
                    int x = node.x;
                    int y = node.y;
                    if (x < lastColumn) {
                            addNode(node, x + 1, y, cost(x, y, x + 1, y));
                            if (y < lastRow) addNode(node, x + 1, y + 1, cost(x, y, x + 1, y + 1)); // Diagonals cost more, roughly equivalent to sqrt(2).
                            if (y > 0) addNode(node, x + 1, y - 1, cost(x, y, x + 1, y - 1));
                    }
                    if (x > 0) {
                            addNode(node, x - 1, y, cost(x, y, x - 1, y));
                            if (y < lastRow) addNode(node, x - 1, y + 1, cost(x, y, x - 1, y + 1));
                            if (y > 0) addNode(node, x - 1, y - 1, cost(x, y, x - 1, y - 1));
                    }
                    if (y < lastRow) addNode(node, x, y + 1, cost(x, y, x, y + 1));
                    if (y > 0) addNode(node, x, y - 1, cost(x, y, x, y - 1));
                    i++;
            }
            return path;
    }

    private void addNode (PathNode parent, int x, int y, int cost) {
            if (!isValid(x, y)) return;

            int pathCost = parent.pathCost + cost;
            float score = pathCost + Math.abs(x - targetX) + Math.abs(y - targetY);

            int index = y * width + x;
            PathNode node = nodes[index];
            if (node != null && node.runID == runID) { // Node already encountered for this run.
                    if (node.closedID != runID && pathCost < node.pathCost) { // Node isn't closed and new cost is lower.
                            // Update the existing node.
                            open.setValue(node, score);
                            node.parent = parent;
                            node.pathCost = pathCost;
                    }
            } else {
                    // Use node from the cache or create a new one.
                    if (node == null) {
                            node = new PathNode(0);
                            node.x = x;
                            node.y = y;
                            nodes[index] = node;
                    }
                    open.add(node, score);
                    node.runID = runID;
                    node.parent = parent;
                    node.pathCost = pathCost;
            }
    }

    protected boolean isValid (int x, int y) {
            return true;
    }

    public int getWidth () {
            return width;
    }

    public int getHeight () {
            return height;
    }

    public int cost(int x1, int y1, int x2, int y2){
        if (map.getCoordinate(x2, y2).edge){
            return 100000;
        }
        return (int) Math.pow(((map.getCoordinate(x1, y1).depth-map.getCoordinate(x2, y2).depth)/10), 2);
    }
}

