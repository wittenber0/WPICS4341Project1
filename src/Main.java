/**
 * Created by Ryan on 9/1/2017.
 */
public class Main {
    public static void main(String[] args){
	    Graph graphFromFile = new Graph(args[0]);
	    Searcher searcher = Searcher.getInstance();
	    Node initial = graphFromFile.getStartNode();
	
	    searcher.General_Search(initial, SearchType.DEPTHFIRST);
	    searcher.General_Search(initial, SearchType.BREADTHFIRST);
	    searcher.General_Search(initial, SearchType.DEPTHLIMITED);
	    searcher.General_Search(initial, SearchType.ITERATIVEDEEPENING);
	    searcher.General_Search(initial, SearchType.UNIFORMCOST);
	    searcher.General_Search(initial, SearchType.GREEDY);
	    searcher.General_Search(initial, SearchType.ASTAR);
	    searcher.General_Search(initial, SearchType.HILLCLIMBING);
	    searcher.General_Search(initial, SearchType.BEAM);
    }
}
