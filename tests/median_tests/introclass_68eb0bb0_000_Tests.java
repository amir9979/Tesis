package median_tests;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import introclass.median.*;

@RunWith(Parameterized.class)
public class introclass_68eb0bb0_000_Tests {

    private static int scope = 15;

    private Integer x, y, z;

    public introclass_68eb0bb0_000_Tests(Integer a, Integer b, Integer c) {
        this.x = a;
        this.y = b;
        this.z = c;
    }

    @Parameterized.Parameters
    public static Collection<Integer[]> values() {
        List<Integer[]> result = new LinkedList<Integer[]>();
        for (int i = -14; i <= scope; i++) {
            for (int j = -14; j <= scope; j++) {
                for (int k = -14; k <= scope; k++) {
                    Integer[] param = { new Integer(i), new Integer(j), new Integer(k) };
                    result.add(param);
                }
            }
        }
        return result;
    }

    @Test
    public void test1() {
    	introclass_68eb0bb0_000 m = new introclass_68eb0bb0_000();
        int result = m.median(x.intValue(), y.intValue(), z.intValue());
        assertTrue((x.intValue() > y.intValue() && y.intValue() > z.intValue() && result == y.intValue()) || (x.intValue() > y.intValue() && y.intValue() <= z.intValue() && x.intValue() > z.intValue() && result == z.intValue()) || (x.intValue() > y.intValue() && y.intValue() <= z.intValue() && x.intValue() <= z.intValue() && result == x.intValue()) || (x.intValue() <= y.intValue() && y.intValue() > z.intValue() && x.intValue() > z.intValue() && result == x.intValue()) || (x.intValue() <= y.intValue() && y.intValue() > z.intValue() && x.intValue() <= z.intValue() && result == z.intValue()) || (x.intValue() <= y.intValue() && y.intValue() <= z.intValue() && result == y.intValue()));
    }
}
