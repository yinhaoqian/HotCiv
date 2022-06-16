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

public class TestBetaCiv {
    private Game game;

    @Before
    public void setUp() {
        FactoryImpl betaStrategy = new FactoryImpl(new DummyUnitActionStrategy(), new ConquerAllWinningStrategy(), new DynamicWorldAgeStrategy(), new AlphaLayout(), new AttackerAlwaysWinsAttackStrategy(), new Original3UnitStatStrategy());
        game = new GameImpl(betaStrategy);
    }

    @Test
    public void worldAgeShouldIncrementBy100Between4000BCAnd100BC() {
        //Make sure the world age increments by 100 years
        for (int i = 0; i < 38; i++) { //     (4000 - 100) / 100 = 39
            assertThat(game.getAge(), is(-4000 + (100 * i)));
            game.endOfTurn();
        }
    }

    @Test
    public void worldAgeShouldBeSequence_100_1_1_50_Around_ChristBirth() {
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
    public void worldAgeShouldIncrementBy50Between50ADAnd1750AD() {
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
    public void worldAgeShouldIncrementBy25Between1750ADAnd1900AD() {
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
    public void worldAgeShouldIncrementBy5Between1900ADAnd1970AD() {
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
    public void worldAgeShouldIncrementBy1After1970AD() {
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
    public void nobodyWinsWhenTheyDoNotOwnAllCities() {
        //Make sure there is no winner
        for (int i = 0; i < 42 + 34 + 6 + 14 + 5; i++) {
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1975));
        assertThat(game.getWinner(), is(nullValue()));
    }

    //TEST STUB BELOW (For playerWhoOwnsAllCitiesFirstWins test, seen below)
    public class FixedCitiesWinningStrategy implements WinningStrategy {
        City[][] cities;
        private Player winner;
        public FixedCitiesWinningStrategy(City[][] cities) {
            this.cities = cities;
        }

        public Player getWinner(GameImpl game) {
            if (this.winner == null) {
                Player potentialWinner = null;
                for (int i = 0; i < GameConstants.WORLDSIZE; i++) {
                    for (int j = 0; j < GameConstants.WORLDSIZE; j++) {
                        //For every city
                        City city = this.cities[i][j];
                        if (city != null) {
                            //Check owner
                            Player owner = city.getOwner();
                            if (potentialWinner == null) {
                                potentialWinner = owner;
                            }
                            else if (potentialWinner != owner){
                                //If two+ players own a city, return null
                                return null;
                            }
                        }
                    }
                }
                this.winner = potentialWinner;
                return potentialWinner;
            }
            //If a winner was already declared, don't recalculate winner
            return this.winner;
        }
    }

    @Test
    public void playerWhoOwnsAllCitiesFirstWins() {
        //Make sure winner is player who owns all cities FIRST
        City[][] cities = new CityImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        cities[1][2] = new CityImpl(Player.RED);
        cities[3][4] = new CityImpl(Player.RED);
        WinningStrategy strategy = new FixedCitiesWinningStrategy(cities);
        //Red has all cities. Red should be winner
        assertThat(strategy.getWinner((GameImpl) game), is(Player.RED));

        cities[3][4] = new CityImpl(Player.BLUE);
        //Red already won. So Red should still be winner
        assertThat(strategy.getWinner((GameImpl) game), is(Player.RED));

        //New game (new strategy), blue can now win if conquers all. Both currently have 1 city

        cities[1][2] = new CityImpl(Player.RED);
        cities[3][4] = new CityImpl(Player.BLUE);
        strategy = new FixedCitiesWinningStrategy(cities);
        assertThat(strategy.getWinner((GameImpl) game), is(nullValue()));

        cities[1][2] = new CityImpl(Player.BLUE);
        strategy = new FixedCitiesWinningStrategy(cities);
        //Blue now has both cities & should win.
        assertThat(strategy.getWinner((GameImpl) game), is(Player.BLUE));
    }

}