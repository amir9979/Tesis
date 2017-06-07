package introclass.median;

public class introclass_95362737_003 {

    public introclass_95362737_003() {
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
    	if (a == b || a == c || (b < a && a < c) || (c < a && a < b)) { 
            return a;
        } else if (b == c || (a < b && b < c) || (c < b && b < a)) { 
            return b;
        } else if (a < c && c < b) { 
            return c;
        }
    	return 0; 
    }
	
}
