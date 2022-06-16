package hotciv.strategies.unitActionStrategies;

import hotciv.framework.*;

public interface UnitActionStrategy {
    public void performAction(Position p, Game game);
    public void onMoveUnit(Position p, Game game);
}
