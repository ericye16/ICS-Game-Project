import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The GraveyardPanel is a JPanel that displays the difference between the pieces on the board and the number of pieces
 * that should be on a normal chess board.
 * Usually, this means that it will count the number of pieces that have been captured. However, in circumstances
 * such as following a pawn promotion, it may count negative captured pieces, for example when three rooks of a colour
 * are on a board.
 */
public class GraveyardPanel extends JPanel {
    private Board board;
    private HashMap<Piece, Integer> theDead = new HashMap<Piece, Integer>();

    /**
     * Constructor for the GraveyardPanel.
     * @param board the Board object the GraveyardPanel will count.
     */
    public GraveyardPanel(Board board) {
        setBoard(board);
        resetCount();
    }

    /**
     * Method to manually set the GraveyardPanel's board without reconstructing it. Useful for restarting games.
     * @param board the Board object to count.
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Reset the count of the pieces on the board.
     */
    private void resetCount() {
        for (Piece piece: Piece.values()) {
            theDead.put(piece, 0);
        }
    }

    /**
     * Method to be called to "refresh" the GraveyardPanel.
     * It recounts all the pieces on the board and also calls repaint() afterwards.
     */
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

    /**
     * Method to paint all the components on the GraveyardPanel. Should not be called manually.
     * @param g the Graphics object to paint on.
     */
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

    /**
     * Get the preferred size of the GraveyardPanel (86x640). Designed to be compatible with ChessPanel but still
     * visually appealing.
     * @return
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(86, 640);
    }

    /**
     * Find the correct height, in pixels, of one piece drawn on the GraveyardPanel.
     * @return the height in pixels.
     */
    private int getDiv() {
        return getHeight() / 10;
    }

    /**
     * Draw the piece and also show how many are remaining.
     * @param piece the piece to draw.
     * @param remaining the number remaining.
     * @param position the position of the piece on the graveyard (0 - 9).
     * @param g the Graphics Object to draw on.
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
