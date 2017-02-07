package introclass.median;

public class introclass_af81ffd4_007 {

    public introclass_af81ffd4_007() {
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
    	int median = (a + b + c) / 3;
        int comp_fir = abs (a - median); 
        int comp_sec = abs (b - median); 
        int comp_thi = abs (c - median); 
        if (comp_fir < comp_sec && comp_fir < comp_thi) { 
            return a;
        } else if (comp_sec < comp_fir && comp_sec < comp_thi) { 
            return b;
        } else if (comp_thi < comp_fir && comp_thi < comp_sec) { 
            return c;
        }
        return 0; 
    }
    
    public int abs(int value) {
    	if (value < 0) return -value;
    	return value;
    }
	
}
