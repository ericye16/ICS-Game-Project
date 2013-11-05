import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class Board implements Cloneable{
    private Piece[][] board;
    private ArrayList<Board> history = new ArrayList<Board>();
    private boolean isWhitesTurn, whiteCheck, blackCheck, whiteStalemate, blackStalemate, fiftyMoves, threeBoards, stalemate, whiteMate, blackMate;
    private boolean[][] castlingFlags = {{true, true}, {true, true}};
    private boolean[][][] enPassant = {{{false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false}},
            {{false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false}}}; //array representing whether one can currently en passant there are 7 ways to do this, in 2 directions, by 2 colours, array is 2 * 2 * 7
    private int turn = 0;
    private int[] whiteKingLocation, blackKingLocation;

    /**
     * Constructor for the Board, assuming no previous Board
     */
    Board() {
        board = new Piece[][] {{Piece.WhiteRook, Piece.WhitePawn, null, null, null, null, Piece.BlackPawn, Piece.BlackRook},
                {Piece.WhiteKnight, Piece.WhitePawn, null, null, null, null, Piece.BlackPawn, Piece.BlackKnight},
                {Piece.WhiteBishop, Piece.WhitePawn, null, null, null, null, Piece.BlackPawn, Piece.BlackBishop},
                {Piece.WhiteQueen, Piece.WhitePawn, null, null, null, null, Piece.BlackPawn, Piece.BlackQueen},
                {Piece.WhiteKing, Piece.WhitePawn, null, null, null, null, Piece.BlackPawn, Piece.BlackKing},
                {Piece.WhiteBishop, Piece.WhitePawn, null, null, null, null, Piece.BlackPawn, Piece.BlackBishop},
                {Piece.WhiteKnight, Piece.WhitePawn, null, null, null, null, Piece.BlackPawn, Piece.BlackKnight},
                {Piece.WhiteRook, Piece.WhitePawn, null, null, null, null, Piece.BlackPawn, Piece.BlackRook}};
        isWhitesTurn = true;
    }

    /**
     * Constructor for the Board, assuming a previous Board exists
     * @param currentBoard the previous Board to use
     */
    Board(Piece[][] currentBoard) {
        board = currentBoard;
        whiteCheck = false;
        blackCheck = false;
        whiteStalemate = false;
        blackStalemate = false;
        fiftyMoves = false;
        threeBoards = false;
        stalemate = false;
        whiteMate = false;
        blackMate = false;
    }

    /**
     * Find all the valid moves, given the piece and the location of all pieces around it
     * @param piece the Piece to check
     * @param location the location of the piece
     * @return a 2D array of coordinates <i>relative</i> to the piece it can move
     */
    ArrayList<Integer[]> allValidMoves(Piece piece, int[] location) {
        if (board[location[0]][location[1]] != piece || (piece == null)) {
            throw new InvalidParameterException();
        }
        ArrayList<Integer[]> validMoves = new ArrayList<Integer[]>();
        boolean colour = piece.isWhite();
        int[][] possibleMoves = piece.getPossibleMoves();
        Integer[][] possibleMovesInteger = new Integer[possibleMoves.length][possibleMoves[0].length];
        for (int i = 0; i < possibleMoves.length; i++) {
            for (int j = 0; j < possibleMoves[i].length; j++) {
                possibleMovesInteger[i][j] = new Integer(possibleMoves[i][j]);
            }
        }
        for (int i = 0; i < possibleMoves.length; i++) {
            try {
                boolean added = false, safeFlag = true;//flag that will be used later, represents whether the possible move has been accepted
                Piece placeholder = board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]];//target square
                board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] = piece;//simulates moving piece, puts piece on target
                board[location[0]][location[1]] = null;//empties square that piece was on
                if (!safe(findKing(piece.isWhite()), piece.isWhite())) {
                     safeFlag = false;
                }
                board[location[0]][location[1]] = piece;
                board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] = placeholder;
                switch (piece){
                    case WhitePawn:
                    case BlackPawn:
                        switch (i){
                            case 0:
                            case 1:
                                if ((placeholder != null && colour != placeholder.isWhite()) ||//allows if attacker wants to capture enemy piece
                                        (i == 0 && location[1] == (colour ? 4 : 3) &&
                                                ((location[0] != 7 && enPassant[colour ? 0 : 1][0][location[0]]) ||//en passant right
                                                (i == 1 && location[0] != 0 && enPassant[colour ? 0 : 1][1][location[0] - 1])))) {//en passant left
                                    validMoves.add(possibleMovesInteger[i]);//validate
                                    added = true;//flag
                                }
                                break;
                            case 3:
                                if (placeholder != null && location[1] == (colour ?  1 : 6)) {//checks 2 squares ahead is empty, if it is, it will move on to check one square ahead
                                    break;
                                }
                            case 2:
                                if (board[location[0] + possibleMoves[2][0]][location[1] + possibleMoves[2][1]] == null) {//checks 1 square ahead is empty
                                    validMoves.add(possibleMovesInteger[i]);//validate
                                    added = true;//flag
                                }
                                break;
                        }
                        break;
                    case WhiteKnight:
                    case BlackKnight:
                    case WhiteKing:
                    case BlackKing:
                        if (placeholder == null ||
                                colour != placeholder.isWhite() ||//allows if attacker wants to capture enemy piece
                                (i == 8 && castlingFlags[colour ? 0 : 1][0] &&//all conditions for castling: squares are empty, king and rook haven't moved yet (flags), king not moving out of, into, or through check, right
                                        board[location[0] + 1][location[1]] == null &&
                                        board[location[0] + 2][location[1]] == null &&
                                        safe(location, colour) &&
                                        safe(new int[] {location[0] + 1, location[1]}, colour) &&
                                        safe(new int[] {location[0] + 2, location[1]}, colour)) ||
                                (i == 9 && castlingFlags[colour ? 0 : 1][1] &&//all conditions for castling: squares are empty, king and rook haven't moved yet (flags), king not moving out of, into, or through check, left
                                        board[location[0] - 1][location[1]] == null &&
                                        board[location[0] - 2][location[1]] == null &&
                                        board[location[0] - 3][location[1]] == null &&
                                        safe(location, colour) &&
                                        safe(new int[] {location[0] - 1, location[1]}, colour) &&
                                        safe(new int[] {location[0] - 2, location[1]}, colour))) {
                            validMoves.add(possibleMovesInteger[i]);//validate
                            added = true;//flag
                        }
                        break;
                    case WhiteBishop:
                    case BlackBishop:
                    case WhiteRook:
                    case BlackRook:
                    case WhiteQueen:
                    case BlackQueen:
                        if ((placeholder != null && colour == placeholder.isWhite()) ||//sees friendly piece, blocked
                            (i % 7 != 0 && board[location[0] + possibleMoves[i - 1][0]][location[1] + possibleMoves[i - 1][1]] != null &&
                            colour != board[location[0] + possibleMoves[i - 1][0]][location[1] + possibleMoves[i - 1][1]].isWhite())) {//saw enemy piece one tile ago, this tile blocked
                            i -= i % 7 - 6;//adjust counter to next direction if has encountered one of the above
                        } else {//move allowed
                            validMoves.add(possibleMovesInteger[i]);//validate
                            added = true;//flag
                        }
                        break;
                }
                if (!safeFlag && added) {//checks that the king is safe
                    validMoves.remove(validMoves.size() - 1);//if king wasn't safe and the flag was raised (the move was validated) it removes it
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return validMoves;
    }

    /**
     * Check the current conditions in terms of stalemates, checkmates, checks, etc and update the boolean flags
     * To be run after every move.
     */
    void checkConditions() {
        whiteStalemate = true;
        blackStalemate = true;
        if (safe(findKing(true), true)) {
            whiteCheck = true;
            whiteMate = true;
        }
        if (safe(findKing(false), false)) {
            blackCheck = true;
            blackMate = true;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null && board[i][j].isWhite() && allValidMoves(board[i][j], new int[] {i, j}).size() != 0) {
                    whiteStalemate = false;
                    whiteMate = false;
                }
                if (board[i][j] != null && !board[i][j].isWhite() && allValidMoves(board[i][j], new int[] {i, j}).size() != 0) {
                    blackStalemate = false;
                    blackMate = false;
                }
            }
        }
        if (history.size() >= 100) {
            fiftyMoves = true;
        }
        for (Board boardOne : history) {
            for (Board boardTwo : history) {
                for (Board boardThree : history) {
                    if (boardOne != boardTwo && boardOne != boardThree && boardTwo != boardThree && boardOne.equals(boardTwo) && boardOne.equals(boardThree) && boardTwo.equals(boardThree)) {
                        threeBoards = true;
                    }
                }
            }
        }
        if (whiteStalemate || blackStalemate || fiftyMoves || threeBoards) {
            stalemate = true;
        }
    }

    @Override
    /**
     * Clone the object and everything within it.
     */
    protected Object clone() {
        Board toReturn = new Board(this.board);
        toReturn.turn = turn;
        toReturn.castlingFlags = castlingFlags.clone();
        toReturn.enPassant = enPassant.clone();
        toReturn.history = (ArrayList<Board>) history.clone();
        toReturn.isWhitesTurn = isWhitesTurn;
        toReturn.turn = turn;
        return toReturn;
    }

    @Override
    /**
     * Check if two boards have pieces at the same spot
     */
    public boolean equals(Object other) {
        return other instanceof Board && Arrays.deepEquals(board, ((Board) other).board);
    }

    /**
     * Find the location of the king
     * @param colour the colour of the king to find
     * @return the location of the king
     */
    int[] findKing(boolean colour) {
        return colour ? whiteKingLocation : blackKingLocation;
    }

    /**
     * Get the 2D Piece array
     * @return the 2D Piece array
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * Move a piece from a location to a destination, doing validity- and move-checking
     * @param location the current location of the piece
     * @param destination the future location of the piece
     * @return the piece moved
     * @throws IsNotYourTurnException if the piece moved is not the piece currently playing
     * @throws IllegalMoveException if the piece is not moved to a legal position
     */
    Piece movePiece(int[] location, int[] destination) throws IsNotYourTurnException, IllegalMoveException, NeedToPromotePawnException {
        boolean wasAValidMove = false;
        Piece capturerer = board[location[0]][location[1]];
        if (capturerer.isWhite() != getIsWhitesTurn()) {
            throw new IsNotYourTurnException();
        }

        ArrayList<Integer[]> validMoves = allValidMoves(capturerer, location);
        for (Integer[] possibleDestination : validMoves) {
            if (location[0] + possibleDestination[0] == destination[0] && location[1] + possibleDestination[1] == destination[1]) {
                wasAValidMove = true;
            }
        }
        if (wasAValidMove) {
            Piece capturee = board[destination[0]][destination[1]];
            if ((capturerer == Piece.WhitePawn || capturerer == Piece.BlackPawn) && capturee == null && location[0] != destination[0] && location[1] != destination[1]) {
                capturee = (capturerer.isWhite() ? Piece.BlackPawn : Piece.WhitePawn);
                board[destination[0]][location[1] + 2 * (destination[1] - location[1])] = null;
            }
            Board prevBoard = (Board) this.clone();
            prevBoard.history = null; // to remove the amount of memory required
            board[destination[0]][destination[1]] = capturerer;
            board[location[0]][location[1]] = null;
            history.add(prevBoard);
            isWhitesTurn = !isWhitesTurn;
            turn++;
            if (capturerer == Piece.WhiteKing) {
                whiteKingLocation = new int[] {destination[0], destination[1]};
                castlingFlags[0][0] = false;
                castlingFlags[0][1] = false;
            } else if (capturerer == Piece.BlackKing) {
                blackKingLocation = new int[] {destination[0], destination[1]};
                castlingFlags[1][0] = false;
                castlingFlags[1][1] = false;
            }
            for (int i = 0; i < castlingFlags.length; i++) {
                for (int j = 0; j < castlingFlags.length; j++) {
                    if (board[i * 7][j * 7] == (i == 0 ? Piece.WhiteRook : Piece.BlackRook)) {
                        castlingFlags[i][j] = false;
                    }
                }
            }
            for (int i = 0; i < enPassant.length; i++) {
                for (int j = 0; j < enPassant.length; j++) {
                    for (int k = 0; k < 7; k++) {
                        enPassant[i][j][k] = false;
                    }
                }
            }
            if ((capturerer == Piece.WhitePawn || capturerer == Piece.BlackPawn) && location[1] == (capturerer.isWhite() ? 1 : 6) && destination[1] == (capturerer.isWhite() ? 3 : 4)) {
                if (location [0] != 7 && board[location[0] + 1][destination[1]] == (capturerer.isWhite() ? Piece.BlackPawn : Piece.WhitePawn)) {
                    enPassant[capturerer.isWhite() ? 1 : 0][0][location[0]] = true;
                }
                if (location [0] != 0 && board[location[0] - 1][destination[1]] == (capturerer.isWhite() ? Piece.BlackPawn : Piece.WhitePawn)) {
                    enPassant[capturerer.isWhite() ? 1 : 0][0][location[0] - 1] = true;
                }
            }
            if ((capturerer == Piece.WhitePawn || capturerer == Piece.BlackPawn) && destination[1] == (capturerer.isWhite() ? 7 : 0)) {
                throw new NeedToPromotePawnException();
            }
            checkConditions();
            //throw new NotImplementedException();
            return capturee;
        } else {
            throw new IllegalMoveException();
        }
    }

    /**
     * Find if a particular location is ``safe'' for a colour
     * @param location the location to check
     * @param colour the colour for the location
     * @return if it is safe
     */
    boolean safe(int[] location, boolean colour){
        boolean isSafe = true;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {//cycles through board looking for attackers
                Piece threat = board[i][j];//potential attacker
                if (threat != null && colour != threat.isWhite()) {//attacker is of the opposite colour
                    if((threat == Piece.WhitePawn || threat == Piece.BlackPawn || threat == Piece.WhiteKnight ||
                            threat == Piece.BlackKnight || threat == Piece.WhiteKing || threat == Piece.BlackKing)){//piece moves to specific squares
                        for (int k = 0; k < (threat == Piece.WhitePawn || threat == Piece.BlackPawn ? 2 : 8); k++) {//searches through target squares of attacker
                            try {
                                if (Arrays.equals(location, new int[]//if you are being attacked
                                        {i + threat.getPossibleMoves()[k][0], j + threat.getPossibleMoves()[k][1]})) {
                                    isSafe = false;//you are not safe
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                        }
                    } else {//piece moves "infinitely"
                        int[][] spread = Piece.WhiteQueen.getPossibleMoves();//spread represents the 8 pointed "sun" radiating from one point in 8 directions (4 cardinal, 4 diagonal) for 7 squares as that is the max distance you can travel on a chessboard
                        for (int k = 0; k < spread.length; k++) {//cycles through all possible squares of attacker
                            try {
                                if (Arrays.equals(location, new int[]{i + spread[k][0], j + spread[k][1]}) &&//if the attacker has a direct line to you AND is a queen OR
                                        (threat == Piece.WhiteQueen || threat == Piece.BlackQueen ||
                                                (((threat == Piece.WhiteBishop || threat == Piece.BlackBishop) && k % 14 >= 7) ||//is a bishop AND we are checking a diagonal OR
                                                        (threat == Piece.WhiteRook || threat == Piece.BlackRook) && k % 14 < 7))) {//is a rook AND we are checking a cardinal direction
                                    isSafe = false;//you are not safe
                                } else if ((board[i + spread[k][0]][j + spread[k][1]] != null &&
                                        colour != board[i + spread[k][0]][j + spread[k][1]].isWhite()) ||//this else if is to prevent a queen from seeing you if there is something in the middle, if its looking at a square occupied by a piece friendly to the attacker OR
                                        (k % 7 != 0 && board[i + spread[k - 1][0]][j + spread[k - 1][1]] != null &&
                                                colour == board[i + spread[k - 1][0]][j + spread[k - 1][1]].isWhite())) {//the "previous" square was occupied by an opposing piece
                                    k -= k % 7 - 6;//jump ahead to next of 8 directions
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                        }
                    }
                }
            }
        }
        return isSafe;
    }

    /**
     * The number of plies passed
     * @return the number of plies played in the game
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Is it White's turn
     * @return if it's White's move
     */
    public boolean getIsWhitesTurn() {
        return isWhitesTurn;
    }

    public class ChessException extends Throwable {}

    public class IsNotYourTurnException extends ChessException {}

    public class IllegalMoveException extends ChessException {}

    public class NeedToPromotePawnException extends ChessException {}
}