package introclass.median;

public class introclass_1bf73a9c_000 {

    public introclass_1bf73a9c_000() {
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
	public int median(int a, int b, int c) {
		int theMedian;
        if ((a > b && a < c) || (a > c && a < b)) {
            theMedian = a;
        } else if ((b > a && b < c) || (b > c && b < a)) {
            theMedian = b;
        } else {
            theMedian = c;
        }
        return theMedian;
	}
	
}
