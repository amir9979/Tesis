package introclass.smallest;

public class introclass_af81ffd4_000 {
	
	public introclass_af81ffd4_000() {
	}

    /*@
    @ requires true;
    @ ensures ((\result == \old(a)) || (\result == \old(b)) || (\result == \old(c)) || (\result == \old(d)) );
    @ ensures ((\result <= \old(a)) && (\result <= \old(b)) && (\result <= \old(c)) && (\result <= \old(d)) );
    @ signals (RuntimeException e) false;
    @
    @*/
	public int smallest(int a, int b, int c, int d) {
		int m = 0;
		int p = 0;
		int n = 0;
		if (a > b) { //mutGenLimit 1
            m = b;
        } else if (a < b) {
            m = a;
        }
        if (m > c) { //mutGenLimit 1
            n = c;
        } else if (m < c) {
            n = m;
        }
        if (n > d) { //mutGenLimit 1
            p = d;
        } else if (n < d) {
            p = n;
        }
        return p;
	}
	
}
