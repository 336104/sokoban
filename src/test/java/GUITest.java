import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;


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
    public void testCopyTextToLabel() throws InterruptedException {
        frame.panel("imagePanel").pressKey(KeyEvent.VK_UP);
        Thread.sleep(10000);
    }
}


/**
 * 键盘按键检测的案例程序<br/>
 * create by LINKSINKE on 2020/2/29
 */
class CheckKeyboard extends JFrame {
    public CheckKeyboard() {
        setSize(200, 100);
        // 设置close按钮的操作（关闭窗口并且停止程序的运行）
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口默认显示的位置和大小
        this.setBounds(300, 200, 500, 233);
        // 窗口不能够最大化显示(最大化按钮禁用，并且不能拖拽窗体的大小)
        this.setResizable(false);
        // 设置窗口的标题文字
        this.setTitle("键盘按键检测程序");
        // 获取容器对象
        Container ctainer = this.getContentPane();
        // 设置布局方式为流式布局
        ctainer.setLayout(null);

        // 文本输入框
        JLabel lb = new JLabel("内容：");
        lb.setBounds(12, 2, 45, 35);
        lb.setForeground(Color.red);
        JTextField txt = new JTextField(40);
        txt.setName("txt");
        txt.setBounds(50, 5, 400, 25);

        // 键盘区域
        Panel p1 = new Panel();
        p1.setLayout(new FlowLayout());
        p1.setBounds(0, 35, 500, 233);
        // 取得按钮集合并添加到容器对象里面
        final List<JButton> btnLis = createKeyboardLayout();
        for (int i = 0; i < btnLis.size(); i++) {
            p1.add(btnLis.get(i));
        }

        // 向容器中添加文本标签、文本输入框、面板组件
        ctainer.add(lb);
        ctainer.add(txt);
        ctainer.add(p1);

        // 添加文本框的键盘监听事件
        txt.addKeyListener(new KeyListener() {
            // 发生击键时被触发
            @Override
            public void keyTyped(KeyEvent e) {

            }

            // 按键被释放时被触发
            @Override
            public void keyReleased(KeyEvent e) {
                // 获取键入的键盘字符
                char letter = e.getKeyChar();
                System.out.println(letter);
                /**
                 * 循环判断输入的值是否和btnLis集合里的值是否一样<br/>
                 * 如果输入的内容和按钮上的文字内容一样就设置背景颜色
                 */
                for (JButton jButton : btnLis) {
                    String btnStr = jButton.getText();
                    // 判断jButton上的文本是否和输入的内容一样（忽略了大小写）
                    if (btnStr.equalsIgnoreCase(String.valueOf(letter))) {
                        jButton.setBackground(Color.WHITE);
                    }
                }
            }

            // 按键被按下（手指按下键盘并未松开）时被触发
            @Override
            public void keyPressed(KeyEvent e) {
                // 获取键入的键盘字符
                char letter = e.getKeyChar();
                System.out.println(letter);
                /**
                 * 循环判断输入的值是否和btnLis集合里的值是否一样<br/>
                 * 如果输入的内容和按钮上的文字内容一样就设置背景颜色
                 */
                for (JButton jButton : btnLis) {
                    String btnStr = jButton.getText();
                    // 判断jButton上的文本是否和输入的内容一样（忽略了大小写）
                    if (btnStr.equalsIgnoreCase(String.valueOf(letter))) {
                        jButton.setBackground(Color.BLUE);
                    }
                }
            }
        });

        // 设置窗口是否显示（true为显示窗口，false为不显示窗口）
        this.setVisible(true);
    }

    /**
     * 创建一个键盘布局
     *
     * @return JButton对象集合
     */
    public List<JButton> createKeyboardLayout() {
        // 键盘上的文字
        String[] str = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "Q", "W", "E", "R", "T", "Y", "U", "I", "O",
                "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M"};
        // 存放JButton对象
        List<JButton> btnLis = new ArrayList<JButton>();

        // 循环创建JButton对象并添加至集合里
        for (int i = 0; i < str.length; i++) {
            JButton btn = new JButton(str[i]);
            btn.setFont(new Font("微软雅黑", Font.ITALIC, 12));
            btn.setBackground(Color.WHITE);
            btn.setBorderPainted(false);
            btnLis.add(btn);
        }
        return btnLis;
    }

    public static void main(String args[]) {
        JFrame jframe = new CheckKeyboard();
    }
}

class GUI extends JFrame {
    JTextArea textArea;


    public GUI() {
        System.out.println("new window");
        setTitle("键盘测试");
        JPanel panel = new JPanel();
        textArea = new JTextArea();
        panel.add(textArea);
        panel.setBounds(100, 100, 200, 200);
        textArea.addKeyListener(new MyListener());
        textArea.append("开始吧：\n");
        add(textArea);
        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    class MyListener implements KeyListener {
        @Override // 按下
        public void keyPressed(KeyEvent e) {
            textArea.append("按下：" + KeyEvent.getKeyText(e.getKeyCode()) + "\n");
        }

        @Override // 松开
        public void keyReleased(KeyEvent e) {
            textArea.append("松开：" + KeyEvent.getKeyText(e.getKeyCode()) + "\n");
            if (KeyEvent.getKeyText(e.getKeyCode()).equals("C")) {
                textArea.setText("");
            }
        }

        @Override // 输入的内容
        public void keyTyped(KeyEvent e) {
            textArea.append("输入：" + e.getKeyChar() + "\n");
        }
    }
}

class Demo2 {
    public static void main(String[] args) {
        GUI g = new GUI();
    }
}