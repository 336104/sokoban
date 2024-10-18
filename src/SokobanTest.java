import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class SokobanTest {
    private SokobanGame sokobanGame;

    @Before
    public void setUp() {
        // 初始化游戏实例
        sokobanGame = new SokobanGame(); // 确保每个测试都有一个新实例
        sokobanGame.iCurLevel = 1; // 或根据你的逻辑初始化

        // 定义地图的大小（例如16x16的地图）
        int mapSize = 16;
        int numberOfMaps = 100;

        // 初始化levels数组，存储100个16x16的地图
        sokobanGame.levels = new int[numberOfMaps][mapSize][mapSize];

        // 读取文件并解析
        try {
            SokobanGame.loadMapsFromFile("maps.txt", numberOfMaps, mapSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 因为输入的关卡数与getLevelFromInput有关，它的内部逻辑是输入的键数要减1传给要测试的selectLevel
     * 所以实际selectLevel传入的参数与实际输入的关卡数是差1的，所以传入的参数应该+1才是实际选择的关卡数目
     */

    @Test
    public void testSelectLevel0() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(-1); // 选择第0关
        Assert.assertEquals(0, sokobanGame.iCurLevel + 1); // 超出范围无效
    }

    @Test
    public void testSelectLevel1() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(0); // 选择第1关
        Assert.assertEquals(1, sokobanGame.iCurLevel + 1); // 预期关卡应该是1
    }

    @Test
    public void testSelectLevel2() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(1); // 选择第2关
        Assert.assertEquals(2, sokobanGame.iCurLevel + 1); // 预期关卡应该是2
    }

    @Test
    public void testSelectLevel3() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(49); // 选择第50关
        Assert.assertEquals(50, sokobanGame.iCurLevel + 1); // 预期关卡应该是50
    }

    @Test
    public void testSelectLevel4() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(98); // 选择第99关
        Assert.assertEquals(99, sokobanGame.iCurLevel + 1); // 预期关卡应该是99
    }

    @Test
    public void testSelectLevel5() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(99); // 选择第100关
        Assert.assertEquals(100, sokobanGame.iCurLevel + 1); // 预期关卡应该是100
    }

    @Test
    public void testSelectLevel6() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(100); // 选择第101关
        Assert.assertEquals(101, sokobanGame.iCurLevel + 1); // 超出范围无效
    }

    @Test
    public void testPlayerMove28() {
        int[][] expectMap = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 3, 4, 3, 2, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 2, 0, 3, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        sokobanGame.selectLevel(0);
        sokobanGame.go("up");
        Assert.assertTrue(compareMap(expectMap, sokobanGame.curMap));
        Assert.assertEquals(1, sokobanGame.moveTimes);
    }

    // example
    @Test
    public void testPlayerMove29() {
        int[][] expectMap = new int[][]{
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
        sokobanGame.selectLevel(0);
        sokobanGame.go("left");
        Assert.assertTrue(compareMap(expectMap, sokobanGame.curMap));
        Assert.assertEquals(1, sokobanGame.moveTimes);
    }

    @Test
    public void testPlayerMove30() {
        int[][] expectMap = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 3, 0, 3, 2, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 2, 0, 3, 4, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        sokobanGame.selectLevel(0);
        sokobanGame.go("right");
        Assert.assertTrue(compareMap(expectMap, sokobanGame.curMap));
        Assert.assertEquals(0, sokobanGame.moveTimes);
    }

    @Test
    public void testSuccess() {
        sokobanGame.selectLevel(0);
        sokobanGame.curMap = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 3, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 3, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 3, 0, 0, 4, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        Assert.assertTrue(sokobanGame.checkFinish());

    }


    @Test
    public void testGameGuide() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        sokobanGame.selectLevel(0);
        sokobanGame.action = 'p';
        sokobanGame.execute();
        System.setOut(originalOut);
        Assert.assertTrue(outputStream.toString().contains("\n\uD83D\uDC4B这是一个推箱子小游戏，游戏目标是将\uD83D\uDCE6推到⭐处。\r\n\u001B[34ma\u001B[0m左移，\u001B[34md\u001B[0m右移，\u001B[34mw\u001B[0m上移，\u001B[34ms\u001B[0m下移，\u001B[34mr\u001B[0m撤销，\u001B[34me\u001B[0m选择关卡，\u001B[34mp\u001B[0m输出本条消息\n"));
    }

    @Test
    public void testReplay() {
        sokobanGame.selectLevel(0);
        sokobanGame.action = 'a';
        sokobanGame.execute();
        sokobanGame.action = 't';
        sokobanGame.execute();
        Assert.assertTrue(compareMap(SokobanGame.levels[sokobanGame.iCurLevel], sokobanGame.curMap));
        Assert.assertEquals(0, sokobanGame.moveTimes);
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
