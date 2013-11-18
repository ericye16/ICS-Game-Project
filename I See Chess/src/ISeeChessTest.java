import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

public class ISeeChessTest {
    Board board;

    @Before
    /**
     * Create a fresh board every run.
     */
    public void init() {
        board = new Board();
    }

    @After
    /**
     * Destroy the board after each run.
     * This keeps tests from interfering with each other.
     */
    public void destroy() {
        board = null;
    }

    @Test
    /**
     * Check that all pawns go where they should.
     */
    public void testAllPawns() {
        for (int i = 0; i < 8; i++) {
            testPawn(i, 1, Piece.WhitePawn);
            testPawn(i, 6, Piece.BlackPawn);
        }
    }


    /**
     * Check to see that one pawn says it can go where it can go.
     * @param x the x-location of the pawn in Board coordinates.
     * @param y the y-location of the pawn in Board coordinates.
     * @param pieceType the type of the piece to test, either a WhitePawn or a BlackPawn.
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
     * Test that none of the pawns have moved after checking a piece for its movability.
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
                        assertEquals(second.getBoard()[r][c], board.getBoard()[r][c]);
                    }
                }
            }
        }
    }

    @Test
    /**
     * Test that white can move on first turn.
     */
    public void testWhiteFirstMove() {
        try {
            board.movePiece(new int[] {4,1}, new int[] {4,2});
            assertEquals(board.getBoard()[4][2], Piece.WhitePawn);
        } catch (Board.ChessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**
     * Test that black can move on second turn.
     */
    public void testBlackSecondMove() {
        try {
            board.movePiece(new int[] {4,1}, new int[] {4,2});
            assertEquals(board.getBoard()[4][2], Piece.WhitePawn);
            board.movePiece(new int[] {4, 6}, new int[] {4, 4});
            assertEquals(board.getBoard()[4][4], Piece.BlackPawn);
            assertNull(board.getBoard()[4][6]);
        } catch (Board.ChessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**
     * Test that black can't move into unexpected places.
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

    /**
     * Test that pieces can be captured as expected.
     */
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
     * Test that black can't move on first turn.
     */
    public void testBlackFirstMove() {
        try {
            board.movePiece(new int[] {3, 6}, new int[] {3, 5});
            fail(); // if it gets here it sucks
        } catch (Board.IsNotYourTurnException e) { // this is what should happen
            assertNull(board.getBoard()[3][5]); //check that nothing else has changed
            assertEquals(board.getBoard()[3][6], Piece.BlackPawn);
            //e.printStackTrace();
        } catch (Board.ChessException e) {
            e.printStackTrace();
            fail(); // not that kind of exception
        }
    }

    @Test
    /**
     * Test that white can't move to an illegal position on first turn.
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
        } catch (Board.ChessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**
     * Test that safe() does not modify the board in any way.
     */
    public void testSafe () {
        Board second = new Board();
        //please note that safe() _should not_ modify the board in any way
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
    /**
     * Check that the second move a pawn does only has one possibility (one square ahead) instead of two.
     */
    public void testSecondPawnMove() throws Board.ChessException {
        board.movePiece(new int[] {4, 1}, new int[] {4, 2}); // white pawn forward one move
        board.movePiece(new int[] {5, 6}, new int[] {5, 5}); //black pawn forward one
        ArrayList<Integer[]> possibleMoves = board.allValidMoves(Piece.WhitePawn, new int[] {4,2});
        assertEquals(1, possibleMoves.size()); // there should only be one possible move
        assertTrue(Arrays.equals(possibleMoves.get(0), new Integer[] {0, 1}));
    }

    @Test
    /**
     * Test whether the isIn() function in ChessPanel works as expected.
     */
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
    /**
     * Test whether the board can detect when the white king is in check.
     */
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

    @Test
    /**
     * Test whether the board detects that the pawn should be promoted.
     */
    public void testPawnPromotion() throws Board.ChessException, CloneNotSupportedException {
        board.movePiece(new int[]{4, 1}, new int[]{4, 3});
        board.movePiece(new int[]{5, 6}, new int[]{5, 4});
        board.movePiece(new int[]{4, 3}, new int[]{5, 4}); //eat it!
        board.movePiece(new int[]{4, 6}, new int[]{4, 5});//move the pawn out of the way for the bishop
        board.movePiece(new int[]{5, 4}, new int[]{5, 5});
        board.movePiece(new int[]{5, 7}, new int[]{4, 6});
        board.movePiece(new int[]{5, 5}, new int[]{6, 6});
        board.movePiece(new int[] {6, 7}, new int[] {7, 5});
        try {
            board.movePiece(new int[] {6, 6}, new int[] {6, 7});
            fail("Should have thrown NeedToPromotePawnException");
        } catch (Board.NeedToPromotePawnException e) {
            assertEquals(true, e.getColour());
            assertEquals(6, board.getPawnLocation(true));
        }
        Board[] newBoards = new Board[4];
        for (int i = 0; i < 4; i++) {
            newBoards[i] = (Board) board.clone();
        }
        newBoards[0].promotePawn(new int[] {6, 7}, Piece.WhiteBishop);
        assertEquals(Piece.WhiteBishop, newBoards[0].getBoard()[6][7]);
        newBoards[1].promotePawn(new int[] {6, 7}, Piece.WhiteKnight);
        assertEquals(Piece.WhiteKnight, newBoards[1].getBoard()[6][7]);
        newBoards[2].promotePawn(new int[] {6, 7}, Piece.WhiteQueen);
        assertEquals(Piece.WhiteQueen, newBoards[2].getBoard()[6][7]);
        newBoards[3].promotePawn(new int[] {6, 7}, Piece.WhiteRook);
        assertEquals(Piece.WhiteRook, newBoards[3].getBoard()[6][7]);
    }

    @Test
    /**
     * Test whether the board detects that the pawn shouldn't be promoted.
     */
    public void testPawnPromotionFail() throws Board.ChessException {
        board.movePiece(new int[]{4, 1}, new int[]{4, 3});
        board.movePiece(new int[]{5, 6}, new int[]{5, 4});
        board.movePiece(new int[]{4, 3}, new int[]{5, 4}); //eat it!
        board.movePiece(new int[]{4, 6}, new int[]{4, 5});//move the pawn out of the way for the bishop
        board.movePiece(new int[]{5, 4}, new int[]{5, 5});
        board.movePiece(new int[]{5, 7}, new int[]{4, 6});
        board.movePiece(new int[]{5, 5}, new int[]{6, 6});
        board.movePiece(new int[] {6, 7}, new int[] {7, 5});
    }

    @Test
    /**
     * Test whether the clone() method works in Board class.
     */
    public void testClone() throws CloneNotSupportedException {
        Board second = (Board) board.clone();
        assertNotSame(second.getBoard(), board.getBoard()); // they should be equal but not the same object
    }

    @Ignore
    public void testIntegerClone() {
        Integer[] a = new Integer[] {1,2,3};
        assertNotSame(a, a.clone());
    }
}