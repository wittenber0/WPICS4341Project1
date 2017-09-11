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
	
	boolean isGoal() {
		return name.equals("G");
	}
}
