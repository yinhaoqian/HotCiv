package hotciv.strategies.unitActionStrategies;

import hotciv.framework.Game;
import hotciv.framework.GameConstants;
import hotciv.framework.Unit;
import hotciv.framework.Position;
import hotciv.standard.GameImpl;
import hotciv.standard.UnitImpl;

public class GammaMoveStrategy implements UnitActionStrategy {

    public void onMoveUnit(Position p, Game game) {
        Unit unit = game.getUnitAt(p);
        if (unit.getTypeString().equals(GameConstants.ARCHER)) {
            if (((UnitImpl) unit).getFortified()) {
                ((UnitImpl) unit).setFortified(false);
                ((UnitImpl) unit).setDefensiveStrength(((UnitImpl) unit).getDefensiveStrength() / 2);
            }
        }
    }

    public void performAction(Position p, Game game) {
        Unit unit = game.getUnitAt(p);
        if (unit.getTypeString().equals(GameConstants.ARCHER)) {
            if (((UnitImpl) unit).getFortified()) {
                ((UnitImpl) unit).setFortified(false);
                ((UnitImpl) unit).setDefensiveStrength(((UnitImpl) unit).getDefensiveStrength() / 2);
            } else {
                ((UnitImpl) unit).setFortified(true);
                ((UnitImpl) unit).setDefensiveStrength(((UnitImpl) unit).getDefensiveStrength() * 2);
            }
        } else if (unit.getTypeString().equals(GameConstants.SETTLER)) {
            ((GameImpl) game).createCity(p.getRow(),p.getColumn(), unit.getOwner(), 1);
            ((GameImpl) game).removeUnit(p);
        }
    }
}
