package hotciv.strategies.winningStrategies;
import hotciv.framework.*;
import hotciv.standard.GameImpl;

import java.util.HashMap;

public class ConquerAllWinningStrategy implements WinningStrategy {

    private Player winner;

    public Player getWinner(GameImpl game) {

        if (this.winner == null) {
            Player potentialWinner = null;
            for (int i = 0; i < GameConstants.WORLDSIZE; i++) {
                for (int j = 0; j < GameConstants.WORLDSIZE; j++) {
                    //For every city
                    City city = getCityAt(game, new Position(i,j));
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

    public City getCityAt(GameImpl game, Position pos) {
        return game.getCityAt(pos);
    }
}
