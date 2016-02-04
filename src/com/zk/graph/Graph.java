package com.zk.graph;

import java.util.Map;
import java.util.List;
import java.util.Queue;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A graph is a set of nodes.
 *
 * The main functionality implemented here is building a graph from a grid, and
 * a method to find the best path from one node to another.
 *
 * @author zkokaja
 * @version 1.0
 */
public class Graph {

  private Map<String,Node> nodes;

  public Graph() {
    init(); 
  }

  private void init() {
    nodes = new HashMap<>();
  }

  public void addNode(Node node) {
    nodes.put(node.getName(), node);
  }

  public Node get(String nodeName) {
    return nodes.get(nodeName);
  }

  public Node get(String nodeName, int x, int y) {

    Node node = nodes.get(nodeName);

    if (node == null) {
      node = new Node(nodeName, x, y);
      addNode(node);
    }

    return node;
  }

  /**
   * A* algorithm to find the best path.
   *
   * @param from the starting node
   * @param to the ending node
   * @return A list of nodes from start to finish
   */
  public List<Node> findPath(String from, String to) {

    Node start = get(from);
    Node goal  = get(to);

    Queue<Node> open = new PriorityQueue<>();
    open.add(start);

    Map<Node,Node> cameFrom = new HashMap<>();
    cameFrom.put(start, start);

    Map<Node,Integer> costSoFar = new HashMap<>();
    costSoFar.put(start, 0);
     
    while (!open.isEmpty()) {

      Node current = open.remove();

      if (goal.equals(current)) {
          break;
      }

      Edge[] edges = current.getEdges();
      for (int i=0; i<edges.length; i++) {

        Edge edge = edges[i];
        if (edge == null) continue;

        Node edgeNode = edge.getTo();
        String edgeName = edgeNode.getName();

        int newCost = costSoFar.get(current) + current.h(edgeNode);
        if (!costSoFar.containsKey(edgeNode) || newCost < costSoFar.get(edgeNode)) {

          costSoFar.put(edgeNode, newCost);
          int priority = newCost + edgeNode.h(goal);
          edgeNode.setPriority(priority);
          open.add(edgeNode); // TODO
          cameFrom.put(edgeNode, current);
        }
      }

    }

    List<Node> path = new ArrayList<>();
    Node current = goal;
    path.add(current);

    while (!current.equals(start)) {
      current = cameFrom.get(current);
      path.add(current);
    }

    return path;
  }

  public static Graph buildGraph(String[][] keys) {

    Graph graph = new Graph();

    int rows = keys.length;

    for (int r=0; r<rows; r++) {
    int cols = keys[r].length;

      for (int c=0; c<cols; c++) {

        String key = keys[r][c];
        Node node = graph.get(key, r, c);

        // Has a north node?
        if (r != 0 && c < keys[r-1].length) {
          String northKey = keys[r-1][c];
          Node northNode  = graph.get(northKey, r-1, c);

          node.addEdgeFromThisTo(northNode, Direction.NORTH);
        }

        // Has an east node?
        if (c != cols-1) {
          String eastKey = keys[r][c+1];
          Node eastNode  = graph.get(eastKey, r, c+1);

          node.addEdgeFromThisTo(eastNode, Direction.EAST);
        }

        // Has a west node?
        if (c != 0) {
          String westKey = keys[r][c-1];
          Node westNode = graph.get(westKey, r, c-1);

          node.addEdgeFromThisTo(westNode, Direction.WEST);
        }

        // Has a south node?
        if (r != rows -1 && c < keys[r+1].length) {
          String southKey = keys[r+1][c];
          Node southNode  = graph.get(southKey, r+1, c);

          node.addEdgeFromThisTo(southNode, Direction.SOUTH);
        }

      }
    }

    return graph;
  }

}
