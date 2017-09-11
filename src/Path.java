import java.util.LinkedList;

public class Path implements Comparable<Path> {
	private boolean informed;
	double value;
	private LinkedList<Node> trace;
	
	Path(Node firstNode) {
		this.informed = false;
		this.value = 0;
		this.trace = new LinkedList<>();
		addNode(firstNode);
	}
	
	Path(double value, Node firstNode) {
		this.informed = true;
		this.value = value;
		this.trace = new LinkedList<>();
		addNode(firstNode);
	}
	
	Path(Path previous, Node newNode) {
		this.informed = previous.informed;
		this.value = 0;
		this.trace = previous.trace;
		addNode(newNode);
	}
	
	Path(Path previous, double value, Node newNode) {
		this.informed = previous.informed;
		this.value = value;
		this.trace = previous.trace;
		addNode(newNode);
	}
	
	private void addNode(Node newNode) {
		trace.addFirst(newNode);
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
