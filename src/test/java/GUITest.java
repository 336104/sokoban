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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
        frame.show();
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
    }

    @Test
    public void testLoadMap1() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat mScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test_images/testLoadMap1.png");
        BufferedImage bufferedImage = ImageIO.read(stream);
        Mat expected = Java2DFrameUtils.toMat(bufferedImage);
        Assert.assertTrue(ssim(mScreenShot, expected) > 0.9);
    }

    @Test
    public void testLoadMap2() throws AWTException, IOException {
        frame.textBox("levelToJumpTextField").enterText("50");
        frame.button("jumpButton").click();
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat mScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test_images/testLoadMap2.png");
        BufferedImage bufferedImage = ImageIO.read(stream);
        Mat expected = Java2DFrameUtils.toMat(bufferedImage);
        Assert.assertTrue(ssim(mScreenShot, expected) > 0.9);
    }

    @Test
    public void testLoadMap3() throws AWTException, IOException {
        frame.textBox("levelToJumpTextField").enterText("100");
        frame.button("jumpButton").click();
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat mScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test_images/testLoadMap3.png");
        BufferedImage bufferedImage = ImageIO.read(stream);
        Mat expected = Java2DFrameUtils.toMat(bufferedImage);
        Assert.assertTrue(ssim(mScreenShot, expected) > 0.9);
    }

    public static double ssim(Mat img1, Mat img2) {
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

        return maxVal[0];
    }
}