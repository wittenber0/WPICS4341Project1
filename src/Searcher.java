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
		System.out.println("Running General Search: \n");
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
	
	private boolean depthFirstHelper(int limit) {
		Path successPath = null;
		boolean droppedPath = false;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		queue.add(new Path(graph.getStartNode()));
		printQueue(queue);
		
		while (!queue.isEmpty()) {
			Path pathToExpand = queue.removeFirst();
			Node nodeToExpand = pathToExpand.getNextNode();
			
			if (nodeToExpand.isGoal()) {
				successPath = pathToExpand;
				break;
			}
			
			if (limit >= 0 && pathToExpand.getDepth() >= limit) {
				droppedPath = true;
				printQueue(queue);
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
			printQueue(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!\n");
			return true;
		} else {
			System.out.println("Goal not reached.\n");
			return !droppedPath;
		}
	}
	
	void depthFirst() {
		System.out.println("Depth-First Search");
		depthFirstHelper(-1);
	}
	
	void depthLimited(int limit) {
		System.out.print("Depth-Limited Search (l = ");
		System.out.print(limit);
		System.out.print(")\n");
		depthFirstHelper(2);
	}
	
	void iterativeDeepening() {
		int limit = 0;
		String limitMessage = "L = ";
		
		System.out.println("Iterative Deepening Search");
		System.out.print(limitMessage);
		System.out.println(limit);
		
		while (!depthFirstHelper(limit)) {
			limit += 1;
			System.out.print(limitMessage);
			System.out.println(limit);
		}
	}
	
	void breadthFirst() {
		System.out.println("Breadth-First Search");
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		queue.add(new Path(graph.getStartNode()));
		printQueue(queue);
		
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
			printQueue(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!\n");
		} else {
			System.out.println("Goal not reached.\n");
		}
	}
	
	void uniformCost() {
		System.out.println("Uniform Cost Search");
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		queue.add(new Path(0, graph.getStartNode()));
		printQueue(queue);
		
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
			printQueue(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!\n");
		} else {
			System.out.println("Goal not reached.\n");
		}
	}
	
	void greedy() {
		System.out.println("Greedy Search");
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		Node start = graph.getStartNode();
		queue.add(new Path(start.heuristic, start));
		printQueue(queue);
		
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
			printQueue(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!\n");
		} else {
			System.out.println("Goal not reached.\n");
		}
	}
	
	void aStar() {
		System.out.println("A* Search");
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		Node start = graph.getStartNode();
		queue.add(new Path(start.heuristic, start));
		printQueue(queue);
		
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
			printQueue(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!\n");
		} else {
			System.out.println("Goal not reached.\n");
		}
	}
	
	void hillClimbing() {
		System.out.println("Hill Climbing Search");
		Path successPath = null;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		Node start = graph.getStartNode();
		queue.add(new Path(start.heuristic, start));
		printQueue(queue);
		
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
			if (!children.isEmpty()) {
				Collections.sort(children);
				queue.addFirst(children.getFirst());
			}
			printQueue(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!\n");
		} else {
			System.out.println("Goal not reached.\n");
		}
	}
	
	void beam(int limit) {
		System.out.print("Beam Search (w = ");
		System.out.print(limit);
		System.out.print(")\n");
		
		Path successPath = null;
		int currentDepth = 0;
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		LinkedList<Path> pruningList;
		
		Node start = graph.getStartNode();
		queue.add(new Path(start.heuristic, start));
		printQueue(queue);
		
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
				
				if (queue.size() > limit) {
					pruningList = new LinkedList<>();
					pruningList.addAll(queue);
					Collections.sort(pruningList);
					
					for (int i = 0; i < limit; i++) {
						pruningList.removeFirst();
					}
					
					for (Path pathToPrune : pruningList) {
						queue.remove(pathToPrune);
					}
				}
			}
			printQueue(queue);
		}
		
		if (successPath != null) {
			System.out.println("Goal reached!\n");
		} else {
			System.out.println("Goal not reached.\n");
		}
	}
	
	private void printQueue(LinkedList<Path> queue) {
		if (!queue.isEmpty()) {
			String message = "\t\t\t";
			message = message.concat(queue.getFirst().getNextNode().toString());
			
			message = message.concat("\t [");
			message = message.concat(queue.getFirst().toString());
			for (int index = 1; index < queue.size(); index++) {
				message = message.concat(", ");
				message = message.concat(queue.get(index).toString());
			}
			message = message.concat("]");
			
			System.out.println(message);
		}
	}
}
