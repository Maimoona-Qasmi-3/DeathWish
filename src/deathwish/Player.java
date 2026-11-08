package deathwish;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player {
    private static final int WIDTH = 22;
    private static final int HEIGHT = 28;
    private static final int MOVE_SPEED = 4;
    private static final int JUMP_SPEED = 10;
    private static final int GRAVITY = 1;
    private static final int MAX_FALL_SPEED = 8;

    private int x;
    private int y;
    private int verticalSpeed;
    private boolean onGround;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(World world, boolean movingLeft, boolean movingRight, boolean jumpPressed) {
        int horizontalSpeed = 0;
        if (movingLeft) {
            horizontalSpeed -= MOVE_SPEED;
        }
        if (movingRight) {
            horizontalSpeed += MOVE_SPEED;
        }
        moveHorizontally(world, horizontalSpeed);

        if (jumpPressed && onGround) {
            verticalSpeed = -JUMP_SPEED;
            onGround = false;
        }

        verticalSpeed = Math.min(verticalSpeed + GRAVITY, MAX_FALL_SPEED);
        moveVertically(world, verticalSpeed);
    }

    public void draw(Graphics2D graphics) {
        graphics.setColor(new Color(220, 220, 220));
        graphics.fillRect(x, y, WIDTH, HEIGHT);
    }

    private void moveHorizontally(World world, int distance) {
        int direction = Integer.signum(distance);
        for (int step = 0; step < Math.abs(distance); step++) {
            if (world.collides(x + direction, y, WIDTH, HEIGHT)) {
                return;
            }
            x += direction;
        }
    }

    private void moveVertically(World world, int distance) {
        int direction = Integer.signum(distance);
        onGround = false;

        for (int step = 0; step < Math.abs(distance); step++) {
            if (world.collides(x, y + direction, WIDTH, HEIGHT)) {
                if (direction > 0) {
                    onGround = true;
                }
                verticalSpeed = 0;
                return;
            }
            y += direction;
        }
    }
}
