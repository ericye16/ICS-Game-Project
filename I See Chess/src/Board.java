import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.ArrayList;
public class Board {
    private Piece[][] board;
    private boolean[][] castlingFlags = {{true, true}, {true, true}};
    private boolean[][][] enPassant = {{{false, false, false, false, false, false, false}, {false, false, false, false, false, false, false}},
            {{false, false, false, false, false, false, false}, {false, false, false, false, false, false, false}}};

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

    public ArrayList<Integer[]> allValidMoves(Piece piece, int[] location) {
        ArrayList validMoves = new ArrayList<Integer[]>();
        boolean colour = piece.isWhite();
        int[][] possibleMoves = piece.getPossibleMoves();
        for (int i = 0; i < possibleMoves.length; i++) {
            boolean added = false;
            Piece placeholder = board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]];
            try {
                switch (piece){
                    case WhitePawn:
                    case BlackPawn:
                        switch (i){
                            case 0:
                            case 1:
                                if ((placeholder != null
                                        && colour != placeholder.isWhite()) ||
                                        (location[1] == (colour ? 5 : 4) && ((location[0] != 7 && enPassant[colour ? 0 : 1][0][location[0]]) ||
                                                (location[0] != 0 && enPassant[colour ? 0 : 1][1][location[0] - 1])))) {
                                    validMoves.add(possibleMoves[i]);
                                    added = true;
                                }
                                break;
                            case 3:
                                if (placeholder != null) {
                                    break;
                                }
                            case 2:
                                if (board[location[0] + possibleMoves[2][0]][location[1] + possibleMoves[2][1]] == null) {
                                    validMoves.add(possibleMoves[i]);
                                    added = true;
                                }
                                break;
                        }
                        break;
                    case WhiteKnight:
                    case BlackKnight:
                    case WhiteKing:
                    case BlackKing:
                        if (placeholder == null ||
                                colour != placeholder.isWhite() ||
                                (i == 8 && castlingFlags[colour ? 0 : 1][0] && board[location[0] + 1][location[1]] == null &&
                                        board[location[0] + 2][location[1]] == null &&
                                        safe(location, colour) && safe(new int[] {location[0] + 1, location[1]}, colour) && safe(new int[] {location[0] + 2, location[1]}, colour)) ||
                                (i == 9 && castlingFlags[colour ? 0 : 1][1] && board[location[0] - 1][location[1]] == null &&
                                        board[location[0] - 2][location[1]] == null && board[location[0] - 3][location[1]] == null &&
                                        safe(location, colour) && safe(new int[] {location[0] - 1, location[1]}, colour) && safe(new int[] {location[0] - 2, location[1]}, colour))) {
                            validMoves.add(possibleMoves[i]);
                            added = true;
                        }
                        break;
                    case WhiteBishop:
                    case BlackBishop:
                    case WhiteRook:
                    case BlackRook:
                    case WhiteQueen:
                    case BlackQueen:
                        if ((placeholder != null &&
                                colour == placeholder.isWhite()) ||
                                (board[location[0] + possibleMoves[i - 1][0]][location[1] + possibleMoves[i - 1][1]] != null &&
                                        colour != board[location[0] + possibleMoves[i - 1][0]][location[1] + possibleMoves[i - 1][1]].isWhite())) {
                            i -= i % 7 - 7;
                        } else {
                            validMoves.add(possibleMoves[i]);
                            added = true;
                        }
                        break;
                }
                board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] = piece;
                board[location[0]][location[1]] = null;
                if (!safe(findKing(piece.isWhite()), piece.isWhite()) && added) {
                    validMoves.remove(validMoves.size() - 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return validMoves;
    }

    int[] findKing(boolean colour) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == (colour ? Piece.WhiteKing : Piece.BlackKing)) {
                    return new int[] {i, j};
                }
            }
        }
        return null;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void movePiece(int[] location, int[] destination) {
        throw new NotImplementedException();
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
                            if (location.equals (new int[] {i + spread[k][0], j + spread[k][1]}) && (threat == Piece.WhiteQueen || threat == Piece.BlackQueen ||
                                    (((threat == Piece.WhiteBishop || threat == Piece.BlackBishop) && k % 14 >= 7) ||
                                            (threat == Piece.WhiteRook || threat == Piece.BlackRook) && k % 14 < 7))) {
                                isSafe = false;
                            } else if ((board[i + spread[k][0]][j + spread[k][1]] != null && colour == board[i + spread[k][0]][j + spread[k][1]].isWhite()) ||
                                    (k % 7 != 0 && board[i + spread[k - 1][0]][j + spread[k - 1][1]] != null && colour != board[i + spread[k][0]][j + spread[k][1]].isWhite())) {
                                k -= k % 7 - 7;
                            }
                        }
                    }
                }
            }
        }
        return isSafe;
    }
}