package 人工智能实验.八数码搜索;

enum Direction {
    //上
    UP("↑"),
    //下
    DOWN("↓"),
    //左
    LEFT("←"),
    //右
    RIGHT("→");
    private String name;

    Direction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
