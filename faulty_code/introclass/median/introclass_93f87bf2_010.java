package introclass.median;

public class introclass_93f87bf2_010 {

    public introclass_93f87bf2_010() {
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
    	if (((a < b) && (a > c)) || ((a < b) && (a > c))) { 
            return a;
        } else if ((((b < a)) && (b > c)) || ((b < c) && (b > a))) { 
            return b;
        } else if (((c < a) && (c > b)) || ((c < b) && (c > a))) { 
            return c;
        }
    	return 0; 
    }
	
}
