import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

public class ISeeChessTest {
    Board board;

    @Before
    /**
     * Create a fresh board every run
     */
    public void init() {
        board = new Board();
    }

    @After
    /**
     * Destroy the board after each run
     */
    public void destroy() {
        board = null;
    }

    @Test
    /**
     * Check that all pawns go where they should
     */
    public void testAllPawns() {
        for (int i = 0; i < 8; i++) {
            testPawn(i, 1, Piece.WhitePawn);
            testPawn(i, 6, Piece.BlackPawn);
        }
    }


    /**
     * Check to see that the pawn says it can go where it can go
     */
    public void testPawn(int x, int y, Piece pieceType) {
        ArrayList<Integer[]> pawnPossibles = board.allValidMoves(pieceType, new int[] {x, y});
        ArrayList<Integer[]> correct = new ArrayList<Integer[]>();
        if (pieceType == Piece.WhitePawn) {
            correct.add(new Integer[] {0,1});
            correct.add(new Integer[] {0,2});
        } else {
            correct.add(new Integer[] {0, -1});
            correct.add(new Integer[] {0, -2});
        }
        System.err.println(correct.size());
        System.err.println(pawnPossibles.size());
        assertTrue(correct.size() == pawnPossibles.size());
        boolean[] correctFlags = new boolean[pawnPossibles.size()];
        for (int i1 = 0; i1 < correctFlags.length; i1++) {
            correctFlags[i1] = true;
        }
        for (Integer[] pawnLoc : pawnPossibles) {
            for (int i = 0; i < correct.size(); i++) {
                if (!correctFlags[i]) {
                    continue;
                }
                Integer[] correctLoc = correct.get(i);
                if (Arrays.deepEquals(pawnLoc, correctLoc)) {
                    correctFlags[i] = false;
                    break;
                }
            }
        }
        for (boolean flag: correctFlags) {
            if (flag) {
                fail("Flags don't work");
            }
        }
    }

