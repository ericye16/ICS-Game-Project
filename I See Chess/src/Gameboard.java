import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 25/10/13
 * Time: 10:25 PM
 */
public class Gameboard {
    private Piece[][] board;
    private byte[][] colour;

    final static byte WHITE = 1;
    final static byte BLACK = 2;
    final static byte EMPTY = 0;

    public Gameboard() {
        board = new Piece[][]{
                {Piece.Rook, Piece.Knight, Piece.Bishop, Piece.Queen, Piece.King, Piece.Bishop,Piece.Knight, Piece.Rook},
                {Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn, Piece.Pawn},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn,Piece.Pawn, Piece.Pawn},
                {Piece.Rook, Piece.Knight, Piece.Bishop, Piece.Queen, Piece.King, Piece.Bishop,Piece.Knight, Piece.Rook},
        };

        colour = new byte[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (r < 2) {// white
                    colour[r][c] = WHITE;
                } else if (r > 5) { //black
                    colour[r][c] = BLACK;
                } else {
                    colour[r][c] = EMPTY; //empty
                }
            }
        }
    }

    public void movePiece(int xfrom, int yfrom, int xto, int yto) {
        throw new NotImplementedException();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public byte[][] getColour() {
        return colour;
    }
}
