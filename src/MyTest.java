import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;


public class MyTest {
    @Test
    public void test() {
    }

    @Test
    public void tes1() {
        int[][] a = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] b = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Assert.assertTrue(Arrays.deepEquals(a, b));
    }
}
//todo:读取w ,位置bei'yi
