import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;
import java.io.IOException;


public class GUITest {

    private FrameFixture frame;

    @Before
    public void setUp() {
        int mapSize = 16;
        int numberOfMaps = 100;

        // 初始化levels数组，存储100个16x16的地图
        SokobanGame.levels = new int[numberOfMaps][mapSize][mapSize];

        // 读取文件并解析
        try {
            SokobanGame.loadMapsFromFile("maps.txt", numberOfMaps, mapSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SokobanGame sokobanGame = new SokobanGame();
        sokobanGame.init();
        Renderer renderer = new Renderer(sokobanGame);
        renderer.init();
        frame = new FrameFixture(renderer.frame);
        frame.show();
    }

    @After
    public void tearDown() {
        frame.cleanUp();
    }

    @Test
    public void demoTest() throws InterruptedException {
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
    }
}