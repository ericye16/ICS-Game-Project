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
    private boolean[][] castlingFlags = {{true, true}, {true, true}};

    public Board() {
        board = new Piece[][] {
                {Piece.WhiteRook, Piece.WhiteKnight, Piece.WhiteBishop, Piece.WhiteQueen,
                Piece.WhiteKing, Piece.WhiteBishop, Piece.WhiteKnight, Piece.WhiteRook},
                {Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn,
                Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn, Piece.WhitePawn},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn,
                Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn, Piece.BlackPawn},
                {Piece.BlackRook, Piece.BlackKnight, Piece.BlackBishop, Piece.BlackQueen,
                Piece.BlackKing, Piece.BlackBishop, Piece.BlackKnight, Piece.BlackRook}
        };
    }

    public boolean safe(int[] location, boolean colour){
        boolean isSafe = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece threat = board[i][j];
                if (threat != null && colour != threat.isWhite()) {
                    if(threat.isDirectPiece()){
                        for (int k = 0; k < (threat == Piece.WhitePawn || threat == Piece.BlackPawn ? 2 : 8); k++) {
                            if (location.equals (new int[] {i + threat.getPossibleMoves()[k][0], j + threat.getPossibleMoves()[k][1]})) {
                                isSafe = false;
                            }
                        }
                    } else {
                        int[][] spread = Piece.WhiteQueen.getPossibleMoves();
                        for (int k = 0; k < 56; k++) {
                            if (location.equals (new int[] {i + spread[k][0], j + spread[k][1]}) && (threat == Piece.WhiteQueen || threat == Piece.BlackQueen || (((threat == Piece.WhiteBishop || threat == Piece.BlackBishop) && k % 14 >= 7) || (threat == Piece.WhiteRook || threat == Piece.BlackRook) && k % 14 < 7))) {
                                isSafe = false;
                            } else if ((board[i + spread[k][0]][j + spread[k][1]] != null && colour == board[i + spread[k][0]][j + spread[k][1]].isWhite()) || (k % 7 != 0 && board[i + spread[k - 1][0]][j + spread[k - 1][1]] != null && colour != board[i + spread[k][0]][j + spread[k][1]].isWhite())) {
                                k -= k % 7 - 7;
                            }
                        }
                    }
                }
            }
        }
        return isSafe;
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
                                if (board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] != null
                                    && piece.isWhite() != board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]].isWhite()) {
                                    validMoves.add(possibleMoves[i]);
                                }
                                break;
                            case 3:
                                if (board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] != null) {
                                    break;
                                }
                            case 2:
                                if (board[location[0] + possibleMoves[2][0]][location[1] + possibleMoves[2][1]] == null) {
                                    validMoves.add(possibleMoves[i]);
                                }
                                break;
                        }
                        break;
                    case WhiteKnight:
                    case BlackKnight:
                    case WhiteKing:
                    case BlackKing:
                        if (board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] == null ||
                            piece.isWhite() != board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]].isWhite() ||
                            (i == 8 && castlingFlags[piece.isWhite() ? 0 : 1][0] && board[location[0] + 1][location[1]] == null && board[location[0] + 2][location[1]] == null && safe(location, piece.isWhite()) && safe(new int[] {location[0] + 1, location[1]}, piece.isWhite()) && safe(new int[] {location[0] + 2, location[1]}, piece.isWhite())) ||
                            (i == 9 && castlingFlags[piece.isWhite() ? 0 : 1][1] && board[location[0] - 1][location[1]] == null && board[location[0] - 2][location[1]] == null && board[location[0] - 3][location[1]] == null && safe(location, piece.isWhite()) && safe(new int[] {location[0] - 1, location[1]}, piece.isWhite()) && safe(new int[] {location[0] - 2, location[1]}, piece.isWhite()))) {
                            validMoves.add(possibleMoves[i]);
                        }
                        break;
                    case WhiteBishop:
                    case BlackBishop:
                    case WhiteRook:
                    case BlackRook:
                    case WhiteQueen:
                    case BlackQueen:
                        if ((board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] != null &&
                            piece.isWhite() == board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]].isWhite()) ||
                            (board[location[0] + possibleMoves[i - 1][0]][location[1] + possibleMoves[i - 1][1]] != null &&
                            piece.isWhite() != board[location[0] + possibleMoves[i - 1][0]][location[1] + possibleMoves[i - 1][1]].isWhite())) {
                            i -= i % 7 - 7;
                        } else {
                            validMoves.add(possibleMoves[i]);
                        }
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return validMoves;
    }
    public void movePiece(int[] location, int[] destination) {
        throw new NotImplementedException();
    }
    public Piece[][] getBoard() {
        return board;
    }
}