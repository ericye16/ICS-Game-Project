import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 11/6/13
 * Time: 8:37 AM
 */
public class GraveyardPanel extends JPanel {
    private Board board;
    private HashMap<Piece, Integer> theDead = new HashMap<Piece, Integer>();

    public GraveyardPanel(Board board) {
        this.board = board;
        for (Piece piece : Piece.values()) {
            theDead.put(piece, 0);
        }
    }

    public void reCount() {
        for (Piece[] col: board.getBoard()) {
            for (Piece piece: col) {
                theDead.put(piece, theDead.get(piece) + 1);
            }
        }
        for (Piece piece : Piece.values()) {
            int numberShouldExist;
            switch (piece) {
                case WhitePawn:
                case BlackPawn:
                    numberShouldExist = 8;
                    break;
                case WhiteKnight:
                case BlackKnight:
                case WhiteBishop:
                case BlackBishop:
                case WhiteRook:
                case BlackRook:
                    numberShouldExist = 2;
                    break;
                case WhiteQueen:
                case BlackQueen:
                case WhiteKing:
                case BlackKing:
                    numberShouldExist = 1;
                    break;
            }
        }
    }
}
