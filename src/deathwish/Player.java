package deathwish;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player {
    public static final int WIDTH = 22;
    public static final int HEIGHT = 28;
    private static final int MOVE_SPEED = 5;
    private static final int AIR_DRIFT = 2;
    private static final int JUMP_SPEED = 12;
    private static final int SECOND_JUMP_SPEED = 15;
    private static final int GRAVITY = 1;
    private static final int MAX_FALL_SPEED = 9;
    private static final int MAX_SLEEP = 100;

    private int x;
    private int y;
    private int verticalSpeed;
    private boolean onGround;
    private int coyoteFrames;
    private int jumpBufferFrames;
    private int jumpsUsed;
    private int sleepMeter;
    private int invulnerabilityTimer;
    private final int spawnX;
    private final int spawnY;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.spawnX = x;
        this.spawnY = y;
    }

    public void update(World world, boolean movingLeft, boolean movingRight, boolean jumpPressed) {
        if (invulnerabilityTimer > 0) {
            invulnerabilityTimer--;
        }

        if (jumpPressed) {
            jumpBufferFrames = 8;
        } else if (jumpBufferFrames > 0) {
            jumpBufferFrames--;
        }

        if (onGround) {
            coyoteFrames = 8;
            jumpsUsed = 0;
        } else if (coyoteFrames > 0) {
            coyoteFrames--;
        }

        int horizontalSpeed = 0;
        if (movingLeft) {
            horizontalSpeed -= onGround ? MOVE_SPEED : AIR_DRIFT;
        }
        if (movingRight) {
            horizontalSpeed += onGround ? MOVE_SPEED : AIR_DRIFT;
        }
        moveHorizontally(world, horizontalSpeed);

        if (jumpBufferFrames > 0) {
            if (coyoteFrames > 0 || onGround) {
                verticalSpeed = -JUMP_SPEED;
                onGround = false;
                coyoteFrames = 0;
                jumpsUsed = 1;
                jumpBufferFrames = 0;
            } else if (jumpsUsed < 2) {
                verticalSpeed = -SECOND_JUMP_SPEED;
                jumpsUsed = 2;
                jumpBufferFrames = 0;
            }
        }

        verticalSpeed = Math.min(verticalSpeed + GRAVITY, MAX_FALL_SPEED);
        moveVertically(world, verticalSpeed);
    }

    public void draw(Graphics2D graphics) {
        graphics.setColor(invulnerabilityTimer > 0 ? new Color(155, 203, 255) : new Color(220, 220, 220));
        graphics.fillRect(x, y, WIDTH, HEIGHT);

        graphics.setColor(new Color(120, 120, 120));
        graphics.fillRect(x - 2, y - 10, WIDTH + 4, 5);
        graphics.setColor(new Color(76, 175, 80));
        graphics.fillRect(x - 2, y - 10, (int) ((WIDTH + 4) * (sleepMeter / (double) MAX_SLEEP)), 5);
    }

    public void takeHit() {
        if (invulnerabilityTimer > 0) {
            return;
        }

        sleepMeter += 25;
        invulnerabilityTimer = 60;

        if (sleepMeter >= MAX_SLEEP) {
            resetSpawn(spawnX, spawnY);
            sleepMeter = 0;
            return;
        }

        x = Math.max(32, x - 16);
        y = Math.max(40, y - 20);
    }

    public void resetSpawn(int x, int y) {
        this.x = x;
        this.y = y;
        this.verticalSpeed = 0;
        this.onGround = false;
        this.coyoteFrames = 0;
        this.jumpBufferFrames = 0;
        this.jumpsUsed = 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
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
                    coyoteFrames = 8;
                }
                verticalSpeed = 0;
                return;
            }
            y += direction;
        }
    }
}
