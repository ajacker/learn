package 人工智能实验.八数码搜索;


import java.util.*;

import static 人工智能实验.八数码搜索.Direction.*;

/**
 * @author ajacker
 */
class Eight {
    /**
     * 目标状态
     */
    private EightPuzzle targetEp;
    /**
     * 深度
     */
    private int depth = 0;
    /**
     * 查找次数
     */
    private int times = 0;
    /**
     * 深度优先栈
     */
    private Stack<EightPuzzle> stack = new Stack<>();
    /**
     * 已查找过的列表
     */
    private ArrayList<EightPuzzle> searchedList = new ArrayList<>();
    /**
     * 记录查找路径
     */
    private Stack<EightPuzzle> route = new Stack<>();
    /**
     * 广度优先队列
     */
    private Queue<EightPuzzle> queue = new LinkedList<>();

    public static void main(String[] args) {
        Eight e = new Eight();
        e.depthFirstSearch();
        e.breadthFirstSearch();
    }

    private Eight() {
        // 初始化栈和队列，以及列表
        route.clear();
        stack.clear();
        searchedList.clear();
        queue.clear();

        Scanner scanner = new Scanner(System.in);
        // 输入初始位置
        System.out.println("请输入初始位置（其中输入0代表空白块，例如：2 8 3 1 0 4 7 6 5）：");
        int[][] array = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array[i][j] = scanner.nextInt();
            }
        }
        // 输入目标位置
        System.out.println("请输入目标位置（其中输入0代表空白块，例如：2 8 3 1 4 0 7 6 5）：");
        int[][] target = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                target[i][j] = scanner.nextInt();
            }
        }

        EightPuzzle ep = new EightPuzzle(array);
        ep.setDepth(depth);
        // 设置栈底元素
        stack.push(ep);
        targetEp = new EightPuzzle(target);
        scanner.close();
        // 设置队首元素
        queue.offer(ep);
    }

    /**
     * 深度优先搜索
     */
    private void depthFirstSearch() {
        route.clear();
        times = 0;
        System.out.println("深度优先搜索方法路径！");
        if (!searchedList.isEmpty()) {
            searchedList.clear();
        }

        while (!stack.isEmpty()) {
            times++;
            // 取出栈顶元素进行查找
            EightPuzzle newState = stack.pop();
            depth = newState.getDepth();
            newState.getPosition();
            int x = newState.getBlankPosX(), y = newState.getBlankPosY();
            searchedList.add(newState);
            // 如果找到了就停止
            if (newState.isEquals(targetEp)) {
                StringBuilder operation = new StringBuilder();
                // 回朔路径
                while (newState != null) {
                    route.push(newState);
                    newState = newState.getParent();
                }
                while (!route.isEmpty()) {
                    EightPuzzle e = route.pop();
                    e.print();
                    String content = e.getDirect() == null ? "" : e.getDirect().toString();
                    operation.append(content);
                }
                System.out.println("操作：" + operation.toString());
                System.out.println("找到目标");
                System.out.println("查找深度：" + depth);
                System.out.println("查找次数：" + times);
                return;
            }
            depth++;// 增加深度
            // 尝试进行移动
            stackMove(newState, x, y, UP);
            stackMove(newState, x, y, RIGHT);
            stackMove(newState, x, y, DOWN);
            stackMove(newState, x, y, LEFT);
        }
        System.out.println("没有搜索到目标状态");
        System.out.println("查找深度：" + depth);
    }

    /**
     * 宽度（广度）优先搜索实现
     */
    private void breadthFirstSearch() {
        route.clear();
        times = 0;
        if (!searchedList.isEmpty()) {
            searchedList.clear();
        }
        System.out.println("广度优先搜索方法路径！");
        while (!queue.isEmpty()) {
            times++;
            // 取出队尾元素进行查找
            EightPuzzle newState = queue.poll();
            depth = newState.getDepth();
            newState.getPosition();
            int x = newState.getBlankPosX(), y = newState.getBlankPosY();
            searchedList.add(newState);
            // 如果找到了就停止
            if (newState.isEquals(targetEp)) {
                StringBuilder operation = new StringBuilder();
                // 回朔路径
                while (newState != null) {
                    route.push(newState);
                    newState = newState.getParent();
                }
                while (!route.isEmpty()) {
                    EightPuzzle e = route.pop();
                    e.print();
                    String content = e.getDirect() == null ? "" : e.getDirect().toString();
                    operation.append(content);
                }
                System.out.println("操作：" + operation.toString());
                System.out.println("找到目标");
                System.out.println("查找深度：" + depth);
                System.out.println("查找次数：" + times);
                return;
            }
            depth++;// 增加深度
            // 尝试进行移动
            queueMove(newState, x, y, UP);
            queueMove(newState, x, y, RIGHT);
            queueMove(newState, x, y, DOWN);
            queueMove(newState, x, y, LEFT);
        }
        System.out.println("没有搜索到目标状态");
        System.out.println("查找次数：" + depth);
    }

    /**
     * 入栈方式的扩展
     *
     * @param newState 当前状态
     * @param x        空格x坐标
     * @param y        空格y坐标
     * @param direct   移动方向
     */
    private void stackMove(EightPuzzle newState, int x, int y, Direction direct) {
        EightPuzzle temp;
        if (EightPuzzleOperator.canMove(x, y, direct)) {
            temp = EightPuzzleOperator.movePosition(newState, direct);
            temp.setDepth(depth);
            // 如果没有搜索过就压入栈
            if (!searchedList.contains(temp)) {
                stack.push(temp);
            }
        }
    }

    /**
     * 队列方式的扩展
     *
     * @param newState 当前状态
     * @param x        空格x坐标
     * @param y        空格y坐标
     * @param direct   移动方向
     */
    private void queueMove(EightPuzzle newState, int x, int y, Direction direct) {
        EightPuzzle temp;
        if (EightPuzzleOperator.canMove(x, y, direct)) {
            temp = EightPuzzleOperator.movePosition(newState, direct);
            temp.setDepth(depth);
            // 如果没有搜索过就进队列
            if (!searchedList.contains(temp)) {
                queue.offer(temp);
            }
        }
    }
}





