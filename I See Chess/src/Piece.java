import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public enum Piece {
    WhitePawn("img/PawnWhite.png"),
    BlackPawn("img/PawnBlack.png"),
    WhiteKnight("img/KnightWhite.png"),
    BlackKnight("img/KnightBlack.png"),
    WhiteBishop("img/BishopWhite.png"),
    BlackBishop("img/BishopBlack.png"),
    WhiteRook("img/RookWhite.png"),
    BlackRook("img/RookBlack.png"),
    WhiteQueen("img/QueenWhite.png"),
    BlackQueen("img/QueenBlack.png"),
    WhiteKing("img/KingWhite.png"),
    BlackKing("img/KingBlack.png");

    BufferedImage image;

    boolean isWhite() {
        return (this == WhitePawn || this == WhiteKnight || this == WhiteBishop ||
                this == WhiteRook || this == WhiteQueen || this == WhiteKing);
    }

    boolean isDirectPiece() {
        return (this == WhitePawn || this == BlackPawn || this == WhiteKnight ||
                this == BlackKnight || this == WhiteKing || this == BlackKing);
    }

    Piece(String imageName) {
        try {
            image = ImageIO.read(new File(imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int[][] getPossibleMoves() {
        int[][] moves = null;
        int pawnConstant = -1;
        switch (this) {
            case WhitePawn:
                pawnConstant = 1;
            case BlackPawn:
                moves = new int[][] {{1, pawnConstant}, {-1, pawnConstant}, {0, pawnConstant}, {0, pawnConstant * 2}};
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
                moves = new int[][] {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {-2, 0}, {2, 0}};
                break;
        }
        return moves;
    }
}