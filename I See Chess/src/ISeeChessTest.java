import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 03/11/13
 * Time: 1:22 PM
 */
public class ISeeChessTest {
    Board board;

    @Before
    public void init() {
        board = new Board();
    }

    @After
    public void destroy() {
        board = null;
    }

    @Test
    /**
     * Check to see that the pawn says it can go where it can go
     */
    public void testPawn() {
        ArrayList<Integer[]> pawnPossibles = board.allValidMoves(Piece.WhitePawn, new int[] {1, 3});
        ArrayList<Integer[]> correct = new ArrayList<Integer[]>();
        correct.add(new Integer[] {2, 3});
        correct.add(new Integer[] {3,3});
        System.err.println(correct.size());
        System.err.println(pawnPossibles.size());
        assertTrue(correct.size() == pawnPossibles.size());
        for (Integer[] pawnLoc : pawnPossibles) {
            for (Integer[] correctLoc: correct) {
                if (Arrays.equals(pawnLoc, correctLoc)) {
                    correct.remove(correctLoc);
                }
            }
        }
        assertTrue(correct.size() == 0);
    }

    @Test
    /**
     * Test that none of the pieces have moved after checking a piece for its movability
     */
    public void testPawnConsistency() {
        Board second = new Board();
        //please note that allValidMoves _should not_ modify the board in any way
        board.allValidMoves(Piece.WhitePawn, new int[] {1,3});
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                assertEquals(second.getBoard()[r][c],board.getBoard()[r][c]);
            }
        }
    }
}
