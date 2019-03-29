package 人工智能实验.汉诺塔;

import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Character;

/**
 * @author ajacker
 */
public class hannoi {
    static int count = 0;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("输入第一个塔的圆盘个数：");
        int n = scan.nextInt();
        System.out.println("输入塔的个数(>3)：");
        int i = scan.nextInt();
        ArrayList<Character> chars = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            chars.add((char) ('A' + j));
        }
        move(n, chars.get(0), chars, chars.get(i - 1));
        System.out.println("总步数：" + count);
    }

    private static void move(int n, char a, ArrayList<Character> chars, char c) {
        ArrayList<Character> free = new ArrayList<>();
        ArrayList<Character> temp = new ArrayList<>();
        for (Character chr : chars) {
            if (chr != a && chr != c) {
                free.add(chr);
            }
        }

        for (Character chr : chars) {
            if (chr != a && !chr.equals(free.get(0))) {
                temp.add(chr);
            }
        }
        if (n < chars.size()) {
            if (n > free.size()) {
                for (Character ch : free) {
                    System.out.println(a + "->" + ch);
                    count++;
                }
                System.out.println(a + "->" + c);
                count++;
                for (int j = free.size() - 1; j >= 0; j--) {
                    System.out.println(free.get(j) + "->" + c);
                    count++;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    System.out.println(a + "->" + free.get(i));
                    count++;
                }
                for (int j = n - 2; j >= 0; j--) {
                    System.out.println(free.get(j) + "->" + c);
                    count++;
                }
                System.out.println(a + "->" + c);
                count++;
            }
            return;
        } else {
            move(n - (chars.size() - 2), a, chars, free.get(0));
            for (Character ch : temp) {
                System.out.println(a + "->" + ch);
                count++;
            }
            for (int j = temp.size() - 2; j >= 0; j--) {
                System.out.println(temp.get(j) + "->" + c);
                count++;
            }
            move(n - (chars.size() - 2), free.get(0), chars, c);
        }
    }
}