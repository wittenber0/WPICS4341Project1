import java.util.*;

/**
 * Created by Ryan on 9/1/2017.
 */
class Searcher {
	private Graph graph;
	
	Searcher(Graph graph) {
		this.graph = graph;
	}
	
	void aStar() {
		Node start = graph.getStartNode();

        // Set of nodes already evaluated
		List<Node> closedSet = new ArrayList<>();

        // Set of nodes visited, but not evaluated
		List<Node> openSet = new ArrayList<>();
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
	
	        if (current.isGoal()) {
		        // return reconstructPath(cameFrom, current);
		        System.out.println("Goal reached!");
	        }

            openSet.remove(current);
            closedSet.add(current);
	
	        for (Object o : current.neighbors.keySet()) {
		        Node neighbor = (Node) o;
		        if (closedSet.contains(neighbor)) {
			        continue;
		        }
		
		        double tentativeCost = costFromStart.get(current) + current.neighbors.get(neighbor);

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
    }
	
	private Node lowestCostThrough(List<Node> openSet, Map<Node, Double> costThrough) {
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
	
	boolean depthLimited(int limit) {
		Path successPath = null;
		boolean droppedPath = false;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		queue.add(new Path(graph.getStartNode()));
		
		while (!queue.isEmpty()) {
			Path pathToExpand = queue.removeFirst();
			Node nodeToExpand = pathToExpand.getNextNode();
			
			if (nodeToExpand.isGoal()) {
				successPath = pathToExpand;
				break;
			}
			
			if (limit >= 0 && pathToExpand.getDepth() >= limit) {
				droppedPath = true;
				continue;
			}
			
			visited.add(nodeToExpand);
			Set<Node> neighborsToAdd = nodeToExpand.neighbors.keySet();
			
			for (Node neighbor : neighborsToAdd) {
				if (!visited.contains(neighbor)) {
					queue.addFirst(new Path(pathToExpand, neighbor));
				}
			}
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!");
			return true;
		} else {
			return !droppedPath;
		}
	}
	
	void depthFirst() {
		depthLimited(-1);
	}
	
	void iterativeDeepening() {
		int limit = 0;
		
		while (!depthLimited(limit)) {
			limit += 1;
		}
	}
	
	void breadthFirst() {
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		queue.add(new Path(graph.getStartNode()));
		
		while (!queue.isEmpty()) {
			Path pathToExpand = queue.removeFirst();
			Node nodeToExpand = pathToExpand.getNextNode();
			
			if (nodeToExpand.isGoal()) {
				successPath = pathToExpand;
				break;
			}
			
			visited.add(nodeToExpand);
			Set<Node> neighborsToAdd = nodeToExpand.neighbors.keySet();
			
			for (Node neighbor : neighborsToAdd) {
				if (!visited.contains(neighbor)) {
					queue.addLast(new Path(pathToExpand, neighbor));
				}
			}
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!");
		}
	}
}
