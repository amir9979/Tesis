package introclass.median;

public class introclass_d009aa71_000 {

    public introclass_d009aa71_000() {
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
    	if ((a < c) && (a > b)) { 
            return a;
        } else if ((a < b) && (a > c)) { 
            return a;
        }
        if ((b < a) && (b > c)) { 
            return b;
        } else if ((b > a) && (b < c)) { 
            return b;
        }
        if ((c > a) && (c < b)) { 
            return c;
        } else if ((c < a) && (c > b)) { 
            return c;
        }
        return 0; 
    }
	
}
