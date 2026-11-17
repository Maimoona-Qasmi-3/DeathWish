package deathwish;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class World {
    private static final int TILE_SIZE = 32;
    private static final int WORLD_WIDTH = 40;
    private static final int WORLD_HEIGHT = 16;
    private static final int GOAL_X = WORLD_WIDTH - 3;
    private static final int GOAL_Y = 2;
    private static final int CHECKPOINT_X = 13;

    private final TileType[][] tiles = new TileType[WORLD_HEIGHT][WORLD_WIDTH];
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();
    private final Random random = new Random(42);
    private int checkpointX = 64;
    private int checkpointY = 280;
    private boolean levelComplete;

    public World() {
        generateLevel();
    }

    public int getWidth() {
        return WORLD_WIDTH * TILE_SIZE;
    }

    public int getHeight() {
        return WORLD_HEIGHT * TILE_SIZE;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void update(Player player) {
        if (levelComplete) {
            return;
        }

        if (player.getX() >= CHECKPOINT_X * TILE_SIZE) {
            checkpointX = CHECKPOINT_X * TILE_SIZE;
            checkpointY = 8 * TILE_SIZE;
            player.resetSpawn(checkpointX, checkpointY);
        }

        for (Enemy enemy : enemies) {
            enemy.update(player, this);
        }

        for (Iterator<Projectile> iterator = projectiles.iterator(); iterator.hasNext();) {
            Projectile projectile = iterator.next();
            projectile.update();

            if (projectile.intersects(player)) {
                player.takeHit();
                iterator.remove();
                continue;
            }

            if (projectile.getX() < -20 || projectile.getX() > getWidth() + 20 || projectile.getY() < -20 || projectile.getY() > getHeight() + 20) {
                iterator.remove();
            }
        }

        for (Enemy enemy : enemies) {
            if (enemy.intersects(player)) {
                player.takeHit();
            }
        }

        if (player.getX() > GOAL_X * TILE_SIZE && player.getY() < (GOAL_Y + 2) * TILE_SIZE + 20) {
            levelComplete = true;
        }

        if (player.getY() > getHeight() + 100) {
            player.resetSpawn(checkpointX, checkpointY);
        }
    }

    public void draw(Graphics2D graphics) {
        graphics.setColor(new Color(18, 22, 30));
        graphics.fillRect(0, 0, getWidth(), getHeight());

        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[row].length; column++) {
                if (tiles[row][column] == TileType.SOLID) {
                    graphics.setColor(new Color(76, 91, 83));
                    graphics.fillRect(column * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        graphics.setColor(new Color(76, 175, 80));
        graphics.fillRect(checkpointX, checkpointY, 10, 40);

        graphics.setColor(new Color(214, 120, 86));
        graphics.fillRect(GOAL_X * TILE_SIZE, GOAL_Y * TILE_SIZE, TILE_SIZE, TILE_SIZE * 2);

        if (levelComplete) {
            graphics.setColor(new Color(255, 255, 255, 200));
            graphics.drawString("LEVEL CLEARED", GOAL_X * TILE_SIZE - 50, GOAL_Y * TILE_SIZE - 18);
        }
    }

    public void spawnProjectile(int x, int y, int velocityX, int velocityY, int speed) {
        projectiles.add(new Projectile(x, y, velocityX, velocityY, speed));
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public boolean collides(int x, int y, int width, int height) {
        int firstColumn = Math.floorDiv(x, TILE_SIZE);
        int lastColumn = Math.floorDiv(x + width - 1, TILE_SIZE);
        int firstRow = Math.floorDiv(y, TILE_SIZE);
        int lastRow = Math.floorDiv(y + height - 1, TILE_SIZE);

        for (int row = firstRow; row <= lastRow; row++) {
            for (int column = firstColumn; column <= lastColumn; column++) {
                if (getTile(column, row) == TileType.SOLID) {
                    return true;
                }
            }
        }

        return false;
    }

    private void generateLevel() {
        for (int row = 0; row < WORLD_HEIGHT; row++) {
            for (int column = 0; column < WORLD_WIDTH; column++) {
                tiles[row][column] = TileType.EMPTY;
            }
        }

        for (int column = 0; column < WORLD_WIDTH; column++) {
            tiles[WORLD_HEIGHT - 1][column] = TileType.SOLID;
        }

        int[][] platformLayout = {
            {0, 11, 4},
            {6, 10, 4},
            {12, 9, 4},
            {18, 10, 4},
            {24, 9, 3},
            {30, 8, 4},
            {35, 10, 4}
        };

        for (int[] platform : platformLayout) {
            int x = platform[0];
            int y = platform[1];
            int width = platform[2];
            placePlatform(x, y, width);

            int enemyX = x * TILE_SIZE + (width / 2) * TILE_SIZE;
            int enemyY = y * TILE_SIZE - 24;
            enemies.add(new Enemy(enemyX, enemyY, x * TILE_SIZE, (x + width) * TILE_SIZE));
        }
    }

    private void placePlatform(int column, int row, int width) {
        for (int x = column; x < column + width && x < WORLD_WIDTH; x++) {
            if (row >= 0 && row < WORLD_HEIGHT) {
                tiles[row][x] = TileType.SOLID;
            }
        }
    }

    private TileType getTile(int column, int row) {
        if (row < 0 || row >= tiles.length || column < 0 || column >= tiles[row].length) {
            return TileType.SOLID;
        }
        return tiles[row][column];
    }
}

