package com.jtwebman.halite;

public class Cell {
    public int owner, strength, production, x, y;
    public boolean mine = false;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
