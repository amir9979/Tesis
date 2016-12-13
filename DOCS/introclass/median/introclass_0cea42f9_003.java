package ase2016.introclass.median;

public class introclass_0cea42f9_003 {

    public introclass_0cea42f9_003() {
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
        if (((a > b) && (a < c)) || ((a < b) && (a > c))) { //mutGenLimit 1
        	return a;
        } else if (((b > a) && (b < c)) || ((b < a) && (b > c))) { //mutGenLimit 1
        	return b;
        } else if (((c > a) && (c < b)) || ((c < a) && (c > b))) { //mutGenLimit 1
        	return c;
        }
        return 0;
	}
	
}
