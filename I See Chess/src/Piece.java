import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 25/10/13
 * Time: 9:11 AM
 */
public enum Piece {
    WhitePawn, BlackPawn, WhiteKnight, BlackKnight, WhiteBishop, BlackBishop,
    WhiteRook, BlackRook, WhiteQueen, BlackQueen, WhiteKing, BlackKing;
    boolean isWhite() {
        return (this == Piece.WhitePawn || this == WhiteKnight || this == WhiteBishop ||
                this == WhiteRook || this == WhiteQueen || this == WhiteKing);
    }
    int[][] getPossibleMoves() {
        int[][] moves = null;
        byte pawnConstant = -1;
        switch (this) {
            case WhitePawn:
                pawnConstant = 1;
            case BlackPawn:
                moves = new int[][] {{1, pawnConstant}, {0, pawnConstant}, {-1, pawnConstant}, {0, pawnConstant * 2}};
                break;
            case WhiteKnight:
            case BlackKnight:
                moves = new int[][] {{1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};
                break;
            case WhiteBishop:
            case BlackBishop:
                moves = new int[][] {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7},
                        {1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7},
                        {-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7},
                        {-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7}};
                break;
            case WhiteRook:
            case BlackRook:
                moves = new int[][] {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7},
                        {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0},
                        {0, -1}, {0, -2}, {0, -3}, {0, -4}, {0, -5}, {0, -6}, {0, -7},
                        {-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}, {-5, 0}, {-6, 0}, {-7, 0}};
                break;
            case WhiteQueen:
            case BlackQueen:
                moves = new int[][] {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7},
                        {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7},
                        {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0},
                        {1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7},
                        {0, -1}, {0, -2}, {0, -3}, {0, -4}, {0, -5}, {0, -6}, {0, -7},
                        {-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7},
                        {-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}, {-5, 0}, {-6, 0}, {-7, 0},
                        {-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7}};
                break;
            case WhiteKing:
            case BlackKing:
                moves = new int[][] {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
                break;
        }
        return moves;
    }
}