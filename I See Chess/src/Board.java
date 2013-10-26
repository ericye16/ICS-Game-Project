import sun.reflect.generics.reflectiveObjects.NotImplementedException;
/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 25/10/13
 * Time: 10:25 PM
 */
public class Board {
    private Piece[][] board;
    public Board() {
        board = new Piece[][]{
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
    public void movePiece(int xFrom, int yFrom, int xTo, int yTo) {
        throw new NotImplementedException();
    }
    public Piece[][] getBoard() {
        return board;
    }
}