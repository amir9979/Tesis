package introclass.median;

public class introclass_9013bd3b_000 {

    public introclass_9013bd3b_000() {
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
    	if ((a > b && a < c) || (a < b && a > c)) { 
            return a;
        }
        if ((b > a && b < c) || (b < a && b > c)) { 
            return b;
        }
        if ((c > a && c < b) || (c < a && c > b)) { 
            return c;
        }
        return 0; 
    }
	
}
