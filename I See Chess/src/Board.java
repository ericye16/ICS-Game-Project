import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class holds information about everything in the current state of the game.
 * The position on the board, whose turn it is, what positions have already occurred, and the like.
 *
 * All chess game logic occurs through the Board class and its member methods.
 * In order to move a piece, movePiece() should be called and its exceptions checked.
 * ChessExceptions should be checked and handled as necessary.
 * Following a movePiece() call, checkConditions() must also be called in order to update
 * the boards check and stalemate flags.
 *
 * The Board's coordinate system is in (x, y), where x indicates the horizontal location of the square,
 * starting from the left. y indicates the vertical location of the square, starting from the bottom, i.e. the white side.
 * Note that this is different from the coordinate system of ChessPanel.
 */
public class Board implements Cloneable {
    private Piece[][] board;
    private ArrayList<Board> history = new ArrayList<Board>();
    private boolean isWhitesTurn, whiteCheck, blackCheck, whiteStalemate, blackStalemate, fiftyMoves, threeBoards, stalemate, whiteMate, blackMate;
    private boolean[][] castlingFlags = {{true, true}, {true, true}};
    private boolean[][][] enPassant = {{{false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false}},
            {{false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false}}}; //array representing whether one can currently en passant there are 7 ways to do this, in 2 directions, by 2 colours, array is 2 * 2 * 7
    private int turn = 0, whitePawnLocation, blackPawnLocation;
    int[] whiteKingLocation, blackKingLocation;

    /**
     * This is the default constructor for the Board class. This sets the values of the board, by putting pieces in their proper default locations.
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
        whiteCheck = false;
        blackCheck = false;
        whiteStalemate = false;
        blackStalemate = false;
        fiftyMoves = false;
        threeBoards = false;
        stalemate = false;
        whiteMate = false;
        blackMate = false;
        whiteKingLocation = new int[] {4, 0};
        blackKingLocation = new int[] {4, 7};
    }

    /**
     * Additional constructor for the Board. It accepts a board and sets the board of the class to the accepted board.
     * @param currentBoard The accepted board that the board of this class will be set to.
     */
    Board(Piece[][] currentBoard) {
        board = currentBoard;
    }

