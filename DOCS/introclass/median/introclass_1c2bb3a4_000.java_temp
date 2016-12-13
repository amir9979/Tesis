package ase2016.introclass.median;

public class introclass_1c2bb3a4_000 {

    public introclass_1c2bb3a4_000() {
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
        if (a == b) { //mutGenLimit 1
        	return a; //mutGenLimit 1
        }
        if (a == c) { //mutGenLimit 1
            return a; //mutGenLimit 1
        }
        if (b == c) { //mutGenLimit 1
            return b; //mutGenLimit 1
        }
        return 0; //mutGenLimit 1
	}
	
}
