package hotciv.strategies.unitStatStrategies;

import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Unit;
import hotciv.standard.UnitImpl;

import java.util.HashMap;

public class Original3AndUFOStatStrategy implements UnitStatStrategy{

    private HashMap<String, Unit> units;

    public Original3AndUFOStatStrategy() {
        units = new HashMap<>();
        units.put("UNSPECIFIED_UNIT", new UnitImpl(Player.GREEN, "UNSPECIFIED_UNIT", 0, 0, 0, 10000, false));
        units.put(GameConstants.ARCHER, new UnitImpl(Player.GREEN, GameConstants.ARCHER, 2, 3, 1, 10, false));
        units.put(GameConstants.LEGION, new UnitImpl(Player.GREEN, GameConstants.LEGION, 4, 2, 1, 15, false));
        units.put(GameConstants.SETTLER, new UnitImpl(Player.GREEN, GameConstants.SETTLER, 2, 3, 1, 30, false));
        units.put("ufo", new UnitImpl(Player.GREEN, "ufo", 1, 8, 2, 60, true));
    }

    public HashMap<String, Unit> getGameUnits() {
        return this.units;
    }
}
