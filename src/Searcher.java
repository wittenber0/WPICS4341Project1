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

    public LinkedList<Node> breadthFirstSearch(){
        System.out.println("\nStarting Breadth First Search");
        LinkedList<LinkedList<Node>> que = new LinkedList<LinkedList<Node>>();
        LinkedList<Node> visited = new LinkedList<Node>();


        Node start = getNodeByName("S");
        Node goal = getNodeByName("G");

        LinkedList<Node> startQ = new LinkedList<Node>();
        startQ.add(start);
        que.add(startQ);
        while(!que.isEmpty()){
            LinkedList<Node> current = que.getFirst();
            if(!visited.contains(current)) {
                System.out.println(current.getFirst() + "  " + que);
                visited.add(current.getFirst());
                if (current.getFirst() == goal) {
                    System.out.println(current);
                    return current;
                }
                if (current.getFirst().adjacents.keySet().size() > 0) {
                    que.remove(current);
                    for (Object o : current.getFirst().adjacents.keySet()) {
                        Node n = (Node) o;
                        if(!visited.contains(n)) {
                            LinkedList<Node> nextQ = new LinkedList<Node>();
                            nextQ.add(n);
                            nextQ.addAll(current);
                            que.add(nextQ);
                        }
                    }
                }
            }else{
                que.remove(current);
            }

        }

        return null;
    }


    public LinkedList<Node> depthFirstSearch(){
        System.out.println("\nStarting Depth First Search");
        LinkedList<Node> path = new LinkedList<>();
        LinkedList<LinkedList<Node>> que = new LinkedList<LinkedList<Node>>();
        LinkedList<Node> visited = new LinkedList<Node>();


        Node start = getNodeByName("S");
        Node goal = getNodeByName("G");

        LinkedList<Node> startQ = new LinkedList<Node>();
        startQ.add(start);
        que.add(startQ);
        while(!que.isEmpty()){
            LinkedList<Node> current = que.getFirst();
            if(!visited.contains(current)) {
                System.out.println(current.getFirst() + "  " + que);
                visited.add(current.getFirst());
                if (current.getFirst() == goal) {
                    System.out.println(current);
                    return current;
                }
                if (current.getFirst().adjacents.keySet().size() > 0) {
                    que.remove(current);
                    for (Object o : current.getFirst().adjacents.keySet()) {
                        Node n = (Node) o;
                        if(!visited.contains(n)) {
                            LinkedList<Node> nextQ = new LinkedList<Node>();
                            nextQ.add(n);
                            nextQ.addAll(current);
                            que.add(0, nextQ);
                        }
                    }
                }
            }else{
                que.remove(current);
            }

        }

        return null;
    }

    public LinkedList<Node> iterativeDeepeningSearch(){
        System.out.println("\nStarting Iterative Deepening Search");
        boolean goalFound = false;
        Node start = getNodeByName("S");
        Node goal = getNodeByName("G");

        int depth = 1;
        while(!goalFound) {

            LinkedList<LinkedList<Node>> que = new LinkedList<LinkedList<Node>>();
            LinkedList<Node> visited = new LinkedList<Node>();
            LinkedList<Node> startQ = new LinkedList<Node>();
            startQ.add(start);
            que.add(startQ);

            while (!que.isEmpty()) {
                LinkedList<Node> current = que.getFirst();
                System.out.println(current.getFirst() + "  " + que);
                if (!visited.contains(current)) {
                    visited.add(current.getFirst());
                    if (current.getFirst() == goal) {
                        goalFound = true;
                        System.out.println(current);
                        return current;
                    }
                    if (current.getFirst().adjacents.keySet().size() > 0) {
                        que.remove(current);
                        for (Object o : current.getFirst().adjacents.keySet()) {
                            Node n = (Node) o;
                            if (!visited.contains(n) && current.size()<depth) {
                                LinkedList<Node> nextQ = new LinkedList<Node>();
                                nextQ.add(n);
                                nextQ.addAll(current);
                                que.add(0, nextQ);
                            }
                        }
                    }
                } else {
                    que.remove(current);
                }

            }

            depth++;
            System.out.println("Depth:" +depth);
        }

        return null;
    }
}
