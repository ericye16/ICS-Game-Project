import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The ChessPanel, used to display the chess board and all the pieces
 */
public class ChessPanel extends JPanel implements MouseInputListener {
    private Board board;
    private DebugPanel debugPanel;
    private Piece selectedPiece = null;
    private int[] selectedLocation = new int[2];
    private ArrayList<ColoredLocation> colouredLocations= new ArrayList<ColoredLocation>();
    private ColoredLocation mouseLocation = new ColoredLocation(0, 0, null);
    private ArrayList<Integer[]> nextLegalMoves = new ArrayList<Integer[]>();

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
        drawAllChessRectangles(g);
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
                (x) * differenceX(),
                (7-y) * differenceY(),
                (x) * differenceX() + differenceX(),
                ((7-y) * differenceY())+differenceY(),
                0,
                0,
                piece.image.getHeight(),
                piece.image.getWidth(),
                null
                );
    }

    /**
     * Draw a rectangle over a square in order to indicate it's moused over, selected or available.
     * @param g the Graphics object to draw on
     * @param color the colour to draw
     * @param x the x-location to draw at
     * @param y the y-location to draw at
     */
    private void drawChessRectangle(Graphics g, Color color, int x, int y) {
        Color oldColor = g.getColor();
        g.setColor(color);
        float thickness = 3;
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRect(x * differenceX(), y * differenceY(), differenceX(), differenceY());
        g2.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    /**
     * Draw all the chess selection and motion rectangles
     * @param g the Graphics object to draw on
     */
    private void drawAllChessRectangles(Graphics g) {
        for (ColoredLocation coloredLocation: colouredLocations) {
            drawChessRectangle(g, coloredLocation.color, coloredLocation.x, coloredLocation.y);
        }
        drawChessRectangle(g, mouseLocation.color, mouseLocation.x, mouseLocation.y);
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
     * Convert (x, y) from Board into panel
     * @param x the x from Board
     * @param y the y from Board
     * @return an integer array containing coordiantes as Panel expects
     */
    private int[] convertBoardToPanel(int x, int y) {
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

    private Piece pawnPromotionChoose(boolean colour) {
        Piece[] possiblePromotions;
        if (colour) {
            possiblePromotions = new Piece[] {
                    Piece.WhiteBishop, Piece.WhiteKnight, Piece.WhiteQueen, Piece.WhiteRook
            };
        } else {
            possiblePromotions = new Piece[] {
                    Piece.BlackBishop, Piece.BlackKnight, Piece.BlackQueen, Piece.BlackRook
            };
        }

        JOptionPane optionPane = new JOptionPane(possiblePromotions,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null,
                new String[] {"What to promote the pawn to?"}
        );
        return null;
    }

    static boolean isIn(ArrayList<Integer[]> list, Integer[] ints) {
        if (ints == null || list == null || list.size() == 0) return false;
        for (Integer[] checks: list) {
            if (checks[0].equals(ints[0]) && checks[1].equals(ints[1])) {
                return true;
            }
        }
        return false;
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
            System.err.println("New selection.");
            selectedPiece = pieceClicked;
            selectedLocation[0] = boardLoc[0];
            selectedLocation[1] = boardLoc[1];
        }
        else if (selectedPiece != null && isIn(nextLegalMoves, new Integer[] {boardLoc[0], boardLoc[1]}) &&
                !Arrays.equals(boardLoc, selectedLocation)
                ) {
            System.err.println("Selected location to move to.");
            try {
                board.movePiece(selectedLocation, boardLoc);
            } catch (Board.IsNotYourTurnException e) {
                e.printStackTrace();
                throw new NotImplementedException();
            } catch (Board.IllegalMoveException e) {
                e.printStackTrace();
                throw new NotImplementedException();
            } catch (Board.NeedToPromotePawnException e) {
                e.printStackTrace();
                throw new NotImplementedException();
            }
            selectedPiece = null;
            colouredLocations.clear();
            nextLegalMoves.clear();
            repaint();
        }
        //if a piece was selected and we click on a different piece, select it OR move the piece there
        else if (selectedPiece != null && (pieceClicked == null || board.getIsWhitesTurn() == pieceClicked.isWhite()) &&
                !Arrays.equals(boardLoc, selectedLocation)) { //but they're in different locations
            System.err.println("Selecting another piece.");
            selectedPiece = pieceClicked;
            selectedLocation[0] = boardLoc[0];
            selectedLocation[1] = boardLoc[1];
        }
        //otherwise if the piece we selected before was the piece clicked on, unselect it
        else if (selectedPiece == pieceClicked && Arrays.equals(boardLoc, selectedLocation)) {
            System.err.println("Unselecting piece by clicking on it.");
            selectedPiece = null;
            nextLegalMoves.clear();
        }

        if (debugPanel != null) {
            debugPanel.updateSelectedPieceLabel(selectedPiece);
        }

        if (selectedPiece != null) {
            ArrayList<Integer[]> validSelectedMoves = board.allValidMoves(selectedPiece, selectedLocation);
            for (Integer[] location: validSelectedMoves) {
                location[0] += selectedLocation[0];
                location[1] += selectedLocation[1];
                nextLegalMoves.add(location);
            }
            colouredLocations.clear();
            colouredLocations.add(new ColoredLocation(selectedLocation[0], 7 - selectedLocation[1], Color.BLUE));
            for (Integer[] possibleLoc: validSelectedMoves) {
                int[] panelLoc = convertBoardToPanel(possibleLoc[0], possibleLoc[1]);
                colouredLocations.add(new ColoredLocation(panelLoc[0], panelLoc[1], Color.GREEN));
            }
        } else {
            colouredLocations.clear();
            nextLegalMoves.clear();
        }

        repaint();
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
        x /= differenceX();
        y /= differenceY();
        int[] boardLoc = convertPanelToBoard(x, y);
        if (debugPanel != null) {
            debugPanel.updateMotionLabel(boardLoc[0], boardLoc[1]);
        }
        if (x != mouseLocation.x || y != mouseLocation.y || mouseLocation.color == null) {
            mouseLocation.x = x;
            mouseLocation.y = y;
            mouseLocation.color = Color.YELLOW;
            repaint();
        }
    }

    /**
     * A class to encapsulate a coloured rectangle.
     */
    private class ColoredLocation {
        public int x;
        public int y;
        public Color color;
        public ColoredLocation(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

}