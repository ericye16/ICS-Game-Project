import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ChessPanel extends JPanel implements MouseInputListener {
    private Board board;
    private DebugPanel debugPanel;
    private Piece selectedPiece = null;
    private int[] selectedLocation = new int[2];

    public ChessPanel(Board board) {
        assert (board != null);
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.board = board;
        addMouseListener(this);
        addMouseMotionListener(this);
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

    private int differenceX() {
        return getWidth() / 8;
    }

    private int differenceY() {
        return getHeight() / 8;
    }

    private int[] convertPanelToBoard(int x, int y) {
        return new int[] {x, 7 - y};
    }

    public Board getBoard() {
        return board;
    }

    public void setDebugPanel(DebugPanel debugPanel) {
        this.debugPanel = debugPanel;
    }

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

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {}

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        if (debugPanel != null) {
            debugPanel.updateMotionLabel(x, y);
        }
    }
}