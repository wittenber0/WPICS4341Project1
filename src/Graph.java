import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

class Graph {
	private LinkedList<Node> nodes;
	
	Graph(String filename) {
		nodes = new LinkedList<>();
		
		try {
			Scanner scanner = new Scanner(new File(filename)).useDelimiter("\n");
			boolean pound = false;
			System.out.println("\nLoading Nodes and Edges: ");
			while (scanner.hasNext()) {
				String s = scanner.next();
				if (!pound) {
					if (s.contains("#####")) {
						pound = true;
						System.out.println("\nLoading Heuristics: ");
						
					} else {
						String[] edgeString = s.split(" ");
						
						String firstNodeName = edgeString[0];
						String secondNodeName = edgeString[1];
						Double edgeCost = Double.parseDouble(edgeString[2]);
						
						Node node1 = getNodeByName(firstNodeName);
						Node node2 = getNodeByName(secondNodeName);
						
						if (node1 == null) {
							node1 = new Node(firstNodeName);
							nodes.push(node1);
							System.out.println("Created node " + node1);
						}
						if (node2 == null) {
							node2 = new Node(secondNodeName);
							nodes.push(node2);
							System.out.println("Created node " + node2);
						}
						
						connectNodes(node1, node2, edgeCost);
						System.out.println("Connected node " + node1 + " to node " + node2 + " with cost " + edgeCost);
					}
				} else {
					if (!s.equals("\n")) {
						String[] heuristicString = s.split(" ");
						Node currentNode = getNodeByName(heuristicString[0]);
						if (currentNode != null) {
							currentNode.heuristic = Double.parseDouble(heuristicString[1]);
							System.out.println("Set node " + currentNode + " to have heuristic value " + currentNode.heuristic);
						}
					}
				}
			}
			System.out.println("\n");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void connectNodes(Node n1, Node n2, double cost) {
		if (!n1.neighbors.containsKey(n2) && !n2.neighbors.containsKey(n1)) {
			n1.neighbors.put(n2, cost);
			n2.neighbors.put(n1, cost);
		}
	}
	
	private Node getNodeByName(String name) {
		for (Node node : nodes) {
			if (node.name.equals(name)) {
				return node;
			}
		}
		return null;
	}
	
	Node getStartNode() {
		return getNodeByName("S");
	}
}
