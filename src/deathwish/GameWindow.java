package deathwish;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow() {
        super("Deathwish - Phase 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
    }
}
