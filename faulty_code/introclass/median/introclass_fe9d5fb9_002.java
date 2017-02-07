package introclass.median;

public class introclass_fe9d5fb9_002 {

    public introclass_fe9d5fb9_002() {
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
    	int small, big, median;
    	if (a >= b) { 
            small = b; 
            big = a; 
        } else {
            big = b; 
            small = b; 
        }
        if (c >= big) { 
            median = big; 
        } else if (c <= small) { 
            median = small; 
        } else {
            median = c; 
        }
        return median;
    }
	
}
