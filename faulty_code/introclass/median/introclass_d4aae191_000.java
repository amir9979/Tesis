package introclass.median;

public class introclass_d4aae191_000 {

    public introclass_d4aae191_000() {
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
    	if ((a > b) && (b > c)) { 
            return b;
        }
        if ((a > b) && (a > c)) { 
            if (c > b) { 
                return c;
            }
        }
        if ((b > a) && (a > c)) { 
            return a;
        }
        if ((b > a) && (b > c)) { 
            if (c > a) { 
                return c;
            }
        }
        if ((c > a) && (a > b)) { 
            return a;
        }
        if ((c > a) && (c > b)) { 
            if (b > a) { 
                return b;
            }
        }
        return 0; 
    }
	
}
