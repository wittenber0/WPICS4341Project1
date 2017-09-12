import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Ryan on 9/1/2017.
 */

enum SearchType {
	DEPTHFIRST, DEPTHLIMITED, ITERATIVEDEEPENING, BREADTHFIRST, ASTAR, GREEDY, HILLCLIMBING, BEAM, UNIFORMCOST
}

class Searcher {
	
	private static Searcher instance = null;
	
	private Searcher() {
	}
	
	static Searcher getInstance() {
		if (instance == null) {
			instance = new Searcher();
		}
		return instance;
	}
	
	Path generalSearch(Node initialState, SearchType method) {
		
		printSearchHeader(method);
		
		Path successPath = null;
		
		int depthLimit = -1;
		boolean droppedPath = false;
		
		int beamDepth = 0;
		LinkedList<Path> beamPruningList;
		
		switch(method) {
			case ITERATIVEDEEPENING:
				depthLimit = 0;
				System.out.println("L = 0");
				break;
			case DEPTHLIMITED:
				depthLimit = 2;
				break;
			default:
				break;
		}
		
		LinkedList<Path> queue = new LinkedList<>();
		LinkedList<Node> visited = new LinkedList<>();
		
		switch(method) {
			case BREADTHFIRST:
			case DEPTHFIRST:
			case DEPTHLIMITED:
			case ITERATIVEDEEPENING:
				queue.add(new Path(initialState));
				break;
			case UNIFORMCOST:
				queue.add(new Path(0, initialState));
				break;
			case ASTAR:
			case BEAM:
			case GREEDY:
			case HILLCLIMBING:
				queue.add(new Path(initialState.heuristic, initialState));
		}
		printQueue(queue);
		
		while (true) {
			if (queue.isEmpty()) {
				if (droppedPath && (method == SearchType.ITERATIVEDEEPENING)) {
					depthLimit += 1;
					
					queue = new LinkedList<>();
					visited = new LinkedList<>();
					queue.add(new Path(initialState));
					
					System.out.print("\nL = ");
					System.out.println(depthLimit);
					printQueue(queue);
					continue;
				} else {
					break;
				}
			}
			
			Path pathToExpand = queue.removeFirst();
			Node nodeToExpand = pathToExpand.getNextNode();
			
			if (nodeToExpand.isGoal()) {
				successPath = pathToExpand;
				break;
			}
			
			if (depthLimit >= 0 && pathToExpand.getDepth() >= depthLimit) {
				droppedPath = true;
				printQueue(queue);
				continue;
			}
			
			visited.add(nodeToExpand);
			HashMap<Node, Double> neighborCosts = nodeToExpand.neighbors;
			LinkedList<Path> pathsToAdd = new LinkedList<>();
			
			for (Node neighbor : neighborCosts.keySet()) {
				if (!visited.contains(neighbor)) {
					switch(method) {
						case DEPTHFIRST:
						case DEPTHLIMITED:
						case BREADTHFIRST:
						case ITERATIVEDEEPENING:
							pathsToAdd.add(new Path(pathToExpand, neighbor));
							break;
						case HILLCLIMBING:
						case GREEDY:
						case BEAM:
							pathsToAdd.add(new Path(pathToExpand, neighbor.heuristic, neighbor));
							break;
						case ASTAR:
							pathsToAdd.add(new Path(pathToExpand, pathToExpand.value - nodeToExpand.heuristic + neighborCosts.get(neighbor) + neighbor.heuristic, neighbor));
							break;
						case UNIFORMCOST:
							pathsToAdd.add(new Path(pathToExpand, pathToExpand.value + neighborCosts.get(neighbor), neighbor));
							break;
					}
				}
			}
			
			Collections.sort(pathsToAdd);
			queue = addToQueue(queue, pathsToAdd, method);
			
			if (method == SearchType.BEAM && queue.getFirst().getDepth() > beamDepth) {
				beamDepth += 1;
				
				beamPruningList = new LinkedList<>();
				beamPruningList.addAll(queue);
				Collections.sort(beamPruningList);
				
				beamPruningList.removeFirst();
				beamPruningList.removeFirst();
				
				queue.removeAll(beamPruningList);
			}
			
			printQueue(queue);
		}
		
		if (successPath == null) {
			System.out.println("Goal could not be reached.\n");
		} else {
			System.out.println("Goal reached!\n");
		}
		
		return successPath;
	}
	
	private void printSearchHeader(SearchType method) {
		String message = "";
		switch(method) {
			case ASTAR:
				message = message.concat("A* Search: ");
				break;
			case BEAM:
				message = message.concat("Beam Search (w = 2):");
				break;
			case BREADTHFIRST:
				message = message.concat("Breadth-First Search: ");
				break;
			case DEPTHFIRST:
				message = message.concat("Depth-First Search: ");
				break;
			case DEPTHLIMITED:
				message = message.concat("Depth-Limited Search (l = 2): ");
				break;
			case GREEDY:
				message = message.concat("Greedy Search: ");
				break;
			case HILLCLIMBING:
				message = message.concat("Hill Climbing Search: ");
				break;
			case ITERATIVEDEEPENING:
				message = message.concat("Iterative Deepening Search: ");
				break;
			case UNIFORMCOST:
				message = message.concat("Uniform-Cost Search: ");
		}
		System.out.println(message);
	}
	
	private LinkedList<Path> addToQueue(LinkedList<Path> queue, LinkedList<Path> pathsToAdd, SearchType method) {
		switch(method) {
			case DEPTHFIRST:
			case DEPTHLIMITED:
			case ITERATIVEDEEPENING:
				queue.addAll(0, pathsToAdd);
				break;
			case BREADTHFIRST:
			case BEAM:
				queue.addAll(pathsToAdd);
				break;
			case ASTAR:
			case GREEDY:
			case UNIFORMCOST:
				queue.addAll(pathsToAdd);
				Collections.sort(queue);
				break;
			case HILLCLIMBING:
				queue.add(pathsToAdd.getFirst());
				break;
		}
		return queue;
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



