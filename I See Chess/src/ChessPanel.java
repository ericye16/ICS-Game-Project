import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The ChessPanel, used to display the chess board and all the pieces. Also includes the necessary MouseInputListener.
 * Note that in this file, multiple coordinate systems are used.
 *
 * Panel coordinates begin at the top left of the screen
 * in (x, y) format whereas Board coordinates begin at the bottom left of the screen, also in (x, y) format.
 * The methods convertPanelToBoard() and convertBoardToPanel() should be used to convert coordinates
 * in one system to another.
 *
 * See the Board class documentation for more information.
 *
 * The ChessPanel is also responsible for launching dialog boxes for pawn promotion, checkmate and stalemate.
 */
public class ChessPanel extends JPanel implements MouseInputListener {
    private Board board;
    private DebugPanel debugPanel;
    private GraveyardPanel graveyardPanel;
    private Piece selectedPiece = null;
    private int[] selectedLocation = new int[2];
    private ArrayList<ColoredLocation> colouredLocations= new ArrayList<ColoredLocation>();
    private ColoredLocation mouseLocation = new ColoredLocation(0, 0, null);
    private ArrayList<Integer[]> nextLegalMoves = new ArrayList<Integer[]>();

    /**
     * Constructor for the ChessPanel. Creates a new ChessPanel object with the given Board.
     * @param board the Board object the ChessPanel will display.
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
     * Get the preferred dimensions (640x640) of the ChessPanel.
     * @return The preferred dimensions of the panel as a Dimension object.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640,640);
    }

    /**
     * Paint the board and the pieces.
     * @param g the Graphics object to draw on.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawAllChessRectangles(g);
        drawPieces(g);
    }

    /**
     * Draw the background, i.e. the checkered squares.
     * Chess pieces drawn on top of this work because the chess pieces should have transparent backgrounds.
     * @param g the Graphics object to draw on.
     */
    private void drawBackground(Graphics g) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0 ; c < 8; c++) {
                if ((r + c) % 2 == 0) { // evens are whitish
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(r * differenceX(), c * differenceY(), differenceX(), differenceY());
            }
        }
    }

    /**
     * Draw each piece onto a checkered square. Pieces should be transparent for this to work.
     * This method must be called after drawBackground in order to avoid the pieces being hidden.
     * @param g the Graphics object to draw on.
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
     * Draw a single piece on the board, given its Board x and y coordinates.
     * @param g the Graphics object to draw on.
     * @param piece the Piece object to draw at the given coordinates.
     * @param x the x-coordinate to draw at, by Board coordinates.
     * @param y the y-coordinate to draw at, by Board coordinates.
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
     * Draw all the chess selection and motion rectangles.
     * A yellow rectangle indicates the square is being hovered over.
     * A blue square indicates the piece currently selected.
     * A green square indicates a square for the piece to move into.
     * @param g the Graphics object to draw on.
     */
    private void drawAllChessRectangles(Graphics g) {
        for (ColoredLocation coloredLocation: colouredLocations) {
            drawChessRectangle(g, coloredLocation.color, coloredLocation.x, coloredLocation.y);
        }
        drawChessRectangle(g, mouseLocation.color, mouseLocation.x, mouseLocation.y);
    }

    /**
     * Find the width of one chess square in pixels.
     * @return the width, in pixels of one chess square.
     */
    private int differenceX() {
        return getWidth() / 8;
    }

    /**
     * Find the height of one chess square in pixels.
     * @return the height, in pixels, of one chess square.
     */
    private int differenceY() {
        return getHeight() / 8;
    }

    /**
     * Convert (x, y) from the panel into an (x, y) for the Board class.
     * @param x the x from the Panel.
     * @param y the y from the Panel.
     * @return an integer array containing coordinates as Board expects it.
     */
    private int[] convertPanelToBoard(int x, int y) {
        return new int[] {x, 7 - y};
    }

    /**
     * Convert (x, y) from Board into panel.
     * @param x the x from Board.
     * @param y the y from Board.
     * @return an integer array containing coordinates as Panel expects.
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
     * Set the DebugPanel, if required. This is optional.
     * @param debugPanel the DebugPanel, or null for none.
     */
    public void setDebugPanel(DebugPanel debugPanel) {
        this.debugPanel = debugPanel;
    }

    /**
     * Set the GraveyardPanel, if required. This is optional.
     * @param graveyardPanel the GraveyardPanel, or null for none.
     */
    public void setGraveyardPanel(GraveyardPanel graveyardPanel) {
        this.graveyardPanel = graveyardPanel;
    }

    /**
     * Dialog box for handling pawn promotion.
     * @param colour the colour (black or white) of the piece to promote the pawn into.
     * @return the Piece the pawn is to be promoted into.
     */
    private Piece pawnPromotionChoose(boolean colour) {
        Object[] possiblePromotions;
        if (colour) {
            possiblePromotions = new Piece[] {
                    Piece.WhiteBishop, Piece.WhiteKnight, Piece.WhiteQueen, Piece.WhiteRook
            };
        } else {
            possiblePromotions = new Piece[] {
                    Piece.BlackBishop, Piece.BlackKnight, Piece.BlackQueen, Piece.BlackRook
            };
        }

        Piece selectedPiece = (Piece) JOptionPane.showInputDialog(
                null,
                "What would you like to promote the pawn to?",
                "Pawn Promotion",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possiblePromotions,
                possiblePromotions[0]
        );
        return selectedPiece;
    }

    /**
     * Check if the values of an Integer array can be found in an ArrayList of Integer Arrays
     * @param list the list of Integer arrays
     * @param ints the Integer array to find
     * @return true if the array is found, false otherwise.
     */
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
            } catch (Board.NeedToPromotePawnException e) {
                try {
                    board.promotePawn(boardLoc, pawnPromotionChoose(!board.getIsWhitesTurn()));
                } catch (Board.ChessException e1) {
                    e1.printStackTrace();
                    throw new InternalError(); // should never happen
                }
            } catch (Board.CheckmateException e) {
                colouredLocations.clear();
                nextLegalMoves.clear();
                repaint();
                winnerDialog(e);
                e.printStackTrace();
            } catch (Board.StalemateException e) {
                colouredLocations.clear();
                nextLegalMoves.clear();
                repaint();
                stalemateDialog(e);
                e.printStackTrace();
            }
            catch (Board.ChessException e) { //the other possible exceptions
                e.printStackTrace();
                throw new InternalError(); //should never happen
            }
            if (graveyardPanel != null) {
                graveyardPanel.reCount();
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
     * Dialog to indicate a stalemate has occurred. Similar to winnerDialog in its options and behaviour.
     * @param e The StalemateException.
     */
    private void stalemateDialog(Board.StalemateException e) {
        String titleString = "Stalemate!";
        String dialogString = "A Stalemate has occurred! What would you like to do?";
        String[] options = {"Start a new game", "Quit game"};
        int choice = JOptionPane.showOptionDialog(
                null,
                dialogString,
                titleString,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (choice == JOptionPane.CLOSED_OPTION) {
            System.exit(-1);
        } else if (choice == 1) {
            System.exit(0);
        } else if (choice == 0) {
            resetBoard();
        } else {
            throw new InternalError(); //this should never happen
        }
    }

    /**
     * Dialog box for endgame. Asks the user whether they wish to restart a new game or to quit the game entirely.
     * Resets or quits the program as necessary.
     * Note that currently, closing this dialog box instead of selecting either option causes the program to quit
     * with a -1 exit code.
     * As well, System.exit() is used, since this class cannot "see" the JFrame.
     * @param e the CheckmateException thrown at the end of the game.
     */
    private void winnerDialog(Board.CheckmateException e) {
        boolean winner = !e.getColourOfMated();
        Object[] options = {"Quit",
                    "Start Again"};
        String titleString = (winner?"White" : "Black") + " won!";
        String dialogString  = "Checkmate by " + (winner?"White": "Black") + "! What would you like to do now?";
        int n = JOptionPane.showOptionDialog(
                null,
                dialogString,
                titleString,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (n == JOptionPane.CLOSED_OPTION) {
            System.exit(-1);
        } else if (n == 0) {
            System.exit(0);
        } else if (n == 1) {
            resetBoard();
        } else {
            throw new InternalError(); //should never happen
        }
    }

    /**
     * Reset the Board object to its initial conditions, and also reset the GraveyardPanel if it exists.
     * Used following a Checkmate or a Stalemate and the user wants to restart the game.
     */
    private void resetBoard() {
        this.board = new Board();
        if (graveyardPanel != null) {
            graveyardPanel.setBoard(board);
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