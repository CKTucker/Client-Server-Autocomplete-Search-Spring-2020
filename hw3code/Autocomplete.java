/*
Author: Chris Tucker
CS404
 */
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.io.IOException;

// A data type that provides autocomplete functionality for a given set of 
// string and weights, using Term and BinarySearchDeluxe. 
public class Autocomplete {
    private Term[] terms;

    // Initialize the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        //This acts as a defensive deep copy as Term is immutable
        this.terms = Arrays.copyOf(terms, terms.length);

        /*
            The implementation of this function is a type of mergesort, which has a worst case time complexity of
            O(nlg(n)), so this fits the requirement.
            https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Arrays.html#sort(java.lang.Object%5B%5D)
         */
        Arrays.sort(this.terms);
    }

    // All terms that start with the given prefix, in descending order of
    // weight.
    public Term[] allMatches(String prefix) {
        Term key = new Term(prefix);
        int first = BinarySearchDeluxe.firstIndexOf(this.terms, key, Term.byPrefixOrder(prefix.length()));
        if(first == -1) return null;    //If there is no instance of key, return without computing last
        //Last does not need to be checked as if first is not -1, there must be at least one instance of key
        int last = BinarySearchDeluxe.lastIndexOf(this.terms, key, Term.byPrefixOrder(prefix.length()));

        Term[] matches = Arrays.copyOfRange(this.terms, first, last+1);
        Arrays.sort(matches, Term.byReverseWeightOrder());
        return matches;
    }

    // The number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        Term key = new Term(prefix);
        int first = BinarySearchDeluxe.firstIndexOf(this.terms, key, Term.byPrefixOrder(prefix.length()));
        if(first == -1) return 0;   //No instance of key, return without computing last
        int last = BinarySearchDeluxe.lastIndexOf(this.terms, key, Term.byPrefixOrder(prefix.length()));
        return last - first + 1;
    }

    // Entry point. [DO NOT EDIT]
    public static void main(String[] args) throws IOException{
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong(); 
            in.readChar(); 
            String query = in.readLine(); 
            terms[i] = new Term(query.trim(), weight); 
        }
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            for (int i = 0; i < Math.min(k, results.length); i++) {
                System.out.println(results[i]);
            }
        }
    }
}
