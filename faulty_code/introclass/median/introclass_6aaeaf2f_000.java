package introclass.median;

public class introclass_6aaeaf2f_000 {

    public introclass_6aaeaf2f_000() {
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
    	int median;
    	if (((a > b) && (a < c)) || ((a < b) && (a > c))) { 
            median = a;
        } else if (((b > a) && (c > b)) || ((b < a) && (b > c))) { 
            median = b;
        } else {
            median = c; 
        }
        return median; 
    }
	
}
