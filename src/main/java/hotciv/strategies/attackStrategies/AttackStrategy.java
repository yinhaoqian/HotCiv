package hotciv.strategies.attackStrategies;
import hotciv.framework.*;

public interface AttackStrategy {
   public Player getAttackWinner(Position attacking, Position defending, Game game);
}
