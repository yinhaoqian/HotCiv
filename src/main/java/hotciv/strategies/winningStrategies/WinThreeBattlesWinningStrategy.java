package hotciv.strategies.winningStrategies;

import hotciv.framework.City;
import hotciv.framework.Player;
import hotciv.standard.GameImpl;

import java.util.HashMap;

public class WinThreeBattlesWinningStrategy implements WinningStrategy {

    public Player getWinner(GameImpl game) {

        int wins[] = new int[4];
        wins[0] = getBattleWins(game, Player.RED);
        wins[1] = getBattleWins(game, Player.BLUE);
        wins[2] = getBattleWins(game, Player.YELLOW);
        wins[3] = getBattleWins(game, Player.GREEN);

        //Get the max number of wins (not the player)
        int max = 0;
        for (int i = 0; i < 4; i++) {
            if (wins[i] > max) {
                max = wins[i];
            }
        }

        //You need at least 3 wins to win the game
        if(max < 3){
            return null;
        }

        //If multiple players got the max, it's a tie, so return null
        int numberOfPlayersWhoGotMax = 0;
        for (int i = 0; i < 4; i++) {
            if (wins[i] == max) {
                numberOfPlayersWhoGotMax++;
            }
        }
        if (numberOfPlayersWhoGotMax > 1) {
            return null;
        }

        //Since only one player got the max, find that player and give them the win
        for (int i = 0; i < 4; i++) {
            if (wins[i] == max) {
                switch (i) {
                    case 0:
                        return Player.RED;
                    case 1:
                        return Player.BLUE;
                    case 2:
                        return Player.YELLOW;
                    case 3:
                        return Player.GREEN;
                }
            }
        }
        //If the switch statement fails somehow, return null
        return null;
    }

    public int getBattleWins(GameImpl game, Player player) {
        return game.getBattleWinsPerPlayer(player);
    }
}
