package 人工智能实验.八数码搜索;

import java.util.Arrays;

/**
 * @author ajacker
 */
public class EightPuzzle implements Cloneable {
    /**
     * 二维数组储存八数码状态
     */
    public int[][] data;
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
     * 构造状态
     */
    private EightPuzzle() {
        data = new int[3][3];
    }

    EightPuzzle(int[][] data) {
        this.data = data;
    }

    /**
     * 判断状态是否相同
     */
    boolean isEquals(EightPuzzle ep) {
        return Arrays.deepEquals(this.data, ep.data);
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
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 获取空格的位置
     */
    void getPostion() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.data[i][j] == 0) {
                    this.setBlankPosX(i);
                    this.setBlankPosY(j);
                }
            }
        }
    }

    /**
     * setters和getters
     */
    private void setBlankPosX(int blankPosX) {
        this.blankPosX = blankPosX;
    }

    private void setBlankPosY(int blankPosY) {
        this.blankPosY = blankPosY;
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

    public void print() {
        System.out.println(this.toString());
    }

    /**
     * 浅拷贝
     */
    @Override
    protected EightPuzzle clone() {
        return new EightPuzzle(Arrays.copyOf(this.data, this.data.length));
    }

    /**
     * 深拷贝
     */
    EightPuzzle depthClone() {
        EightPuzzle tmp = new EightPuzzle();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(this.data[i], 0, tmp.data[i], 0, 3);
        }
        tmp.depth = this.depth;
        return tmp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EightPuzzle) {
            return this.isEquals((EightPuzzle) obj);
        } else {
            return false;
        }
    }

}
