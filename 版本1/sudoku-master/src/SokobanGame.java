import java.awt.*;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.util.*;


public class SokobanGame {

    /**
     * 上一次的地图
     */
    public int[][] preMap;
    /**
     * 撤回
     */
    public int repeal = 0;
    /**
     * 当前地图状态
     */
    public int[][] curMap;
    /**
     * 当前等级的初始地图
     */
    public int[][] curLevel;
    /**
     * 当前关卡数
     */
    public int iCurLevel = 0;
    /**
     * 移动了多少次
     */
    public int moveTimes = 0;
    /**
     * 最大移动次数
     */
    public final int maxSteps = 100;

    public static final int SPACE = 0;
    public static final int WALL = 1;
    public static final int GOAL = 2;
    public static final int BOX = 3;
    public static final int PLAYER = 4;
    /**
     * 用于显示的字符
     */
    public static final Map<Integer, String> MAP = Map.of(
            SPACE, "\uD83C\uDF3F",
            WALL, "\uD83E\uDDF1",
            GOAL, "⭐",
            BOX, "\uD83D\uDCE6",
            PLAYER, "\uD83D\uDE42");

    /**
     * 代表操作的字符
     */
    public char action;
    public Scanner scanner = new Scanner(System.in);
    public Point prePosition;
    /**
     * 全部地图数据
     */
    public static int[][][] levels;

    // 读取文件并将数据存入levels数组
    public static void loadMapsFromFile(String fileName, int numberOfMaps, int mapSize) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        int mapIndex = -1; // 当前正在读取的地图索引
        int rowIndex = 0;  // 当前正在读取的行索引

