import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The Piece enum. Enumerates all the pieces in both colours, and contains their images, valid moves, etc.
 */
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

    /**
     * The image of the piece.
     */
    BufferedImage image;

    /**
     * Method that returns if this piece is white.
     * @return Returns a boolean that is true if the piece is white.
     */
    boolean isWhite() {
        return (this == WhitePawn || this == WhiteKnight || this == WhiteBishop ||
                this == WhiteRook || this == WhiteQueen || this == WhiteKing);
    }

    /**
     * This method checks if the piece moves directly to a location; i.e. unlike a bishop or rook or queen that can move "indefinitely".
     * @return Returns a boolean that is true if the piece does move directly to a location.
     */
    boolean isDirectPiece() {
        return (this == WhitePawn || this == BlackPawn || this == WhiteKnight ||
                this == BlackKnight || this == WhiteKing || this == BlackKing);
    }

    /**
     * This is the constructor for the Piece enum.
     * @param imageName String with the name of the file to be used for the image of the piece.
     */
    Piece(String imageName) {
        try {
            image = ImageIO.read(new File(imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method determines all of the possible moves, in any situation, combined a certain piece relative to itself. It returns these moves as coordinates, working off of this type of piece.
     * @return Returns an array of coordinates (arrays of length 2 of integers) consisting of coordinate pairs of locations the piece can move to relative to itself.
     */
    int[][] getPossibleMoves() {
        int[][] moves = null;
        int pawnConstant = -1;
        switch (this) {
            case WhitePawn:
                pawnConstant = 1;
            case BlackPawn:
                moves = new int[][] {{-1, pawnConstant}, {1, pawnConstant}, {0, pawnConstant}, {0, pawnConstant * 2}};
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
                moves = new int[][] {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {2, 0}, {-2, 0}};
                break;
        }
        return moves;
    }
}