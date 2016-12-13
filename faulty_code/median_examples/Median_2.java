package median_examples;

public class Median_2 {

    int num1, num2, num3;

    public int getMedian(int num1, int num2, int num3) {
        instrumentator.coverage.CoverageTracker.markExecuted(7);
        if ((((num1 > num2) && (num1 < num3))) || ((num1 > num3) && (num1 < num2))) {
            instrumentator.coverage.CoverageTracker.markExecuted(9);
            return num1;
        } else if ((((num2 > num1) && (num2 < num3))) || ((num2 > num3) && (num2 < num1))) {
            instrumentator.coverage.CoverageTracker.markExecuted(15);
            return num2;
        } else if ((((num3 > num2) && (num3 < num1))) || ((num3 > num1) && (num3 < num2))) {
            instrumentator.coverage.CoverageTracker.markExecuted(19);
            return num3;
        }
        return 0;
    }
}
