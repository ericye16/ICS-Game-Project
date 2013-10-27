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
        ArrayList validMoves = new ArrayList<Integer[]>();
        for (int i = 0; i < possibleMoves.length; i++) {
            try {
                switch (piece){
                    case WhitePawn:
                    case BlackPawn:
                        switch (i){
                            case 0:
                            case 1:
                                if (board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] != null && piece.isWhite() != board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]].isWhite()){
                                    validMoves.add(possibleMoves[i]);
                                }
                                break;
                            case 3:
                                if (board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] != null){
                                    break;
                                }
                            case 2:
                                if (board[location[0] + possibleMoves[2][0]][location[1] + possibleMoves[2][1]] == null){
                                    validMoves.add(possibleMoves[i]);
                                }
                                break;
                        }
                        break;
                    case WhiteKnight:
                    case BlackKnight:
                    case WhiteKing:
                    case BlackKing:
                        break;
                    case WhiteBishop:
                    case BlackBishop:
                    case WhiteRook:
                    case BlackRook:
                    case WhiteQueen:
                    case BlackQueen:
                        break;
                }
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