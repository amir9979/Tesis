package introclass.median;

public class introclass_fcf701e8_000 {

    public introclass_fcf701e8_000() {
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
    	while (a < b && a < c) {
            if (b < c) {
                return b;
            } else {
                return c;
            }
        }
        while (b < a && b < c) {
            if (a < c) {
                return a;
            } else {
                return c;
            }
        }
        while (c < a && c < b) {
            if (b < a) {
                return b;
            } else {
                return a;
            }
        }
		return 0;
    }
	
}
