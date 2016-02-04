package com.zk.graph;

/**
 * A node is a vertex on a grid graph.
 *
 * This kind of node will have at least two and at most four edges.
 *
 * @author zkokaja
 * @version 1.0
 */
public class Node implements Comparable {
  
  private int    x;
  private int    y;
  private int    p;
  private String name;
  private Edge[] edges;

  public Node(String name, int x, int y) {
    init(); 

    this.p    = 0;
    this.x    = x;
    this.y    = y;
    this.name = name;
  }

  private void init() {
    edges = new Edge[4];
  }

  public Direction getDirectionTo(Node to) {
    for (int i=0; i<edges.length; i++) {
      if (edges[i] != null && edges[i].getTo().equals(to)) {
        return Direction.values()[i];
      }
    }

    return null;
  }

  public void addEdgeFromThisTo(Node to, Direction direction) {
    edges[direction.ordinal()] = new Edge(this, to, direction); 
  }

  public void addEdgeFrom(Node from, Direction direction) {
    edges[direction.ordinal()] = new Edge(from, this, direction); 
  }

  public String getName() {
    return name;
  }

  public Edge[] getEdges() {
    return edges;
  }

  public void setPriority(int p) {
    this.p = p;
  }

  public int getPriority() {
    return p;
  }

  // The exact cost to travel to this node
  public int g(Node n) {
    return 1;
  }

  // The estimated cost from this node to the goal, using manhatten distance
  public int h(Node goal) {
    int dx = Math.abs(this.x - goal.x);
    int dy = Math.abs(this.y - goal.y);

    return dx + dy;
  }

  public int f(Node n) {
    return g(n) + h(n);
  }

  public int compareTo(Object o) {
    Node n = (Node) o;

    if (p < n.p) return -1;
    if (p > n.p) return 1;

    return 0;
  }

  public String toString() {
    return name;
  }

}
