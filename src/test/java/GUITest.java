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
import java.io.File;
import java.io.FileInputStream;
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

    @Test
    public void testUndo1() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        frame.button("repealButton").click();
        BufferedImage repealScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(repealScreenShot);

        frame.label("moveCountLabel").requireText("移动次数：0");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }

    @Test
    public void testUndo2() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.button("repealButton").click();
        BufferedImage repealScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(repealScreenShot);

        frame.label("moveCountLabel").requireText("移动次数：0");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }

    @Test
    public void testUndo3() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        for (int i=1; 2*i<=96; i++) {
            frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
            frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        }
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        frame.button("repealButton").click();
        BufferedImage repealScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(repealScreenShot);

        frame.label("moveCountLabel").requireText("移动次数：97");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }

    @Test
    public void testUndo4() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        for (int i=1; 2*i<=96; i++) {
            frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
            frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        }
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.button("repealButton").click();
        BufferedImage repealScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(repealScreenShot);

        frame.label("moveCountLabel").requireText("移动次数：98");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }

    @Test
    public void testUndo5() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        for (int i=1; 2*i<=46; i++) {
            frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
            frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        }
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.button("repealButton").click();
        BufferedImage repealScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(repealScreenShot);

        frame.label("moveCountLabel").requireText("移动次数：48");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }

    @Test
    public void testUndo6() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.button("repealButton").click();
        BufferedImage repealScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(repealScreenShot);

        frame.label("moveCountLabel").requireText("移动次数：0");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }
    @Test
    public void testUndo7() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        frame.button("repealButton").click();
        frame.button("repealButton").click();
        BufferedImage repealScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(repealScreenShot);

        frame.label("moveCountLabel").requireText("移动次数：1");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }

    @Test
    public void testUndo8() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
//        writeImageFile(bScreenShot, "p1.png");
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        frame.button("repealButton").click();
        BufferedImage repealScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(repealScreenShot);
//        writeImageFile(repealScreenShot, "p2.png");

        frame.label("moveCountLabel").requireText("移动次数：0");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }

    @Test
    public void testUndo9() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        frame.button("repealButton").click();
        BufferedImage repealScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(repealScreenShot);

        frame.label("moveCountLabel").requireText("移动次数：0");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }

    @Test
    public void testMaxStepCheck1() throws AWTException, IOException {
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);

        frame.label("moveCountLabel").requireText("移动次数：1");
    }

    @Test
    public void testMaxStepCheck2() throws AWTException, IOException {
        JPanel imagePanel = frame.panel("imagePanel").target();
        Point location = imagePanel.getLocationOnScreen();
        Rectangle bounds = imagePanel.getBounds();
        Robot robot = new Robot();
        BufferedImage bScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat expectedScreenShot = Java2DFrameUtils.toMat(bScreenShot);
        for (int i=1; 2*i<=100; i++) {
            frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
            frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        }
        BufferedImage overScreenShot = robot.createScreenCapture(new Rectangle(location.x, location.y, bounds.width, bounds.height));
        Mat currentScreenShot = Java2DFrameUtils.toMat(overScreenShot);

        frame.label("moveCountLabel").requireText("移动次数：0");
        Assert.assertTrue(ssim(expectedScreenShot, currentScreenShot) > 0.9);
    }

    @Test
    public void testMaxStepCheck3() throws AWTException, IOException {
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_LEFT);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_LEFT);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_DOWN);
        for (int i=1; 2*i<=90; i++) {
            frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT);
            frame.panel("imagePanel").pressKey(KeyEvent.VK_LEFT);
        }
        frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT);

        frame.label("levelLabel").requireText("第2/100关");
        frame.label("moveCountLabel").requireText("移动次数：0");
    }
    @Test
    public void testMaxStepCheck4() throws AWTException, IOException {
        frame.textBox("levelToJumpTextField").enterText("2");
        frame.button("jumpButton").click();

        for (int i=1; 2*i<=48; i++) {
            frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT);
            frame.panel("imagePanel").pressKey(KeyEvent.VK_LEFT);
        }
        frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT);
        frame.panel("imagePanel").pressKey(KeyEvent.VK_LEFT);
        frame.label("moveCountLabel").requireText("移动次数：50");
    }
    @Test
    public void testMaxStepCheck5() throws AWTException, IOException {
        frame.textBox("levelToJumpTextField").enterText("100");
        frame.button("jumpButton").click();

        for (int i=1; 2*i<=98; i++) {
            frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT);
            frame.panel("imagePanel").pressKey(KeyEvent.VK_LEFT);
        }
        frame.panel("imagePanel").pressKey(KeyEvent.VK_RIGHT);
        frame.label("moveCountLabel").requireText("移动次数：99");
    }

    public void writeImageFile(BufferedImage bi, String fileName) throws IOException {
        File outputfile = new File("D:/"+fileName);
        ImageIO.write(bi, "png", outputfile);
    }

}