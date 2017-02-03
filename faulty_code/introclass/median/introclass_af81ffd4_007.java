package introclass.median;

public class introclass_af81ffd4_007 {

    public introclass_af81ffd4_007() {
    }
    
    /*@
    @ requires true;
    @ ensures ((\result == \old(a)) || (\result == \old(b)) || (\result == \old(c)));
    @ ensures ((\old(a)!=\old(b) || \old(a)!=\old(c)) ==> ( ((\old(a)==\old(b)) ==> (\result == \old(a))) && ((\old(b)==\old(c)) ==> (\result ==\old(b)))));
    @ ensures ((\old(a)!=\old(b) && \old(a)!=\old(c) && \old(b)!=\old(c)) ==> (\exists int n; (n == \old(a)) || (n == \old(b)) || (n == \old(c)); \result>n));
    @ ensures ((\old(a)!=\old(b) && \old(a)!=\old(c) && \old(b)!=\old(c)) ==> (\exists int n; (n == \old(a)) || (n == \old(b)) || (n == \old(c)); \result<n));
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
