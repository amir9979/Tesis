package introclass.median;

public class introclass_9c9308d4_012 {

    public introclass_9c9308d4_012() {
    }
    
    /*@
    @ requires true;
    @ ensures ((\result == a) || (\result == b) || (\result == c));
    @ ensures ( (a == b) ==> ((\result == a) || (\result == b) ) );
    @ ensures ( (b == c) ==> ((\result == b) || (\result == c) ) );
    @ ensures ( (a == c) ==> ((\result == a) || (\result == c) ) );
    @ ensures ((a!=b && a!=c && b!=c) ==> (\exists int n; (n == a) || (n == b) || (n == c); \result>n));
    @ ensures ((a!=b && a!=c && b!=c) ==> (\exists int n; (n == a) || (n == b) || (n == c); \result<n));
    @ signals (RuntimeException e) false;
    @
    @*/
    public int median( int a, int b, int c ) {
    	int median;
    	if (a >= b || a >= c) { 
            if (b >= c && a >= b) { 
                median = b;
            } else if (b >= a) { 
                median = a;
            } else {
                median = c; 
            }
        } else if (b >= c) { 
            median = c;
        } else {
            median = b; 
        }
        return median;
    }
	
}
