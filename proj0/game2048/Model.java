package game2048;

import java.util.EmptyStackException;
import java.util.Formatter;
import java.util.Observable;
import java.util.function.Predicate;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.


//    my first code
//        int arow=0;
//        int acol=0;
//        boolean merged[][]=new boolean[board.size()][board.size()];
//        for (int i = 0; i < merged.length; i++) {
//            for (int j = 0; j < merged.length; j++) {
//                merged[i][j]=false;
//            }
//        }
//switch (side) {
//    case NORTH:case WEST: {
//        for (int c = 0; c < board.size(); c++) {    //north   west
//            for (int r = board.size() - 1; r >= 0; r--) {
//                Tile tile = board.tile(c, r);
//                if (tile != null) {
//                    arow=0;
//                    acol=0;
//if (side==Side.NORTH) {
//
//    if (tile.row() != board.size() - 1 ) {
//        if (board.tile(tile.col(), tile.row() + 1) == null||board.tile(tile.col(), tile.row() + 1)
//                .value()==board.tile(tile.col(), tile.row() ) .value() ) {
//            //it needs mov
//            arow = 0;
//            acol = 0;
//            for (int r1 = tile.row() + 1; r1 <= board.size() - 1; r1++) {
//
//                if (board.tile(tile.col(), r1) != null) {
//                    break;
//                }
//                arow++;
//            }
//            if (tile.row() + arow != board.size() - 1)
//                if (board.tile(tile.col(), tile.row() + arow + 1).value() == board.tile(tile.col(), tile.row()).value()) {
//                    arow++;
//                }
//
//        }
//
//    }
//}
//else {
//    if (tile.col()!= 0 ) {
//        if (board.tile(tile.col() - 1, tile.row()) == null||board.tile(tile.col() - 1, tile.row())
//                .value()==board.tile(tile.col(), tile.row() ) .value() ){
//            //it needs mov
//            acol = 0;
//            arow = 0;
//            for (int c1 = tile.col() - 1; c1 >= 0; c1--) {
//
//                if (board.tile(c1, tile.row()) != null) {
//                    break;
//                }
//                acol--;
//            }
//            if (tile.col() + acol != 0)
//                if (board.tile(tile.col() + acol - 1, tile.row()).value() == board.tile(tile.col(), tile.row()).value()) {
//                    acol--;
//                }
//
//        }
//    }
//
//}
//                    if(arow!=0||acol!=0) {
//                        if(merged[tile.col() + acol][tile.row() + arow]) {
//                            merged[tile.col() + acol][tile.row() + arow]=false;
//                            switch (side){
//                                case NORTH:arow--;break;
//                                case WEST:acol++;break;
//                            }
//                        }
//
//                        if (board.move(tile.col() + acol, tile.row() + arow, tile)) {
//                            merged[tile.col() + acol][tile.row() + arow] = true;
//                            score += tile.value() * 2;
//                        }
//                        changed = true;
//                    }
//                }
//            }
//        }
//        break;
//    }
//    case SOUTH:case EAST: {
//        for (int c = board.size() - 1; c >= 0; c--) {    //sourth  east
//            for (int r = 0; r < board.size(); r++) {
//                Tile tile = board.tile(c, r);
//                if (tile != null) {
//                    arow=0;
//                    acol=0;
//                    if (side == Side.SOUTH) {
//
//                        if (tile.row() != 0 )
//                           if (board.tile(tile.col(), tile.row() -1) == null||board.tile(tile.col(), tile.row() -1)
//                                   .value()==board.tile(tile.col(), tile.row() ) .value()) {
//                            //it needs mov
//                            arow = 0;
//                            acol=0;
//                            for (int r1 = tile.row() -1; r1 >=0; r1--) {
//
//                                if (board.tile(tile.col(), r1) != null) {
//                                    break;
//                                }
//                                arow--;
//                            }
//                            if(tile.row()+arow!=0)
//                            if (board.tile(tile.col(), tile.row() + arow -1).value() == board.tile(tile.col(), tile.row()).value()) {
//                                arow--;
//                            }
//
//                        }
//
//                    } else {
//                        if (tile.col() != board.size()-1 )
//                               if(  board.tile(tile.col() + 1, tile.row()) == null|| board.tile(tile.col() + 1, tile.row())
//                                       .value()==board.tile(tile.col(), tile.row() ) .value()) {
//                            //it needs mov
//                            acol = 0;
//                            arow=0;
//                            for (int c1 = tile.col() +1; c1<=board.size()-1; c1++) {
//
//                                if (board.tile(c1, tile.row()) != null) {
//                                    break;
//                                }
//                                acol++;
//                            }
//                            if(tile.col()+acol!=board.size()-1)
//                            if (board.tile(tile.col() + acol +1, tile.row()).value() == board.tile(tile.col(), tile.row()).value()) {
//                                acol++;
//                            }
//
//                        }
//
//                    }
//                    if(arow!=0||acol!=0) {
//                        if(merged[tile.col() + acol][tile.row() + arow]) {
//                            merged[tile.col() + acol][tile.row() + arow]=false;
//                            switch (side){
//                                case SOUTH:arow++;break;
//                                case EAST:acol--;break;
//                            }
//                        }
//                        if(board.move(tile.col() + acol, tile.row() + arow, tile)){
//                            score+=tile.value()*2;
//                            merged[tile.col() + acol][tile.row() + arow]=true;
//                        }
//
//                        changed=true;
//                    }
//                }
//            }
//        }
//    }
//
//}


