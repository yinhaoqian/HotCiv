package hotciv.strategies.winningStrategies;

import hotciv.framework.Player;
import hotciv.standard.GameImpl;

import java.util.HashMap;

public class WinThreeBattlesAfter20WinningStrategy extends WinThreeBattlesWinningStrategy{


    @Override
    public int getBattleWins(GameImpl game, Player player) {
        return game.getBattleWinsPerPlayer(player) - game.getBattleWinsPerPlayerUpTo20(player);
    }
}
