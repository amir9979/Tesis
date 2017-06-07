package introclass.median;

public class introclass_9c9308d4_007 {

    public introclass_9c9308d4_007() {
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
            } else {
                median = a; 
            }
        } else if (b >= c) { 
            median = c;
        } else {
            median = b; 
        }
        return median;
    }
	
}
