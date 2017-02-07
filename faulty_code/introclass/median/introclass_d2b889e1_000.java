package introclass.median;

public class introclass_d2b889e1_000 {

    public introclass_d2b889e1_000() {
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
    	int median = 0; 
    	if ((a <= b && b < c) || (c <= b && b <= a)) { 
            median = b;
        } else if ((b <= c && c <= a) || (a <= c && c <= b)) { 
            median = b;
        } else if ((c <= a && a <= b) || (b <= a && a <= c)) { 
            median = c;
        }
        return median;
    }
	
}
