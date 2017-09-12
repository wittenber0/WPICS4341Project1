/**
 * Created by Ryan on 9/1/2017.
 */
public class Main {
    public static void main(String[] args){
	    Graph graphFromFile = new Graph(args[0]);
	    Searcher searcher = Searcher.getInstance();
	    Node initial = graphFromFile.getStartNode();
	
	    searcher.generalSearch(initial, SearchType.DEPTHFIRST);
	    searcher.generalSearch(initial, SearchType.BREADTHFIRST);
	    searcher.generalSearch(initial, SearchType.DEPTHLIMITED);
	    searcher.generalSearch(initial, SearchType.ITERATIVEDEEPENING);
	    searcher.generalSearch(initial, SearchType.UNIFORMCOST);
	    searcher.generalSearch(initial, SearchType.GREEDY);
	    searcher.generalSearch(initial, SearchType.ASTAR);
	    searcher.generalSearch(initial, SearchType.HILLCLIMBING);
	    searcher.generalSearch(initial, SearchType.BEAM);
    }
}
