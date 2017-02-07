package introclass.median;

public class introclass_d4aae191_000 {

    public introclass_d4aae191_000() {
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
    	if ((a > b) && (b > c)) { 
            return b;
        }
        if ((a > b) && (a > c)) { 
            if (c > b) { 
                return c;
            }
        }
        if ((b > a) && (a > c)) { 
            return a;
        }
        if ((b > a) && (b > c)) { 
            if (c > a) { 
                return c;
            }
        }
        if ((c > a) && (a > b)) { 
            return a;
        }
        if ((c > a) && (c > b)) { 
            if (b > a) { 
                return b;
            }
        }
        return 0; 
    }
	
}
