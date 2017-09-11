import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by Ryan on 9/1/2017.
 */
class Searcher {
	private Graph graph;
	
	Searcher(Graph graph) {
		setGraph(graph);
	}
	
	void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	void generalSearch() {
		depthFirst();
		breadthFirst();
		depthLimited(2);
		iterativeDeepening();
		uniformCost();
		greedy();
		aStar();
		hillClimbing();
		beam(2);
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
			Set<Node> neighbors = nodeToExpand.neighbors.keySet();
			LinkedList<Path> pathsToAdd = new LinkedList<>();
			
			for (Node neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					pathsToAdd.add(new Path(pathToExpand, neighbor));
				}
			}
			Collections.sort(pathsToAdd);
			queue.addAll(0, pathsToAdd);
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
			Set<Node> neighbors = nodeToExpand.neighbors.keySet();
			LinkedList<Path> pathsToAdd = new LinkedList<>();
			
			for (Node neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					pathsToAdd.add(new Path(pathToExpand, neighbor));
				}
			}
			
			Collections.sort(pathsToAdd);
			queue.addAll(pathsToAdd);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!");
		}
	}
	
	void uniformCost() {
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		queue.add(new Path(0, graph.getStartNode()));
		
		while (!queue.isEmpty()) {
			Path pathToExpand = queue.removeFirst();
			Node nodeToExpand = pathToExpand.getNextNode();
			
			if (nodeToExpand.isGoal()) {
				successPath = pathToExpand;
				break;
			}
			
			visited.add(nodeToExpand);
			HashMap<Node, Double> neighbors = nodeToExpand.neighbors;
			
			for (Node neighbor : neighbors.keySet()) {
				if (!visited.contains(neighbor)) {
					queue.add(new Path(pathToExpand, pathToExpand.value + neighbors.get(neighbor), neighbor));
				}
			}
			
			Collections.sort(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!");
		}
	}
	
	void greedy() {
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		Node start = graph.getStartNode();
		queue.add(new Path(start.heuristic, start));
		
		while (!queue.isEmpty()) {
			Path pathToExpand = queue.removeFirst();
			Node nodeToExpand = pathToExpand.getNextNode();
			
			if (nodeToExpand.isGoal()) {
				successPath = pathToExpand;
				break;
			}
			
			visited.add(nodeToExpand);
			Set<Node> neighbors = nodeToExpand.neighbors.keySet();
			
			for (Node neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					queue.add(new Path(pathToExpand, neighbor.heuristic, neighbor));
				}
			}
			
			Collections.sort(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!");
		}
	}
	
	void aStar() {
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		Node start = graph.getStartNode();
		queue.add(new Path(start.heuristic, start));
		
		while (!queue.isEmpty()) {
			Path pathToExpand = queue.removeFirst();
			Node nodeToExpand = pathToExpand.getNextNode();
			
			if (nodeToExpand.isGoal()) {
				successPath = pathToExpand;
				break;
			}
			
			visited.add(nodeToExpand);
			HashMap<Node, Double> neighbors = nodeToExpand.neighbors;
			double oldPathCost = pathToExpand.value - nodeToExpand.heuristic;
			double newPathValue;
			
			for (Node neighbor : neighbors.keySet()) {
				if (!visited.contains(neighbor)) {
					newPathValue = oldPathCost + neighbors.get(neighbor) + neighbor.heuristic;
					queue.add(new Path(pathToExpand, newPathValue, neighbor));
				}
			}
			
			Collections.sort(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!");
		}
	}
	
	void hillClimbing() {
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		Node start = graph.getStartNode();
		queue.add(new Path(start.heuristic, start));
		
		while (!queue.isEmpty()) {
			Path pathToExpand = queue.removeFirst();
			Node nodeToExpand = pathToExpand.getNextNode();
			
			if (nodeToExpand.isGoal()) {
				successPath = pathToExpand;
				break;
			}
			
			visited.add(nodeToExpand);
			Set<Node> neighbors = nodeToExpand.neighbors.keySet();
			LinkedList<Path> children = new LinkedList<>();
			
			for (Node neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					children.add(new Path(pathToExpand, neighbor.heuristic, neighbor));
				}
			}
			
			Collections.sort(children);
			queue.addFirst(children.getFirst());
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!");
		}
	}
	
	void beam(int limit) {
		Path successPath = null;
		int currentDepth = 0;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		LinkedList<Path> pruningList;
		
		Node start = graph.getStartNode();
		queue.add(new Path(start.heuristic, start));
		
		while (!queue.isEmpty()) {
			Path pathToExpand = queue.removeFirst();
			Node nodeToExpand = pathToExpand.getNextNode();
			
			if (nodeToExpand.isGoal()) {
				successPath = pathToExpand;
				break;
			}
			
			visited.add(nodeToExpand);
			Set<Node> neighbors = nodeToExpand.neighbors.keySet();
			
			for (Node neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					queue.add(new Path(pathToExpand, neighbor.heuristic, neighbor));
				}
			}
			
			if (queue.getFirst().getDepth() > currentDepth) {
				currentDepth += 1;
				pruningList = queue;
				Collections.sort(pruningList);
				
				for (int i = 0; i < limit; i++) {
					pruningList.removeFirst();
				}
				
				for (Path pathToPrune : pruningList) {
					queue.remove(pathToPrune);
				}
			}
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!");
		}
	}
}
