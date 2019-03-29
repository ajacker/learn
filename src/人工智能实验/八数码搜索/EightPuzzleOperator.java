package 人工智能实验.八数码搜索;

class EightPuzzleOperator {
    /**
     * 判断是否可以继续移动
     */
    static boolean canMove(int x, int y, Direction direction) {
        boolean condition = (direction == Direction.UP && x == 0)
                || (direction == Direction.DOWN && x == 2)
                || (direction == Direction.RIGHT && y == 2)
                || (direction == Direction.LEFT && y == 0);
        return !condition;
    }

    /**
     * 根据给出的参数，进行空格位置的移动
     */
    static EightPuzzle movePosition(EightPuzzle ep, Direction direction) {
        EightPuzzle result = null;
        // 复制并得到空格位置
        result = ep.depthClone();
        result.getPostion();
        int blankPosX = result.getBlankPosX();
        int blankPosY = result.getBlankPosY();
        int temp;
        // 对空白格进行上下左右移动
        switch (direction) {
            case UP:
                temp = result.data[blankPosX][blankPosY];
                result.data[blankPosX][blankPosY] = result.data[blankPosX - 1][blankPosY];
                result.data[blankPosX - 1][blankPosY] = temp;
                break;
            case DOWN:
                temp = result.data[blankPosX][blankPosY];
                result.data[blankPosX][blankPosY] = result.data[blankPosX + 1][blankPosY];
                result.data[blankPosX + 1][blankPosY] = temp;
                break;
            case RIGHT:
                temp = result.data[blankPosX][blankPosY];
                result.data[blankPosX][blankPosY] = result.data[blankPosX][blankPosY + 1];
                result.data[blankPosX][blankPosY + 1] = temp;
                break;
            case LEFT:
                temp = result.data[blankPosX][blankPosY];
                result.data[blankPosX][blankPosY] = result.data[blankPosX][blankPosY - 1];
                result.data[blankPosX][blankPosY - 1] = temp;
                break;
            default:
        }
        return result;
    }
}
