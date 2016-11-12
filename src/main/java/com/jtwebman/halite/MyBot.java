package com.jtwebman.halite;

import java.util.*;

public class MyBot {
    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;
        GameMap gameMap = iPackage.map;

        Networking.sendInit("JTWebManV4");

        while(true) {
            ArrayList<Move> moves = new ArrayList<Move>();

            gameMap = Networking.getFrame();

            for(int y = 0; y < gameMap.height; y++) {
                for(int x = 0; x < gameMap.width; x++) {
                    Location currentLoc = new Location(x, y);
                    Cell cell = gameMap.getCell(currentLoc);
                    if(cell.owner == myID) {
                        boolean moved = false;

                        if (allDirectionsMine(gameMap, currentLoc, myID)) {
                            if (gameMap.getCell(currentLoc).strength >= 50) {
                                moved = true;
                                moves.add(new Move(currentLoc, directionFromInteger(new Random().nextInt(3))));
                            }
                        }

                        if (!moved) {
                            for (Direction d : Direction.CARDINALS) {
                                if (gameMap.getCell(currentLoc, d).owner != myID
                                        && gameMap.getCell(currentLoc, d).strength < gameMap.getCell(currentLoc).strength) {
                                    moved = true;
                                    moves.add(new Move(currentLoc, d));
                                }
                            }
                        }

                        if (!moved) {
                            moves.add(new Move(currentLoc, Direction.STILL));
                        }
                    }
                }
            }
            Networking.sendFrame(moves);
        }
    }

    private static boolean allDirectionsMine(GameMap gameMap, Location currentLoc, int myID) {
        for (Direction d : Direction.CARDINALS) {
            if (gameMap.getCell(currentLoc, d).owner != myID) return false;
        }
        return true;
    }

    private static Direction directionFromInteger(int value) {
        if(value == 1) {
            return Direction.NORTH;
        }
        if(value == 2) {
            return Direction.EAST;
        }
        if(value == 3) {
            return Direction.SOUTH;
        }
        if(value == 4) {
            return Direction.WEST;
        }
        return Direction.STILL;
    }
}
