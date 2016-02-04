package com.zk.graph;

/**
 * A directional edge between two nodes.
 *
 * @author zkokaja
 * @version 1.0
 */
public class Edge {
  
  private Node to;
  private Node from;
  private Direction direction;

  public Edge(Node from, Node to, Direction direction) {
    this.to   = to;
    this.from = from;
    this.direction = direction;
  } 

  public Node getTo() {
    return to;
  }
}