    @Test
    /**
     * Test that none of the pawns have moved after checking a piece for its movability
     */
    public void testPawnConsistency() {
        Board second = new Board();
        //please note that allValidMoves _should not_ modify the board in any way
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 2) {
                    j = 6;
                }
                board.allValidMoves(second.getBoard()[i][j], new int[]{i, j});
                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        System.err.print(board.getBoard()[r][c]);
                        assertEquals(second.getBoard()[r][c], board.getBoard()[r][c]);
                    }
                    System.err.println();
                }
            }
        }
    }

    @Test
    /**
     * Test that white can move on first turn
     */
    public void testWhiteFirstMove() {
        try {
            board.movePiece(new int[] {4,1}, new int[] {4,2});
            assertEquals(board.getBoard()[4][2], Piece.WhitePawn);
        } catch (Board.IsNotYourTurnException e) {
            e.printStackTrace();
            fail();
        } catch (Board.IllegalMoveException e) {
            e.printStackTrace();
            fail();
        } catch (Board.NeedToPromotePawnException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**
     * Test that black can move on second turn
     */
    public void testBlackSecondMove() {
        try {
            board.movePiece(new int[] {4,1}, new int[] {4,2});
            assertEquals(board.getBoard()[4][2], Piece.WhitePawn);
            board.movePiece(new int[] {4, 6}, new int[] {4, 4});
            assertEquals(board.getBoard()[4][4], Piece.BlackPawn);
            assertNull(board.getBoard()[4][6]);
        } catch (Board.IsNotYourTurnException e) {
            e.printStackTrace();
            fail();
        } catch (Board.IllegalMoveException e) {
            e.printStackTrace();
            fail();
        } catch (Board.NeedToPromotePawnException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**
     * Test that black cant move whereever
     */
    public void testBlackFail() throws Board.ChessException {
        try {
            board.movePiece(new int[] {4,1}, new int[] {4,2});
            assertEquals(board.getBoard()[4][2], Piece.WhitePawn);
            board.movePiece(new int[] {4, 6}, new int[] {5, 5});
            fail();
        } catch (Board.IllegalMoveException e) {
        }
    }

    @Test
    public void nomTest() {
        try {
            board.movePiece(new int[] {4, 1}, new int[] {4, 3});
            board.movePiece(new int[] {3, 6}, new int[] {3, 4});
            board.movePiece(new int[] {4, 3}, new int[] {3, 4});
            assertNull(board.getBoard()[4][3]);
            assertEquals(board.getBoard()[3][4], Piece.WhitePawn);
        } catch (Board.ChessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**
     * Test that black can't move on first turn
     */
    public void testBlackFirstMove() {
        try {
            board.movePiece(new int[] {3, 6}, new int[] {3, 5});
            fail(); // if it gets here it sucks
        } catch (Board.IsNotYourTurnException e) { // this is what should happen
            assertNull(board.getBoard()[3][5]); //check that nothing else has changed
            assertEquals(board.getBoard()[3][6], Piece.BlackPawn);
            //e.printStackTrace();
        } catch (Board.IllegalMoveException e) {
            e.printStackTrace();
            fail(); // not that kind of exception
        } catch (Board.NeedToPromotePawnException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**
     * Test that white can't move to an illegal position on first turn
     */
    public void testWhiteFirstIllegalMove() {
        try {
            board.movePiece(new int[] {3, 1}, new int[] {4, 2});
            fail(); // should not get here
        } catch (Board.IsNotYourTurnException e) { //should not be this
            e.printStackTrace();
            fail();
        } catch (Board.IllegalMoveException e) { //should be this
            assertNull(board.getBoard()[4][2]);
            assertEquals(board.getBoard()[3][1], Piece.WhitePawn);
        } catch (Board.NeedToPromotePawnException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSafe () {
        Board second = new Board();
        //please note that allValidMoves _should not_ modify the board in any way
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.safe(new int[] {i, j}, true);
                board.safe(new int[] {i, j}, false);
                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        assertEquals(second.getBoard()[r][c], board.getBoard()[r][c]);
                    }
                }
            }
        }
    }

    @Test
    public void testSecondPawnMove() throws Board.ChessException {
        board.movePiece(new int[] {4, 1}, new int[] {4, 2}); // white pawn forward one move
        board.movePiece(new int[] {5, 6}, new int[] {5, 5}); //black pawn forward one
        ArrayList<Integer[]> possibleMoves = board.allValidMoves(Piece.WhitePawn, new int[] {4,2});
        assertEquals(1, possibleMoves.size()); // there should only be one possible move
        assertTrue(Arrays.equals(possibleMoves.get(0), new Integer[] {0, 1}));
    }

    @Test
    public void testIsIn() {
        ArrayList<Integer[]> testAgainst = new ArrayList<Integer[]>();
        testAgainst.add(new Integer[]{1, 2});
        testAgainst.add(new Integer[]{2, 3});
        testAgainst.add(new Integer[] {4, 5});
        assertTrue(ChessPanel.isIn(testAgainst, new Integer[] {1,2}));
        assertFalse(ChessPanel.isIn(testAgainst, new Integer[] {1,3}));
        assertFalse(ChessPanel.isIn(testAgainst, new Integer[] {6,7}));
    }

    @Test
    public void testWhiteKingCheck() throws Board.ChessException {
        board.movePiece(new int[] {5, 1}, new int[] {5, 2});
        board.movePiece(new int[] {4, 6}, new int[] {4, 5});
        board.movePiece(new int[] {3, 1}, new int[] {3, 2});
        board.movePiece(new int[] {3, 7}, new int[] {7, 3});
        ArrayList<Integer[]> possibleMoves;
        //king should be able to hide
        possibleMoves = board.allValidMoves(Piece.WhiteKing, new int[] {4, 0});
        assertEquals(1, possibleMoves.size());
        assertArrayEquals(new Integer[] {1,2}, new Integer[] {1,2});
        assertArrayEquals(new Integer[] {-1, 1}, possibleMoves.get(0));
        //pawn should be able to block queen
        possibleMoves = board.allValidMoves(Piece.WhitePawn, new int[] {6, 1});
        assertEquals(1, possibleMoves.size());
        assertArrayEquals(new Integer[] {0, 1}, possibleMoves.get(0));
        //other pawn should not be able to move
        possibleMoves = board.allValidMoves(Piece.WhitePawn, new int[] {7, 1});
        assertEquals(0, possibleMoves.size());

    }

    /*@Test
    public void kingTest() throws Board.ChessException {
        Board board = new Board();
        System.err.println(board.whiteKingLocation[0] + "\t" + board.whiteKingLocation[1]);
        board.movePiece(new int[] {5, 1}, new int[] {5, 2});
        System.err.println(board.whiteKingLocation[0] + "\t" + board.whiteKingLocation[1]);
        board.movePiece(new int[] {4, 6}, new int[] {4, 5});
        System.err.println(board.whiteKingLocation[0] + "\t" + board.whiteKingLocation[1]);
        board.movePiece(new int[] {0, 1}, new int[] {0, 2});
        System.err.println(board.whiteKingLocation[0] + "\t" + board.whiteKingLocation[1]);
        board.movePiece(new int[] {3, 7}, new int[] {7, 3});
        System.err.println(board.whiteKingLocation[0] + "\t" + board.whiteKingLocation[1]);
    }

    @Test
    public void safeMethodTest() {
        Piece[][] prelimBoard = new Piece[][] {{null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}};
        prelimBoard[4][4] = Piece.WhiteQueen;
        prelimBoard[0][0] = Piece.WhiteKnight;
        prelimBoard[7][7] = Piece.WhiteKing;
        prelimBoard[1][7] = Piece.WhiteBishop;
        prelimBoard[0][1] = Piece.WhitePawn;
        prelimBoard[5][6] = Piece.WhiteRook;
        Board board = new Board(prelimBoard);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!board.safe(new int[] {i, j}, false)) {
                    System.err.println((i + 1) + "\t" + (j + 1));
                }
            }
        }
    }      */
}