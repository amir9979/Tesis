package introclass.median;

public class introclass_317aa705_003 {

    public introclass_317aa705_003() {
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
    	if ((b > a && b < c) || (b < a && b > c)) { 
            return b; 
        }
        if ((c > a && c < b) || (c < a && c > b)) { 
            return c; 
        }
        if ((a > b && a < c) || (a < b && a > c)) { 
            return a; 
        }
        return 0; 
    }
	
}
