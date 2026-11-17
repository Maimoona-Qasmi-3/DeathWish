package deathwish;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Projectile {
    private int x;
    private int y;
    private final int width = 8;
    private final int height = 8;
    private final int velocityX;
    private final int velocityY;

    public Projectile(int x, int y, int velocityX, int velocityY, int speed) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX * speed;
        this.velocityY = velocityY * speed;
    }

    public void update() {
        x += velocityX;
        y += velocityY;
    }

    public void draw(Graphics2D graphics) {
        graphics.setColor(new Color(255, 194, 72));
        graphics.fillOval(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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
