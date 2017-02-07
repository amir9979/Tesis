package introclass.median;

public class introclass_317aa705_000 {

    public introclass_317aa705_000() {
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
    	int temp1 = a;
        int temp2 = b;
    	if (a > b) { 
            a = b;
            b = temp1; 
        }
        if (b > c) { 
            b = c;
            c = temp2; 
        }
        if (a > b) { 
            a = b;
            b = temp1; 
        }
        return b; 
    }
	
}
