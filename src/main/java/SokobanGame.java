import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * @author 310
 */
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
    /**
     * 游戏是否已失败
     */
    public boolean is_lost = false;

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
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
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
        //游戏是否失败置为false
        is_lost = false;
    }

    /**
     * 寻找角色初始位置
     *
     * @param curMap
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

    public void selectLevel(int level) {
        //iCurLevel当前的地图关数
        iCurLevel = level;
        int len = levels.length;
        if (iCurLevel < 0 || iCurLevel > len - 1) {
            System.out.println("关卡范围为1~" + len);
            return;
        }
        //初始当前等级关卡
        init();
    }

    /**
     * 小人移动
     *
     * @param dir 移动方向
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
            if(checkBox()){
                is_lost = true;
                System.out.println("游戏已失败，请重置或跳转关卡！");
            }else{
                is_lost = false;
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
            is_lost = checkBox();
            curMap = copyArray(preMap);
            searchPosition(curMap);
            drawMap();
            showMoveInfo();
        } else {
            System.out.println("当前不能进行撤回操作！");
        }
    }

    public void showMoveInfo() {
        System.out.println("当前关卡：" + (iCurLevel + 1));
        System.out.println("当前步数：" + moveTimes);
        System.out.println("1：围墙   2：目标点   3：箱子    4：人物");
    }


    /**
     * 判断是否推成功
     *
     * @return true 推成功 false 推失败
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
     * 使用DFS检测图中是否存在环
     *
     * @return true 发现环 false 无环
     */
    public static boolean hasCycle(Map<Integer, List<Integer>> graph) {
        // 记录节点的访问状态：1 - 正在访问，2 - 已访问
        Map<Integer, Integer> visitStatus = new HashMap<>();
        for (Integer node : graph.keySet()) { // 对图中的每个节点进行DFS遍历
            if (!visitStatus.containsKey(node)) {
                if (dfs(graph, visitStatus, node)) {
                    return true; // 发现环
                }
            }
        }

        return false; // 未发现环
    }
    /**
     * 深度优先搜索辅助函数
     *
     * @return true 发现环 false 无环
     */
    private static boolean dfs(Map<Integer, List<Integer>> graph, Map<Integer, Integer> visitStatus, int currentNode) {
        // 标记当前节点为正在访问
        visitStatus.put(currentNode, 1);
        // 遍历当前节点的所有邻居节点
        for (Integer neighbor : graph.getOrDefault(currentNode, Collections.emptyList())) {
            if (!visitStatus.containsKey(neighbor)) { // 如果邻居节点未访问过，则继续DFS
                if (dfs(graph, visitStatus, neighbor)) {
                    return true; // 发现环
                }
            } else if (visitStatus.get(neighbor) == 1) { // 如果邻居节点正在被访问，则发现环
                return true;
            }
        }
        // 标记当前节点为已访问
        visitStatus.put(currentNode, 2);
        return false; // 未发现环
    }
    /**
     * 检测边界行是否封闭，若封闭则游戏已经失败
     *
     * @return true 封闭 false 不封闭
     */
    public boolean checkLow(int i, int j, int add){
        int front=0,back=curMap[i].length-1;
        for(int k=j-1;k>=0;k--){
            if(curMap[i][k] == WALL){
                front=k;
                break;
            }
            else if(curMap[i+add][k] != WALL){
                return false;
            }
        }
        for(int k=j+1;k<curMap[i].length;k++){
            if(curMap[i][k] == WALL){
                back=k;
                break;
            }
            else if(curMap[i+add][k] != WALL){
                return false;
            }
        }
        int goals=0,boxs=0;
        for(int k=front;k<=back;k++){
            if(curMap[i][k] == BOX){
                boxs++;
            }
            if(curLevel[i][k] == GOAL){
                goals++;
            }
        }
        return goals < boxs;
    };
    /**
     * 检测边界列是否封闭，若封闭则游戏已经失败
     *
     * @return true 封闭 false 不封闭
     */
    public boolean checkCol(int i, int j, int add){
        int front=0,back=curMap.length-1;
        for(int k=i-1;k>=0;k--){
            if(curMap[k][j] == WALL){
                front=k;
                break;
            }
            else if(curMap[k][j+add] != WALL){
                return false;
            }
        }
        for(int k=i+1;k<curMap.length;k++){
            if(curMap[k][j] == WALL){
                back=k;
                break;
            }
            else if(curMap[k][j+add] != WALL){
                return false;
            }
        }
        int goals=0, boxs=0;
        for(int k=front;k<=back;k++){
            if(curMap[k][j] == BOX){
                boxs++;
            }
            if(curLevel[k][j] == GOAL){
                goals++;
            }
        }
        return goals < boxs;
    };
    /**
     * 判断游戏是否已经失败，即箱子无法移动且不在目标点上
     *
     * @return true已经失败  false 推失败
     */
    public boolean checkBox() {
        Map<Integer, Integer> box_pos =new HashMap<>();
        Map<Integer, List<Integer>> graph=new HashMap<>();
        int start=0;
        for (int i = 0; i < curMap.length; i++) {
            for (int j = 0; j < curMap[i].length; j++) {
                //当前移动过的地图和初始地图进行比较，若果初始地图上的陷进参数在移动之后不是箱子的话就指代没推成功
                if (curLevel[i][j] != GOAL && curMap[i][j] == BOX) {
                    boolean up=false, down=false, left=false, right=false;
                    if(curMap[i+1][j] == WALL) {
                        up=true;
                        if (checkLow(i,j,1)){
                            return true;
                        }
                    }
                    if(curMap[i-1][j] == WALL ) {
                        down=true;
                        if (checkLow(i,j,-1)){
                            return true;
                        }
                    }
                    if(curMap[i][j-1] == WALL) {
                        left=true;
                        if (checkCol(i,j,-1)){
                            return true;
                        }
                    }
                    if(curMap[i][j+1] == WALL) {
                        right=true;
                        if (checkCol(i,j,+1)){
                            return true;
                        }
                    }
                    if((up||down)&&(left||right)) {
                        return true;
                    }
                    else{
                        box_pos.put(100*i+j, start);
                        graph.put(start++,new ArrayList<>());
                    }
                }
            }
        }
        for (Integer p : box_pos.keySet()) {
            int i=p/100, j=p-100*i;
            if (curLevel[i][j] != GOAL && curMap[i][j] == BOX) {
                boolean up=false, down=false, left=false, right=false;
                if(curMap[i-1][j] == WALL||curMap[i-1][j] == BOX) { up=true;}
                if(curMap[i+1][j] == WALL || curMap[i+1][j] == BOX) { down=true; }
                if(curMap[i][j-1] == WALL || curMap[i][j-1] == BOX) { left=true; }
                if(curMap[i][j+1] == WALL || curMap[i][j+1] == BOX) { right=true; }
                if((up||down)&&(left||right)) {
                    if(curMap[i-1][j] == BOX){
                        graph.get(box_pos.get(p)).add(box_pos.get(100*(i-1)+j));
                    }
                    if(curMap[i+1][j] == BOX){
                        graph.get(box_pos.get(p)).add(box_pos.get(100*(i+1)+j));
                    }
                    if(curMap[i][j-1] == BOX){
                        graph.get(box_pos.get(p)).add(box_pos.get(100*i+j-1));
                    }
                    if(curMap[i][j+1] == BOX){
                        graph.get(box_pos.get(p)).add(box_pos.get(100*i+j+1));
                    }
                }
            }
        }
        if (hasCycle(graph)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * 判断小人是否能够移动
     *
     * @param p1 小人前面的坐标
     * @param p2 小人前面的前面的坐标
     * @return true 能够移动 false 不能移动
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
        while (true) { // 持续循环直到输入有效
            System.out.print("请输入要跳转的关卡：");
            try {
                double Temp = scanner.nextDouble();  // 读取输入
                int toLevel = (int) Temp;            // 将小数部分舍弃
                // 检查关卡范围
                if (toLevel < 1 || toLevel > levels.length) {
                    System.out.println("关卡范围为1~" + levels.length + "，请重新输入。");
                    continue; // 继续循环以获取新的输入
                }
                // 调整输入关卡到从0开始的索引
                selectLevel(toLevel - 1);
                break; // 输入有效，跳出循环
            } catch (InputMismatchException e) {
                System.out.println("输入无效，请输入一个数字。");
                scanner.next(); // 清空无效输入
            }
        }
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
            case 'p':
                showGameGuide();
                break;
            case 't':
                selectLevel(iCurLevel);
                break;
            default:
                break;
        }
    }

    public void execute(Character action) {
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

    public void showGameGuide() {
        System.out.println("\n\uD83D\uDC4B这是一个推箱子小游戏，游戏目标是将\uD83D\uDCE6推到⭐处。");
        System.out.println("\u001B[34ma\u001B[0m左移，\u001B[34md\u001B[0m右移，\u001B[34mw\u001B[0m上移，\u001B[34ms\u001B[0m下移，\u001B[34mr\u001B[0m撤销，\u001B[34me\u001B[0m选择关卡，\u001B[34mp\u001B[0m输出本条消息，\u001B[34mt\u001B[0m重新开始\n");
    }

    public static void main(String[] args) {

        // 定义地图的大小（例如16x16的地图）
        int mapSize = 16;
        int numberOfMaps = 100;

        // 初始化levels数组，存储100个16x16的地图
        levels = new int[numberOfMaps][mapSize][mapSize];

        // 读取文件并解析
        try {
            loadMapsFromFile("maps.txt", numberOfMaps, mapSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SokobanGame sokobanGame = new SokobanGame();

        sokobanGame.init();
        sokobanGame.showGameGuide();
        while (true) {
            sokobanGame.getInput();
            sokobanGame.execute();
        }
    }
}
