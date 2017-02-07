package introclass.median;

public class introclass_aaceaf4a_003 {

    public introclass_aaceaf4a_003() {
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
    	if ((a > b) && (a < c)) { 
            return a;
        }
        if ((a > c) && (a < b)) { 
            return a;
        }
        if ((b > a) && (b < c)) { 
            return b;
        }
        if ((b > c) && (b < a)) { 
            return b;
        }
        if ((c > b) && (c < a)) { 
            return c;
        }
        if ((c > a) && (c < b)) { 
            return c;
        }
        return 0; 
    }
	
}
