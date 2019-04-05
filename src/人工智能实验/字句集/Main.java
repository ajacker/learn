package 人工智能实验.字句集;

import java.util.*;
//符号代替申明
//蕴含符号：⇒
//非：¬
//合取符号：∧
//析取符号：∨
//全称量词：∀
//存在量词：∃
//括号（英文状态）：()
//变量=m-z 常量a-l
/**
 * @author ajacker
 */
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //String input = scan.nextLine();
        String input = "(∀x)(P(x)⇒((∀y)(P(y)⇒P(f(x,y)))∧¬(∀y)(Q(x,y)⇒P(y))))";
        //input = "(∀x)(P(x)⇒((∀y)(P(y)⇒P(f(x,y)))∧(∀y)¬((P(x)∨G(x))∧(B(x)∨F(x)))))";
        System.out.println("输入：" + input);
        System.out.println("------------------------------");
        String first = firstStep(input);
        System.out.println("---------------消去蕴涵符号---------------");
        System.out.println("第一步结果：" + first);
        System.out.println("------------------------------");
        String second = secondStep(first);
        System.out.println("---------------减少否定符号辖域---------------");
        System.out.println("第二步结果：" + second);
        System.out.println("------------------------------");
        String third = thirdStep(second);
        System.out.println("---------------对变量标准化---------------");
        System.out.println("第三步结果：" + third);
    }

    static String thirdStep(String str) {
        ArrayList<Character> used = new ArrayList<>();
        char[] variables = {'m', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        StringBuilder sb = new StringBuilder(str);
        int last = -1;
        while (true) {
            last = sb.indexOf("(", last + 1);
            if (last == -1) {
                break;
            }
            Stack<Integer> stack = new Stack<>();
            if (sb.charAt(last + 1) == '∀' || sb.charAt(last + 1) == '∃') {
                int i = last + 1;
                int start = i;
                while (sb.charAt(i) != '(') {
                    i++;
                }
                do {
                    if (sb.charAt(i) == '(') {
                        stack.push(i);
                    } else if (sb.charAt(i) == ')') {
                        stack.pop();
                    }
                    if (!stack.isEmpty()) {
                        i++;
                    }
                } while (!stack.isEmpty());
                int end = i;
                for (int j = start; j <= end; j++) {
                    boolean flag = false;
                    char current = sb.charAt(j);
                    //判断是否已经被使用过
                    for (char c : used) {
                        if (current == c) {
                            flag = true;
                        }
                    }
                    //如果使用过就替换
                    if (flag) {
                        //选出没有使用过的
                        for (char c : variables) {
                            if (!used.contains(c)) {
                                if (current == str.charAt(last + 2)) {
                                    sb.setCharAt(j, c);
                                }
                                break;
                            }
                        }
                    }
                }
                used.add(sb.charAt(last + 2));
            }
            System.out.println("第三步过程:" + sb.toString());
        }
        return sb.toString();
    }
    static String secondStep(String str) {
        StringBuilder sb = new StringBuilder(str);
        int last;
        while (true) {
            last = sb.indexOf("¬(");
            if (last == -1) {
                break;
            }
            Stack<Integer> stack = new Stack<>();
            String temp = sb.substring(last);
            int i = 0;
            while (temp.charAt(i) != '(') {
                i++;
            }
            do {
                if (temp.charAt(i) == '(') {
                    stack.push(i);
                } else if (temp.charAt(i) == ')') {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    i++;
                }
            } while (!stack.isEmpty());
            //这是括号的内容
            String content = temp.substring(2, i);
            //后面的内容
            String after = temp.substring(i + 1);
            //要替换的内容
            String toReplace;
            //要用来替换的
            String replace = "";
            if (content.charAt(0) == '∀' || content.charAt(0) == '∃') {
                int j = 0;
                while (after.charAt(j) != '(') {
                    j++;
                }
                do {
                    if (after.charAt(j) == '(') {
                        stack.push(j);
                    } else if (after.charAt(j) == ')') {
                        stack.pop();
                    }
                    if (!stack.isEmpty()) {
                        j++;
                    }
                } while (!stack.isEmpty());
                after = after.substring(0, j + 1);
                toReplace = temp.substring(0, i + 1) + after;
                switch (content.charAt(0)) {
                    case '∀':
                        replace = "(∃" + content.charAt(1) + ")¬" + after;
                        break;
                    case '∃':
                        replace = "(∀" + content.charAt(1) + ")¬" + after;
                        break;
                    default:
                }
            } else if (content.charAt(0) == '¬') {
                toReplace = temp.substring(0, i + 1);
                int j = 0;
                while (toReplace.charAt(j) != '(') {
                    j++;
                }
                do {
                    if (toReplace.charAt(j) == '(') {
                        stack.push(j);
                    } else if (toReplace.charAt(j) == ')') {
                        stack.pop();
                    }
                    if (!stack.isEmpty()) {
                        j++;
                    }
                } while (!stack.isEmpty());
                replace = "(" + reverse(toReplace.substring(2, j)) + ")";
            } else {
                toReplace = temp.substring(0, i + 1);
                int j = 0;
                while (content.charAt(j) != '(') {
                    j++;
                }
                do {
                    if (content.charAt(j) == '(') {
                        stack.push(j);
                    } else if (content.charAt(j) == ')') {
                        stack.pop();
                    }
                    if (!stack.isEmpty()) {
                        j++;
                    }
                } while (!stack.isEmpty());
                String first = content.substring(0, j + 1);
                String second = content.substring(j + 2);
                switch (content.charAt(j + 1)) {
                    case '∧':
                        replace = "(¬" + first + '∨' + "¬" + second + ")";
                        break;
                    case '∨':
                        replace = "(¬" + first + '∧' + "¬" + second + ")";
                        break;
                    default:
                }
            }
            sb.replace(last, last + toReplace.length(), replace);
            //System.out.println("要替换的:"+toReplace);
            //System.out.println("用于替换的:"+replace);
            System.out.println("第二步过程:" + sb.toString());
        }
        return sb.toString();
    }

    static String reverse(String str) {
        StringBuilder sb = new StringBuilder(str);
        int last = -1;
        while (true) {
            int a = sb.indexOf("∨", last + 1) == -1 ? str.length() : sb.indexOf("∨", last + 1);
            int b = sb.indexOf("∧", last + 1) == -1 ? str.length() : sb.indexOf("∧", last + 1);
            if (a == str.length() && b == str.length()) {
                break;
            }
            last = Integer.min(a, b);
            if (sb.charAt(last) == '∨') {
                sb.setCharAt(last, '∧');
            } else {
                sb.setCharAt(last, '∨');
            }
            String[] strs = new String[2];
            strs[0] = sb.substring(0, last);
            strs[1] = sb.substring(last + 1);
            Stack<Integer> stack = new Stack<>();
            String before = strs[0];
            String after = strs[1];
            //记录前向匹配的括号位置
            int i = before.length() - 1;
            ;
            do {
                if (before.charAt(i) == ')') {
                    stack.push(i);
                } else if (before.charAt(i) == '(') {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    i--;
                }
            } while (!stack.isEmpty());
            //匹配的括号前面是字母
            if (i - 1 >= 0 && before.charAt(i - 1) >= 'A' && before.charAt(i - 1) <= 'Z') {
                if (i - 2 >= 0 && before.charAt(i - 2) == '¬') {
                    sb.deleteCharAt(i - 2);
                } else {
                    sb.insert(i - 1, '¬');
                }
            } else if (i - 1 >= 0 && before.charAt(i - 1) == '¬') {
                sb.deleteCharAt(i - 1);
            } else {
                sb.insert(i, '¬');
            }
            stack.clear();
            int j = 0;
            while (after.charAt(j) != '(') {
                j++;
            }
            if (j - 1 >= 0 && after.charAt(j - 1) >= 'A' && after.charAt(j - 1) <= 'Z') {
                if (j - 2 >= 0 && after.charAt(j - 2) == '¬') {
                    sb.deleteCharAt(last + j - 2);
                } else {
                    sb.insert(last + j - 1, '¬');
                }
            } else if (j - 1 >= 0 && before.charAt(j - 1) == '¬') {
                sb.deleteCharAt(last + j - 1);
            } else {
                sb.insert(last + j, '¬');
            }
        }
        return sb.toString();
    }

    static String firstStep(String str) {//消去蕴含符号
        StringBuilder sb = new StringBuilder(str);
        int last;
        while (true) {
            last = sb.indexOf("⇒");
            if (last == -1) {
                break;
            }
            sb.setCharAt(last, '∨');
            String[] strs = new String[2];
            strs[0] = sb.substring(0, last);
            strs[1] = sb.substring(last + 1);
            Stack<Integer> stack = new Stack<>();
            String before = strs[0];
            String after = strs[1];
            //记录前向匹配的括号位置
            int i = before.length() - 1;
            ;
            do {
                if (before.charAt(i) == ')') {
                    stack.push(i);
                } else if (before.charAt(i) == '(') {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    i--;
                }
            } while (!stack.isEmpty());
            //匹配的括号前面是字母
            if (i - 1 >= 0 && before.charAt(i - 1) >= 'A' && before.charAt(i - 1) <= 'Z') {
                if (before.charAt(i - 2) == '¬') {
                    sb.deleteCharAt(i - 2);
                } else {
                    sb.insert(i - 1, '¬');
                }
            } else if (before.charAt(i - 1) == '¬') {
                sb.deleteCharAt(i - 1);
            } else {
                sb.insert(i, '¬');
            }
            stack.clear();
            int j = 0;
            while (after.charAt(j) != '(') {
                j++;
            }
            do {
                if (after.charAt(j) == '(') {
                    stack.push(j);
                } else if (after.charAt(j) == ')') {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    j++;
                }
            } while (!stack.isEmpty());
            System.out.println("第一步过程:" + sb.toString());
        }
        return sb.toString();
    }
}
