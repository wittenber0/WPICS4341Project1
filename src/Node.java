import java.util.HashMap;

/**
 * Created by Me on 3/31/2017.
 */
public class Node {
	String name;
	double heuristic;
	HashMap<Node, Double> neighbors = new HashMap<>();
	
	Node(String name) {
		this.name = name;
		this.heuristic = 0;
		neighbors = new HashMap<>();
	}

    public String toString(){
        return this.name;
    }
	
	public boolean hasBetterHeuristicThan(Node other) {
		return this.heuristic < other.heuristic;
	}
	
	public boolean equals(Node other) {
		return this.name.equals(other.name);
	}
	
	boolean isGoal() {
		return name.equals("G");
	}
}
