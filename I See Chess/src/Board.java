import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.ArrayList;
/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 25/10/13
 * Time: 10:25 PM
 */
public class Board {
    private Piece[][] board;
    public Board() {
        board = new Piece[][] {
                {Piece.WhiteRook, Piece.WhiteKnight, Piece.WhiteBishop, Piece.WhiteQueen, Piece.WhiteKing, Piece.WhiteBishop, Piece.WhiteKnight, Piece.WhiteRook},
                {Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn},
                {Piece.BlackRook, Piece.BlackKnight, Piece.BlackBishop, Piece.BlackQueen, Piece.BlackKing, Piece.BlackBishop, Piece.BlackKnight, Piece.BlackRook}
        };
    }
    public ArrayList<Integer[]> allValidMoves(Piece piece, int[] location) {
        int[][] possibleMoves = piece.getPossibleMoves();
        for (int i = 0; i < possibleMoves.length; i++) {
            try {
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        throw new NotImplementedException();
    }
    public void movePiece(int[] location, int[] destination) {
        throw new NotImplementedException();
    }
    public Piece[][] getBoard() {
        return board;
    }
}