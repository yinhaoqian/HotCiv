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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class TestGammaCiv {
    private Game game;

    @Before
    public void setUp() {
        FactoryImpl deltaStrategy = new FactoryImpl(new GammaMoveStrategy(),new Red3000BCWinningStrategy(), new Add100WorldAgeStrategy(), new AlphaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3UnitStatStrategy());
        game = new GameImpl(deltaStrategy);
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
        game.endOfTurn(); //end turn to allow unit to move again
        game.endOfTurn(); //end turn to allow unit to move again
        game.moveUnit(new Position(2, 1), new Position(2, 0));
        assertThat(game.getUnitAt(new Position(2, 0)).getDefensiveStrength(), is(initialStrength));
    }
}