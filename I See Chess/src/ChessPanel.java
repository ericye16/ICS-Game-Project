import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 26/10/13
 * Time: 5:32 PM
 */
public class ChessPanel extends JPanel implements MouseListener {

    private Board board;

    public ChessPanel(Board board) {
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.board = board;
        addMouseListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640,640);
    }

    @Override
    public Color getBackground() {
        return Color.WHITE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
    }

    private void drawBackground(Graphics g) {
        for (int line = 0; line < 8; line++) {
            int differenceX = getWidth() / 8;
            int differenceY = getHeight() / 8;
            //draw the vertical line
            g.drawLine(line * differenceX, 0, line * differenceX, getHeight());
            g.drawLine(0, line * differenceY, getWidth(), line * differenceY);
        }

    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {} // we really don't care

    @Override
    public void mouseExited(MouseEvent mouseEvent) {} //don't care
}
