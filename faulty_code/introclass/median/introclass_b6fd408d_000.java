package introclass.median;

public class introclass_b6fd408d_000 {

    public introclass_b6fd408d_000() {
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
    	int temp;
    	if (b < a) { 
            temp = b; 
            b = a; 
            a = temp; 
        }
        if ((c < b) && (c > a)) { 
            temp = b; 
            b = c; 
            c = temp; 
        }
        return b; 
    }
	
}
