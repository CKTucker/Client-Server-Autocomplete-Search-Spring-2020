/*
Author: Chris Tucker
CS404
 */
import java.util.Arrays;
import java.util.Comparator;

// An immutable data type that represents an autocomplete term: a query string 
// and an associated real-valued weight.
public class Term implements Comparable<Term> {
    private final String query;
    private final long weight;

    // Construct a term given the associated query string, having weight 0.
    public Term(String query) {
        this(query, 0);
    }

    // Construct a term given the associated query string and weight.
    public Term(String query, long weight) {
        if(query == null)
            throw new NullPointerException("Query string is null");
        if(weight < 0)
            throw new IllegalArgumentException("Query weight must be non-negative");

        this.query = query;
        this.weight = weight;
    }

    // A reverse-weight comparator.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightOrder();
    }

    // Helper reverse-weight comparator.
    private static class ReverseWeightOrder implements Comparator<Term> {
        //Sort descending weight
        public int compare(Term v, Term w) {
            //return (int)(w.weight - v.weight);
            //Your email mentioned there was an issue in he existing implementation so I changed it, I couldn't reproduce
            //any issue with the existing implementation, but I can see where maybe strings with weights larger than an
            //integer may be sorted incorrectly.
            if (w.weight == v.weight) return 0;
            else if (w.weight > v.weight) return 1;
            else return -1;
        }
    }

    // A prefix-order comparator.
    public static Comparator<Term> byPrefixOrder(int r) {
        if(r < 0)
            throw new IllegalArgumentException("Number of prefix characters cannot be negative");
        return new PrefixOrder(r);
    }

    // Helper prefix-order comparator.
    private static class PrefixOrder implements Comparator<Term> {
        private int r;

        PrefixOrder(int r) {
            this.r = r;
        }

        public int compare(Term v, Term w) {
            int aLen = Math.min(r, v.query.length());
            int bLen = Math.min(r, v.query.length());
            return v.query.substring(0, aLen).compareTo(w.query.substring(0, bLen));
        }
    }

    // Compare this term to that in lexicographic order by query and 
    // return a negative, zero, or positive integer based on whether this 
    // term is smaller, equal to, or larger than that term.
    public int compareTo(Term that) {
        return this.query.compareTo(that.query);
    }

    // A string representation of this term.
    public String toString() {
        return weight + "\t" + query;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        int k = Integer.parseInt(args[1]);
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong(); 
            in.readChar(); 
            String query = in.readLine(); 
            terms[i] = new Term(query.trim(), weight); 
        }
        System.out.printf("Top %d by lexicographic order:\n", k);
        Arrays.sort(terms);
        for (int i = 0; i < k; i++) {
            System.out.println(terms[i]);
        }
        System.out.printf("Top %d by reverse-weight order:\n", k);
        Arrays.sort(terms, Term.byReverseWeightOrder());
        for (int i = 0; i < k; i++) {
            System.out.println(terms[i]);
        }
    }
}