    /**
     * This method accepts a piece and its location. It will then proceed to analyze the board and determine which squares the piece can move onto. Then return an ArrayList of valid locations relative to the piece. This method is run every time the user tries to move a piece.
     * @param piece This is the type of piece that is being moved.
     * @param location This is an array of length 2 representing the X and Y coordinates of the piece the user is trying to move.
     * @return Returns an ArrayList of arrays of length 2. These array are the X and Y differences between the piece we are moving and the possible target square. These coordinates are all relative to the piece we are moving.
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
                boolean isSafe, added = false;
                Piece placeholder = board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]];//target square
                board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] = piece;//simulates moving piece, puts piece on target
                if (piece == Piece.WhiteKing) {
                    whiteKingLocation = new int[] {location[0] + possibleMoves[i][0], location[1] + possibleMoves[i][1]};
                } else if (piece == Piece.BlackKing) {
                    blackKingLocation = new int[] {location[0] + possibleMoves[i][0], location[1] + possibleMoves[i][1]};
                }
                board[location[0]][location[1]] = null;//empties square that piece was on
                isSafe = safe(findKing(colour), colour);
                board[location[0]][location[1]] = piece;
                if (piece == Piece.WhiteKing) {
                    whiteKingLocation = new int[] {location[0], location[1]};
                } else if (piece == Piece.BlackKing) {
                    blackKingLocation = new int[] {location[0], location[1]};
                }
                board[location[0] + possibleMoves[i][0]][location[1] + possibleMoves[i][1]] = placeholder;
                switch (piece){
                    case WhitePawn:
                    case BlackPawn:
                        switch (i){
                            case 0:
                            case 1:
                                if ((placeholder != null && colour != placeholder.isWhite()) ||//allows if attacker wants to capture enemy piece
                                    (i == 0 && location[0] != 0 && enPassant[(colour ? 0 : 1)][0][location[0] - 1]) ||
                                        (i == 1 && location[0] != 7 && enPassant[(colour ? 0 : 1)][1][location[0]])) {//en passant right
                                    validMoves.add(possibleMovesInteger[i]);//validate
                                    added = true;
                                }
                                break;
                            case 3:
                                if (placeholder != null || !(location[1] == (colour ?  1 : 6))) {//checks 2 squares ahead is empty, if it is, it will move on to check one square ahead
                                    break;
                                }
                            case 2:
                                if (board[location[0] + possibleMoves[2][0]][location[1] + possibleMoves[2][1]] == null) {//checks 1 square ahead is empty
                                    validMoves.add(possibleMovesInteger[i]);//validate
                                    added = true;
                                }
                                break;
                        }
                        break;
                    case WhiteKnight:
                    case BlackKnight:
                    case WhiteKing:
                    case BlackKing:
                        if ((i < 8 && (placeholder == null ||
                                colour != placeholder.isWhite())) ||//allows if attacker wants to capture enemy piece
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
                            added = true;
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
                            added = true;
                        }
                        break;
                }
                if (!isSafe && added) {
                    validMoves.remove(validMoves.size() - 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return validMoves;
    }

    /**
     * This method analyzes the board to see if any endgame conditions have occurred. It checks for check, mate, and stalemate. This method is run after every move.
     */
    void checkConditions() throws StalemateException, CheckmateException {
        whiteStalemate = true;
        blackStalemate = true;
        if (!safe(findKing(true), true)) {
            whiteCheck = true;
            whiteMate = true;
        }
        if (!safe(findKing(false), false)) {
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
        if (history.size() > 100) {
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
        if (stalemate && !whiteMate && !blackMate) {
            throw new StalemateException();
        }
        if (whiteMate || blackMate) {
            throw new CheckmateException(whiteMate);
        }
    }

    /**
     * Method to perform pawn promotion. The method also validates the promotion and will throw exceptions
     * as necessary if the promotion does not validate.
     * @param prevLocation The current location of the pawn to be promoted.
     * @param newPiece The Piece to promote the pawn into.
     * @throws IllegalMoveException If the pawn being promoted does not exist or otherwise should not be promoted.
     * @throws IsNotYourTurnException If the pawn being promoted is the wrong colour or is being promoted into the wrong colour.
     */
    public void promotePawn(int[] prevLocation, Piece newPiece) throws IllegalMoveException, IsNotYourTurnException {
        /*
        PSA: Remember that at this point, the turn is listed as the next player's turn
         */
        Piece pieceAt = board[prevLocation[0]][prevLocation[1]];
        if (pieceAt == null || pieceAt.isWhite() != !getIsWhitesTurn()) {
            throw new IsNotYourTurnException();
        }
        if ((pieceAt != Piece.BlackPawn && pieceAt != Piece.WhitePawn) || prevLocation[0] != getPawnLocation(!getIsWhitesTurn()) || prevLocation[1] != (!getIsWhitesTurn()? 7 : 0)) {
            throw new IllegalMoveException();
        }
        if (newPiece.isWhite() != pieceAt.isWhite()) {
            throw new IllegalMoveException();
        }
        if (newPiece == Piece.BlackBishop || newPiece == Piece.BlackKnight || newPiece == Piece.BlackQueen || newPiece == Piece.BlackRook ||
                newPiece == Piece.WhiteBishop || newPiece == Piece.WhiteKnight || newPiece == Piece.WhiteQueen || newPiece == Piece.WhiteRook) {
            board[prevLocation[0]][prevLocation[1]] = newPiece;
        } else {
            throw new IllegalMoveException();
        }
    }

    @Override
    /**
     * Clone the object and everything within it.
     */
    protected Object clone() throws CloneNotSupportedException {
        Board toReturn = (Board) super.clone();
        toReturn.board = board.clone();
        for (int i = 0; i < 8; i++) {
            toReturn.board[i] = board[i].clone();
        }
        toReturn.castlingFlags = castlingFlags.clone();
        toReturn.enPassant = enPassant.clone();
        toReturn.history = (ArrayList<Board>) history.clone();
        return toReturn;
    }

    @Override
    /**
     * Check equality between two Board objects.
     * Note that this only checks whether the pieces are in the same locations and disregards history, which
     * means that stalemate or castling may not be identical in both boards.
     * @param other The other Board object
     * @return true if the objects are equal.
     */
    public boolean equals(Object other) {
        return other instanceof Board && Arrays.deepEquals(board, ((Board) other).board);
    }

    /**
     * Returns an array of length 2 with the coordinates of the king given my the parameter.
     * @param colour Boolean for which king you want to find. True is white, false is black.
     * @return Returns the location of the king, which is saved in a variable.
     */
    int[] findKing(boolean colour) {
        return colour ? whiteKingLocation : blackKingLocation;
    }

    /**
     * Getter that returns the board as a 2-Dimensional array of pieces.
     * @return Returns the board.
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * This is a necessary method for the column in which a promoting pawn is located. The parameter determines the colour of the variable you want.
     * @param colour True is white and false is black.
     * @return Returns the appropriate variable saved in this class.
     */
    public int getPawnLocation(boolean colour) {
        return (colour ? whitePawnLocation : blackPawnLocation);
    }


    /**
     * This method is run after the move has been selected. It will undergo the procedure of actually moving the piece. It accepts the coordinates of the location from and to where the piece is moving. It returns the type of piece that is being moved so that the panel can draw it correctly. This method also is responsible for raising and lowering flags responsible for en passant and castling.
     * @param location An array of length 2, representing the coordinates of where the piece was, prior to being moved.
     * @param destination An array of length 2, representing where the piece will end up, after being moved.
     * @return Returns the type of piece that was being moved.
     * @throws IsNotYourTurnException This is an exception used for testing. It will never actually occur in a game. It occurs when the variable keeping track of who's turn it is will state that it is not the turn of the player to whom belongs the piece located on the square from which the piece is being moved.
     * @throws IllegalMoveException This is an exception used for testing. It will never actually occur in a game. It occurs when one tries to move a piece to a location that the piece is not allowed to move to.
     */
    Piece movePiece(int[] location, int[] destination) throws ChessException {
        Piece capturerer = board[location[0]][location[1]];
        boolean wasAValidMove = false, colour = capturerer.isWhite();
        if (colour != getIsWhitesTurn()) {
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
            if ((capturerer == Piece.WhitePawn || capturerer == Piece.BlackPawn) && capturee == null && location[0] != destination[0]) {
                capturee = (colour ? Piece.BlackPawn : Piece.WhitePawn);
                board[destination[0]][location[1]] = null;
            }
            Board prevBoard = null;
            try {
                prevBoard = (Board) this.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError(); // shouldn't happen
            }
            prevBoard.history = null; // to remove the amount of memory required
            board[destination[0]][destination[1]] = capturerer;
            board[location[0]][location[1]] = null;
            if (capturerer == Piece.WhitePawn || capturerer == Piece.BlackPawn || capturee != null) {
                history = new ArrayList<Board>();
            }
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
            if ((capturerer == Piece.WhiteKing || capturerer == Piece.BlackKing) && Math.abs(destination[0] - location[0]) == 2) {
                board[(destination[0] < location[0] ? 0 : 7)][location[1]] = null;
                board[location[0] + (destination[0] - location[0]) / 2][location[1]] = (colour ? Piece.WhiteRook : Piece.BlackRook);
            }

            for (int i = 0; i < castlingFlags.length; i++) {
                for (int j = 0; j < castlingFlags.length; j++) {
                    if (board[i * 7][j * 7] != (j == 0 ? Piece.WhiteRook : Piece.BlackRook)) {
                        castlingFlags[j][i] = false;
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
            if ((capturerer == Piece.WhitePawn || capturerer == Piece.BlackPawn) && location[1] == (colour ? 1 : 6) && destination[1] == (colour ? 3 : 4)) {
                if (location [0] != 7 && board[location[0] + 1][destination[1]] == (colour ? Piece.BlackPawn : Piece.WhitePawn)) {
                    enPassant[colour ? 1 : 0][0][location[0]] = true;
                }
                if (location [0] != 0 && board[location[0] - 1][destination[1]] == (colour ? Piece.BlackPawn : Piece.WhitePawn)) {
                    enPassant[colour ? 1 : 0][1][location[0] - 1] = true;
                }
            }
            if ((capturerer == Piece.WhitePawn || capturerer == Piece.BlackPawn) && destination[1] == (colour ? 7 : 0)) {

                if (capturerer.isWhite()) {
                    whitePawnLocation = destination[0];
                } else {
                    blackPawnLocation = destination[0];
                }
                throw new NeedToPromotePawnException(capturerer.isWhite());
            }
            checkConditions();
            return capturee;
        } else {
            throw new IllegalMoveException();
        }
    }

    /**
     * This method figures out whether a certain square is safe for a certain colour. Safe means that there is not a single enemy piece that can capture a piece on the square in one move.
     * @param location An array of length 2 that represents the coordinates of the square that needs to be checked.
     * @param colour The colour of pieces that the square is or isn't safe for. True is white and false is black.
     * @return Returns a boolean value that is true if the square was safe for that colour.
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
                                if ((board[i + spread[k][0]][j + spread[k][1]] != null &&
                                        colour != board[i + spread[k][0]][j + spread[k][1]].isWhite()) ||//this else if is to prevent a queen from seeing you if there is something in the middle, if its looking at a square occupied by a piece friendly to the attacker OR
                                        (k % 7 != 0 && board[i + spread[k - 1][0]][j + spread[k - 1][1]] != null &&
                                                colour == board[i + spread[k - 1][0]][j + spread[k - 1][1]].isWhite())) {//the "previous" square was occupied by an opposing piece
                                    k -= k % 7 - 6;//jump ahead to next of 8 directions
                                } else if (Arrays.equals(location, new int[]{i + spread[k][0], j + spread[k][1]}) &&//if the attacker has a direct line to you AND is a queen OR
                                        (threat == Piece.WhiteQueen || threat == Piece.BlackQueen ||
                                                (((threat == Piece.WhiteBishop || threat == Piece.BlackBishop) && k % 14 >= 7) ||//is a bishop AND we are checking a diagonal OR
                                                        ((threat == Piece.WhiteRook || threat == Piece.BlackRook) && k % 14 < 7)))) {//is a rook AND we are checking a cardinal direction
                                    isSafe = false;//you are not safe
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
     * A getter for the current turn. Every move counts as 1 turn. So in one normal chess turn, where both players have gone once, this variable will have increased by 2.
     * @return The turn variable.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * A method that returns whether it is currently white's turn.
     * @return Returns a variable keeping track of this.
     */
    public boolean getIsWhitesTurn() {
        return isWhitesTurn;
    }

    /**
     * A ChessException is any chess game-related exception.
     */
    public class ChessException extends Throwable {}

    /**
     * An IsNotYourTurnException is thrown whenever a move is made for the wrong colour, e.g. if it is White's turn
     * but Black is moved.
     */
    public class IsNotYourTurnException extends ChessException {}

    /**
     * An IllegalMoveException is thrown whenever a move is illegal, for example moving a pawn three squares forward.
     */
    public class IllegalMoveException extends ChessException {}

    /**
     * A NeedToPromotePawnException does not indicate an error, but rather is used to signal the containing
     * ChessPanel that a promotion needs to occur.
     */
    public class NeedToPromotePawnException extends ChessException {
        private boolean colour;

        /**
         * Constructor for the NeedToPromotePawnException.
         * @param colour The colour of the pawn that needs to be promoted.
         */
        public NeedToPromotePawnException(boolean colour) {
            this.colour = colour;
        }

        /**
         * Get the colour of the pawn that needs to be promoted.
         * @return The colour of the pawn to be promoted.
         */
        public boolean getColour() {
            return colour;
        }
    }

    /**
     * A CheckmateException does not indicate an error, but rather is used to signal to the containing ChessPanel
     * that a checkmate has occurred and needs to be handled.
     */
    public class CheckmateException extends ChessException {
        private boolean colourOfMated;

        /**
         * Constructor for the CheckmateException. Sets the colour of the <i>mated</i> player.
         * @param colourOfMated The colour of the mated player.
         */
        public CheckmateException(boolean colourOfMated) {
            this.colourOfMated = colourOfMated;
        }

        /**
         * Get the colour of the mated player.
         * @return The colour of the mated player.
         */
        public boolean getColourOfMated() {
            return colourOfMated;
        }
    }

    /**
     * A Stalemate Exception does not indicate an error, but rather is used to signal the containing ChessPanel
     * that a stalemate has occurred.
     */
    public class StalemateException extends ChessException {}
}