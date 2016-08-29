package instrumentator;

import java.util.LinkedList;
import java.util.List;

public class Hello {

    private static LinkedList<Integer> list = new LinkedList<Integer>();

    public static void main(String[] args) {
        instrumentator.coverage.CoverageTracker.markExecuted("./src/instrumentator/Hello.java", 10);
        setList(new LinkedList<>());
        for (int i = 1; i <= 10; i++) {
            instrumentator.coverage.CoverageTracker.markExecuted("./src/instrumentator/Hello.java", 12);
            if (list.size() >= 0) {
                instrumentator.coverage.CoverageTracker.markExecuted("./src/instrumentator/Hello.java", 13);
                list.add(1);
                list.add(3);
            } else {
                instrumentator.coverage.CoverageTracker.markExecuted("./src/instrumentator/Hello.java", 16);
                list.add(5);
            }
            list.add(1);
            list.add(2);
        }
    }

    public static List<Integer> getList() {
        instrumentator.coverage.CoverageTracker.markExecuted("./src/instrumentator/Hello.java", 24);
        return list;
    }

    public static void setList(LinkedList<Integer> newlist) {
        instrumentator.coverage.CoverageTracker.markExecuted("./src/instrumentator/Hello.java", 28);
        list = newlist;
    }
}
