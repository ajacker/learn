package 人工智能实验.八数码搜索;

import java.util.Arrays;

/**
 * @author ajacker
 */
public class EightPuzzle implements Cloneable, Comparable<EightPuzzle> {
    /**
     * 二维数组储存八数码状态
     */
    int[][] data;
    /**
     * 空白格的坐标
     */
    private int blankPosX;
    private int blankPosY;
    /**
     * 当前状态深度
     */
    private int depth;


    /**
     * 估计函数f(n)
     */
    private int evaluation;
    /**
     * 启发函数h(n)
     */
    private int misposition;
    /**
     * 父节点
     */
    private EightPuzzle parent;
    /**
     * 这一步操作的方向
     */
    private Direction direct;

    /**
     * 构造状态
     */
    private EightPuzzle() {
        data = new int[3][3];
    }

    EightPuzzle(int[][] data) {
        this.data = data;
    }

    /**
     * 八数码状态转换成输出
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(this.data[i][j]);
                sb.append(" ");
            }
            if (i == 1) {
                sb.append(direct == null ? "" : direct);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 获取空格的位置
     */
    void getPosition() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.data[i][j] == 0) {
                    blankPosX = i;
                    blankPosY = j;
                }
            }
        }
    }

    /**
     * 求估计函数
     *
     * @param target 目标状态
     */
    void init(EightPuzzle target) {
        int temp = 0;
        //记录当前节点与目标节点差异的度量
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (data[i][j] != target.data[i][j]) {
                    temp++;
                }
            }
        }
        this.misposition = temp;
        //设置当前状态的估计值
        this.evaluation = this.getDepth() + this.misposition;
    }


    int getBlankPosX() {
        return blankPosX;
    }

    int getBlankPosY() {
        return blankPosY;
    }

    int getDepth() {
        return depth;
    }

    void setDepth(int depth) {
        this.depth = depth;
    }

    void setParent(EightPuzzle parent) {
        this.parent = parent;
    }

    EightPuzzle getParent() {
        return this.parent;
    }

    Direction getDirect() {
        return direct;
    }

    void setDirect(Direction direct) {
        this.direct = direct;
    }

    void print() {
        System.out.println(this.toString());
    }


    /**
     * 浅拷贝
     */
    @Override
    protected EightPuzzle clone() {
        EightPuzzle clone = null;
        try {
            clone = (EightPuzzle) super.clone();
            clone.data = Arrays.copyOf(this.data, this.data.length);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    /**
     * 深拷贝
     */
    EightPuzzle depthClone() {
        EightPuzzle tmpEp = new EightPuzzle();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(this.data[i], 0, tmpEp.data[i], 0, 3);
        }
        tmpEp.depth = this.depth;
        return tmpEp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EightPuzzle) {
            EightPuzzle ep = (EightPuzzle) obj;
            return Arrays.deepEquals(this.data, ep.data);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(EightPuzzle c) {
        //默认排序为f(n)由小到大排序
        return this.evaluation - c.evaluation;
    }
}
