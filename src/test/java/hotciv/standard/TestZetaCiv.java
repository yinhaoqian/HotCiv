package hotciv.standard;

import hotciv.framework.*;
import hotciv.strategies.attackStrategies.AttackerAlwaysWinsAttackStrategy;
import hotciv.strategies.layoutStrategies.*;
import hotciv.strategies.unitActionStrategies.*;
import hotciv.strategies.unitStatStrategies.Original3UnitStatStrategy;
import hotciv.strategies.winningStrategies.*;
import hotciv.strategies.worldAgeStrategies.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TestZetaCiv {
    private Game game;

    @Before
    public void setUp() {
        FactoryImpl zetaFactory = new FactoryImpl(new DummyUnitActionStrategy(), new ZetaWinningStrategy(new ConquerAllWinningStrategy(), new WinThreeBattlesAfter20WinningStrategy()), new Add100WorldAgeStrategy(), new AlphaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3UnitStatStrategy());
        game = new GameImpl(zetaFactory);
    }


    //TEST STUB BELOW
    public class FixedCities2WinningStrategy extends ConquerAllWinningStrategy {
        private City[][] cities;
        private Player winner;
        public FixedCities2WinningStrategy() {
            super();
        }
        public void setCities(City[][] cities) {
            this.cities = cities;
        }
        @Override
        public City getCityAt(GameImpl game, Position pos) {
            return cities[pos.getColumn()][pos.getRow()];
        }
    }


    //TEST STUB BELOW
    public class FixedCitiesWinningStrategy extends ZetaWinningStrategy {
        private int turns;
        private Player winner;
        private WinningStrategy preIncluding20Strategy, post20Strategy, currentState;
        public FixedCitiesWinningStrategy(WinningStrategy preIncluding20Strategy, WinningStrategy post20Strategy) {
            super(preIncluding20Strategy, post20Strategy);
        }
        public void setTurns(int turns) {
            this.turns = turns;
        }
        @Override
        public int getTurns(GameImpl game) {
            return this.turns;
        }
    }



    @Test
    public void beforeTurn21ThePlayerWhoOwnsAllCitiesFirstWins() {
        //Make sure winner is player who owns all cities FIRST

        City[][] cities = new CityImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        cities[1][2] = new CityImpl(Player.RED);
        cities[3][4] = new CityImpl(Player.RED);

        //Init using test stub
        FixedCities2WinningStrategy testStubConq = new FixedCities2WinningStrategy();
        testStubConq.setCities(cities);
        FixedCitiesWinningStrategy testStubZeta = new FixedCitiesWinningStrategy(testStubConq, new WinThreeBattlesAfter20WinningStrategy());
        testStubZeta.setTurns(0);
        FactoryImpl zetaFactory = new FactoryImpl(new DummyUnitActionStrategy(), testStubZeta, new Add100WorldAgeStrategy(), new AlphaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3UnitStatStrategy());
        game = new GameImpl(zetaFactory);

        //Red has all cities. Red should be winner
        assertThat(game.getWinner(), is(Player.RED));


        //Init using test stub
        testStubConq = new FixedCities2WinningStrategy();
        testStubConq.setCities(cities);
        testStubZeta.setTurns(20);
        zetaFactory = new FactoryImpl(new DummyUnitActionStrategy(), testStubZeta, new Add100WorldAgeStrategy(), new AlphaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3UnitStatStrategy());
        game = new GameImpl(zetaFactory);

        //Red has all cities. Red should be winner
        assertThat(game.getWinner(), is(Player.RED));


        cities = new CityImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        cities[1][2] = new CityImpl(Player.RED);
        cities[3][4] = new CityImpl(Player.BLUE);

        //Init using test stub
        testStubConq = new FixedCities2WinningStrategy();
        testStubConq.setCities(cities);
        testStubZeta = new FixedCitiesWinningStrategy(testStubConq, new WinThreeBattlesAfter20WinningStrategy());
        zetaFactory = new FactoryImpl(new DummyUnitActionStrategy(), testStubZeta, new Add100WorldAgeStrategy(), new AlphaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3UnitStatStrategy());
        game = new GameImpl(zetaFactory);

        //Nobody has all cities. Nobody wins
        assertThat(game.getWinner(), is(nullValue()));
    }

    @Test
    public void afterTurn21ThePlayerWhoWins3BattlesFirstWins() {
        //Make sure winner is player who wins 3 battles FIRST

        //Init using test stub
        FixedCitiesWinningStrategy testStubZeta = new FixedCitiesWinningStrategy(new ConquerAllWinningStrategy(), new WinThreeBattlesAfter20WinningStrategy());
        testStubZeta.setTurns(21);
        FactoryImpl zetaFactory = new FactoryImpl(new DummyUnitActionStrategy(), testStubZeta, new Add100WorldAgeStrategy(), new AlphaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3UnitStatStrategy());
        game = new GameImpl(zetaFactory);

        for(int i = 0; i < 10; i++){
            //Red should attack blue over and over, replacing the blue guy each time
            ((GameImpl)game).createUnit(4, 3, Player.RED, GameConstants.LEGION);
            game.moveUnit(new Position(4, 3), new Position(4, 4));
            ((GameImpl)game).createUnit(4, 4, Player.BLUE, GameConstants.SETTLER);
        }

        //Red should be the winner
        assertThat(game.getWinner(), is(Player.RED));
    }
}
