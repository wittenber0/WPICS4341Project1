/**
 * Created by Ryan on 9/1/2017.
 */
public class Main {
    public static void main(String[] args){
        System.out.println("Helllo");
        Searcher searcher = new Searcher();
        searcher.populateFromFile();
        searcher.aStar();
    }
}
