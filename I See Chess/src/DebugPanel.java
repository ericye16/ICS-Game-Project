import javax.swing.*;
import java.awt.*;

/**
 * A Panel to help debug ChessPanel.
 * Shows where the mouse cursor is, as well as which pieces are selected and have last been clicked on.
 */
public class DebugPanel extends JPanel {

    private JLabel clickLabel;
    private JLabel motionLabel;
    private JLabel clickedPieceLabel;
    private JLabel selectedPieceLabel;

    /**
     * Constructor for the Debug Panel. Sets the labels to default values.
     */
    public DebugPanel() {
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        clickLabel = new JLabel("| Click x: y: |");
        motionLabel = new JLabel("| Motion x: y: |");
        clickedPieceLabel = new JLabel("| Clicked: |");
        selectedPieceLabel = new JLabel("| Selected: |");
        this.add(clickLabel);
        this.add(clickedPieceLabel);
        this.add(selectedPieceLabel);
        this.add(motionLabel);
    }

    /**
     * Update the click label.
     * @param x the x-location of the click.
     * @param y the y-location of the click.
     */
    public void updateClickLabel(int x, int y) {
        clickLabel.setText("| Click x: " + x + " y: " + y + " |");
    }

    /**
     * Update the motion label.
     * @param x the x-location of the motion.
     * @param y the y-location of the motion.
     */
    public void updateMotionLabel(int x, int y) {
        motionLabel.setText("| Motion x: " + x + " y: " + y + " |");
    }

    /**
     * Update the type of piece Clicked on last.
     * @param piece the type of piece.
     */
    public void updateClickedPieceLabel(Piece piece) {
        clickedPieceLabel.setText("| Clicked: " + piece + " |");
    }

    /**
     * Update the type of piece Selected.
     * @param piece the type of piece.
     */
    public void updateSelectedPieceLabel(Piece piece) {
        selectedPieceLabel.setText("| Selected: " + piece + " |");
    }
}