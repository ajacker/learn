package 人工智能实验.决策树;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author ajacker
 */
public class DecisionTree {

    public static void main(String[] args) {
        //用于存储所有属性可能的取值
        //颜色的取值范围
        ArrayList<String> attrColor = new ArrayList<>();
        attrColor.add("yellow");
        attrColor.add("purple");
        //大小的可能取值
        ArrayList<String> attrSize = new ArrayList<>();
        attrSize.add("small");
        attrSize.add("large");
        //act的可能取值
        ArrayList<String> attrAct = new ArrayList<>();
        attrAct.add("dip");
        attrAct.add("stretch");
        //age的可能取值
        ArrayList<String> attrAge = new ArrayList<>();
        attrAge.add("adult");
        attrAge.add("child");


        //属性名与属性的取值进行对应
        HashMap<String, ArrayList<String>> attr = new HashMap<>();
        attr.put("color", attrColor);
        attr.put("size", attrSize);
        attr.put("act", attrAct);
        attr.put("age", attrAge);

        //存储属性的索引， 便于在对数据统计
        HashMap<String, Integer> attrIndex = new HashMap<>();
        attrIndex.put("color", 0);
        attrIndex.put("size", 1);
        attrIndex.put("act", 2);
        attrIndex.put("age", 3);

        //数据集
        String[][] data = {{"yellow", "small", "stretch", "adult", "true"},
                {"yellow", "small", "stretch", "child", "true"},
                {"yellow", "small", "dip", "child", "false"},
                {"yellow", "large", "stretch", "adult", "true"},
                {"yellow", "large", "dip", "adult", "true"},
                {"yellow", "large", "dip", "child", "false"},
                {"purple", "small", "stretch", "child", "true"},
                {"purple", "small", "dip", "adult", "true"},
                {"purple", "small", "dip", "child", "false"},
                {"purple", "large", "stretch", "child", "true"}};
        ID3Tree root = new ID3Tree();
        buildID3Tree(root, data, attr, attrIndex);
        outputID3Tree(root, true);
    }

    /**
     * 构造决策树
     *
     * @param root      树根
     * @param data      数据集
     * @param attr      属性名和属性值对应
     * @param attrIndex 属性索引
     */
    private static void buildID3Tree(ID3Tree root, String[][] data,
                                     HashMap<String, ArrayList<String>> attr, HashMap<String, Integer> attrIndex) {
        Iterator<String> attrIt = attr.keySet().iterator();
        String maxAttr = null;
        String attrName;//属性名称
        HashMap<String, Double> attrValueList = new HashMap<>();
        //用于记录每一个属性的取值在样本中出现的次数
        HashMap<String, Double> attrValueMap = new HashMap<>();
        while (attrIt.hasNext() && (!attr.isEmpty())) {
            attrName = attrIt.next();
            //取得属性可能出现的取值列表
            ArrayList<String> attrList = attr.get(attrName);
            //取得属性的索引值
            int index = attrIndex.get(attrName);
            //用于扫描每一个属性的所有取值
            for (String attrValue : attrList) {
                int isTrue = 0;
                int isFalse = 0;
                //扫描书样本中每一个属性的取值出现的次数
                for (String[] aData : data) {
                    if (aData[index] == null) {
                        break;
                    }
                    if (aData[index].equals(attrValue) && "true".equals(aData[4])) {
                        isTrue++;
                    }
                    if (aData[index].equals(attrValue) && "false".equals(aData[4])) {
                        isFalse++;
                    }
                }
                double num = (-1 * log(((double) isTrue / (double) (isTrue + isFalse))) * ((double) isTrue / (double) (isTrue + isFalse))) - log(((double) isFalse / (double) (isTrue + isFalse))) * ((double) isFalse / (double) (isTrue + isFalse));
                double sum;
                if (Double.compare(num, Double.NaN) == 0) {
                    num = 0.0;
                }
                attrValueMap.put(attrValue, num);
                //计算每一个属性的熵值
                if (attrValueList.get(attrName) == null) {
                    sum = num * (double) (isTrue + isFalse) / data.length;
                    attrValueList.put(attrName, sum);
                } else {
                    sum = attrValueList.get(attrName) + num * (double) (isTrue + isFalse) / data.length;
                    attrValueList.put(attrName, sum);
                }
            }
            System.out.println(attrName + "，熵值：" + attrValueList.get(attrName));
            if (maxAttr == null) {
                maxAttr = attrName;
            } else {
                if (attrValueList.get(attrName) - attrValueList.get(maxAttr) < 0.0) {
                    maxAttr = attrName;
                }
            }
        }
        if (maxAttr != null) {
            int index = attrIndex.get(maxAttr);
            ArrayList<String> attrList = attr.get(maxAttr);
            root.attrName = maxAttr;
            root.treeList = new ArrayList<>();
            for (String valueName : attrList) {
                double value = attrValueMap.get(valueName);
                ID3Tree node = new ID3Tree();
                int isTrue = 0;
                int isAttr = 0;
                for (String[] aData : data) {
                    if (aData[index] == null) {
                        break;
                    }
                    if (aData[index].equals(valueName)) {
                        isAttr++;
                        if ("true".equals(aData[4])) {
                            isTrue++;
                        }
                    }
                }
                if (value == 0.0) {
                    node.isLeaf = true;
                    if (isTrue == isAttr) {
                        node.isPlay = true;
                    }
                    node.attrValue = valueName;
                    root.treeList.add(node);
                } else {
                    node.isLeaf = false;
                    node.attrValue = valueName;
                    String[][] da = new String[14][4];
                    for (int k = 0, n = 0; k < data.length; k++) {
                        if (data[k][index].equals(valueName)) {
                            da[n++] = data[k];
                        }
                    }
                    attr.remove(maxAttr);
                    buildID3Tree(node, da, attr, attrIndex);
                    root.treeList.add(node);
                }
            }

        }
    }

    /**
     * 输出决策树
     *
     * @param root        树根
     * @param printHeader 是否打印表头
     */
    private static void outputID3Tree(ID3Tree root, boolean printHeader) {
        if (printHeader) {
            System.out.printf("%-8s\t%-8s\t%-8s\t%-8s\n", "属性名", "属性值", "类标号", "叶节点");
        }
        System.out.printf("%-8s\t%-8s\t%-8s\t%-8s\n", root.attrName, root.attrValue, root.isPlay, root.isLeaf);
        ArrayList<ID3Tree> treeList = root.treeList;
        if (root.treeList != null) {
            for (ID3Tree aTreeList : treeList) {
                outputID3Tree(aTreeList, false);
            }
        }
    }

    /**
     * 计算以二为底的对数
     *
     * @param value n
     * @return 以二为底的对数
     */
    private static double log(double value) {
        return Math.log(value) / Math.log(2.0);
    }


    private static class ID3Tree {
        /**
         * 是否是叶子节点
         */
        private boolean isLeaf;
        /**
         * 是否出去玩,该值只有在叶子节点中出现
         */
        private boolean isPlay;
        /**
         * 上一个节点在该节点的取值
         */
        private String attrValue;
        /**
         * 孩子节点数组
         */
        private ArrayList<ID3Tree> treeList;
        private String attrName;
    }
}