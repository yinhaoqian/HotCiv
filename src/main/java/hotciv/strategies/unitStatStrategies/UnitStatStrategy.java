package hotciv.strategies.unitStatStrategies;

import hotciv.framework.Unit;
import hotciv.standard.UnitImpl;

import java.util.HashMap;

public interface UnitStatStrategy {
    public HashMap<String, Unit> getGameUnits();

}
