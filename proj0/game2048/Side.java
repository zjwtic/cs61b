package game2048;

/** Symbolic names for the four sides of a board.
 *  @author P. N. Hilfinger */
public enum Side {
    /** The parameters (COL0, ROW0, DCOL, and DROW) for each of the
     *  symbolic directions, D, below are to be interpreted as follows:
     *     The board's standard orientation has the top of the board
     *     as NORTH, and rows and columns (see Model) are numbered
     *     from its lower-left corner. Consider the board oriented
     *     so that side D of the board is farthest from you. Then
     *        * (COL0*s, ROW0*s) are the standard coordinates of the
     *          lower-left corner of the reoriented board (where s is the
     *          board size), and
     *        * If (c, r) are the standard coordinates of a certain
     *          square on the reoriented board, then (c+DCOL, r+DROW)
     *          are the standard coordinates of the squares immediately
     *          above it on the reoriented board.
     *  The idea behind going to this trouble is that by using the
     *  col() and row() methods below to translate from reoriented to
     *  standard coordinates, one can arrange to use exactly the same code
     *  to compute the result of tilting the board in any particular
     *  direction. */

    /** 每个方向D的参数（COL0、ROW0、DCOL和DROW）的解释如下：
     棋盘的标准方向将棋盘的顶部作为NORTH，行和列（请参见Model）从其左下角编号。考虑到棋盘朝向，使棋盘的D面离您最远。然后，
     （COL0s，ROW0s）是重新定向的棋盘（其中s是棋盘大小）左下角的标准坐标，
     如果（c，r）是重新定向的棋盘上某个方格的标准坐标，则（c+DCOL，r+DROW）是立即在重新定向的棋盘上方格的标准坐标。
     通过使用下面的col()和row()方法从重新定向到标准坐标进行转换，可以安排使用完全相同的代码来计算在任何特定方向上倾斜棋盘的结果。*/



    NORTH(0, 0, 0, 1), EAST(0, 1, 1, 0), SOUTH(1, 1, 0, -1),
    WEST(1, 0, -1, 0);

    /** The side that is in the direction (DCOL, DROW) from any square
     *  of the board.  Here, "direction (DCOL, DROW) means that to
     *  move one space in the direction of this Side increases the row
     *  by DROW and the colunn by DCOL.  (COL0, ROW0) are the row and
     *  column of the lower-left square when sitting at the board facing
     *  towards this Side. */

    /** 从棋盘上任何一个方格出发，方向（DCOL，DROW）的一侧。这里，“方向（DCOL，DROW）
     * ”的意思是，向该侧移动一个空间将行增加DROW，列增加DCOL。当面对这一侧时，
     *（COL0，ROW0）是坐在棋盘上的左下角方格的行和列。*/
    Side(int col0, int row0, int dcol, int drow) {
        this.row0 = row0;
        this.col0 = col0;
        this.drow = drow;
        this.dcol = dcol;
    }

    /** Returns the side opposite of side S. */
    static Side opposite(Side s) {
        if (s == NORTH) {
            return SOUTH;
        } else if (s == SOUTH) {
            return NORTH;
        } else if (s == EAST) {
            return WEST;
        } else {
            return EAST;
        }
    }

    /** Return the standard column number for square (C, R) on a board
     *  of size SIZE oriented with this Side on top. */
    /**：返回大小为SIZE的棋盘上（C，R）方格的标准列号，该棋盘以此侧为顶部。*/
    public int col(int c, int r, int size) {
        return col0 * (size - 1) + c * drow + r * dcol;
    }

    /** Return the standard row number for square (C, R) on a board
     *  of size SIZE oriented with this Side on top. */
    public int row(int c, int r, int size) {
        return row0 * (size - 1) - c * dcol + r * drow;
    }

    /** Parameters describing this Side, as documented in the comment at the
     *  start of this class. */
    private int row0, col0, drow, dcol;

};
//         * * * * *       c 3   r 1   size  5     north 情况：3  1     south情况：4-3     4-1     ： 1  3
//         *1 * * *
//         * * * * *
//         * * * * *
//         * * * * *