import org.junit.Assert;
import org.junit.Test;

public class GameMapTests {

    @Test
    public void getLocationCenteredOnTestXFarEast() {
        GameMap testMap = new GameMap(10, 10);

        Location testLocation = testMap.getLocationCenteredOn(new Location(0,0), new Location(9,0));

        Assert.assertEquals(5, testLocation.x);
    }

    @Test
    public void getLocationCenteredOnTestXFarWest() {
        GameMap testMap = new GameMap(10, 10);

        Location testLocation = testMap.getLocationCenteredOn(new Location(9,0), new Location(0,0));

        Assert.assertEquals(3, testLocation.x);
    }

    @Test
    public void getLocationCenteredOnTestYFarNorth() {
        GameMap testMap = new GameMap(10, 10);

        Location testLocation = testMap.getLocationCenteredOn(new Location(0,9), new Location(0,0));

        Assert.assertEquals(3, testLocation.y);
    }

    @Test
    public void getLocationCenteredOnTestYFarSouth() {
        GameMap testMap = new GameMap(10, 10);

        Location testLocation = testMap.getLocationCenteredOn(new Location(0,0), new Location(0,9));

        Assert.assertEquals(5, testLocation.y);
    }

}
