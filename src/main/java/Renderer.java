import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private static final int SPACE = 0;
    private static final int WALL = 1;
    private static final int GOAL = 2;
    private static final int BOX = 3;
    private static final int PLAYER = 4;

    private final SokobanGame sokobanGame;
    public JPanel imagePanel;
    public UIPanel uiPanel;
    public JFrame frame;

    public Renderer(SokobanGame sokobanGame) {
        this.sokobanGame = sokobanGame;
    }

    public void init() {
        // 创建一个JFrame窗口
        frame = new JFrame("Box");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        int width = 36 * 16;
        int height = 36 * 16 + 40;
        frame.setSize(width, height);

        // 加载图片到内存
        List<BufferedImage> images = new ArrayList<>();
        String[] imagePaths = {
                "images/block.gif",
                "images/wall.png",
                "images/ball.png",
                "images/box.png",
                "images/down.png",
                "images/left.png",
                "images/right.png",
                "images/up.png"
        };
        for (String path : imagePaths) {
            try {
                InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                BufferedImage image = ImageIO.read(stream);
                images.add(image);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load image: " + path);
            }
        }

        // 创建一个自定义的JPanel来绘制图片
        imagePanel = new MultiImagePanel(this, sokobanGame, images);
        imagePanel.setName("imagePanel");

        // 将JPanel添加到JFrame中
        frame.add(imagePanel, BorderLayout.CENTER);

        uiPanel = new UIPanel(this, sokobanGame);
        uiPanel.setName("uiPanel");
        frame.add(uiPanel, BorderLayout.SOUTH);


        imagePanel.setFocusable(true);
        imagePanel.requestFocusInWindow();
        // 设置JFrame可见
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        // 定义地图的大小（例如16x16的地图）
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
    }

    static class MultiImagePanel extends JPanel implements KeyListener {
        private List<BufferedImage> images;
        SokobanGame sokobanGame;
        Character action = 'n';
        Renderer renderer;

        public MultiImagePanel(Renderer renderer, SokobanGame sokobanGame, List<BufferedImage> images) {
            this.renderer = renderer;
            this.sokobanGame = sokobanGame;
            this.images = images;
            int width = 36 * 16;
            int height = 36 * 16;
            setPreferredSize(new Dimension(width, height));
            addKeyListener(this);

        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int[][] curMap = sokobanGame.curMap;
            //绘制背景
            for (int i = 0; i < 16; i++)
                for (int j = 0; j < 16; j++) {
                    BufferedImage currentImage = images.get(0);
                    int x = j * 35;
                    int y = i * 35;
                    g.drawImage(currentImage, x, y, currentImage.getWidth(), currentImage.getHeight(), null);
                }
            //绘制物体
            for (int i = 0; i < 16; i++)
                for (int j = 0; j < 16; j++) {
                    int index = curMap[i][j];
                    if (index != SPACE && index != PLAYER) {
                        BufferedImage currentImage = images.get(index);
                        int x = j * 35;
                        int y = i * 35;
                        int width = currentImage.getWidth();
                        int height = currentImage.getHeight();
                        x = x - (width - 35) / 2;
                        y = y - (height - 35);
                        g.drawImage(currentImage, x, y, width, height, null);
                    }

                }
            //绘制人物
            for (int i = 0; i < 16; i++)
                for (int j = 0; j < 16; j++) {
                    int index = curMap[i][j];
                    if (index == PLAYER) {
                        BufferedImage currentImage = images.get(index);
                        int x = j * 35;
                        int y = i * 35;
                        int width = currentImage.getWidth();
                        int height = currentImage.getHeight();
                        x = x - (width - 35) / 2;
                        y = y - (height - 35);
                        g.drawImage(currentImage, x, y, width, height, null);
                        break;
                    }

                }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case 38:
                case 87:
                    action = 'w';
                    break;
                case 40:
                case 83:
                    action = 's';
                    break;
                case 37:
                case 65:

                    action = 'a';
                    break;
                case 39:
                case 68:

                    action = 'd';
            }
            sokobanGame.execute(action);
            renderer.uiPanel.refresh();
            repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    static class UIPanel extends JPanel implements ActionListener {


        private JButton previousButton;
        private JButton nextButton;
        private JButton repealButton;
        private JButton briefButton;
        private JLabel levelLabel;
        private JLabel moveCountLabel;
        private JButton jumpButton;
        private JTextField levelToJumpTextField;
        private JButton resetButton;
        private SokobanGame sokobanGame;
        private Renderer renderer;

        public UIPanel(Renderer renderer, SokobanGame sokobanGame) {

            this.renderer = renderer;
            this.sokobanGame = sokobanGame;
            previousButton = new JButton("上一关");
            previousButton.setName("previousButton");
            nextButton = new JButton("下一关");
            nextButton.setName("nextButton");
            repealButton = new JButton("撤销");
            repealButton.setName("repealButton");
            briefButton = new JButton("游戏说明");
            briefButton.setName("briefButton");
            jumpButton = new JButton("跳转");
            jumpButton.setName("jumpButton");
            levelToJumpTextField = new JTextField();
            levelToJumpTextField.setName("levelToJumpTextField");
            resetButton = new JButton("重置关卡");
            resetButton.setName("resetButton");

            jumpButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = levelToJumpTextField.getText();
                    int level = Integer.parseInt(text);
                    sokobanGame.selectLevel(level - 1);
                    refresh();
                    renderer.imagePanel.repaint();
                    renderer.imagePanel.requestFocusInWindow();

                }
            });

            repealButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sokobanGame.repeal();
                    refresh();
                    renderer.imagePanel.repaint();
                    renderer.imagePanel.requestFocusInWindow();
                }
            });

            previousButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("上一关");
                    sokobanGame.selectLevel(sokobanGame.iCurLevel - 1);
                    refresh();
                    renderer.imagePanel.repaint();
                    renderer.imagePanel.requestFocusInWindow();
                }
            });
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("下一关");
                    sokobanGame.selectLevel(sokobanGame.iCurLevel + 1);
                    refresh();
                    renderer.imagePanel.repaint();
                    renderer.imagePanel.requestFocusInWindow();
                }
            });

            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("重置关卡");
                    sokobanGame.selectLevel(sokobanGame.iCurLevel);
                    refresh();
                    renderer.imagePanel.repaint();
                    renderer.imagePanel.requestFocusInWindow();

                }
            });

            briefButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "这是一个好玩的推箱子游戏，共100关哦", "游戏说明", JOptionPane.PLAIN_MESSAGE);
                    renderer.imagePanel.requestFocusInWindow();
                }
            });


            levelLabel = new JLabel("第" + (sokobanGame.iCurLevel + 1) + "/100关");
            moveCountLabel = new JLabel("移动次数：" + sokobanGame.moveTimes);


            setLayout(new GridLayout(2, 4, 5, 5));

            add(levelLabel);
            add(moveCountLabel);

            JPanel jumpPanel = new JPanel();
            jumpPanel.setLayout(new GridLayout(1, 2, 5, 5));
            jumpPanel.add(jumpButton);
            jumpPanel.add(levelToJumpTextField);
            add(jumpPanel);

//            add(jumpButton);
//            add(levelToJumpTextField);
            add(resetButton);
            add(previousButton);
            add(nextButton);
            add(repealButton);
            add(briefButton);


        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }

        public void refresh() {
            levelLabel.setText("第" + (sokobanGame.iCurLevel + 1) + "/100关");
            moveCountLabel.setText("移动次数：" + sokobanGame.moveTimes);
        }
    }

}

