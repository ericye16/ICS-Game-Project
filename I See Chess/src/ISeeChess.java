import javax.swing.*;
import java.awt.*;

public class ISeeChess {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("I See Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel(new BorderLayout());
        ChessPanel chessPanel = new ChessPanel(new Board());
        DebugPanel debugPanel = new DebugPanel();
        chessPanel.setDebugPanel(debugPanel);
        mainPanel.add(chessPanel, BorderLayout.PAGE_START);
        mainPanel.add(debugPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}