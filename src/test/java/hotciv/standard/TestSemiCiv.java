package hotciv.standard;

import hotciv.framework.*;
import hotciv.strategies.attackStrategies.*;
import hotciv.strategies.layoutStrategies.*;
import hotciv.strategies.unitActionStrategies.*;
import hotciv.strategies.unitStatStrategies.Original3UnitStatStrategy;
import hotciv.strategies.winningStrategies.*;
import hotciv.strategies.worldAgeStrategies.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class TestSemiCiv {
    private Game game;

    @Before
    public void setUp() {
        FactoryImpl thetaFactory = new FactoryImpl(
                new GammaMoveStrategy(),//The settler can build cities like defined by GammaCiv.
                new WinThreeBattlesWinningStrategy(),//The winner is defined as outlined by EpsilonCiv
                new DynamicWorldAgeStrategy(),//The algorithm of BetaCiv is used.
                new DeltaLayout(),//The world layout is as specified by DeltaCiv.
                new CombinedStrengthAttackStrategy(),//Attacks and defenses are handled as defined by EpsilonCiv
                new Original3UnitStatStrategy()//Only original 3 units (Settler, Archer, Legion) are used
        );
        game = new GameImpl(thetaFactory);
    }

    @Test
    public void settlerIsReplacedByCityWithSize1(){//The settler can build cities like defined by GammaCiv.
        assertNotNull(game.getUnitAt(new Position(4, 3)));
        assertThat(game.getUnitAt(new Position(4, 3)).getTypeString(), is(GameConstants.SETTLER));
        game.performUnitActionAt(new Position(4, 3));
        assertNotNull(game.getCityAt(new Position(4, 3)));
        assertThat(game.getCityAt(new Position(4, 3)).getSize(), is(1));
    }

    @Test
    public void playerWinsAfterThreeBattleWins(){//The winner is defined as outlined by EpsilonCiv

        ((GameImpl)game).createUnit(3, 4, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(3, 3, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(3, 2, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(4, 2, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(5, 2, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(5, 3, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(5, 4, Player.RED, GameConstants.LEGION);

        for(int i = 0; i < 10; i++){
            //Red should attack blue over and over, replacing the blue guy each time
            ((GameImpl)game).createUnit(4, 3, Player.RED, GameConstants.LEGION);
            game.moveUnit(new Position(4, 3), new Position(4, 4));
            ((GameImpl)game).createUnit(4, 4, Player.BLUE, GameConstants.SETTLER);
        }
        assertThat(((GameImpl) game).getBattleWinsPerPlayer(Player.RED), is(10));
        assertThat(((GameImpl) game).getBattleWinsPerPlayer(Player.BLUE), is(0));
        assertThat(game.getWinner(), is(Player.RED));
    }

    @Test
    public void worldAgeShouldIncrementBy100Between4000BCAnd100BC() {//The algorithm of BetaCiv is used.
        //Make sure the world age increments by 100 years
        for (int i = 0; i < 38; i++) { //     (4000 - 100) / 100 = 39
            assertThat(game.getAge(), is(-4000 + (100 * i)));
            game.endOfTurn();
        }
    }

    @Test
    public void worldAgeShouldBeSequence_100_1_1_50_Around_ChristBirth() {//The algorithm of BetaCiv is used.
        //Make sure the world age is dictated by sequence
        for (int i = 0; i < 38; i++) {
            game.endOfTurn();
        }
        game.endOfTurn();
        assertThat(game.getAge(), is(-100));
        game.endOfTurn();
        assertThat(game.getAge(), is(-1));
        game.endOfTurn();
        assertThat(game.getAge(), is(1));
        game.endOfTurn();
        assertThat(game.getAge(), is(50));
    }

    @Test
    public void worldAgeShouldIncrementBy50Between50ADAnd1750AD() {//The algorithm of BetaCiv is used.
        //Make sure the world age increments by 50 years
        for (int i = 0; i < 41; i++) {
            game.endOfTurn();
        }

        for (int i = 1; i < 35; i++) { //     (1750 - 50) / 50 = 34
            game.endOfTurn();
            assertThat(game.getAge(), is(50 * i));
        }
    }

    @Test
    public void worldAgeShouldIncrementBy25Between1750ADAnd1900AD() {//The algorithm of BetaCiv is used.
        //Make sure the world age increments by 25 years
        for (int i = 0; i < 42 + 34; i++) {
            game.endOfTurn();
        }

        game.endOfTurn();
        assertThat(game.getAge(), is(1775));
        game.endOfTurn();
        assertThat(game.getAge(), is(1800));
        game.endOfTurn();
        assertThat(game.getAge(), is(1825));
        game.endOfTurn();
        assertThat(game.getAge(), is(1850));
        game.endOfTurn();
        assertThat(game.getAge(), is(1875));
        game.endOfTurn();
        assertThat(game.getAge(), is(1900));
    }

    @Test
    public void worldAgeShouldIncrementBy5Between1900ADAnd1970AD() {//The algorithm of BetaCiv is used.
        //Make sure the world age increments by 5 years
        for (int i = 0; i < 42 + 34 + 6; i++) {
            game.endOfTurn();
        }

        for (int i = 1; i < 15; i++) { //     (1970 - 1900) / 5 = 14
            game.endOfTurn();
            assertThat(game.getAge(), is(1900 + (5 * i)));
        }
    }

    @Test
    public void worldAgeShouldIncrementBy1After1970AD() {//The algorithm of BetaCiv is used.
        //Make sure the world age increments by 1 year
        for (int i = 0; i < 42 + 34 + 6 + 14; i++) {
            game.endOfTurn();
        }
        game.endOfTurn();
        assertThat(game.getAge(), is(1971));
        game.endOfTurn();
        assertThat(game.getAge(), is(1972));
        game.endOfTurn();
        assertThat(game.getAge(), is(1973));
        game.endOfTurn();
        assertThat(game.getAge(), is(1974));
        game.endOfTurn();
        assertThat(game.getAge(), is(1975));
    }


    @Test
    public void testIfAllTilesAreCreatedAsRequested() {//The world layout is as specified by DeltaCiv.
        String[] layoutString = new String[]{
                //o - OCEAN UNOCCUPIED       i - OCEAN W/CITY FROM RED PLAYER        I - OCEAN W/CITY FROM BLUE PLAYER
                //f - FORESTS UNOCCUPIED     d - FORESTS W/CITY FROM RED PLAYER      D - FORESTS W/CITY FROM BLUE PLAYER
                //h - HILLS UNOCCUPIED       g - HILLS W/CITY FROM RED PLAYER        G - HILLS W/CITY FROM BLUE PLAYER
                //m - MOUNTAINS UNOCCUPIED   n - MOUNTAINS W/CITY FROM RED PLAYER    N - MOUNTAINS W/CITY FROM BLUE PLAYER
                //. - PLAINS UNOCCUPIED      , - PLAINS W/CITY FROM RED PLAYER       < - PLAINS W/CITY FROM BLUE PLAYER
                "ooo..m.....ooooo",
                "oo.hh....fff..oo",
                "o.....m...ooo..o",
                "o..mmm....oo....",
                "ooo.f,..hh....oo",
                "o.f..f.....hh..o",
                "ooo...oooooooooo",
                "o.....o...h..moo",
                "o.....o..h..<foo",
                ".fff....o.ff....",
                "........ooo.....",
                "o..mmm....oooooo",
                "oo......ff....oo",
                "oooo.........ooo",
                "oo...hh..ooooooo",
                "ooooo.........oo",
        };
        for (int i = 0; i < GameConstants.WORLDSIZE; i++) {
            String row_layout_iter = layoutString[i];
            for (int j = 0; j < GameConstants.WORLDSIZE; j++) {
                char tile_layout_iter = row_layout_iter.charAt(j);
                if (tile_layout_iter == '.') {
                    assert (game.getTileAt(new Position(i, j)).getTypeString().equals(GameConstants.PLAINS));
                } else if (tile_layout_iter == 'o') {
                    assert (game.getTileAt(new Position(i, j)).getTypeString().equals(GameConstants.OCEANS));
                } else if (tile_layout_iter == 'h') {
                    assert (game.getTileAt(new Position(i, j)).getTypeString().equals(GameConstants.HILLS));
                } else if (tile_layout_iter == 'm') {
                    assert (game.getTileAt(new Position(i, j)).getTypeString().equals(GameConstants.MOUNTAINS));
                } else if (tile_layout_iter == 'f') {
                    assert (game.getTileAt(new Position(i, j)).getTypeString().equals(GameConstants.FOREST));
                } else if (tile_layout_iter == ',') {
                    assert (game.getTileAt(new Position(i, j)).getTypeString().equals(GameConstants.PLAINS));
                    assert (game.getCityAt(new Position(i, j)).getOwner() == Player.RED);
                } else if (tile_layout_iter == '<') {
                    assert (game.getTileAt(new Position(i, j)).getTypeString().equals(GameConstants.PLAINS));
                    assert (game.getCityAt(new Position(i, j)).getOwner() == Player.BLUE);
                } else {
                    throw new ArithmeticException();
                }
            }
        }

    }
    @Test
    public void battleWinBasedOnCombinedAttackDefense(){//Attacks and defenses are handled as defined by EpsilonCiv
        ((GameImpl)game).createUnit(3, 4, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(3, 3, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(3, 2, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(4, 2, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(5, 2, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(5, 3, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(5, 4, Player.RED, GameConstants.LEGION);
        //Red should win
        assertThat(game.moveUnit(new Position(4, 3), new Position(4, 4)), is(true));
        game.endOfTurn(); //Allow unit to move again
        game.endOfTurn(); //Allow unit to move again
        assertThat(game.moveUnit(new Position(4, 4), new Position(4, 3)), is(true));
    }
}
