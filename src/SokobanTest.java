import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.util.Scanner;
import java.util.stream.IntStream;

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
    public void testSelectLevel1() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(-1); // 选择第0关
        Assert.assertEquals(0, sokobanGame.iCurLevel + 1); // 超出范围无效
    }

    @Test
    public void testSelectLevel2() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(0); // 选择第1关
        Assert.assertEquals(1, sokobanGame.iCurLevel + 1); // 预期关卡应该是1
    }

    @Test
    public void testSelectLevel3() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(1); // 选择第2关
        Assert.assertEquals(2, sokobanGame.iCurLevel + 1); // 预期关卡应该是2
    }

    @Test
    public void testSelectLevel4() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(49); // 选择第50关
        Assert.assertEquals(50, sokobanGame.iCurLevel + 1); // 预期关卡应该是50
    }

    @Test
    public void testSelectLevel5() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(98); // 选择第99关
        Assert.assertEquals(99, sokobanGame.iCurLevel + 1); // 预期关卡应该是99
    }

    @Test
    public void testSelectLevel6() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(99); // 选择第100关
        Assert.assertEquals(100, sokobanGame.iCurLevel + 1); // 预期关卡应该是100
    }

    @Test
    public void testSelectLevel7() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(100); // 选择第101关
        Assert.assertEquals(101, sokobanGame.iCurLevel + 1); // 超出范围无效
    }

    @Test
    public void testSelectLevel8() {
        // 测试选择有效关卡
        sokobanGame.selectLevel((int) 1.5); // 选择第2.5关,注意因为如果有小数点的话，这个处理部分应该是在输入读取的时候处理，再传参数给select函数
        Assert.assertEquals(2, sokobanGame.iCurLevel + 1); //预期关卡是2
    }

    @Test
    public void testGetLevelFromInput1() {
        // 模拟用户输入的第1关卡
        sokobanGame.scanner = new Scanner("1\n");

        // 调用 getLevelFromInput 方法
        sokobanGame.getLevelFromInput();

        // 验证选择的关卡是否正确
        Assert.assertEquals(0, sokobanGame.iCurLevel);
    }

    @Test
    public void testGetLevelFromInput2() {
        // 模拟用户输入的第2关卡
        sokobanGame.scanner = new Scanner("2\n");

        // 调用 getLevelFromInput 方法
        sokobanGame.getLevelFromInput();

        // 验证选择的关卡是否正确
        Assert.assertEquals(1, sokobanGame.iCurLevel);
    }

    @Test
    public void testGetLevelFromInput3() {
        // 模拟用户输入的第-1关卡
        sokobanGame.scanner = new Scanner("-1\n1\n");

        // 调用 getLevelFromInput 方法
        sokobanGame.getLevelFromInput();

        // 验证选择的关卡是否正确
        Assert.assertEquals(0, sokobanGame.iCurLevel);
    }

    @Test
    public void testGetLevelFromInput4() {
        // 模拟用户输入的第50关卡
        sokobanGame.scanner = new Scanner("50\n");

        // 调用 getLevelFromInput 方法
        sokobanGame.getLevelFromInput();

        // 验证选择的关卡是否正确
        Assert.assertEquals(49, sokobanGame.iCurLevel);
    }

    @Test
    public void testGetLevelFromInput5() {
        // 模拟用户输入的第100关卡
        sokobanGame.scanner = new Scanner("100\n");

        // 调用 getLevelFromInput 方法
        sokobanGame.getLevelFromInput();

        // 验证选择的关卡是否正确
        Assert.assertEquals(99, sokobanGame.iCurLevel);
    }

    @Test
    public void testGetLevelFromInput6() {
        // 模拟用户输入的第101关卡
        sokobanGame.scanner = new Scanner("101\n100\n");

        // 调用 getLevelFromInput 方法
        sokobanGame.getLevelFromInput();

        // 验证选择的关卡是否正确
        Assert.assertEquals(99, sokobanGame.iCurLevel);
    }

    @Test
    public void testGetLevelFromInput7() {
        // 模拟用户输入的第99关卡
        sokobanGame.scanner = new Scanner("99\n");

        // 调用 getLevelFromInput 方法
        sokobanGame.getLevelFromInput();

        // 验证选择的关卡是否正确
        Assert.assertEquals(98, sokobanGame.iCurLevel);
    }

    @Test
    public void testGetLevelFromInput8() {

        sokobanGame.scanner = new Scanner("2.5\n");
        // 调用 getLevelFromInput 方法
        sokobanGame.getLevelFromInput();

        // 验证选择的关卡是否正确
        Assert.assertEquals(1, sokobanGame.iCurLevel);
    }

    @Test
    public void testGetLevelFromInput9() {

        sokobanGame.scanner = new Scanner("a\n2.5\n");

        // 调用 getLevelFromInput 方法
        sokobanGame.getLevelFromInput();

        // 验证选择的关卡是否正确
        Assert.assertEquals(1, sokobanGame.iCurLevel);
    }

    //测试p1超出左边界
    @Test
    public void testTryGo1() {
        sokobanGame.selectLevel(0);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[0][0] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertFalse(sokobanGame.tryGo(new Point(0, -1), new Point(0, -2)));
    }

    //测试p1超出右边界
    @Test
    public void testTryGo2() {
        sokobanGame.selectLevel(0);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[0][15] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertFalse(sokobanGame.tryGo(new Point(0, 16), new Point(0, 17)));
    }

    //测试p1超出上边界
    @Test
    public void testTryGo3() {
        sokobanGame.selectLevel(0);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[0][0] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertFalse(sokobanGame.tryGo(new Point(-1, 0), new Point(-2, 0)));
    }

    //测试p1超出下边界
    @Test
    public void testTryGo4() {
        sokobanGame.selectLevel(0);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[15][0] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertFalse(sokobanGame.tryGo(new Point(16, 0), new Point(17, 0)));
    }

    //测试p1为墙
    @Test
    public void testTryGo5() {
        sokobanGame.selectLevel(0);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[8][8] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertFalse(sokobanGame.tryGo(new Point(8, 9), new Point(8, 10)));
    }

    //测试p1为箱子，p2为墙
    @Test
    public void testTryGo6() {
        sokobanGame.selectLevel(0);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[7][5] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertFalse(sokobanGame.tryGo(new Point(7, 7), new Point(7, 6)));
    }

    //测试p1为箱子，p2为箱子
    @Test
    public void testTryGo7() {
        sokobanGame.selectLevel(1);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[4][6] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertFalse(sokobanGame.tryGo(new Point(5, 6), new Point(6, 6)));
    }

    //测试p1为箱子，p2为空地
    @Test
    public void testTryGo8() {
        sokobanGame.selectLevel(0);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[8][5] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertTrue(sokobanGame.tryGo(new Point(8, 7), new Point(8, 6)));
    }

    //测试p1为箱子，p2为目标
    @Test
    public void testTryGo9() {
        sokobanGame.selectLevel(0);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[8][4] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertTrue(sokobanGame.tryGo(new Point(8, 6), new Point(8, 5)));
    }

    //测试p1为空地
    @Test
    public void testTryGo10() {
        sokobanGame.selectLevel(0);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[5][8] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertTrue(sokobanGame.tryGo(new Point(7, 8), new Point(6, 8)));
    }

    //测试p1为目标点
    @Test
    public void testTryGo11() {
        sokobanGame.selectLevel(1);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[8][10] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertTrue(sokobanGame.tryGo(new Point(8, 11), new Point(8, 12)));
    }

    //当前位置有目标
    @Test
    public void testTryGo12() {
        sokobanGame.selectLevel(1);
        int x = sokobanGame.prePosition.x;
        int y = sokobanGame.prePosition.y;
        sokobanGame.curMap[x][y] = SokobanGame.SPACE;
        sokobanGame.curMap[5][11] = SokobanGame.PLAYER;
        sokobanGame.searchPosition(sokobanGame.curMap);
        sokobanGame.drawMap();
        Assert.assertTrue(sokobanGame.tryGo(new Point(7, 11), new Point(6, 11)));
    }

    //测试非连续撤回
    @Test
    public void testRepeal1() {
        sokobanGame.selectLevel(1);
        sokobanGame.go("right");
        sokobanGame.repeal();
        Assert.assertEquals(0, sokobanGame.moveTimes);
        Assert.assertTrue(compareMap(sokobanGame.curLevel, sokobanGame.curMap));
    }

    //测试连续撤回
    @Test
    public void testRepeal2() {
        sokobanGame.selectLevel(1);
        sokobanGame.go("right");
        sokobanGame.go("right");
        sokobanGame.repeal();
        sokobanGame.repeal();
        Assert.assertEquals(1, sokobanGame.moveTimes);
        SokobanGame sokobanGame1 = new SokobanGame();
        sokobanGame1.selectLevel(1);
        sokobanGame1.go("right");
        Assert.assertTrue(compareMap(sokobanGame1.curMap, sokobanGame.curMap));
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

    @Test
    public void testSearchPosition1() {
        int[][] curMap = new int[][]{
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
        sokobanGame.searchPosition(curMap);
        Assert.assertEquals(sokobanGame.prePosition.x, 7);
        Assert.assertEquals(sokobanGame.prePosition.y, 8);
    }

    @Test(expected = Exception.class)
    public void testSearchPosition2() {
        int[][] curMap = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 3, 3, 3, 2, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 2, 0, 3, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        sokobanGame.searchPosition(curMap);
    }

    @Test
    public void testSearchPosition3() {
        int[][] curMap = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 2, 3},
                {1, 2, 3}
        };
        sokobanGame.searchPosition(curMap);
    }

    @Test
    public void testInitLevel() {
        sokobanGame.initLevel();
        Assert.assertEquals(sokobanGame.moveTimes, 0);
    }

    @Test
    public void testMove1() {
        sokobanGame.init();
        sokobanGame.go("up");
    }

    @Test
    public void testMove2() {
        sokobanGame.init();
        sokobanGame.go("right");
    }

    @Test
    public void testMove3() {
        sokobanGame.init();
        sokobanGame.go("down");
    }

    @Test
    public void testMove4() {
        sokobanGame.init();
        sokobanGame.go("left");
    }

    @Test
    public void testMove5() {
        sokobanGame.iCurLevel = 0;
        sokobanGame.init();
        sokobanGame.go("up");
        sokobanGame.go("down");
        sokobanGame.curMap = new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,3,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,1,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,0,0,0,3,1,0,0,0,0},
                {0,0,0,0,1,3,0,0,4,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,1,3,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,2,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}}.clone();
        sokobanGame.moveTimes = 3;
        sokobanGame.go("down");
        Assert.assertEquals(sokobanGame.moveTimes, 0);
        Assert.assertEquals(sokobanGame.iCurLevel, 1);
    }
    @Test
    public void testMove6() {
        sokobanGame.iCurLevel = 0;
        sokobanGame.init();
        sokobanGame.go("up");
        sokobanGame.go("down");
        sokobanGame.curMap = new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,3,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,1,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,0,0,0,3,1,0,0,0,0},
                {0,0,0,0,1,3,0,0,4,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,1,3,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,2,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}}.clone();
        sokobanGame.moveTimes = 99;
        sokobanGame.go("down");
        Assert.assertEquals(sokobanGame.moveTimes, 0);
        Assert.assertEquals(sokobanGame.iCurLevel, 1);
    }

    @Test
    public void testMove7() {
        sokobanGame.iCurLevel = 0;
        sokobanGame.init();
        sokobanGame.go("up");
        sokobanGame.curMap = new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,3,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,1,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,0,4,0,3,1,0,0,0,0},
                {0,0,0,0,1,3,0,0,0,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,1,3,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,2,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}}.clone();
        sokobanGame.moveTimes = 99;
        sokobanGame.go("down");
        Assert.assertEquals(sokobanGame.moveTimes, 0);
        Assert.assertEquals(sokobanGame.iCurLevel, 0);
    }

    @Test
    public void testShowGameGuide() {
        sokobanGame.showGameGuide();
    }

}
////    测试玩家能否正常移到到空地
//@Test
//public void testPlayerMoveSpace() {
//    int[][] expectMap = new int[][]{
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 3, 4, 3, 2, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 2, 0, 3, 0, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//    sokobanGame.selectLevel(0);
//    sokobanGame.go("up");
//    Assert.assertTrue(compareMap(expectMap, sokobanGame.curMap));
//    Assert.assertEquals(1, sokobanGame.moveTimes);
//}
//
//// 测试玩家在前方有一个箱子时能否正常移动
//@Test
//public void testPlayerMoveBox1() {
//    int[][] expectMap = new int[][]{
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 3, 0, 3, 2, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 2, 3, 4, 0, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//    sokobanGame.selectLevel(0);
//    sokobanGame.go("left");
//    Assert.assertTrue(compareMap(expectMap, sokobanGame.curMap));
//    Assert.assertEquals(1, sokobanGame.moveTimes);
//}
//
//// 测试玩家在前方有两个箱子时能否正常移动
//@Test
//public void testPlayerMoveBox2() {
//    int[][] expectMap = new int[][]{
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
//            {0, 0, 0, 1, 1, 3, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0},
//            {0, 0, 0, 1, 0, 0, 0, 0, 4, 3, 3, 0, 1, 0, 0, 0},
//            {0, 0, 0, 1, 0, 2, 2, 1, 0, 3, 0, 1, 1, 0, 0, 0},
//            {0, 0, 0, 1, 1, 2, 2, 1, 0, 0, 0, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//    sokobanGame.selectLevel(2);
//    IntStream.range(0, 4).forEach(a -> sokobanGame.go("right"));
//    Assert.assertTrue(compareMap(expectMap, sokobanGame.curMap));
//    Assert.assertEquals(3, sokobanGame.moveTimes);
//}
//
////    测试玩家前面有墙时能否正常移动
//@Test
//public void testPlayerMoveWall() {
//    int[][] expectMap = new int[][]{
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 3, 0, 3, 2, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 2, 0, 3, 4, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//    sokobanGame.selectLevel(0);
//    sokobanGame.go("right");
//    Assert.assertTrue(compareMap(expectMap, sokobanGame.curMap));
//    Assert.assertEquals(0, sokobanGame.moveTimes);
//}
//
////    测试玩家能否会移动到地图边界之外，代码缺陷
//@Test
//public void testPlayerMoveOut() {
//    int[][] expectMap = new int[][]{
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 3, 0, 3, 2, 1, 0, 0, 0, 0},
//            {3, 4, 0, 0, 0, 2, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//    sokobanGame.selectLevel(0);
//    sokobanGame.curMap[8][4] = 0;
//    IntStream.range(0, 8).forEach(a -> sokobanGame.go("left"));
//    Assert.assertTrue(compareMap(expectMap, sokobanGame.curMap));
//    Assert.assertEquals(0, sokobanGame.moveTimes);
//}
//
////测试超出最大步数是否失败
//@Test
//public void testFailure() {
//    PrintStream originalOut = System.out;
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    sokobanGame.selectLevel(0);
//    sokobanGame.moveTimes = 99;
//    sokobanGame.go("left");
//
//    System.setOut(originalOut);
////        输出游戏结束
//    Assert.assertTrue(outputStream.toString().contains("ゲームオーバー"));
////        重新开始本局游戏
//    Assert.assertTrue(compareMap(sokobanGame.curLevel, sokobanGame.curMap));
//}
//
////测试完成第一关
//@Test
//public void testSuccess1() {
//    PrintStream originalOut = System.out;
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    sokobanGame.selectLevel(0);
//    sokobanGame.curMap = new int[][]{
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 3, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 0, 4, 3, 2, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 3, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
//            {0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//    sokobanGame.searchPosition(sokobanGame.curMap);
//    sokobanGame.go("right");
//    System.setOut(originalOut);
//    Assert.assertTrue(outputStream.toString().contains("恭喜过关！！"));
//    Assert.assertEquals(1, sokobanGame.iCurLevel);
//}
//
////测试完成第五十关
//@Test
//public void testSuccess50() {
//    PrintStream originalOut = System.out;
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    sokobanGame.selectLevel(49);
//
//    sokobanGame.curMap = new int[][]{
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0},
//            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0},
//            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
//            {0, 1, 1, 0, 1, 3, 1, 3, 1, 3, 1, 0, 0, 1, 0, 0},
//            {1, 1, 1, 3, 3, 3, 3, 3, 3, 2, 3, 4, 0, 1, 1, 0},
//            {0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0},
//            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
//            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//    sokobanGame.searchPosition(sokobanGame.curMap);
//    sokobanGame.go("left");
//    System.setOut(originalOut);
//    Assert.assertTrue(outputStream.toString().contains("恭喜过关！！"));
//    Assert.assertEquals(50, sokobanGame.iCurLevel);
//}
//
////测试完成第一百关
//@Test
//public void testSuccess100() {
//    PrintStream originalOut = System.out;
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//    sokobanGame.selectLevel(99);
//
//    sokobanGame.curMap = new int[][]{
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
//            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1},
//            {0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
//            {1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
//            {0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
//            {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1},
//            {0, 0, 1, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 1, 0},
//            {0, 0, 1, 0, 3, 3, 0, 0, 0, 3, 3, 1, 1, 1, 0, 0},
//            {0, 0, 0, 1, 3, 3, 3, 3, 3, 2, 1, 1, 1, 1, 1, 0},
//            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//    sokobanGame.searchPosition(sokobanGame.curMap);
//    sokobanGame.go("down");
//    System.setOut(originalOut);
//    Assert.assertTrue(outputStream.toString().contains("恭喜过关！！"));
//}
//
////第一关测试输出指引
//@Test
//public void testGameGuide1() {
//    PrintStream originalOut = System.out;
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    sokobanGame.selectLevel(0);
//    sokobanGame.action = 'p';
//    sokobanGame.execute();
//
//    System.setOut(originalOut);
//    Assert.assertTrue(outputStream.toString().contains("\n\uD83D\uDC4B这是一个推箱子小游戏，游戏目标是将\uD83D\uDCE6推到⭐处。\r\n\u001B[34ma\u001B[0m左移，\u001B[34md\u001B[0m右移，\u001B[34mw\u001B[0m上移，\u001B[34ms\u001B[0m下移，\u001B[34mr\u001B[0m撤销，\u001B[34me\u001B[0m选择关卡，\u001B[34mp\u001B[0m输出本条消息，\u001B[34mt\u001B[0m重新开始\n"));
//}
//
////第五十关测试输出指引
//@Test
//public void testGameGuide50() {
//    PrintStream originalOut = System.out;
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    sokobanGame.selectLevel(49);
//    sokobanGame.action = 'p';
//    sokobanGame.execute();
//
//    System.setOut(originalOut);
//    Assert.assertTrue(outputStream.toString().contains("\n\uD83D\uDC4B这是一个推箱子小游戏，游戏目标是将\uD83D\uDCE6推到⭐处。\r\n\u001B[34ma\u001B[0m左移，\u001B[34md\u001B[0m右移，\u001B[34mw\u001B[0m上移，\u001B[34ms\u001B[0m下移，\u001B[34mr\u001B[0m撤销，\u001B[34me\u001B[0m选择关卡，\u001B[34mp\u001B[0m输出本条消息，\u001B[34mt\u001B[0m重新开始\n"));
//}
//
////第一百关测试输出指引
//@Test
//public void testGameGuide100() {
//    PrintStream originalOut = System.out;
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    sokobanGame.selectLevel(99);
//    sokobanGame.action = 'p';
//    sokobanGame.execute();
//
//    System.setOut(originalOut);
//    Assert.assertTrue(outputStream.toString().contains("\n\uD83D\uDC4B这是一个推箱子小游戏，游戏目标是将\uD83D\uDCE6推到⭐处。\r\n\u001B[34ma\u001B[0m左移，\u001B[34md\u001B[0m右移，\u001B[34mw\u001B[0m上移，\u001B[34ms\u001B[0m下移，\u001B[34mr\u001B[0m撤销，\u001B[34me\u001B[0m选择关卡，\u001B[34mp\u001B[0m输出本条消息，\u001B[34mt\u001B[0m重新开始\n"));
//}
//
////第一关重玩
//@Test
//public void testReplay1() {
//    sokobanGame.selectLevel(0);
//    sokobanGame.action = 'a';
//    sokobanGame.execute();
//    sokobanGame.action = 't';
//    sokobanGame.execute();
//    Assert.assertTrue(compareMap(SokobanGame.levels[sokobanGame.iCurLevel], sokobanGame.curMap));
//    Assert.assertEquals(0, sokobanGame.moveTimes);
//}
//
////第五十关重玩
//@Test
//public void testReplay50() {
//    sokobanGame.selectLevel(49);
//    sokobanGame.action = 'a';
//    sokobanGame.execute();
//    sokobanGame.action = 't';
//    sokobanGame.execute();
//    Assert.assertTrue(compareMap(SokobanGame.levels[sokobanGame.iCurLevel], sokobanGame.curMap));
//    Assert.assertEquals(0, sokobanGame.moveTimes);
//}
//
////第一百关重玩
//@Test
//public void testReplay100() {
//    sokobanGame.selectLevel(99);
//    sokobanGame.action = 'a';
//    sokobanGame.execute();
//    sokobanGame.action = 't';
//    sokobanGame.execute();
//    Assert.assertTrue(compareMap(SokobanGame.levels[sokobanGame.iCurLevel], sokobanGame.curMap));
//    Assert.assertEquals(0, sokobanGame.moveTimes);
//}