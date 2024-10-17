import org.junit.Assert;
import org.junit.Test;

public class SokobanTest {
    // example
    @Test
    public void testMoveLeft() {
        int [][]expectMap=new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 3, 0, 3, 2, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 2, 3, 4, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        SokobanGame sokobanGame = new SokobanGame();
        sokobanGame.selectLevel(0);
        sokobanGame.go("left");
        Assert.assertTrue(compareMap(expectMap, sokobanGame.curMap));
        Assert.assertEquals(1,sokobanGame.moveTimes);
    }

    public static boolean compareMap(int[][] map1, int[][] map2) {
        if (map1.length != map2.length || map1[0].length != map2[0].length) {
            return false;
        }
        for (int i = 0; i < map1.length; i++) {
            for (int j = 0; j < map1[i].length; j++) {
                if (map1[i][j] != map2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
