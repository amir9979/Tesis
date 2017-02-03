package introclass.median;

public class introclass_3b2376ab_006 {

    public introclass_3b2376ab_006() {
    }
    
    /*@
    @ requires true;
    @ ensures ((\result == a) || (\result == b) || (\result == c));
    @ ensures ((a!=b || a!=c) ==> ( ((a==b) ==> (\result == a)) && ((b==c) ==> (\result ==b))));
    @ ensures ((a!=b && a!=c && b!=c) ==> (\exists int n; (n == a) || (n == b) || (n == c); \result>n));
    @ ensures ((a!=b && a!=c && b!=c) ==> (\exists int n; (n == a) || (n == b) || (n == c); \result<n));
    @ signals (RuntimeException e) false;
    @
    @*/
	public int median(int a, int b, int c) {
		int small;
        if (a <= b) { 
            small = a;
            if (small > c) { 
                return a;
            } else if (c > b) { 
                return b;
            } else {
                return c;
            }
        } else {
            small = b;
            if (small > c) { 
                return b;
            } else if (c > a) { 
                return a;
            } else {
                return c;
            }
        }		
	}
	
}
