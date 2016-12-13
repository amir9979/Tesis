package ase2016.introclass.median;

public class introclass_15cb07a7_003 {

    public introclass_15cb07a7_003() {
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
    	int cmp1, cmp2, med;
    	if (a <= b) { //mutGenLimit 1
            cmp1 = a;
        } else {
            cmp1 = b;
        }
        if (b <= c) { //mutGenLimit 1
            cmp2 = b;
        } else {
            cmp2 = c;
        }
        if (cmp1 >= cmp2) { //mutGenLimit 1
            med = cmp1; //mutGenLimit 1
        } else {
            med = cmp2; //mutGenLimit 1
        }
        return med;
    }
	
}