//      my  second code
        board.setViewingPerspective(side);
        boolean[][] merged = new boolean[board.size()][board.size()];
        for (int r = board.size() -2; r >= 0; r--) {  //第一行不要动
           for (int c = 0; c < board.size(); c++) {
                   Tile t= board.tile(c, r);   //记住我们这的c r 是改变方向的tile 位置
                   if (t != null) {
                   int  dr=r+1;
                       while (dr<board.size()) {
                           Tile temp = board.tile(c, dr);
                           if (temp == null || temp.value() == t.value() && !merged[c][dr]) {  //如果可以移动  1  前面为null
                               dr++;                                                             //  2 前面可合并以及为合并过
                           } else {    //不能 说明多移动了
                               dr--;
                               break;
                           }
                       }
                    if (dr==board.size())   //如果顶部为null或者多合并了  说明多移动了
                        dr--;
                    if (dr != r) {       // 如果发生了移动，设置changed
                        changed = true;
                    }
                    // 如果发生了merge
                    if (board.move(c, dr, t)) {
                        merged[c][dr] = true;
                        score += board.tile(c, dr).value();
                    }
                }
            }
        }
        board.setViewingPerspective(Side.NORTH);

//        others  code
//        board.setViewingPerspective(side);
//        boolean[][] merged = new boolean[board.size()][board.size()];
//        for (int r = board.size() -2; r >= 0; r--) {  //第一行不要动
//            for (int c = 0; c < board.size(); c++) {
//                Tile t= board.tile(c, r);   //记住我们这的c r 是改变方向的tile 位置
//                if (t != null) {
////                     处理当前tile：判断是否需要move，需要move到哪里，是否merge？
////                     向上移动，c不变，r加一
//                    int dc = c, dr = r + 1;
//                    while (dr < board.size()) {
//                        Tile dt = board.tile(dc, dr);
//                        if (dt != null) {
//                            // 如果不应该移动到(dc,dr)，dr回退一格
//                            if (merged[dc][dr] == true || dt.value() != t.value()) {
//                                dr--;
//                            }
//                            break;
//                        }
//
//                        if (dr == board.size()-1) {  //到最顶上了
//                            break;
//                        }
//                        dr++;
//                    }
//
//                    // 如果发生了移动，设置changed
//                    if (dr != r) {
//                        changed = true;
//                    }
//                    // 如果发生了merge
//                    if (board.move(dc, dr, t)) {
//                        merged[dc][dr] = true;
//                        score += board.tile(dc, dr).value();
//                    }
//
//
//                }
//            }
//        }
//        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        for (Tile tile : b) {
            if(tile==null)
                return true;
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for (Tile tile : b) {
            if (tile != null)
                if (tile.value() == MAX_PIECE) {
                    return true;
                }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        if (emptySpaceExists(b)) {
            return true;
        } else {
            for (int c = 0; c < b.size(); c++) {
                for (int r = 0; r < b.size(); r++) {
                    if (c == b.size() - 1 && r == b.size() - 1) {
                        return false;
                    }
                   else if (c == b.size() - 1) {
                        if (b.tile(c, r).value() == b.tile(c, r + 1).value()) {
                            return true;
                        }
                    }

                  else   if (r == b.size() - 1) {
                        if (b.tile(c, r).value() == b.tile(c + 1, r).value()) {
                            return true;
                        }
                    }
                  else   if (b.tile(c, r).value() == b.tile(c + 1, r).value() || b.tile(c, r).value() == b.tile(c, r + 1).value()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
