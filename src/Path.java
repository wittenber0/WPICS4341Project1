import java.util.LinkedList;

public class Path implements Comparable<Path> {
	private boolean informed;
	double value;
	private LinkedList<Node> trace = new LinkedList<Node>();
	
	Path(Node firstNode) {
		informed = false;
		value = 0;
		trace = new LinkedList<>();
		trace.push(firstNode);
	}
	
	Path(double value, Node firstNode) {
		informed = true;
		this.value = value;
		trace = new LinkedList<>();
		trace.push(firstNode);
	}
	
	Path(Path previous, Node newNode) {
		this.informed = previous.informed;
		value = 0;
		trace = new LinkedList<>();
		trace.addAll(previous.trace);
		trace.push(newNode);
	}
	
	Path(Path previous, double value, Node newNode) {
		this.informed = previous.informed;
		this.value = value;
		trace = new LinkedList<>();
		trace.addAll(previous.trace);
		trace.push(newNode);
	}
	
	Node getNextNode() {
		return trace.getFirst();
	}
	
	int getDepth() {
		return trace.size() - 1;
	}
	
	public String toString() {
		String result = "";
		
		if (informed) {
			result = result.concat(Double.toString(value));
		}
		
		result = result.concat("<");
		
		result = result.concat(trace.getFirst().toString());
		
		for (int index = 1; index < trace.size(); index += 1) {
			result = result.concat(", ");
			result = result.concat(trace.get(index).toString());
		}
		
		result = result.concat(">");
		
		return result;
	}
	
	public int compareTo(Path other) {
		if (value < other.value) {
			return -1;
		} else if (value > other.value) {
			return 1;
		} else {
			return getNextNode().name.compareTo(other.getNextNode().name);
		}
	}
}