        while ((line = br.readLine()) != null) {
            line = line.trim();

            // 开始新的地图
            if (line.equals("{")) {
                mapIndex++;
                rowIndex = 0; // 重置行索引
            }
            // 解析每一行地图数据
            else if (line.startsWith("{")) {
                String[] rowValues = line.replace("{", "").replace("}", "").split(",");
                for (int col = 0; col < mapSize; col++) {
                    levels[mapIndex][rowIndex][col] = Integer.parseInt(rowValues[col].trim());
                }
                rowIndex++;
            }
        }
        br.close();
    }

    // 打印一个地图
    public static void printMap(int[][] map) {
        for (int[] row : map) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }


    /**
     * 初始化游戏
     */
    public void init() {
        initLevel();//初始化对应等级的游戏
        drawMap();//绘制出当前等级的地图
        showMoveInfo();//初始化对应等级的游戏数据
    }


    /**
     * 初始化游戏等级
     */
    public int[][] copyArray(int[][] motoArray) {
        int row = motoArray.length;
        int column = motoArray[0].length;
        int[][] res = new int[row][column];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                res[i][j] = motoArray[i][j];
            }
        }

        return res;
    }

    public void initLevel() {

        //当前移动过的游戏地图
        curMap = copyArray(levels[iCurLevel]);
        //当前等级的初始地图
        curLevel = levels[iCurLevel];

        searchPosition(curLevel);
        //游戏关卡移动步数清零
        moveTimes = 0;
    }

    /**
     * 寻找角色初始位置
     *
     */
    public void searchPosition(int[][] curMap) {
        //寻找角色初始位置
        for (int i = 0; i < curMap.length; i++) {
            for (int j = 0; j < curMap[0].length; j++) {
                if (curMap[i][j] == PLAYER) {
                    prePosition = new Point(i, j);
                    break;
                }
            }
        }
    }
    
    /**
     * 选择关卡
     *
     */
    public void selectLevel(int level) {
        //iCurLevel当前的地图关数
        iCurLevel = level;
        int len = levels.length;
            if (iCurLevel < 0 || iCurLevel > len - 1) {
                System.out.println("关卡范围为1~" + len +"；您输入的关卡数字是"+iCurLevel+"无效，请重新输入，谢谢！");
                return;
        }
        //初始当前等级关卡
        init();
    }

    /**
     * 显示关卡
     *
     */
    public void showMoveInfo() {
        int guankashu=iCurLevel;
        System.out.println("当前关卡：" + guankashu);
        System.out.println("当前步数：" + moveTimes);
        System.out.println("w：往前一步   s：往后一步   a：往左一步   d：往右一步");
        System.out.println("r：回退到上一步   e：选择关卡");
    }

    /**
     * 小人移动
     *
     * dir 移动方向
     */
    public void go(String dir) {
        preMap = copyArray(curMap);

        Point p1 = null, p2 = null;
        switch (dir) {
            case "up":
                //获取小人前面的两个坐标位置来进行判断小人是否能够移动
                p1 = new Point(prePosition.x - 1, prePosition.y);
                p2 = new Point(prePosition.x - 2, prePosition.y);
                break;
            case "down":
                p1 = new Point(prePosition.x + 1, prePosition.y);
                p2 = new Point(prePosition.x + 2, prePosition.y);
                break;
            case "left":
                p1 = new Point(prePosition.x, prePosition.y - 1);
                p2 = new Point(prePosition.x, prePosition.y - 2);
                break;
            case "right":
                p1 = new Point(prePosition.x, prePosition.y + 1);
                p2 = new Point(prePosition.x, prePosition.y + 2);
                break;
            default:
                break;
        }
        //若果小人能够移动的话，更新游戏数据，并重绘地图
        if (tryGo(p1, p2)) {
            moveTimes++;
            repeal = 1;

            //重绘当前更新了数据的地图
            drawMap();
            showMoveInfo();

            //若果移动完成了进入下一关
            if (checkFinish()) {
                System.out.println("恭喜过关！！");
                selectLevel(iCurLevel + 1);
            }
            if (moveTimes == maxSteps) {
                System.out.println("ゲームオーバー");
                selectLevel(iCurLevel);
            }
        }
    }

    /**
     * 输出地图
     */
    public void drawMap() {
        for (int i = 0; i < curMap.length; i++) {
            for (int j = 0; j < curMap[0].length; j++) {
                System.out.print(MAP.get(curMap[i][j]));
            }
            System.out.println();
        }
    }

    /**
     * 撤回操作
     */
    public void repeal() {
        if (repeal != 0) {
            repeal = 0;
            moveTimes--;
            curMap = copyArray(preMap);
            searchPosition(curMap);
            drawMap();
            showMoveInfo();
        } else {
            System.out.println("当前不能进行撤回操作！");
        }
    }



    /**
     * 判断是否推成功
     *
     * true 推成功 false 推失败
     */
    public boolean checkFinish() {
        for (int i = 0; i < curMap.length; i++) {
            for (int j = 0; j < curMap[i].length; j++) {
                //当前移动过的地图和初始地图进行比较，若果初始地图上的陷进参数在移动之后不是箱子的话就指代没推成功
                if (curLevel[i][j] == GOAL && curMap[i][j] != BOX) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 判断小人是否能够移动
     *
     * p1 小人前面的坐标
     * p2 小人前面的前面的坐标
     * true 能够移动 false 不能移动
     */
    public boolean tryGo(Point p1, Point p2) {
        if (p1.x < 0 || p1.y < 0 || p1.x > curMap.length || p1.y > curMap[0].length || curMap[p1.x][p1.y] == WALL) {
            return false;
        }
        //若果小人前面是箱子那就还需要判断箱子前面有没有障碍物(箱子/墙)
        if (curMap[p1.x][p1.y] == BOX) {
            if (curMap[p2.x][p2.y] == WALL || curMap[p2.x][p2.y] == BOX) {
                return false;
            }
            //若果判断不成功小人前面的箱子前进一步
            //更改地图对应坐标点的值
            curMap[p2.x][p2.y] = BOX;
        }
        //若果都没判断成功小人前进一步
        //更改地图对应坐标点的值
        curMap[p1.x][p1.y] = PLAYER;

        //若果小人前进了一步，小人原来的位置如何显示
        int v = curLevel[prePosition.x][prePosition.y];
        //小人移开之后之前小人的位置改为地板
        if (v != GOAL) {
            v = SPACE;
        }
        curMap[prePosition.x][prePosition.y] = v;
        //若果判断小人前进了一步，更新坐标值
        prePosition = p1;
        //若果小动了 返回true 指代能够移动小人
        return true;
    }


    /**
     * 获取操作输入
     */
    public void getInput() {
        System.out.print("请输入一个字符：");
        // 读取字符
        action = scanner.next().charAt(0);
    }

    /**
     * 选择关卡
     */
    public void getLevelFromInput() {

        System.out.print("请输入要跳转的关卡：");
        // 读取字符
        int toLevel = scanner.nextInt();
        selectLevel(toLevel-1);
    }

    /**
     * 根据输入的字符执行对应的操作
     */
    public void execute() {
        switch (action) {
            case 'a':
                go("left");
                break;
            case 'w':
                go("up");
                break;
            case 'd':
                go("right");
                break;
            case 's':
                go("down");
                break;
            case 'r':
                repeal();
                break;
            case 'e':
                getLevelFromInput();
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {

        // 定义地图的大小（例如16x16的地图）
        int mapSize = 16;
        int numberOfMaps = 100;

        // 初始化levels数组，存储100个16x16的地图
        levels = new int[numberOfMaps][mapSize][mapSize];

        // 读取文件并解析
        try {
            loadMapsFromFile("C:/Users/10424/Desktop/postgraduate/maps.txt", numberOfMaps, mapSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SokobanGame sokobanGame = new SokobanGame();

        sokobanGame.init();
        while (true) {
            sokobanGame.getInput();
            sokobanGame.execute();
        }
    }
}
