package introclass.median;

public class introclass_89b1a701_010 {

    public introclass_89b1a701_010() {
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
    	int m = 0; 
    	if (a == b || a == c) { 
            m = a;
        } else if (b == c || b == a) { 
            m = b;
        } else if (c == a || c == b) { 
            m = c;
        } else {
            if ((a >= b && a <= c) || (a >= c && a <= b)) { 
                m = b;
            } else if ((b >= a && b <= c) || (b >= c && b <= a)) { 
                m = b;
            } else if ((c >= a && c <= b) || (c >= b && c <= a)) { 
                m = c;
            }
        }
        return m;
    }
	
}
