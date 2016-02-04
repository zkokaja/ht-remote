package com.zk.oppo.remote;

import java.io.*;
import java.net.*;
import java.util.List;
import java.net.URLEncoder;

import com.zk.graph.Graph;
import com.zk.graph.Node;
import com.zk.graph.Direction;

/**
 * A remote allows you to type in a word and it will traverse the keyboard, and
 * selecting each key.
 *
 * This class will build a graph of the keyboard, use the given word to find the
 * shortest path, and send the intermediate keys to the player via a REST call.
 *
 * @author zkokaja
 * @version 1.0
 */
public class OppoRemote {
  
  /**
   * Entry point to the program.
   *
   * Accepts 3 arguments: host, port, and the word.
   */
  public static void main(String[] args) throws Exception {
    
    Graph graph = Graph.buildGraph(getOppoKeyboard());

    if (args.length < 3) {
      System.err.println("Need an argument");
      System.exit(1);
    }
      
    String host = args[0];
    String port = args[1];
    String word = args[2];

    char[] chars = word.toCharArray();

    for (int i=0; i<chars.length-1; i++) {
      String from = "" + chars[i];
      String to   = "" + chars[i+1];

      List<Node> nodes = graph.findPath(from, to);
      for (int j=nodes.size()-1; j>0; j--) {
        Node f = (Node) nodes.get(j);
        Node t = (Node) nodes.get(j-1);

        Direction dir = f.getDirectionTo(t);
        System.out.println(f + "->" + t + " " + dir);

        String key = null;
        switch (dir) {
          case NORTH:
            key = "NUP";
            break;
          case EAST:
            key = "NRT";
            break;
          case SOUTH:
            key = "NDN";
            break;
          case WEST:
            key = "NLT";
            break;
        }

        sendKey(host, port, key);
        Thread.sleep(750);
      }
        
      sendKey(host, port, "SEL");
      System.out.println();
    }

  }

  /**
   * Send the given key to the host via HTTP REST call.
   *
   * TODO: Better exception handling
   *
   * @param host the host to connect to
   * @param port the port to connect to for the host
   * @param key the key to send the host
   */
  public static void sendKey(String host, String port, String key) throws Exception {

    String data = "{\"key\": \"" + key + "\"}";
    String encoded = URLEncoder.encode(data, "UTF-8");

    String path = String.format("http://%s:%s/sendremotekey?%s", host, port, encoded);

    URL url = new URL(path);
    URLConnection conn = url.openConnection();
    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      //System.out.println(inputLine);
    }

    in.close();
  }

  public static String[][] getOppoKeyboard() {

    return new String[][] {
      {"a", "b", "c", "d", "e", "f"},
      {"g", "h", "i", "j", "k", "l"},
      {"m", "n", "o", "p", "q", "r"},
      {"s", "t", "u", "v", "w", "x"},
      {"u", "z", "1", "2", "3", "4"},
      {"5", "6", "7", "8", "9", "0"}
    };
  }

  public static String[][] getQuertyKeyboard() {

    return new String[][] {
      {"~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "="},
      {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]"},
      {"a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'"},
      {"z", "x", "c", "v", "b", "n", "m", ",", ".", "/",}
    };
  }

}
