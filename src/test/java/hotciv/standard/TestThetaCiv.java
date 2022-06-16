package hotciv.standard;

import hotciv.framework.*;
import hotciv.strategies.attackStrategies.*;
import hotciv.strategies.layoutStrategies.*;
import hotciv.strategies.unitActionStrategies.*;
import hotciv.strategies.unitStatStrategies.Original3AndUFOStatStrategy;
import hotciv.strategies.unitStatStrategies.Original3UnitStatStrategy;
import hotciv.strategies.winningStrategies.*;
import hotciv.strategies.worldAgeStrategies.*;
import org.hamcrest.Factory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class TestThetaCiv {
    private Game game;

    @Before
    public void setUp() {
        FactoryImpl thetaFactory = new FactoryImpl(new ThetaActionStrategy(),new Red3000BCWinningStrategy(), new Add100WorldAgeStrategy(), new AlphaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3AndUFOStatStrategy());
        game = new GameImpl(thetaFactory);
    }

    @Test
    public void settlerIsReplacedByCityWithSize1(){
        assertNotNull(game.getUnitAt(new Position(4, 3)));
        assertThat(game.getUnitAt(new Position(4, 3)).getTypeString(), is(GameConstants.SETTLER));
        game.performUnitActionAt(new Position(4, 3));
        assertNotNull(game.getCityAt(new Position(4, 3)));
        assertThat(game.getCityAt(new Position(4, 3)).getSize(), is(1));
    }

    @Test
    public void archerMovementRemovesFortification(){
        ((UnitImpl)game.getUnitAt(new Position(2, 0))).setDefensiveStrength(1);
        int initialStrength = game.getUnitAt(new Position(2, 0)).getDefensiveStrength();
        game.performUnitActionAt(new Position(2, 0));
        assertThat(game.getUnitAt(new Position(2, 0)).getDefensiveStrength(), is(initialStrength * 2));
        game.moveUnit(new Position(2, 0), new Position(2, 1));
        game.endOfTurn(); //Allows unit to move again
        game.endOfTurn(); //Allows unit to move again
        game.moveUnit(new Position(2, 1), new Position(2, 0));
        assertThat(game.getUnitAt(new Position(2, 0)).getDefensiveStrength(), is(initialStrength));
    }

    @Test
    public void cityCanProduceUFO_For60Production(){
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction("ufo");
        assertThat(game.getCityAt(new Position(4, 1)).getProduction(), is("ufo"));

        assertThat(game.getCityAt(new Position(4, 1)).getTreasury(), is(0));
        assertThat(game.getUnitAt(new Position(4, 1)), is(nullValue()));

        for (int i=1 ; i<10 ; i++) {
            game.endOfTurn();
            assertThat(game.getCityAt(new Position(4, 1)).getTreasury(), is(6 * i));
            assertThat(game.getUnitAt(new Position(4, 1)), is(nullValue()));
        }

        assertThat(game.getCityAt(new Position(4, 1)).getTreasury(), is(54));
        game.endOfTurn();
        assertThat(game.getCityAt(new Position(4, 1)).getTreasury(), is(0));
        assertThat(game.getUnitAt(new Position(4, 1)).getTypeString(), is("ufo"));
    }

    @Test
    public void UFO_CanFlyOverUnoccupiedCityWithoutConquering(){
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction("ufo");
        for (int i=1 ; i<11 ; i++) {
            game.endOfTurn();
        }
        //The For-Loop created a UFO at 4,1

        //Verify location of Blue UFO
        assertThat(game.getUnitAt(new Position(4, 1)).getOwner(), is(Player.BLUE));
        assertThat(game.getUnitAt(new Position(4, 1)).getTypeString(), is("ufo"));

        //Move UFO unit to the unoccupied Red city
        game.moveUnit(new Position(4, 1), new Position(3, 1));
        game.moveUnit(new Position(3, 1), new Position(2, 1));
        game.endOfTurn();
        assertThat(game.getUnitAt(new Position(1, 1)), is(nullValue()));
        game.moveUnit(new Position(2, 1), new Position(1, 1));

        //Verify city has not changed ownership
        assertThat(game.getCityAt(new Position(1, 1)).getOwner(), is(Player.RED));
    }

    @Test
    public void UFO_CanBattleAndConquerOccupiedCity(){
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction("ufo");
        for (int i=1 ; i<12 ; i++) {
            game.endOfTurn();
        }
        //The For-Loop created a UFO at 4,1

        //Verify location of Blue UFO
        assertThat(game.getUnitAt(new Position(4, 1)).getOwner(), is(Player.BLUE));
        assertThat(game.getUnitAt(new Position(4, 1)).getTypeString(), is("ufo"));

        //Move UFO unit next to the unoccupied Red city
        game.moveUnit(new Position(4, 1), new Position(3, 1));
        game.moveUnit(new Position(3, 1), new Position(2, 1));

        //Create an Archer in the Red city
        ((CityImpl) game.getCityAt(new Position(1, 1))).setProduction(GameConstants.ARCHER);
        game.endOfTurn();
        game.endOfTurn();
        assertThat(game.getUnitAt(new Position(1, 1)), is(notNullValue()));

        //Attack Red city with UFO
        game.moveUnit(new Position(2, 1), new Position(1, 1));

        //Verify city has changed ownership
        assertThat(game.getCityAt(new Position(1, 1)).getOwner(), is(Player.BLUE));
    }


    //TEST LAYOUT (NEED A CITY ON FOREST)
    public class TestingLayout implements LayoutStrategy {
        @Override
        public String[] generateLayout() {
            String layout[] = {
                    //o - OCEAN UNOCCUPIED       i - OCEAN W/CITY FROM RED PLAYER        I - OCEAN W/CITY FROM BLUE PLAYER
                    //f - FORESTS UNOCCUPIED     d - FORESTS W/CITY FROM RED PLAYER      D - FORESTS W/CITY FROM BLUE PLAYER
                    //h - HILLS UNOCCUPIED       g - HILLS W/CITY FROM RED PLAYER        G - HILLS W/CITY FROM BLUE PLAYER
                    //m - MOUNTAINS UNOCCUPIED   n - MOUNTAINS W/CITY FROM RED PLAYER    N - MOUNTAINS W/CITY FROM BLUE PLAYER
                    //. - PLAINS UNOCCUPIED      , - PLAINS W/CITY FROM RED PLAYER       < - PLAINS W/CITY FROM BLUE PLAYER
                    ".h..............",
                    ".d..............",
                    "..m.............",
                    "................",
                    ".<..............",
                    "................",
                    "................",
                    "................",
                    "................",
                    "................",
                    "................",
                    "................",
                    "................",
                    "................",
                    "................",
                    "................",
            };
            return layout;
        }
    }


    @Test
    public void UFO_CanAbductFromAndEliminateCity(){
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction("ufo");
        for (int i=1 ; i<12 ; i++) {
            game.endOfTurn();
        }
        //The For-Loop created a UFO at 4,1

        //Verify location of Blue UFO
        assertThat(game.getUnitAt(new Position(4, 1)).getOwner(), is(Player.BLUE));
        assertThat(game.getUnitAt(new Position(4, 1)).getTypeString(), is("ufo"));

        //Move UFO unit to the unoccupied Red city
        game.moveUnit(new Position(4, 1), new Position(3, 1));
        game.moveUnit(new Position(3, 1), new Position(2, 1));
        game.endOfTurn();
        game.endOfTurn();
        assertThat(game.getUnitAt(new Position(1, 1)), is(nullValue()));
        game.moveUnit(new Position(2, 1), new Position(1, 1));

        //Change city population to be 2
        ((GameImpl) game).createCity(1, 1, Player.RED, 2);
        assertThat(game.getCityAt(new Position(1, 1)).getOwner(), is(Player.RED));
        assertThat(game.getCityAt(new Position(1, 1)).getSize(), is(2));

        //Perform action & verify success
        game.performUnitActionAt(new Position(1, 1));
        assertThat(game.getCityAt(new Position(1, 1)).getOwner(), is(Player.RED));
        assertThat(game.getCityAt(new Position(1, 1)).getSize(), is(1));

        //Perform action & verify success
        game.performUnitActionAt(new Position(1, 1));
        assertThat(game.getCityAt(new Position(1, 1)), is(nullValue()));
    }

    @Test
    public void eliminatedCityInForestConvertsToPlains(){
        //Use test layout that has a city in a forest
        FactoryImpl thetaFactory2 = new FactoryImpl(new ThetaActionStrategy(),new Red3000BCWinningStrategy(), new Add100WorldAgeStrategy(), new TestingLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3AndUFOStatStrategy());
        game = new GameImpl(thetaFactory2);

        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction("ufo");
        for (int i=1 ; i<12 ; i++) {
            game.endOfTurn();
        }
        //The For-Loop created a UFO at 4,1

        //Verify location of Blue UFO
        assertThat(game.getUnitAt(new Position(4, 1)).getOwner(), is(Player.BLUE));
        assertThat(game.getUnitAt(new Position(4, 1)).getTypeString(), is("ufo"));

        //Move UFO unit to the unoccupied Red city
        game.moveUnit(new Position(4, 1), new Position(3, 1));
        game.moveUnit(new Position(3, 1), new Position(2, 1));
        game.endOfTurn();
        game.endOfTurn();
        assertThat(game.getUnitAt(new Position(1, 1)), is(nullValue()));
        game.moveUnit(new Position(2, 1), new Position(1, 1));

        //Verify the city is in a forest
        assertThat(game.getTileAt(new Position(1, 1)).getTypeString(), is(GameConstants.FOREST));

        //Perform action & verify city is destroyed & tile becomes plains
        game.performUnitActionAt(new Position(1, 1));
        assertThat(game.getCityAt(new Position(1, 1)), is(nullValue()));
        assertThat(game.getTileAt(new Position(1, 1)).getTypeString(), is(GameConstants.PLAINS));
    }

    @Test
    public void UFO_CanTravelOverMountainsOceans(){
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction("ufo");
        for (int i=1 ; i<12 ; i++) {
            game.endOfTurn();
        }
        //The For-Loop created a UFO at 4,1

        //Verify location of Blue UFO
        assertThat(game.getUnitAt(new Position(4, 1)).getOwner(), is(Player.BLUE));
        assertThat(game.getUnitAt(new Position(4, 1)).getTypeString(), is("ufo"));

        //Move UFO unit over mountain
        game.moveUnit(new Position(4, 1), new Position(3, 1));
        game.moveUnit(new Position(3, 1), new Position(2, 1));
        game.endOfTurn();
        game.endOfTurn();
        game.moveUnit(new Position(2, 1), new Position(2, 2));
        assertThat(game.getUnitAt(new Position(2, 2)), is(notNullValue()));
        assertThat(game.getTileAt(new Position(2, 2)).getTypeString(), is(GameConstants.MOUNTAINS));
    }
}