class Cell {
    int owner, strength, production, x, y;
    boolean mine = false, someoneMovingHere = false, moving = false, processed = false;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
