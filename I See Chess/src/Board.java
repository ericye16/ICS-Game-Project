import sun.reflect.generics.reflectiveObjects.NotImplementedException;
/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 25/10/13
 * Time: 10:25 PM
 */
public class Board {
    private Piece[][] board;
    private byte[][] colour;

    final static byte WHITE = 1;
    final static byte BLACK = 2;
    final static byte EMPTY = 0;

    public Board() {
        board = new Piece[][]{
                {Piece.Rook, Piece.Knight, Piece.Bishop, Piece.Queen, Piece.King, Piece.Bishop,Piece.Knight, Piece.Rook},
                {Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn, Piece.Pawn},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn, Piece.Pawn},
                {Piece.Rook, Piece.Knight, Piece.Bishop, Piece.Queen, Piece.King, Piece.Bishop,Piece.Knight, Piece.Rook}
        };

        colour = new byte[8][8];
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (row < 2) {// white
                    colour[row][column] = WHITE;
                } else if (row >= 6) { //black
                    colour[row][column] = BLACK;
                } else {
                    colour[row][column] = EMPTY; //empty
                }
            }
        }
    }

    public void movePiece(int xFrom, int yFrom, int xTo, int yTo) {
        throw new NotImplementedException();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public byte[][] getColour() {
        return colour;
    }
}