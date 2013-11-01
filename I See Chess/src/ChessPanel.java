import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
        drawPieces(g);
    }

    private void drawBackground(Graphics g) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0 ; c < 8; c++) {
                if ((r + c) % 2 == 0) { // evens are white
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(r * differenceX(), c * differenceY(), differenceX(), differenceY());
            }
        }
    }

    private void drawPieces(Graphics g) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece currentPiece = board.getBoard()[x][y];
                if (currentPiece != null) {
                    drawPieceOnBoard(g, currentPiece, x, y);
                }
            }
        }
    }

    private void drawPieceOnBoard(Graphics g, Piece piece, int x, int y) {
        g.drawImage(piece.image,
                y * differenceY(),
                (7-x) * differenceX(),
                y * differenceY() + differenceY(),
                ((7-x) * differenceX())+differenceX(),
                0,
                0,
                piece.image.getHeight(),
                piece.image.getWidth(),
                null
                );
    }

    private int differenceX() {
        return getWidth() / 8;
    }

    private int differenceY() {
        return getHeight() / 8;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}