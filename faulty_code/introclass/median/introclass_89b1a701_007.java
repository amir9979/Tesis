package introclass.median;

public class introclass_89b1a701_007 {

    public introclass_89b1a701_007() {
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
    	int m = 0; 
    	if ((a >= b && a <= c) || (a >= c && a <= b)) { 
            m = b;
        } else if ((b >= a && b <= c) || (b >= c && b <= a)) { 
            m = b;
        } else if ((c >= a && c <= b) || (c >= b && c <= a)) { 
            m = c;
        }
        return m; 
    }
	
}
