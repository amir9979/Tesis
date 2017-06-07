package introclass.median;

public class introclass_90a14c1a_000 {

    public introclass_90a14c1a_000() {
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
    	if ((a > b) && (a > c) && (b > c)) { 
            return b;
        } else if ((a > b) && (a > c) && (c > b)) { 
            return c;
        } else if ((b > a) && (b > c) && (c > a)) { 
            return c;
        } else if ((b > a) && (b > c) && (a > c)) { 
            return a;
        } else if ((c > a) && (c > b) && (a > b)) { 
            return a;
        } else if ((c > a) && (c > b) && (b > a)) { 
            return b;
        }
    	return 0; 
    }
	
}
