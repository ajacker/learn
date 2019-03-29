package 人工智能实验.猴子拿香蕉;

import java.util.Scanner;

/**
 * @author ajacker
 */
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入猴子水平位置:");
        int W = scan.nextInt();
        System.out.println("请输入箱子水平位置:");
        int Y = scan.nextInt();
        System.out.println("请输入香蕉水平位置:");
        int c = scan.nextInt();
        State s1 = new State(W, Y, c, false, false);
        s1.run();
        s1.printStates();
    }
}
