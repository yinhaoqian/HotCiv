
package hotciv.standard;

import hotciv.framework.*;
import hotciv.strategies.attackStrategies.AttackerAlwaysWinsAttackStrategy;
import hotciv.strategies.attackStrategies.CombinedStrengthAttackStrategy;
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

public class TestEpsilonCiv {
    private Game game;

    @Before
    public void setUp() {
        FactoryImpl epsilonStrategy = new FactoryImpl(
                new DummyUnitActionStrategy(),
                new WinThreeBattlesWinningStrategy(),
                new Add100WorldAgeStrategy(),
                new AlphaLayout(),
                new CombinedStrengthAttackStrategy(),
                new Original3UnitStatStrategy()
        );
        game = new GameImpl(epsilonStrategy);
    }

    @Test
    public void battleWinBasedOnCombinedAttackDefense(){
        //We are stacking the cards against Blue so that the result will always be a win for Red
        //Everything involved is on a Plains tile
        ((GameImpl)game).createUnit(3, 4, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(3, 3, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(3, 2, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(4, 2, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(5, 2, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(5, 3, Player.RED, GameConstants.LEGION);
        ((GameImpl)game).createUnit(5, 4, Player.RED, GameConstants.LEGION);

        //Red should win
        assertThat(game.moveUnit(new Position(4, 3), new Position(4, 4)), is(true));
        game.endOfTurn(); //end turn to allow unit to move again
        game.endOfTurn(); //end turn to allow unit to move again
        assertThat(game.moveUnit(new Position(4, 4), new Position(4, 3)), is(true));
    }

    @Test
    public void playerWinsAfterThreeBattleWins(){

        for(int i = 0; i < 10; i++){
            //Red should attack blue over and over, replacing the blue guy each time
            ((GameImpl)game).createUnit(4, 3, Player.RED, GameConstants.LEGION);
            game.moveUnit(new Position(4, 3), new Position(4, 4));
            ((GameImpl)game).createUnit(4, 4, Player.BLUE, GameConstants.SETTLER);
        }

        assertThat(((GameImpl) game).getBattleWinsPerPlayer(Player.RED), is(10));
        assertThat(((GameImpl) game).getBattleWinsPerPlayer(Player.BLUE), is(0));

        //Red should be the winner
        assertThat(game.getWinner(), is(Player.RED));
    }
}