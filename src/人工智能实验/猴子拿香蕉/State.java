package 人工智能实验.猴子拿香蕉;

import java.util.ArrayList;

/**
 * @author ajacker
 */
class State {
    /**
     * 猴子水平位置
     */
    private int W;
    /**
     * 箱子水平位置
     */
    private int Y;
    /**
     * 猴子是否在箱子上
     */
    private boolean isTop;
    /**
     * 猴子是否取到香蕉了
     */
    private boolean isGet;
    /**
     * 香蕉位置
     */
    int c;
    /**
     * 记录操作步骤
     */
    private ArrayList<State> route;
    /**
     * 控制可用操作
     */
    private boolean canPush, canClimb, canGrasp;

    State(int W, int Y, int c, boolean isTop, boolean isGet) {
        this.W = W;
        this.Y = Y;
        this.c = c;
        this.isTop = isTop;
        this.isGet = isGet;
        route = new ArrayList<>();
    }

    void printStates() {
        System.out.println("W\tY\tisTop\tisGet\t");
        System.out.println("------------------------------");
        for (State state : route) {
            System.out.println(state.W + "\t" + state.Y + "\t" + String.valueOf(state.isTop) + "\t" + String.valueOf(state.isGet));
        }
    }

    private void checkState() {
        if (this.W == this.Y) {
            this.canClimb = true;
            if (!this.isTop) {
                this.canPush = true;
            } else {
                this.canPush = false;
                this.canGrasp = this.W == this.c;
            }
        } else {
            this.canClimb = false;
        }

    }

    void run() {
        route.add(new State(this.W, this.Y, this.c, this.isTop, this.isGet));
        if (this.W < this.Y) {
            for (int i = this.W; i < this.Y; ) {
                goTo(++i);
            }
        } else if (this.W > this.Y) {
            for (int i = this.W; i > this.Y; ) {
                goTo(--i);
            }
        }
        if (this.W < this.c) {
            for (int i = this.W; i < this.c; ) {
                pushBox(++i);
            }
        } else if (this.W > this.c) {
            for (int i = this.W; i > this.c; ) {
                pushBox(--i);
            }
        }
        climbBox();
        grasp();
    }

    /**
     * 猴子改变自身位置
     */
    private void goTo(int U) {
        this.W = U;
        System.out.println("猴子空手走到" + U);
        route.add(new State(this.W, this.Y, this.c, this.isTop, this.isGet));
    }

    /**
     * 猴子把箱子推到V
     */
    private void pushBox(int V) {
        checkState();
        if (this.canPush) {
            this.W = V;
            this.Y = V;
            System.out.println("猴子把箱子推到" + V);
            route.add(new State(this.W, this.Y, this.c, this.isTop, this.isGet));
        }
    }

    private void climbBox() {
        checkState();
        if (this.canClimb) {
            this.isTop = true;
            System.out.println("猴子爬上箱子");
            route.add(new State(this.W, this.Y, this.c, this.isTop, this.isGet));
        }
    }

    private void grasp() {
        checkState();
        if (this.canGrasp) {
            this.isGet = true;
            System.out.println("猴子拿到香蕉");
            route.add(new State(this.W, this.Y, this.c, this.isTop, this.isGet));
        }
    }
}
