package hotciv.standard;

import hotciv.framework.*;
import hotciv.strategies.attackStrategies.CombinedStrengthAttackStrategy;
import hotciv.strategies.layoutStrategies.DeltaLayout;
import hotciv.strategies.unitActionStrategies.GammaMoveStrategy;
import hotciv.strategies.unitStatStrategies.Original3UnitStatStrategy;
import hotciv.strategies.winningStrategies.WinThreeBattlesWinningStrategy;
import hotciv.strategies.worldAgeStrategies.DynamicWorldAgeStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class TestGameObserver {
    private Game game;

    @Before
    public void setUp() {
        //Uses SemiCiv Factory
        FactoryImpl semiFactory = new FactoryImpl(
                new GammaMoveStrategy(),
                new WinThreeBattlesWinningStrategy(),
                new DynamicWorldAgeStrategy(),
                new DeltaLayout(),
                new CombinedStrengthAttackStrategy(),
                new Original3UnitStatStrategy()
        );
        game = new GameImpl(semiFactory);
        game.addObserver(new TestObserver());
    }

    public class TestObserver implements GameObserver {

        public boolean detectedWorldChange = false;
        public List<Position> wcp = new ArrayList<Position>();

        public boolean detectedTurnEnd = false;
        public Player nplayer = null;
        public int nage = 0;

        public void reset(){
            detectedWorldChange = false;
            wcp = new ArrayList<Position>();

            detectedTurnEnd = false;
            nplayer = null;
            nage = 0;
        }

        public void worldChangedAt(Position pos){detectedWorldChange = true; wcp.add(pos);};


        public void turnEnds(Player nextPlayer, int age){detectedTurnEnd = true; nplayer = nextPlayer; nage = age;};


        public void tileFocusChangedAt(Position position){};
    }

    @Test
    public void turnEndIsObserved(){
        //Check nothing is observed by default
        TestObserver ob = ((TestObserver) ((GameImpl) game).getObserver(0));
        assertThat(ob.detectedWorldChange, is(false));
        assertThat(ob.detectedTurnEnd, is(false));

        //Check that the end of turn is detected
        game.endOfTurn();
        assertThat(ob.detectedWorldChange, is(false));
        assertThat(ob.detectedTurnEnd, is(true));
        assertThat(ob.nplayer, is(Player.BLUE));
        assertThat(ob.nage, is(-3900));
    }

    @Test
    public void removedUnitIsObserved(){
        //Check nothing is observed by default
        TestObserver ob = ((TestObserver) ((GameImpl) game).getObserver(0));
        assertThat(ob.detectedWorldChange, is(false));
        assertThat(ob.detectedTurnEnd, is(false));

        //Check that a unit move is detected
        ((GameImpl) game).removeUnit(new Position(2,0));
        assertThat(ob.detectedWorldChange, is(true));
        assertThat(ob.detectedTurnEnd, is(false));
        assertThat(ob.wcp.get(0), is(new Position(2, 0)));
    }

    @Test
    public void producedUnitIsObserved(){
        //Check nothing is observed by default
        TestObserver ob = ((TestObserver) ((GameImpl) game).getObserver(0));
        assertThat(ob.detectedWorldChange, is(false));
        assertThat(ob.detectedTurnEnd, is(false));

        //Check that a unit creation is detected
        ((GameImpl) game).createUnit(5, 5, Player.RED, GameConstants.ARCHER);
        assertThat(ob.detectedWorldChange, is(true));
        assertThat(ob.detectedTurnEnd, is(false));
        assertThat(ob.wcp.get(0), is(new Position(5, 5)));

        //Reset observer & check nothing is observed by default
        ((TestObserver) ((GameImpl) game).getObserver(0)).reset();
        ob = ((TestObserver) ((GameImpl) game).getObserver(0));
        assertThat(ob.detectedWorldChange, is(false));
        assertThat(ob.detectedTurnEnd, is(false));

        //Check that a unit produced is detected
        ((CityImpl) game.getCityAt(new Position(4, 5))).setProduction(GameConstants.ARCHER);
        game.endOfTurn();//It takes 2 turns to produce an Archer (at 6 production/turn)
        game.endOfTurn();
        assertThat(ob.detectedWorldChange, is(true));
        assertThat(ob.detectedTurnEnd, is(true));
        assertThat(ob.wcp.get(0), is(new Position(4, 5)));
    }

    @Test
    public void newCityIsObserved(){
        //Check nothing is observed by default
        TestObserver ob = ((TestObserver) ((GameImpl) game).getObserver(0));
        assertThat(ob.detectedWorldChange, is(false));
        assertThat(ob.detectedTurnEnd, is(false));

        //Check that a city creation is detected
        ((GameImpl) game).createCity(5, 7, Player.RED, 2);
        assertThat(ob.detectedWorldChange, is(true));
        assertThat(ob.detectedTurnEnd, is(false));
        assertThat(ob.wcp.get(0), is(new Position(5, 7)));
    }

    @Test
    public void cityConquerIsObserved(){
        //Check nothing is observed by default
        ((GameImpl) game).createCity(7, 7, Player.BLUE, 2);
        ((GameImpl) game).createUnit(6, 7, Player.RED, GameConstants.LEGION);
        ((TestObserver) ((GameImpl) game).getObserver(0)).reset();
        TestObserver ob = ((TestObserver) ((GameImpl) game).getObserver(0));
        assertThat(ob.detectedWorldChange, is(false));
        assertThat(ob.detectedTurnEnd, is(false));

        //Check that a city getting conquered is detected
        ((GameImpl) game).moveUnit(new Position(6,7), new Position(7, 7));
        assertThat(ob.detectedWorldChange, is(true));
        assertThat(ob.detectedTurnEnd, is(false));
        assertThat(ob.wcp.get(0), is(new Position(6, 7)));
        assertThat(ob.wcp.get(1), is(new Position(7, 7)));
    }

    @Test
    public void changedTileIsObserved(){
        //Check nothing is observed by default
        TestObserver ob = ((TestObserver) ((GameImpl) game).getObserver(0));
        assertThat(ob.detectedWorldChange, is(false));
        assertThat(ob.detectedTurnEnd, is(false));

        //Check that a changed tile is detected
        ((GameImpl) game).createTile(7, 7, GameConstants.PLAINS);
        assertThat(ob.detectedWorldChange, is(true));
        assertThat(ob.detectedTurnEnd, is(false));
        assertThat(ob.wcp.get(0), is(new Position(7, 7)));
    }

}
