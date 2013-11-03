import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 03/11/13
 * Time: 1:22 PM
 */
public class ISeeChessTest {
    Board board = new Board();

    @Test
    public void testPawn() {
        ArrayList<Integer[]> pawnPossibles = board.allValidMoves(Piece.WhitePawn, new int[] {3, 1});
        ArrayList<Integer[]> correct = new ArrayList<Integer[]>();
        correct.add(new Integer[] {3,2});
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
}
