package hotciv.standard;

import hotciv.framework.*;

import hotciv.strategies.attackStrategies.AttackerAlwaysWinsAttackStrategy;
import hotciv.strategies.layoutStrategies.AlphaLayout;
import hotciv.strategies.unitActionStrategies.*;
import hotciv.strategies.unitStatStrategies.Original3UnitStatStrategy;
import hotciv.strategies.winningStrategies.*;
import hotciv.strategies.worldAgeStrategies.*;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestAlphaCiv {
    private Game game;

    /**
     * Fixture for AlphaCiv testing.
     */
    @Before
    public void setUp() {
        FactoryImpl alphaStrategy = new FactoryImpl(new DummyUnitActionStrategy(), new Red3000BCWinningStrategy(), new Add100WorldAgeStrategy(), new AlphaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3UnitStatStrategy());
        game = new GameImpl(alphaStrategy);
    }

  /*
  @Test
  public void tileTypeShouldBePlains() {
    //Make sure that tile type is plains for any position
    assertThat(game.getTileAt(new Position(0, 0)).getTypeString(), is(GameConstants.PLAINS));
  }
   */

    @Test
    public void redShouldHaveArcherAt2_0() {
        //Make sure that red has an archer at 2,0
        assertThat(game.getUnitAt(new Position(2, 0)).getTypeString(), is(GameConstants.ARCHER));
        assertThat(game.getUnitAt(new Position(2, 0)).getOwner(), is(Player.RED));
    }

    @Test
    public void redShouldHaveCityAt1_1() {
        //Make sure Red has a city at 1,1
        assertThat(game.getCityAt(new Position(1, 1)).getOwner(), is(Player.RED));
    }

    @Test
    public void shouldBeRedAsStartingPlayer() {
        //Make sure that the game is not initialized to null & it is Red's turn
        assertThat(game, is(notNullValue()));
        assertThat(game.getPlayerInTurn(), is(Player.RED));
    }

    @Test
    public void worldAgeShouldStartAt4000BC() {
        //Make sure the world age starts at 4000 BC (-4000)
        assertThat(game.getAge(), is(-4000));
    }

    @Test
    public void thereShouldBeAMountainAt2_2() {
        //Make sure that tile type is mountain for position 2,2
        assertThat(game.getTileAt(new Position(2, 2)).getTypeString(), is(GameConstants.MOUNTAINS));
    }

    @Test
    public void blueShouldHaveLegionAt3_2() {
        //Make sure that blue has legion at 3,2
        assertThat(game.getUnitAt(new Position(3, 2)).getTypeString(), is(GameConstants.LEGION));
        assertThat(game.getUnitAt(new Position(3, 2)).getOwner(), is(Player.BLUE));
    }

    @Test
    public void thereShouldBeNoCityAt0_2() {
        //Make sure there is no city at 0,2
        assertThat(game.getCityAt(new Position(0, 2)), is(nullValue()));
    }

    @Test
    public void itCanBeBluesTurn() {
        //Make sure Red can pass turn to Blue
        game.endOfTurn();
        assertThat(game.getPlayerInTurn(), is(Player.BLUE));
    }

    @Test
    public void worldAgeShouldIncrementBy100() {
        //Make sure the world age increments by 100 years
        game.endOfTurn();
        assertThat(game.getAge(), is(-3900));
    }

    @Test
    public void redWinsInYear3000BC() {
        //Make sure the red wins in year 3000 BC
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        assertThat(game.getAge(), is(-3000));
        assertThat(game.getWinner(), is(Player.RED));
    }

    @Test
    public void itsRedsTurnAfterBlue() {
        //Make sure Blue can pass turn to Red
        assertThat(game.getPlayerInTurn(), is(Player.RED));
        game.endOfTurn();
        assertThat(game.getPlayerInTurn(), is(Player.BLUE));
        game.endOfTurn();
        assertThat(game.getPlayerInTurn(), is(Player.RED));
    }

    @Test
    public void thereIsOceanAt1_0() {
        //Make sure there is an ocean tile at (1,0)
        assertThat(game.getTileAt(new Position(1, 0)).getTypeString(), is(GameConstants.OCEANS));
    }

    @Test
    public void thereIsRedSettlerAt4_3() {
        //Make sure there is a Red settler at (4,3)
        assertThat(game.getUnitAt(new Position(4, 3)).getTypeString(), is(GameConstants.SETTLER));
    }

    @Test
    public void thereIsBlueCityAt4_1() {
        assertThat(game.getCityAt(new Position(4, 1)).getOwner(), is(Player.BLUE));
    }

    @Test
    public void UnitsCannotMoveOntoTheSameTile() {
        //This test moves the settler at (4, 3) to (4, 4), then moves it back
        assertThat(game.getUnitAt(new Position(5, 3)), is(nullValue()));
        assertThat(game.moveUnit(new Position(4, 3), new Position(5, 3)), is(true));
        assertThat(game.getUnitAt(new Position(5, 3)).getTypeString(), is(GameConstants.SETTLER));
        game.endOfTurn(); //end turn to allow unit to move again
        game.endOfTurn(); //end turn to allow unit to move again
        assertThat(game.moveUnit(new Position(5, 3), new Position(4, 3)), is(true));
        assertThat(game.getUnitAt(new Position(4, 3)).getTypeString(), is(GameConstants.SETTLER));
    }

    @Test
    public void CitiesCanSetTheirWorkforceProduction() {
        ((CityImpl) game.getCityAt(new Position(4, 1))).setWorkforceFocus(GameConstants.ARCHER);
        assertThat(game.getCityAt(new Position(4, 1)).getWorkforceFocus(), is(GameConstants.ARCHER));
        ((CityImpl) game.getCityAt(new Position(4, 1))).setWorkforceFocus(GameConstants.LEGION);
        assertThat(game.getCityAt(new Position(4, 1)).getWorkforceFocus(), is(GameConstants.LEGION));
        ((CityImpl) game.getCityAt(new Position(4, 1))).setWorkforceFocus(GameConstants.SETTLER);
        assertThat(game.getCityAt(new Position(4, 1)).getWorkforceFocus(), is(GameConstants.SETTLER));
    }

    @Test
    public void AttackingUnitAlwaysWins() {
        assertThat(game.getUnitAt(new Position(4, 3)).getOwner(), is(Player.RED));
        assertThat(game.getUnitAt(new Position(4, 4)).getOwner(), is(Player.BLUE));
        game.moveUnit(new Position(4, 3), new Position(4, 4));
        assertThat(game.getUnitAt(new Position(4, 4)).getOwner(), is(Player.RED));
    }

    //There are hills at 0,1 - PASSED
    //Cities produce 6 production per round - PASSED
    //Cities have population of 1 - PASSED
    //Cities create unit on city tile when production hits 18 - PASSED
    //Cities create unit on first non-occupied tile if city tile is occupied - PASSED
    //When cities create unit, subtract 18 from production - PASSED

    /*
        @Test
        public void Position_hillsAtPos01() {//TEST PASSED
            //TEST: There are hills at 0,1
            assertEquals(game.getTileAt(new Position(0, 1)).getTypeString(), GameConstants.HILLS);
        }
    */
    @Test
    public void Cities_produce_6_Per_Round() { //TEST PASSED
        //TEST: Cities produce 6 production per round
        short productionBeforeTurnAt41 = (short) (game.getCityAt(new Position(4, 1)).getTreasury());
        short productionBeforeTurnAt11 = (short) (game.getCityAt(new Position(1, 1)).getTreasury());
        game.endOfTurn();
        short productionAfterTurnAt41 = (short) (game.getCityAt(new Position(4, 1)).getTreasury());
        short productionAfterTurnAt11 = (short) (game.getCityAt(new Position(1, 1)).getTreasury());
        assert (productionBeforeTurnAt41 == (productionAfterTurnAt41 - 6));
        assert (productionBeforeTurnAt11 == (productionAfterTurnAt11 - 6));
    }

    @Test
    public void Cities_have_Population_of_1() {//TEST PASSED
        //TEST: Cities have population of 1
        assertEquals(game.getCityAt(new Position(4, 1)).getSize(), 1);
        assertEquals(game.getCityAt(new Position(1, 1)).getSize(), 1);
    }

    @Test
    public void City_create_Unit_At_10() {//TEST PASSED
        //TEST: Cities create unit on city tile when production hits 10
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction(GameConstants.ARCHER);
        ((CityImpl) game.getCityAt(new Position(1, 1))).setProduction(GameConstants.ARCHER);

        assertNull(game.getUnitAt(new Position(4, 1)));
        assertNull(game.getUnitAt(new Position(1, 1)));
        game.endOfTurn();
        assertNull(game.getUnitAt(new Position(4, 1)));
        assertNull(game.getUnitAt(new Position(1, 1)));
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(4, 1)));
        assertNotNull(game.getUnitAt(new Position(1, 1)));
        assertThat(game.getCityAt(new Position(4, 1)).getTreasury(), is(2));
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(2));
    }
    //Test test case is commented out because the new subtraction mechanism will reset production to 0
    //once it hits 18 and creates unit.
    /*
    @Test
    public void City_occupyTest() {//TEST PASSED
        //TEST: Cities create unit on first non-occupied tile if city tile is occupied
        //--starting north of city, moving clockwise
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        //Up OF CITY - Second 18 Productions:
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(4, 1)));//BLUE-middle
        assertNotNull(game.getUnitAt(new Position(3, 1)));//BLUE-up
        assertNull(game.getUnitAt(new Position(4, 2)));//BLUE-right
        assertNull(game.getUnitAt(new Position(5, 1)));//BLUE-down
        assertNull(game.getUnitAt(new Position(4, 0)));//BLUE-left
        assertNotNull(game.getUnitAt(new Position(1, 1)));//RED-middle
        assertNotNull(game.getUnitAt(new Position(0, 1)));//RED-up
        assertNull(game.getUnitAt(new Position(1, 2)));//RED-right
        assertNull(game.getUnitAt(new Position(2, 1)));//RED-down
        assertNull(game.getUnitAt(new Position(1, 0)));//RED-left
        //Right OF CITY - Third 18 Productions:
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(4, 1)));//BLUE-middle
        assertNotNull(game.getUnitAt(new Position(3, 1)));//BLUE-up
        assertNotNull(game.getUnitAt(new Position(4, 2)));//BLUE-right
        assertNull(game.getUnitAt(new Position(5, 1)));//BLUE-down
        assertNull(game.getUnitAt(new Position(4, 0)));//BLUE-left
        assertNotNull(game.getUnitAt(new Position(1, 1)));//RED-middle
        assertNotNull(game.getUnitAt(new Position(0, 1)));//RED-up
        assertNotNull(game.getUnitAt(new Position(1, 2)));//RED-right
        assertNull(game.getUnitAt(new Position(2, 1)));//RED-down
        assertNull(game.getUnitAt(new Position(1, 0)));//RED-left
        //Down OF CITY - Fourth 18 Productions:
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(4, 1)));//BLUE-middle
        assertNotNull(game.getUnitAt(new Position(3, 1)));//BLUE-up
        assertNotNull(game.getUnitAt(new Position(4, 2)));//BLUE-right
        assertNotNull(game.getUnitAt(new Position(5, 1)));//BLUE-down
        assertNull(game.getUnitAt(new Position(4, 0)));//BLUE-left
        assertNotNull(game.getUnitAt(new Position(1, 1)));//RED-middle
        assertNotNull(game.getUnitAt(new Position(0, 1)));//RED-up
        assertNotNull(game.getUnitAt(new Position(1, 2)));//RED-right
        assertNotNull(game.getUnitAt(new Position(2, 1)));//RED-down
        assertNull(game.getUnitAt(new Position(1, 0)));//RED-left
        //Up OF CITY - Fifth 18 Productions:
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(4, 1)));//BLUE-middle
        assertNotNull(game.getUnitAt(new Position(3, 1)));//BLUE-up
        assertNotNull(game.getUnitAt(new Position(4, 2)));//BLUE-right
        assertNotNull(game.getUnitAt(new Position(5, 1)));//BLUE-down
        assertNotNull(game.getUnitAt(new Position(4, 0)));//BLUE-left
        assertNotNull(game.getUnitAt(new Position(1, 1)));//RED-middle
        assertNotNull(game.getUnitAt(new Position(0, 1)));//RED-up
        assertNotNull(game.getUnitAt(new Position(1, 2)));//RED-right
        assertNotNull(game.getUnitAt(new Position(2, 1)));//RED-down
        assertNotNull(game.getUnitAt(new Position(1, 0)));//RED-left
    }
    */

    @Test
    public void City_subtract10_when_creating_archer() {//TEST PASSED
        //TEST: When cities create unit, subtract 10 from production
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction(GameConstants.ARCHER);
        ((CityImpl) game.getCityAt(new Position(1, 1))).setProduction(GameConstants.ARCHER);

        assertThat(game.getCityAt(new Position(4, 1)).getTreasury(), is(0));
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(0));
        game.endOfTurn();
        assertThat(game.getCityAt(new Position(4, 1)).getTreasury(), is(6));
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(6));
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(4, 1)));
        assertNotNull(game.getUnitAt(new Position(1, 1)));
        assertThat(game.getCityAt(new Position(4, 1)).getTreasury(), is(2));
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(2));
    }

    @Test
    public void AllTilesShouldBePlainsExceptOceanAndMountains() {
        //Make sure there are only 2 tiles on the 16x16 grid that are not Plains.
        //Two Mountains and Ocean tiles are the only tiles that should be different.

        int non_plains_tiles = 0;
        for (int i = 0; i < GameConstants.WORLDSIZE; i++) {
            for (int j = 0; j < GameConstants.WORLDSIZE; j++) {
                assertThat(game.getTileAt(new Position(i, j)), is(notNullValue()));

                if (!game.getTileAt(new Position(i, j)).getTypeString().equals(GameConstants.PLAINS)) {
                    non_plains_tiles++;
                }
            }
        }
        assertThat(non_plains_tiles, is(3));//I changed this because there is actually 3 nonplain tiles!!
    }

    @Test
    public void CitiesCanChangeProduction() {
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction(GameConstants.foodFocus);
        assertThat(game.getCityAt(new Position(4, 1)).getProduction(), is(GameConstants.foodFocus));
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction(GameConstants.productionFocus);
        assertThat(game.getCityAt(new Position(4, 1)).getProduction(), is(GameConstants.productionFocus));
    }

    @Test
    public void checkIfAllCitiesAreIncludedInEndOfTurnMethod() {
        //To fully test the functionality, please add a few more city tiles to the game!
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (i == 1 && j == 1) assertNotNull(game.getCityAt(new Position(i, j)));
                else if (i == 4 && j == 1) assertNotNull(game.getCityAt(new Position(i, j)));
                    //else if(i==8&&j==8) assertNotNull(game.getCityAt(new Position(i,j)));
                    //else if(i==10&&j==10) assertNotNull(game.getCityAt(new Position(i,j)));
                else assertNull(game.getCityAt(new Position(i, j)));
            }
        }
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction(GameConstants.ARCHER);
        ((CityImpl) game.getCityAt(new Position(1, 1))).setProduction(GameConstants.ARCHER);
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (game.getCityAt(new Position(i, j)) != null) {
                    assertNotNull(game.getUnitAt(new Position(i, j)));
                }
            }
        }
        /*
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++) {
                if (game.getCityAt(new Position(i, j)) != null) {
                    assertNotNull (game.getUnitAt(new Position(i-1, j)));
                }
            }
        }
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++) {
                if (game.getCityAt(new Position(i, j)) != null) {
                    assertNotNull (game.getUnitAt(new Position(i, j+1)));
                }
            }
        }
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++) {
                if (game.getCityAt(new Position(i, j)) != null) {
                    assertNotNull (game.getUnitAt(new Position(i+1, j)));
                }
            }
        }
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++) {
                if (game.getCityAt(new Position(i, j)) != null) {
                    assertNotNull (game.getUnitAt(new Position(i, j-1)));
                }
            }
        }
        */
    }


    @Test
    public void City_occupyTest_with4Corners() {//TEST PASSED
        //TEST: Cities create unit on first non-occupied tile if city tile is occupied (including the 4 adjacent corner tiles)
        ((CityImpl) game.getCityAt(new Position(4, 1))).setProduction(GameConstants.ARCHER);
        ((CityImpl) game.getCityAt(new Position(1, 1))).setProduction(GameConstants.ARCHER);
        //--starting north of city, moving clockwise
        //0 Production
        assertNull(game.getUnitAt(new Position(1, 1)));//RED-center
        assertNull(game.getUnitAt(new Position(0, 1)));//RED-north
        assertNull(game.getUnitAt(new Position(0, 2)));//RED-north east
        assertNull(game.getUnitAt(new Position(1, 2)));//RED-east
        assertNull(game.getUnitAt(new Position(2, 2)));//RED-south east
        assertNull(game.getUnitAt(new Position(2, 1)));//RED-south
        assertNotNull(game.getUnitAt(new Position(2, 0)));//RED-south west (Archer already here from start of game)
        assertNull(game.getUnitAt(new Position(1, 0)));//RED-west
        assertNull(game.getUnitAt(new Position(0, 0)));//RED-north west
        //Center of city - 2 Productions:
        game.endOfTurn();
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(1, 1)));//RED-center
        assertNull(game.getUnitAt(new Position(0, 1)));//RED-north
        assertNull(game.getUnitAt(new Position(0, 2)));//RED-north east
        assertNull(game.getUnitAt(new Position(1, 2)));//RED-east
        assertNull(game.getUnitAt(new Position(2, 2)));//RED-south east
        assertNull(game.getUnitAt(new Position(2, 1)));//RED-south
        assertNotNull(game.getUnitAt(new Position(2, 0)));//RED-south west (Archer already here from start of game)
        assertNull(game.getUnitAt(new Position(1, 0)));//RED-west
        assertNull(game.getUnitAt(new Position(0, 0)));//RED-north west
        //North of city - 4 Productions:
        game.endOfTurn();
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(1, 1)));//RED-center
        assertNotNull(game.getUnitAt(new Position(0, 1)));//RED-north
        assertNull(game.getUnitAt(new Position(0, 2)));//RED-north east
        assertNull(game.getUnitAt(new Position(1, 2)));//RED-east
        assertNull(game.getUnitAt(new Position(2, 2)));//RED-south east
        assertNull(game.getUnitAt(new Position(2, 1)));//RED-south
        assertNotNull(game.getUnitAt(new Position(2, 0)));//RED-south west (Archer already here from start of game)
        assertNull(game.getUnitAt(new Position(1, 0)));//RED-west
        assertNull(game.getUnitAt(new Position(0, 0)));//RED-north west
        //North-east of city - 6 Productions:
        game.endOfTurn();
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(1, 1)));//RED-center
        assertNotNull(game.getUnitAt(new Position(0, 1)));//RED-north
        assertNotNull(game.getUnitAt(new Position(0, 2)));//RED-north east
        assertNull(game.getUnitAt(new Position(1, 2)));//RED-east
        assertNull(game.getUnitAt(new Position(2, 2)));//RED-south east
        assertNull(game.getUnitAt(new Position(2, 1)));//RED-south
        assertNotNull(game.getUnitAt(new Position(2, 0)));//RED-south west (Archer already here from start of game)
        assertNull(game.getUnitAt(new Position(1, 0)));//RED-west
        assertNull(game.getUnitAt(new Position(0, 0)));//RED-north west
        //East of city - 8 Productions:
        game.endOfTurn();
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(1, 1)));//RED-center
        assertNotNull(game.getUnitAt(new Position(0, 1)));//RED-north
        assertNotNull(game.getUnitAt(new Position(0, 2)));//RED-north east
        assertNotNull(game.getUnitAt(new Position(1, 2)));//RED-east
        assertNull(game.getUnitAt(new Position(2, 2)));//RED-south east
        assertNull(game.getUnitAt(new Position(2, 1)));//RED-south
        assertNotNull(game.getUnitAt(new Position(2, 0)));//RED-south west (Archer already here from start of game)
        assertNull(game.getUnitAt(new Position(1, 0)));//RED-west
        assertNull(game.getUnitAt(new Position(0, 0)));//RED-north west
        //South & North-west of city - 0 Productions:
        game.endOfTurn();
        game.endOfTurn();
        assertNotNull(game.getUnitAt(new Position(1, 1)));//RED-center
        assertNotNull(game.getUnitAt(new Position(0, 1)));//RED-north
        assertNotNull(game.getUnitAt(new Position(0, 2)));//RED-north east
        assertNotNull(game.getUnitAt(new Position(1, 2)));//RED-east
        assertNull(game.getUnitAt(new Position(2, 2)));//RED-south east (MOUNTAIN)
        assertNotNull(game.getUnitAt(new Position(2, 1)));//RED-south
        assertNotNull(game.getUnitAt(new Position(2, 0)));//RED-south west (Archer already here from start of game)
        assertNull(game.getUnitAt(new Position(1, 0)));//RED-west (OCEAN)
        assertNotNull(game.getUnitAt(new Position(0, 0)));//RED-north west
    }

    @Test
    public void playerCanChangeCityProduction() {
        //Make sure player can change the production of a city
        game.changeProductionInCityAt(new Position(1, 1), GameConstants.ARCHER);
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        assertThat(game.getUnitAt(new Position(1, 1)).getTypeString(), is(GameConstants.ARCHER));

        game.changeProductionInCityAt(new Position(1, 1), GameConstants.LEGION);
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        assertThat(game.getUnitAt(new Position(0, 1)).getTypeString(), is(GameConstants.LEGION));

        game.changeProductionInCityAt(new Position(1, 1), GameConstants.SETTLER);
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        game.endOfTurn();
        assertThat(game.getUnitAt(new Position(0, 2)).getTypeString(), is(GameConstants.SETTLER));
    }

    @Test
    public void requiredProductionDependsOnUnit() {
        //Make sure archers cost 10 production, legion 15, and settler 30
        game.changeProductionInCityAt(new Position(1, 1), GameConstants.ARCHER);
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(0));
        game.endOfTurn();
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(6));
        game.endOfTurn();
        //There should be 12 production now
        assertThat(game.getUnitAt(new Position(1, 1)).getTypeString(), is(GameConstants.ARCHER));
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(2));
        //There should be only 2 production left now

        game.changeProductionInCityAt(new Position(1, 1), GameConstants.LEGION);
        game.endOfTurn();
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(8));
        game.endOfTurn();
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(14));
        game.endOfTurn();
        assertThat(game.getUnitAt(new Position(0, 1)).getTypeString(), is(GameConstants.LEGION));
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(5));
        //There should be only 5 production left now

        game.changeProductionInCityAt(new Position(1, 1), GameConstants.SETTLER);
        game.endOfTurn();
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(11));
        game.endOfTurn();
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(17));
        game.endOfTurn();
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(23));
        game.endOfTurn();
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(29));
        game.endOfTurn();
        assertThat(game.getUnitAt(new Position(0, 2)).getTypeString(), is(GameConstants.SETTLER));
        assertThat(game.getCityAt(new Position(1, 1)).getTreasury(), is(5));
        //There should be only 5 production left now
    }
}
