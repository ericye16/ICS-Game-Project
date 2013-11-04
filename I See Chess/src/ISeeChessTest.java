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
        ArrayList<Integer[]> pawnPossibles = board.allValidMoves(Piece.WhitePawn, new int[] {3, 1});
        ArrayList<Integer[]> correct = new ArrayList<Integer[]>();
        correct.add(new Integer[] {0,1});
        correct.add(new Integer[] {0,2});
        System.err.println(correct.size());
        System.err.println(pawnPossibles.size());
        assertTrue(correct.size() == pawnPossibles.size());
        System.err.println(pawnPossibles.get(0)[0]);
        for (Integer[] i : pawnPossibles) {
            for (Integer j : i) {
                System.err.print(j);
            }
        }
        for (Integer[] i : correct) {
            for (Integer j : i) {
                System.err.print(j);
            }
        }
    }

    @Test
    /**
     * Test that none of the pieces have moved after checking a piece for its movability
     */
    public void testPawnConsistency() {
        Board second = new Board();
        //please note that allValidMoves _should not_ modify the board in any way
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.allValidMoves(second.getBoard()[i][j], new int[] {i , j});
                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        assertEquals(second.getBoard()[r][c], board.getBoard()[r][c]);
                    }
                }
            }
        }
    }
}
