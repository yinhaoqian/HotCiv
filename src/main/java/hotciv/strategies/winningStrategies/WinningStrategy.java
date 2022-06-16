package hotciv.strategies.winningStrategies;
import java.util.HashMap;
import hotciv.framework.City;
import hotciv.framework.Player;
import hotciv.standard.GameImpl;

public interface WinningStrategy {

    Player getWinner(GameImpl game);
}
