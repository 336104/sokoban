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
        sokobanGame = new SokobanGame();

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

    @Test
    public void testSelectLevel0() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(1); // 选择第一关
        Assert.assertEquals(1, sokobanGame.iCurLevel); // 预期关卡应该是1
    }
    @Test
    public void testSelectLevel1() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(101); // 选择第101关
        Assert.assertEquals(101, sokobanGame.iCurLevel ); // 超出范围无效
    }
    @Test
    public void testSelectLevel2() {
        // 测试选择有效关卡
        sokobanGame.selectLevel(-1); // 选择第101关
        Assert.assertEquals(-1, sokobanGame.iCurLevel); // 超出范围无效
    }

}
