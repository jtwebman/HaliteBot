class Location {
    int x, y;

    Location(int x_, int y_) {
        x = x_;
        y = y_;
    }

    Location(Location l) {
        x = l.x;
        y = l.y;
    }
}
