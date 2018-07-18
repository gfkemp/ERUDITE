/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.BinaryHeap.Node;

/**
 *
 * @author gregclemp
 */
public class PathNode extends BinaryHeap.Node {
            int runID, closedID, x, y, pathCost;
            PathNode parent;

            public PathNode (float value) {
                    super(value);
            }
    }
