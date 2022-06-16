package hotciv.strategies.attackStrategies;

import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.Game;

public class AttackerAlwaysWinsAttackStrategy implements AttackStrategy{
    public Player getAttackWinner(Position attacking, Position defending, Game game) {
        return game.getUnitAt(attacking).getOwner();
    }
}
