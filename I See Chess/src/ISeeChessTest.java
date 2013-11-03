/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 03/11/13
 * Time: 1:22 PM
 */
public class ISeeChessTest {
    public static void main(String[] args) {
        Board board = new Board();
        board.allValidMoves(Piece.WhitePawn, new int[] {3, 1});//breaks
        board.allValidMoves(Piece.WhitePawn, new int[] {2, 0});//also  breaks
    }
}
