import javax.swing.*;
import java.awt.*;

/**
 * Main class for the I See Chess Project.
 * Run this class's main() to start the program.
 */
public class ISeeChess {

    private static final boolean useGraveyardPanel = true;
    private static final boolean useDebugPanel = false;

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        GraveyardPanel graveyardPanel = null;
        JFrame frame = new JFrame("I See Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel(new BorderLayout());
        Board board = new Board();
        ChessPanel chessPanel = new ChessPanel(board);
        DebugPanel debugPanel = null;
        if (useDebugPanel) {
            debugPanel = new DebugPanel();
        }
        if (useGraveyardPanel) {
            graveyardPanel = new GraveyardPanel(board);
            chessPanel.setGraveyardPanel(graveyardPanel);
        }
        chessPanel.setDebugPanel(debugPanel);
        mainPanel.add(chessPanel, BorderLayout.PAGE_START);
        if (useDebugPanel) {
            mainPanel.add(debugPanel, BorderLayout.CENTER);
        }
        frame.add(mainPanel, BorderLayout.LINE_START);
        if (useGraveyardPanel) {
            frame.add(graveyardPanel, BorderLayout.LINE_END);
        }
        frame.pack();
        frame.setVisible(true);
    }
}