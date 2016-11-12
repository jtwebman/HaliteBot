import java.util.*;

public class MyBot {
    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;
        GameMap gameMap;

        Networking.sendInit("JTWebManV6");

        while(true) {
            ArrayList<Move> moves = new ArrayList<>();

            gameMap = Networking.getFrame(myID);

            for(int y = 0; y < gameMap.height; y++) {
                for(int x = 0; x < gameMap.width; x++) {
                    Location currentLoc = new Location(x, y);
                    Cell cell = gameMap.getCell(currentLoc);
                    if(cell.owner == myID) {
                        if (!cell.moving && allDirectionsMine(gameMap, currentLoc)) {
                            if (cell.strength >= cell.production * 10) {
                                Direction moveDirection = closestNotMineDirection(gameMap, currentLoc);
                                Cell moveCell = gameMap.getCell(currentLoc, moveDirection);
                                if (!moveCell.someoneMovingHere) {
                                    cell.moving = true;
                                    moves.add(new Move(currentLoc, moveDirection));
                                    gameMap.getCell(currentLoc, moveDirection).someoneMovingHere = true;
                                }
                            }
                        } else {
                            if (!cell.moving) {
                                Direction bestDirection = Direction.STILL;
                                int bestWeight = 0;
                                for (Direction d : Direction.CARDINALS) {
                                    Cell checkCell = gameMap.getCell(currentLoc, d);
                                    if (!checkCell.mine && checkCell.strength < cell.strength) {
                                        int checkWeight = getAttackWeight(gameMap, checkCell);
                                        if (checkWeight >= bestWeight) {
                                            bestWeight = checkWeight;
                                            bestDirection = d;
                                        }
                                    }
                                }
                                if (bestDirection != Direction.STILL) {
                                    Cell moveCell = gameMap.getCell(currentLoc, bestDirection);
                                    if (!moveCell.someoneMovingHere) {
                                        cell.moving = true;
                                        moves.add(new Move(currentLoc, bestDirection));
                                        moveCell.someoneMovingHere = true;
                                    }
                                } else {
                                    if (!cell.someoneMovingHere) {
                                        Move pullMove = pullMoveToTakeNextTime(gameMap, currentLoc);
                                        if (pullMove != null) {
                                            Cell pullCell = gameMap.getCell(new Location(pullMove.loc.x, pullMove.loc.y));
                                            pullCell.moving = true;
                                            moves.add(pullMove);
                                            cell.someoneMovingHere = true;
                                        }
                                    }
                                }
                            }
                        }

                        if (!cell.moving ) {
                            moves.add(new Move(currentLoc, Direction.STILL));
                        }
                    }
                    cell.processed = true;
                }
            }
            Networking.sendFrame(moves);
        }
    }

    private static Move pullMoveToTakeNextTime(GameMap gameMap, Location currentLoc) {
        Cell currentCell = gameMap.getCell(currentLoc);
        int lowestStrengthNeeded = findLowestStrengthNeeded(gameMap, currentLoc);
        for (Direction d : Direction.CARDINALS) {
            Cell checkCell = gameMap.getCell(currentLoc, d);
            if (!checkCell.moving && checkCell.mine && checkCell.processed &&
                    checkCell.strength + currentCell.strength >= lowestStrengthNeeded) {
                return new Move(new Location(checkCell.x, checkCell.y), switchDirection(d));
            }
        }
        return null;
    }

    private static int findLowestStrengthNeeded(GameMap gameMap, Location currentLoc) {
        int lowestNeeded = 999999;
        for (Direction d : Direction.CARDINALS) {
            Cell checkCell = gameMap.getCell(currentLoc, d);
            if (!checkCell.mine && !checkCell.someoneMovingHere && checkCell.strength < lowestNeeded) {
                lowestNeeded = checkCell.strength;
            }
        }
        return lowestNeeded;
    }

    private static Direction switchDirection(Direction d) {
        switch (d) {
            case NORTH: return Direction.SOUTH;
            case SOUTH: return Direction.NORTH;
            case EAST: return Direction.WEST;
            case WEST: return Direction.EAST;
            default: return Direction.STILL;
        }
    }

    private static int getAttackWeight(GameMap gameMap, Cell attackCell) {
        int weight = 40;
        Location attachLoc = new Location(attackCell.x, attackCell.y);
        if (gameMap.getCell(attachLoc, Direction.NORTH).mine ||
                gameMap.getCell(attachLoc, Direction.NORTH).someoneMovingHere) weight -= 10;
        if (gameMap.getCell(attachLoc, Direction.SOUTH).mine ||
                gameMap.getCell(attachLoc, Direction.SOUTH).someoneMovingHere) weight -= 10;
        if (gameMap.getCell(attachLoc, Direction.EAST).mine ||
                gameMap.getCell(attachLoc, Direction.EAST).someoneMovingHere) weight -= 10;
        if (gameMap.getCell(attachLoc, Direction.WEST).mine ||
                gameMap.getCell(attachLoc, Direction.WEST).someoneMovingHere) weight -= 10;
        return weight;
    }

    private static Direction closestNotMineDirection(GameMap gameMap, Location currentLoc) {
        double smallestDistSoFar = Double.MAX_VALUE;
        Location smallestLoc = null;
        for(int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location searchLoc = new Location(x, y);
                Cell searchCell = gameMap.getCell(searchLoc);
                if (!searchCell.mine) {
                    double distFromCurrent = gameMap.getDistance(searchLoc, currentLoc);
                    if (smallestDistSoFar > distFromCurrent) {
                        smallestDistSoFar = distFromCurrent;
                        smallestLoc = searchLoc;
                    }
                }
            }
        }

        if (smallestLoc == null) return Direction.STILL;
        /*double angle = gameMap.getAngle(currentLoc, smallestLoc);
        if (angle >= 315 || angle <= 45) return Direction.NORTH;
        if (angle >= 45 && angle <= 135) return Direction.EAST;
        if (angle >= 135 && angle <= 225) return Direction.SOUTH;
        return Direction.WEST;*/

        int directionX = smallestLoc.x - currentLoc.x;
        int directionY = smallestLoc.y - currentLoc.y;

        if (Math.abs(directionX) > Math.abs(directionY)) {
            if (directionX < 0) return Direction.WEST;
            if (directionX > 0) return Direction.EAST;
        } else {
            if (directionY < 0) return Direction.NORTH;
            if (directionY > 0) return Direction.SOUTH;
        }

        return Direction.STILL;
    }

    private static boolean allDirectionsMine(GameMap gameMap, Location currentLoc) {
        for (Direction d : Direction.CARDINALS) {
            if (!gameMap.getCell(currentLoc, d).mine) return false;
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
