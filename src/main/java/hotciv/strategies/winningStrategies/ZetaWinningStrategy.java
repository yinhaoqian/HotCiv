package hotciv.strategies.winningStrategies;

import hotciv.framework.City;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.GameImpl;

public class ZetaWinningStrategy implements WinningStrategy {
    private Player winner;
    private WinningStrategy preIncluding20Strategy, post20Strategy, currentState;

    public ZetaWinningStrategy(WinningStrategy preIncluding20Strategy, WinningStrategy post20Strategy) {
        this.preIncluding20Strategy = preIncluding20Strategy;
        this.post20Strategy = post20Strategy;
        this.currentState = null;
    }

    public Player getWinner(GameImpl game) {
        if (this.winner != null) {
            return this.winner;
        }
        if (getTurns(game) > 20) {
            this.currentState = post20Strategy;
        }
        else {
            this.currentState = preIncluding20Strategy;
        }
        this.winner = this.currentState.getWinner(game);
        return this.winner;
    }

    public int getTurns(GameImpl game) {
        return game.getTurns();
    }
}
