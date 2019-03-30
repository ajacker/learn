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
     * A*要用的open表
     */
    private ArrayList<EightPuzzle> open = new ArrayList<>();
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
        Eight eight = new Eight();
        try {
            eight.breadthFirstSearch();
            eight.depthFirstSearch();
            eight.AStarSearch();
        } catch (CloneNotSupportedException e1) {
            e1.printStackTrace();
        }
    }

    private Eight() {
        // 初始化栈和队列，以及列表
        route.clear();
        stack.clear();
        open.clear();
        searchedList.clear();
        queue.clear();

        Scanner scanner = new Scanner(System.in);
        // 输入初始位置
        System.out.println("请输入初始位置（其中输入0代表空白块，例如：8 2 3 1 0 4 7 6 5）：");
        int[][] array = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array[i][j] = scanner.nextInt();
            }
        }
        // 输入目标位置
        System.out.println("请输入目标位置（其中输入0代表空白块，例如：1 2 3 4 5 6 7 8 0）：");
        int[][] target = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                target[i][j] = scanner.nextInt();
            }
        }
        EightPuzzle ep = new EightPuzzle(array);
        ep.setDepth(depth);
        targetEp = new EightPuzzle(target);
        ep.init(targetEp);
        scanner.close();
        // 设置栈底元素
        stack.push(ep);
        // 设置队首元素
        queue.offer(ep);
        open.add(ep);
    }

    /**
     * 深度优先搜索
     */
    private void depthFirstSearch() throws CloneNotSupportedException {
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
            // 判断是否可解
            if (newState.isUnSolvable(targetEp)) {
                System.out.println("目标不可解");
                return;
            }
            depth = newState.getDepth();
            newState.getPosition();
            int x = newState.getBlankPosX(), y = newState.getBlankPosY();
            searchedList.add(newState);
            // 如果找到了就停止
            if (searchCompleted(newState)) {
                return;
            }
            depth++;// 增加深度
            // 尝试进行移动
            move(newState, 1, x, y, UP);
            move(newState, 1, x, y, RIGHT);
            move(newState, 1, x, y, DOWN);
            move(newState, 1, x, y, LEFT);
        }
        System.out.println("没有搜索到目标状态");
        System.out.println("查找深度：" + depth);
    }

    /**
     * 宽度（广度）优先搜索
     */
    private void breadthFirstSearch() throws CloneNotSupportedException {
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
            // 判断是否可解
            if (newState.isUnSolvable(targetEp)) {
                System.out.println("目标不可解");
                return;
            }
            depth = newState.getDepth();
            newState.getPosition();
            int x = newState.getBlankPosX(), y = newState.getBlankPosY();
            searchedList.add(newState);
            // 如果找到了就停止
            if (searchCompleted(newState)) {
                return;
            }
            depth++;// 增加深度
            // 尝试进行移动
            move(newState, 2, x, y, UP);
            move(newState, 2, x, y, RIGHT);
            move(newState, 2, x, y, DOWN);
            move(newState, 2, x, y, LEFT);
        }
        System.out.println("没有搜索到目标状态");
        System.out.println("查找次数：" + depth);
    }

    private void AStarSearch() throws CloneNotSupportedException {
        route.clear();
        times = 0;
        if (!searchedList.isEmpty()) {
            searchedList.clear();
        }
        System.out.println("A*搜索方法路径！");
        while (!open.isEmpty()) {
            times++;
            // 取出最小估计函数值进行查找
            Collections.sort(open);
            EightPuzzle newState = open.get(0);
            // 判断是否可解
            if (newState.isUnSolvable(targetEp)) {
                System.out.println("目标不可解");
                return;
            }
            open.remove(0);
            depth = newState.getDepth();
            newState.getPosition();
            int x = newState.getBlankPosX(), y = newState.getBlankPosY();
            searchedList.add(newState);
            // 如果找到了就停止
            if (searchCompleted(newState)) {
                return;
            }
            depth++;// 增加深度
            // 尝试进行移动
            move(newState, 3, x, y, UP);
            move(newState, 3, x, y, RIGHT);
            move(newState, 3, x, y, DOWN);
            move(newState, 3, x, y, LEFT);
        }
    }

    private boolean searchCompleted(EightPuzzle state) {
        if (state.equals(targetEp)) {
            StringBuilder operation = new StringBuilder();
            // 回朔路径
            while (state != null) {
                route.push(state);
                state = state.getParent();
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
            return true;
        } else {
            return false;
        }
    }

    /**
     * 尝试移动空白格，扩展状态
     *
     * @param newState 当前状态
     * @param type     移动类型(1:深度优先;2:广度优先;3:A*算法)
     * @param x        空格x坐标
     * @param y        空格y坐标
     * @param direct   移动方向
     */
    private void move(EightPuzzle newState, int type, int x, int y, Direction direct) throws CloneNotSupportedException {
        EightPuzzle temp;
        switch (type) {
            //深度优先
            case 1:
                if (EightPuzzleOperator.canMove(x, y, direct)) {
                    temp = EightPuzzleOperator.movePosition(newState, direct);
                    temp.setDepth(depth);
                    // 如果没有搜索过就压入栈
                    if (!searchedList.contains(temp)) {
                        stack.push(temp);
                    }
                }
                break;
            //广度优先
            case 2:
                if (EightPuzzleOperator.canMove(x, y, direct)) {
                    temp = EightPuzzleOperator.movePosition(newState, direct);
                    temp.setDepth(depth);
                    // 如果没有搜索过就进队列
                    if (!searchedList.contains(temp)) {
                        queue.offer(temp);
                    }
                }
                break;
            //A*算法
            case 3:
                if (EightPuzzleOperator.canMove(x, y, direct)) {
                    temp = EightPuzzleOperator.movePosition(newState, direct);
                    temp.setDepth(depth);
                    // 如果没有搜索过就进队列
                    if (!searchedList.contains(temp)) {
                        temp.init(targetEp);
                        open.add(temp);
                    }
                }
                break;
            default:
        }

    }
}





