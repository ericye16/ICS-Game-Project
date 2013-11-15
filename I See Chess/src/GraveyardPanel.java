import javax.swing.*;
import java.awt.*;
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
        resetCount();
    }

    private void resetCount() {
        for (Piece piece: Piece.values()) {
            theDead.put(piece, 0);
        }
    }

    public void reCount() {
        resetCount();
        for (Piece[] col: board.getBoard()) {
            for (Piece piece: col) {
                if (piece != null) {
                    theDead.put(piece, theDead.get(piece) + 1);
                }
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
                default:
                    numberShouldExist = 0; //should never happen
                    break;
            }
            theDead.put(piece, numberShouldExist - theDead.get(piece));
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setOpaque(true);
        setBackground(Color.darkGray);
        //ensure that order is what you want it to be
        Piece[] pieces = new Piece[]
                {Piece.BlackQueen, Piece.BlackRook, Piece.BlackKnight, Piece.BlackBishop, Piece.BlackPawn,
                 Piece.WhiteQueen, Piece.WhiteRook, Piece.WhiteKnight, Piece.WhiteBishop, Piece.WhitePawn};

        for (int i = 0; i < pieces.length; i++) {
            drawPiece(pieces[i], theDead.get(pieces[i]), i, g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(86, 640);
    }

    private int getDiv() {
        return getHeight() / 10;
    }

    /**
     * Draw the piece and also show how many are remaining
     * @param piece the piece to draw
     * @param remaining the number remaining
     * @param position the position of the piece on the graveyard (0 - 9)
     * @param g the Graphics Object to draw on
     */
    private void drawPiece(Piece piece, int remaining, int position, Graphics g) {
        g.drawImage(piece.image,
                0,
                position * getDiv(),
                getDiv(),
                (position + 1) * getDiv(),
                0,
                0,
                piece.image.getWidth(),
                piece.image.getHeight(),
                null
        );
        g.setFont(new Font("Helvetica", Font.BOLD ,24));
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(remaining), getDiv(), position * getDiv() + 32);
    }
}
