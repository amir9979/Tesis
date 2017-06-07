package introclass.median;

public class introclass_48b82975_000 {

    public introclass_48b82975_000() {
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
    public int median( int a, int b, int c ) { //Gives unsat because it's an error with the translation
    	if ((a <= b && b <= c) || (c <= b && b <= a)) { 
            return b;
        }
        if ((b <= a && a <= c) || (c <= a && a <= b)) { 
            return a;
        } else {
            return c; 
        }
    }
	
}
