import java.io.File;
import java.util.*;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

/**
 * Created by Ryan on 9/1/2017.
 */
public class Searcher {
    LinkedList<Edge> allEdges = new LinkedList<Edge>();
    LinkedList<Node> allNodes = new LinkedList<Node>();

    public Searcher(){

    }

    public void populateFromFile(){
        try {
            Scanner scanner = new Scanner(new File("C:/Users/Ryan/SearchingAI/graph.txt")).useDelimiter("\n");
            boolean pound = false;
            while (scanner.hasNext()) {
                String s = scanner.next();
                if(!pound) {
                    if(s.equals("#####")){
                        pound = true;
                        System.out.println("Loading Heuristics");

                    }else{
                        String[] edgeString = s.split(" ");
                        Edge e = new Edge(edgeString[0], edgeString[1], Double.parseDouble(edgeString[2]));
                        allEdges.push(e);

                        boolean n1Exists = false;
                        boolean n2Exists = false;
                        for(Node n : allNodes){
                            if(n.name.equals(e.n1)){
                                n1Exists = true;
                            }
                            if(n.name.equals(e.n2)){
                                n2Exists = true;
                            }
                        }

                        Node n1;
                        Node n2;
                        if(!n1Exists){
                            n1 = new Node(e.n1);
                            allNodes.push(n1);
                        }else{
                            n1 = getNodeByName(e.n1);
                        }
                        if(!n2Exists){
                            n2 = new Node(e.n2);
                            allNodes.push(n2);
                        }else{
                            n2 = getNodeByName(e.n2);
                        }

                        connectNodes(n1, n2, e.cost);


                    }
                }else{
                    if(!s.equals("\n")) {
                        String[] nodeH = s.split(" ");
                        Node n = getNodeByName(nodeH[0]);
                        n.heuristic = Double.parseDouble(nodeH[1]);
                        System.out.println(n + " : " + n.heuristic);
                    }
                }
            }

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public Node getNodeByName(String name){
        for (Node n : allNodes){
            if(n.name.equals(name)){
                return n;
            }
        }
        return null;
    }


    public boolean connectNodes(Node n1, Node n2, double cost){
        if(!n1.adjacents.containsKey(n2) && !n2.adjacents.containsKey(n1)){
            n1.adjacents.put(n2, cost);
            n2.adjacents.put(n1, cost);
        }
        return true;
    }

    public LinkedList<Node> aStar(){
        Node start = getNodeByName("S");
        Node goal = getNodeByName("G");

        // Set of nodes already evaluated
        List<Node> closedSet = new ArrayList<Node>();

        // Set of nodes visited, but not evaluated
        List<Node> openSet = new ArrayList<Node>();
        openSet.add(start);


        // Map of node with shortest path leading to it
        HashMap<Node, Node> cameFrom = new HashMap<>();

        // Map of cost of navigating from start to node
        HashMap<Node, Double> costFromStart = new HashMap<>();
        costFromStart.put(start, 0.0);

        // Map of cost of navigating path from start to end through node
        HashMap<Node, Double> costThrough = new HashMap<>();
        costThrough.put(start, start.heuristic);

        while (!openSet.isEmpty()){

            Node current = lowestCostThrough(openSet, costThrough);

            if(current.equals(goal)){
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for(Object o: current.adjacents.keySet()) {
                Node neighbor = (Node) o;
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeCost = costFromStart.get(current) + (Double) current.adjacents.get(neighbor);

                if (!openSet.contains(neighbor)) { // found new neighbor
                    openSet.add(neighbor);
                } else if (tentativeCost >= costFromStart.get(neighbor)) { // not a better path
                    continue;
                }

                cameFrom.put(neighbor, current);
                costFromStart.put(neighbor, tentativeCost);
                costThrough.put(neighbor, tentativeCost + neighbor.heuristic);

            }
        }


        return null;
    }
    private Node lowestCostThrough(List<Node> openSet, Map<Node, Double> costThrough){
        Node lowest = openSet.get(0);

        for(Node n: openSet){
            if(costThrough.get(n) < costThrough.get(lowest)){
                lowest = n;
            }
        }
        return lowest;
    }

    private LinkedList<Node> reconstructPath(HashMap<Node, Node> cameFrom, Node current){
        LinkedList<Node> bestPath = new LinkedList<>();
        bestPath.add(current);

        while(cameFrom.containsKey(current)){
            current = cameFrom.get(current);
            bestPath.add(current);
        }
        System.out.println(bestPath);
        return bestPath;
    }
}
