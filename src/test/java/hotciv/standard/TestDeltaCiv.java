package hotciv.standard;

import hotciv.framework.*;
import hotciv.strategies.attackStrategies.AttackerAlwaysWinsAttackStrategy;
import hotciv.strategies.layoutStrategies.*;
import hotciv.strategies.unitActionStrategies.*;
import hotciv.strategies.unitStatStrategies.Original3UnitStatStrategy;
import hotciv.strategies.winningStrategies.*;
import hotciv.strategies.worldAgeStrategies.*;
import org.junit.*;


public class TestDeltaCiv {
    private Game game;

    @Before
    public void setUp() {
        FactoryImpl deltaStrategy = new FactoryImpl(new DummyUnitActionStrategy(),new Red3000BCWinningStrategy(), new Add100WorldAgeStrategy(), new DeltaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3UnitStatStrategy());
        game = new GameImpl(deltaStrategy);
    }


    @Test
    public void testIfAllTilesAreCreatedAsRequested() {
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
}