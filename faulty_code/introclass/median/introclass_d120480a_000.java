package introclass.median;

public class introclass_d120480a_000 {

    public introclass_d120480a_000() {
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
    	int median, temp;
    	if (a >= b) { 
            temp = b; 
            b = a; 
            a = temp; 
        }
        if (a < c) { 
            median = b; 
        } else if (b > c) { 
            median = a; 
        } else {
            median = c; 
        }
        return median;
    }
	
}
