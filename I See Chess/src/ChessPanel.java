import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * The ChessPanel, used to display the chess board and all the pieces
 */
public class ChessPanel extends JPanel implements MouseInputListener {
    private Board board;
    private DebugPanel debugPanel;
    private Piece selectedPiece = null;
    private int[] selectedLocation = new int[2];

    /**
     * Constructor for the ChessPanel
     * @param board the Board object the ChessPanel will display
     */
    public ChessPanel(Board board) {
        assert (board != null);
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.board = board;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Get the preferred dimensions (640x640) of the ChessPanel
     * @return The preferred dimensions of the panel.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640,640);
    }

    /**
     * Paint the board and the pieces
     * @param g the Graphics piece to draw on
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawPieces(g);
    }

    /**
     * Draw the background, i.e. the checkered squares
     * @param g the Graphics object to draw on
     */
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

    /**
     * Draw the pieces onto the ChessPanel
     * @param g the Graphics object to draw on
     */
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

    /**
     * Draw a single piece on the board
     * @param g the Graphics object to draw on
     * @param piece the Piece to draw
     * @param x the x-coordinate to draw at, by Board coordinates
     * @param y the y-coordinate to draw at, by Board coordinates
     */
    private void drawPieceOnBoard(Graphics g, Piece piece, int x, int y) {
        g.drawImage(piece.image,
                (x) * differenceY(),
                (7-y) * differenceX(),
                (x) * differenceY() + differenceY(),
                ((7-y) * differenceX())+differenceX(),
                0,
                0,
                piece.image.getHeight(),
                piece.image.getWidth(),
                null
                );
    }

    /**
     * Find the width of one chess square
     * @return the width, in pixels of one chess square
     */
    private int differenceX() {
        return getWidth() / 8;
    }

    /**
     * Find the height of one chess square
     * @return the height, in pixels, of one chess square
     */
    private int differenceY() {
        return getHeight() / 8;
    }

    /**
     * Convert (x, y) from the panel into an (x, y) for the Board class
     * @param x the x from the Panel
     * @param y the y from the Panel
     * @return an integer array containing coordinates as Board expects it
     */
    private int[] convertPanelToBoard(int x, int y) {
        return new int[] {x, 7 - y};
    }

    /**
     * Get the Board class currently being drawn
     * @return the Board class
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Set the DebugPanel, if required
     * @param debugPanel the DebugPanel, or null for none
     */
    public void setDebugPanel(DebugPanel debugPanel) {
        this.debugPanel = debugPanel;
    }

    /**
     * Handle mouse Clicks
     * @param mouseEvent the mouseEvent from the click
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        x /= differenceX();
        y /= differenceY();
        int[] boardLoc = convertPanelToBoard(x, y);
        if (debugPanel != null) {
            debugPanel.updateClickLabel(boardLoc[0],boardLoc[1]);
        }

        Piece pieceClicked = board.getBoard()[boardLoc[0]][boardLoc[1]];
        if (debugPanel != null) {
            debugPanel.updateClickedPieceLabel(pieceClicked);
        }

        //if no piece was selected, we click on a piece and it's our piece, select it
        if (selectedPiece == null && pieceClicked != null && board.getIsWhitesTurn() == pieceClicked.isWhite()) {
            selectedPiece = pieceClicked;
            selectedLocation[0] = x;
            selectedLocation[1] = y;
            //ArrayList<Integer[]> possibleLocations = board.allValidMoves(pieceClicked, boardLoc);
            //System.err.println(possibleLocations.size());
            /*for (Integer[] possibleLocation: possibleLocations) {
                System.err.printf("%d, %d\n", possibleLocation[0], possibleLocation[1]);
            }*/
        }
        //if a piece was selected and we click on a different piece, select it OR move the piece there
        else if (selectedPiece != null && (pieceClicked == null || board.getIsWhitesTurn() == pieceClicked.isWhite()) &&
                (x != selectedLocation[0] || y != selectedLocation[1])) { //but they're in different locations
            selectedPiece = pieceClicked;
            selectedLocation[0] = x;
            selectedLocation[1] = y;
        }
        //otherwise if the piece we selected before was the piece clicked on, unselect it
        else if (selectedPiece == pieceClicked && x == selectedLocation[0] && y == selectedLocation[1]) {
            selectedPiece = null;
        }

        if (debugPanel != null) {
            debugPanel.updateSelectedPieceLabel(selectedPiece);
        }
    }

    /**
     * Not Used. Ignore.
     * @param mouseEvent
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    /**
     * Not Used. Ignore.
     * @param mouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    /**
     * Not Used. Ignore.
     * @param mouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    /**
     * Not Used. Ignore.
     * @param mouseEvent
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    /**
     * Not Used. Ignore.
     * @param mouseEvent
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {}

    /**
     * Handle the movement of the mouse
     * @param mouseEvent the mouseEvent containing the motion
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        if (debugPanel != null) {
            debugPanel.updateMotionLabel(x, y);
        }
    }
}