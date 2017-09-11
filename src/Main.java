/**
 * Created by Ryan on 9/1/2017.
 */
public class Main {
    public static void main(String[] args){
	    Searcher searcher = new Searcher(new Graph(args[1]));
	    searcher.generalSearch();
    }
}
