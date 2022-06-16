package hotciv.strategies.attackStrategies;

import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.utilities.Utility2;
import hotciv.framework.Game;
import java.util.Random;

public class CombinedStrengthAttackStrategy implements AttackStrategy{

    public Player getAttackWinner(Position attacking, Position defending, Game game) {

        //Calculate combined attacking strength
        int A = game.getUnitAt(attacking).getAttackingStrength();
        A += Utility2.getFriendlySupport(game, attacking, game.getUnitAt(attacking).getOwner());
        A *= Utility2.getTerrainFactor(game, attacking);

        //Calculate combined defending strength
        int D = game.getUnitAt(defending).getDefensiveStrength();
        D += Utility2.getFriendlySupport(game, defending, game.getUnitAt(defending).getOwner());
        D *= Utility2.getTerrainFactor(game, defending);

        //Roll the dice, d1 and d2
        Random dice = new Random();
        int d1 = dice.nextInt(6) + 1;
        int d2 = dice.nextInt(6) + 1;

        if(A*d1 > D*d1){
            return game.getUnitAt(attacking).getOwner();
        }else{
            return game.getUnitAt(defending).getOwner();
        }
    }
}
