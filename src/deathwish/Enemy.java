package deathwish;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Enemy {
    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    private int x;
    private int y;
    private final int minX;
    private final int maxX;
    private int direction = 1;
    private final int speed = 2;
    private int attackCooldown = 0;

    public Enemy(int x, int y, int minX, int maxX) {
        this.x = x;
        this.y = y;
        this.minX = minX;
        this.maxX = maxX;
    }

    public void update(Player player, World world) {
        if (attackCooldown > 0) {
            attackCooldown--;
        }

        x += direction * speed;
        if (x <= minX || x + WIDTH >= maxX) {
            direction *= -1;
            x = Math.max(minX, Math.min(x, maxX - WIDTH));
        }

        int playerCenterX = player.getX() + player.getWidth() / 2;
        int enemyCenterX = x + WIDTH / 2;
        int playerCenterY = player.getY() + player.getHeight() / 2;
        int enemyCenterY = y + HEIGHT / 2;

        if (attackCooldown == 0 && Math.abs(playerCenterX - enemyCenterX) < 260 && Math.abs(playerCenterY - enemyCenterY) < 90) {
            int dx = playerCenterX - enemyCenterX;
            int dy = playerCenterY - enemyCenterY;
            int magnitude = (int) Math.max(1, Math.hypot(dx, dy));
            world.spawnProjectile(
                enemyCenterX,
                enemyCenterY,
                (dx * 4) / magnitude,
                (dy * 4) / magnitude,
                4
            );
            attackCooldown = 90;
        }
    }

    public void draw(Graphics2D graphics) {
        graphics.setColor(new Color(181, 42, 42));
        graphics.fillRect(x, y, WIDTH, HEIGHT);
        graphics.setColor(new Color(255, 214, 130));
        graphics.fillRect(x + 6, y + 6, 5, 5);
        graphics.fillRect(x + 13, y + 6, 5, 5);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean intersects(Player player) {
        return getBounds().intersects(player.getBounds());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
