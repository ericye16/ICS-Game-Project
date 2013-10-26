import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 26/10/13
 * Time: 5:32 PM
 */
public class ChessPanel extends JPanel {

    private Board board;

    public ChessPanel(Board board) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.board = board;
    }

    public Dimension getPreferredSize() {
        return new Dimension(640,640);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //do stuff here
    }

    public Board getBoard() {
        return board;
    }
}
