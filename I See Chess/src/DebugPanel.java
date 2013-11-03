import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 02/11/13
 * Time: 6:03 PM
 */
public class DebugPanel extends JPanel {

    private JLabel clickLabel;
    private JLabel motionLabel;

    public DebugPanel() {
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        clickLabel = new JLabel("| Click x: y: |");
        motionLabel = new JLabel("| Motion x: y: |");
        this.add(clickLabel);
        this.add(motionLabel);
    }

    public void updateClickLabel(int x, int y) {
        clickLabel.setText("| Click x: " + x + " y: " + y + " |");
    }

    public void updateMotionLabel(int x, int y) {
        motionLabel.setText("| Motion x: " + x + " y: " + y + " |");
    }
}
