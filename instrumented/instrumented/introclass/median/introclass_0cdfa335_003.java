package instrumented.introclass.median;

public class introclass_0cdfa335_003 {

    public introclass_0cdfa335_003() {
        instrumentator.coverage.CoverageTracker.markExecuted(5);
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
    public int median(int a, int b, int c) {
        instrumentator.coverage.CoverageTracker.markExecuted(17);
        int theMedian;
        if (a >= b && a <= c || a >= c && a <= b) {
            instrumentator.coverage.CoverageTracker.markExecuted(19);
            //mutGenLimit 1
            theMedian = a;
        }
        if (b >= a && b <= c || b >= c && b <= a) {
            instrumentator.coverage.CoverageTracker.markExecuted(22);
            //mutGenLimit 1
            theMedian = b;
        } else {
            instrumentator.coverage.CoverageTracker.markExecuted(24);
            //mutGenLimit 1
            theMedian = c;
        }
        //mutGenLimit 1
        return theMedian;
    }
}
