package com.jtwebman.halite;

import java.util.ArrayList;
import java.util.List;

public class GameMap{
    public List<List<Cell>>contents;
    public int width, height;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;

        contents = new ArrayList<>();
        for(int y = 0; y < height; y++) {
            ArrayList<Cell> row = new ArrayList<>();
            for(int x = 0; x < width; x++) {
                row.add(new Cell(x, y));
            }
            contents.add(row);
        }
    }

    public boolean inBounds(Location loc) {
        return loc.x < width && loc.x >= 0 && loc.y < height && loc.y >= 0;
    }

    public double getDistance(Location loc1, Location loc2) {
        int dx = Math.abs(loc1.x - loc2.x);
        int dy = Math.abs(loc1.y - loc2.y);

        if(dx > width / 2.0) dx = width - dx;
        if(dy > height / 2.0) dy = height - dy;

        return dx + dy;
    }

    public double getAngle(Location loc1, Location loc2) {
        int dx = loc1.x - loc2.x;

        // Flip order because 0,0 is top left
        // and want atan2 to look as it would on the unit circle
        int dy = loc2.y - loc1.y;

        if(dx > width - dx) dx -= width;
        if(-dx > width + dx) dx += width;

        if(dy > height - dy) dy -= height;
        if(-dy > height + dy) dy += height;

        return Math.atan2(dy, dx);
    }

    public Location getLocation(Location loc, Direction dir) {
        Location l = new Location(loc);
        if(dir != Direction.STILL) {
            if(dir == Direction.NORTH) {
                if(l.y == 0) l.y = height - 1;
                else l.y--;
            }
            else if(dir == Direction.EAST) {
                if(l.x == width - 1) l.x = 0;
                else l.x++;
            }
            else if(dir == Direction.SOUTH) {
                if(l.y == height - 1) l.y = 0;
                else l.y++;
            }
            else if(dir == Direction.WEST) {
                if(l.x == 0) l.x = width - 1;
                else l.x--;
            }
        }
        return l;
    }

    public Cell getCell(Location loc, Direction dir) {
        Location l = getLocation(loc, dir);
        return contents.get(l.y).get(l.x);
    }

    public Cell getCell(Location loc) {
        return contents.get(loc.y).get(loc.x);
    }
}
