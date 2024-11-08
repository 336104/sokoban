import org.assertj.swing.fixture.FrameFixture;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.bytedeco.opencv.opencv_core.Mat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;


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
    }

    @After
    public void tearDown() {
        frame.cleanUp();
    }

    @Test
    public void testStart() {
        frame.panel("imagePanel").requireVisible();
        frame.panel("uiPanel").requireVisible();
        frame.button("previousButton").requireEnabled();
        frame.button("nextButton").requireEnabled();
        frame.button("repealButton").requireEnabled();
        frame.button("briefButton").requireEnabled();
        frame.button("jumpButton").requireEnabled();
        frame.button("resetButton").requireEnabled();
        frame.textBox("levelToJumpTextField").requireText("");
        frame.label("moveCountLabel").requireText("移动次数：0");
    }

    @Test
    public void testLoadMap1() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Mat screenCapture = createScreenCapture(imagePanel);
        Mat expected = loadTestImage("testLoadMap1.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    @Test
    public void testLoadMap2() throws AWTException, IOException {
        jump(50);
        JPanel imagePanel = frame.panel("imagePanel").target();
        Mat screenCapture = createScreenCapture(imagePanel);
        Mat expected = loadTestImage("testLoadMap2.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    @Test
    public void testLoadMap3() throws AWTException, IOException {
        jump(100);
        JPanel imagePanel = frame.panel("imagePanel").target();
        Mat screenCapture = createScreenCapture(imagePanel);
        Mat expected = loadTestImage("testLoadMap3.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    @Test
    public void testPlayerMove1() throws IOException, AWTException {
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.label("moveCountLabel").requireText("移动次数：1");
        Mat screenCapture = createScreenCapture(frame.panel("imagePanel").target());
        Mat expected = loadTestImage("testPlayerMove1.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    @Test
    public void testPlayerMove2() throws IOException, AWTException {
        frame.panel("imagePanel").pressKey(KeyEvent.VK_LEFT);
        frame.label("moveCountLabel").requireText("移动次数：1");
        Mat screenCapture = createScreenCapture(frame.panel("imagePanel").target());
        Mat expected = loadTestImage("testPlayerMove2.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    @Test
    public void testPlayerMove3() throws IOException, AWTException {
        jump(3);
        IntStream.range(0, 4).forEach(i -> frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT));
        frame.label("moveCountLabel").requireText("移动次数：3");
        Mat screenCapture = createScreenCapture(frame.panel("imagePanel").target());
        Mat expected = loadTestImage("testPlayerMove3.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    @Test
    public void testPlayerMove4() throws IOException, AWTException {
        frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT);
        frame.label("moveCountLabel").requireText("移动次数：0");
        Mat screenCapture = createScreenCapture(frame.panel("imagePanel").target());
        Mat expected = loadTestImage("testPlayerMove4.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    @Test
    public void testBoxMove1() throws IOException, AWTException {
        IntStream.range(0, 2).forEach(i -> frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN));
        frame.label("moveCountLabel").requireText("移动次数：1");
        Mat screenCapture = createScreenCapture(frame.panel("imagePanel").target());
        Mat expected = loadTestImage("testBoxMove1.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    @Test
    public void testBoxMove2() throws IOException, AWTException {
        frame.panel("imagePanel").pressKey(KeyEvent.VK_LEFT);
        frame.label("moveCountLabel").requireText("移动次数：1");
        Mat screenCapture = createScreenCapture(frame.panel("imagePanel").target());
        Mat expected = loadTestImage("testBoxMove2.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    @Test
    public void testBoxMove3() throws IOException, AWTException {
        jump(3);
        IntStream.range(0, 4).forEach(i -> frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT));
        frame.label("moveCountLabel").requireText("移动次数：3");
        Mat screenCapture = createScreenCapture(frame.panel("imagePanel").target());
        Mat expected = loadTestImage("testBoxMove3.png");
        Assert.assertTrue(compare(screenCapture, expected) > 0.9);
    }

    public static Mat createScreenCapture(JComponent target) throws AWTException {
        Point location = target.getLocationOnScreen();
        Rectangle bounds = target.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
//        ImageIO.write(bScreenShot, "png", new File("src/main/resources/test_images/testBoxMove3.png"));
        return Java2DFrameUtils.toMat(bScreenShot);
    }

    public void jump(Integer level) {
        frame.textBox("levelToJumpTextField").enterText(level.toString());
        frame.button("jumpButton").click();
    }

    public static Mat loadTestImage(String name) throws IOException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test_images/" + name);
        BufferedImage bufferedImage = null;
        if (stream != null) {
            bufferedImage = ImageIO.read(stream);
        }
        return Java2DFrameUtils.toMat(bufferedImage);
    }

    public static double compare(Mat img1, Mat img2) {
        Mat image1Gray = new Mat();
        Mat image2Gray = new Mat();
        resize(img1, img2, img2.size());
        cvtColor(img1, image1Gray, COLOR_BGR2GRAY);
        cvtColor(img2, image2Gray, COLOR_BGR2GRAY);
        Mat ssimMat = new Mat();
        matchTemplate(image1Gray, image2Gray, ssimMat, CV_TM_CCOEFF_NORMED);
        double[] minVal = new double[1];
        var minPoint = new org.bytedeco.opencv.opencv_core.Point();
        double[] maxVal = new double[1];
        var maxPoint = new org.bytedeco.opencv.opencv_core.Point();
        minMaxLoc(ssimMat, minVal, maxVal, minPoint, maxPoint, null);
        System.out.println(maxVal[0]);
        return maxVal[0];
    }
}