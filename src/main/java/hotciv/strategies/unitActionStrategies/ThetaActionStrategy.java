package hotciv.strategies.unitActionStrategies;

import hotciv.framework.*;
import hotciv.standard.CityImpl;
import hotciv.standard.GameImpl;
import hotciv.standard.UnitImpl;

public class ThetaActionStrategy implements UnitActionStrategy{
    @Override
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
        else if (game.getUnitAt(p).getTypeString().equals("ufo")) {
            //If UFO
            if (!game.getCityAt(p).getOwner().equals(game.getUnitAt(p).getOwner())) {
                //If over enemy city, abduct 1 citizen
                ((CityImpl) game.getCityAt(p)).changeSize(-1);
                if (game.getCityAt(p).getSize() == 0) {
                    //If population now == 0, destroy city
                    ((GameImpl) game).createCity(p.getRow(), p.getColumn(), null, 0);
                    if (game.getTileAt(p).getTypeString() == GameConstants.FOREST) {
                        //If city was in a forest, change tile to be plains
                        ((GameImpl) game).createTile(p.getRow(), p.getColumn(), GameConstants.PLAINS);
                    }
                }
            }
        }
    }

    @Override
    public void onMoveUnit(Position p, Game game) {
        Unit unit = game.getUnitAt(p);
        if (unit.getTypeString().equals(GameConstants.ARCHER)) {
            if (((UnitImpl) unit).getFortified()) {
                ((UnitImpl) unit).setFortified(false);
                ((UnitImpl) unit).setDefensiveStrength(((UnitImpl) unit).getDefensiveStrength() / 2);
            }
        }
    }
}
