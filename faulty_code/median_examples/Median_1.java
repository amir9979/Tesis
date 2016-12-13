package median_examples;

import korat.finitization.IFinitization;
import korat.finitization.IIntSet;
import korat.finitization.impl.FinitizationFactory;

public class Median_1 {

    int a, b, c, median;

    public int getMedian(int a, int b, int c) {
        instrumentator.coverage.CoverageTracker.markExecuted(11);
        if ((a >= b && a <= c) || (a >= c && a <= b)) {
            instrumentator.coverage.CoverageTracker.markExecuted(12);
            median = a;
        }
        if ((b >= a && b <= c) || (b >= c && b <= a)) {
            instrumentator.coverage.CoverageTracker.markExecuted(15);
            median = b;
        } else {
            instrumentator.coverage.CoverageTracker.markExecuted(17);
            median = c;
        }
        return median;
    }

    public boolean repOk() {
        instrumentator.coverage.CoverageTracker.markExecuted(23);
        if (a < 0)
            return false;
        if (b < 0)
            return false;
        if (c < 0)
            return false;
        if (median < 0)
            return false;
        return true;
    }

    public static IFinitization finMedian(int maxValue) {
        instrumentator.coverage.CoverageTracker.markExecuted(35);
        IFinitization f = FinitizationFactory.create(Median_1.class);
        IIntSet a = f.createIntSet(0, maxValue);
        IIntSet b = f.createIntSet(0, maxValue);
        IIntSet c = f.createIntSet(0, maxValue);
        f.set("a", a);
        f.set("b", b);
        f.set("c", c);
        return f;
    }
}
