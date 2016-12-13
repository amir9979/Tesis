package median_examples;

public class Median_3 {

    int num1, num2, num3, median;

    public int getMedian(int num1, int num2, int num3) {
        if ((num1 < num2 && num1 > num3) || (num1 > num2 && num1 < num3)) {
            median = num1;
        } else if ((num2 < num1 && num2 > num3) || (num2 > num1 && num2 < num3)) {
            median = num2;
        } else {
            median = num3;
        }
        return (0);
    }
}
