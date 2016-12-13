package instrumented.introclass.median;

public class introclass_30074a0e_000 {

    /*@
    @ requires true;
    @ ensures ((\result == \old(a)) || (\result == \old(b)) || (\result == \old(c)));
    @ ensures ((\old(a)!=\old(b) || \old(a)!=\old(c)) ==> ( ((\old(a)==\old(b)) ==> (\result == \old(a))) && ((\old(b)==\old(c)) ==> (\result ==\old(b)))));
    @ ensures ((\old(a)!=\old(b) && \old(a)!=\old(c) && \old(b)!=\old(c)) ==> (\exists int n; (n == \old(a)) || (n == \old(b)) || (n == \old(c)); \result>n));
    @ ensures ((\old(a)!=\old(b) && \old(a)!=\old(c) && \old(b)!=\old(c)) ==> (\exists int n; (n == \old(a)) || (n == \old(b)) || (n == \old(c)); \result<n));
    @ signals (RuntimeException e) false;
    @
    @*/
    public int median(int a, int b, int c) {
        instrumentator.coverage.CoverageTracker.markExecuted(15);
        if ((a < b && b < c) || (b < a && c < b)) {
            instrumentator.coverage.CoverageTracker.markExecuted(16);
            //mutGenLimit 1
            return b;
        }
        if ((b < a && a < c) || (a < b && c < a)) {
            instrumentator.coverage.CoverageTracker.markExecuted(19);
            //mutGenLimit 1
            return a;
        }
        if ((a < c && c < b) || (b < c && c < a)) {
            instrumentator.coverage.CoverageTracker.markExecuted(22);
            //mutGenLimit 1
            return c;
        }
        //mutGenLimit 1
        return 0;
    }
}
