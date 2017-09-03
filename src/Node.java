/**
 * Created by Ryan on 9/1/2017.
 */

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by Me on 3/31/2017.
 */
public class Node {
    HashMap adjacents = new HashMap<Node, Double>();
    boolean start;
    boolean goal;
    String name;
    double heuristic;

    public Node(String n){
        this.name = n;
        if(n.equals("S")){
            start = true;
        }else{
            start = false;
        }

        if(this.name.equals("G")){
            goal = true;
            heuristic =0.0;
        }else{
            goal = false;
        }
    }


    public String toString(){
        return this.name;
    }

    public boolean compareTo(Node n1, Node n2){
        if(n1.heuristic<n2.heuristic){
            return true;
        }else{
            return false;
        }
    }
    public boolean equals(Node n){
        if(this.name.equals(n.name)){
            return true;
        }
        return false;
    }
}
