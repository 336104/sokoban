import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

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
            SokobanGame.loadMapsFromFile("C:/Users/10424/Desktop/postgraduate/maps.txt", numberOfMaps, mapSize);
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
        Assert.assertEquals(0, sokobanGame.iCurLevel+1); // 超出范围无效
    }
    @Test
    public void testSelectLevel1() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(0); // 选择第1关
        Assert.assertEquals(1, sokobanGame.iCurLevel+1); // 预期关卡应该是1
    }
    @Test
    public void testSelectLevel2() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(1); // 选择第2关
        Assert.assertEquals(2, sokobanGame.iCurLevel+1); // 超出范围无效
    }
    @Test
    public void testSelectLevel3() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(49); // 选择第50关
        Assert.assertEquals(50, sokobanGame.iCurLevel+1); // 超出范围无效
    }
    @Test
    public void testSelectLevel4() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(98); // 选择第99关
        Assert.assertEquals(99, sokobanGame.iCurLevel+1); // 预期关卡应该是99
    }
    @Test
    public void testSelectLevel5() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(99); // 选择第100关
        Assert.assertEquals(100, sokobanGame.iCurLevel+1); // 预期关卡应该是100
    }

    @Test
    public void testSelectLevel6() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(100); // 选择第101关
        Assert.assertEquals(101, sokobanGame.iCurLevel+1); // 超出范围无效
    }

}
