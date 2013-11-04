import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 03/11/13
 * Time: 1:22 PM
 */
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
        for (int i = 0; i < 7; i++) {
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
     * Test that white can move on first turn
     */
    public void testWhiteFirstMove() {
        try {
            board.movePiece(new int[] {3,1}, new int[] {3,2});
        } catch (Board.IsNotYourTurnException e) {
            e.printStackTrace();
            fail();
        } catch (Board.IllegalMoveException e) {
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
        } catch (Board.IsNotYourTurnException e) {
            //e.printStackTrace();
        } catch (Board.IllegalMoveException e) {
            e.printStackTrace();
            fail(); // not that kind of exception
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
        } catch (Board.IllegalMoveException e) {
        }
    }
}
