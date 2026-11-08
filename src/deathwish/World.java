package deathwish;

public class World {
    private static final int TILE_SIZE = 32;

    private final TileType[][] tiles = {
        row("...................."),
        row("...................."),
        row("...................."),
        row("...................."),
        row("...................."),
        row("...................."),
        row("...................."),
        row("...................."),
        row("..............###..."),
        row("...................."),
        row(".....###............"),
        row("...................."),
        row("..####.............."),
        row("####################")
    };

    public int getWidth() {
        return tiles[0].length * TILE_SIZE;
    }

    public int getHeight() {
        return tiles.length * TILE_SIZE;
    }

    public void draw(java.awt.Graphics2D graphics) {
        graphics.setColor(new java.awt.Color(76, 91, 83));

        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[row].length; column++) {
                if (tiles[row][column] == TileType.SOLID) {
                    graphics.fillRect(
                        column * TILE_SIZE,
                        row * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                    );
                }
            }
        }
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

    private TileType getTile(int column, int row) {
        if (row < 0 || row >= tiles.length || column < 0 || column >= tiles[row].length) {
            return TileType.SOLID;
        }
        return tiles[row][column];
    }

    private static TileType[] row(String layout) {
        TileType[] result = new TileType[layout.length()];
        for (int column = 0; column < layout.length(); column++) {
            result[column] = layout.charAt(column) == '#' ? TileType.SOLID : TileType.EMPTY;
        }
        return result;
    }
}
