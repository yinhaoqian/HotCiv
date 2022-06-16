package hotciv.strategies.winningStrategies;
import hotciv.framework.City;
import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.standard.GameImpl;

import java.util.HashMap;

public class Red3000BCWinningStrategy implements WinningStrategy {

    public Player getWinner(GameImpl game) {

        if (game.getAge() >= -3000) {
            return Player.RED;
        }
        return null;
    }
}
