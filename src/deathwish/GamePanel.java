package deathwish;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    private final World world = new World();
    private final Player player = new Player(64, 300);
    private boolean movingLeft;
    private boolean movingRight;
    private boolean jumpPressed;

    public GamePanel() {
        setPreferredSize(new Dimension(world.getWidth(), world.getHeight()));
        setBackground(new Color(24, 27, 31));
        setupControls();

        Timer gameTimer = new Timer(16, event -> updateGame());
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        world.draw(graphics2D);
        player.draw(graphics2D);
        graphics2D.dispose();
    }

    private void updateGame() {
        player.update(world, movingLeft, movingRight, jumpPressed);
        jumpPressed = false;
        repaint();
    }

    private void setupControls() {
        bindKey("pressed LEFT", "left-pressed", () -> movingLeft = true);
        bindKey("released LEFT", "left-released", () -> movingLeft = false);
        bindKey("pressed RIGHT", "right-pressed", () -> movingRight = true);
        bindKey("released RIGHT", "right-released", () -> movingRight = false);
        bindKey("pressed A", "a-pressed", () -> movingLeft = true);
        bindKey("released A", "a-released", () -> movingLeft = false);
        bindKey("pressed D", "d-pressed", () -> movingRight = true);
        bindKey("released D", "d-released", () -> movingRight = false);
        bindKey("pressed SPACE", "jump", () -> jumpPressed = true);
        bindKey("pressed UP", "up-jump", () -> jumpPressed = true);
        bindKey("pressed W", "w-jump", () -> jumpPressed = true);
    }

    private void bindKey(String keyStroke, String actionName, Runnable action) {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(keyStroke), actionName);
        actionMap.put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                action.run();
            }
        });
    }
}
