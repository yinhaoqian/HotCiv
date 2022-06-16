package hotciv.standard;

import hotciv.strategies.attackStrategies.AttackStrategy;
import hotciv.strategies.layoutStrategies.LayoutStrategy;
import hotciv.strategies.unitActionStrategies.UnitActionStrategy;
import hotciv.strategies.unitStatStrategies.UnitStatStrategy;
import hotciv.strategies.winningStrategies.WinningStrategy;
import hotciv.strategies.worldAgeStrategies.WorldAgeStrategy;

public class FactoryImpl {
    private UnitActionStrategy unitActionStrategy;
    private WinningStrategy winningStrategy;
    private WorldAgeStrategy worldAgeStrategy;
    private LayoutStrategy layoutStrategy;
    public AttackStrategy attackStrategy; //TODO Change to private
    private UnitStatStrategy unitStatStrategy;

    public FactoryImpl(UnitActionStrategy u, WinningStrategy w, WorldAgeStrategy o, LayoutStrategy l, AttackStrategy a, UnitStatStrategy us) {
        this.unitActionStrategy = u;
        this.winningStrategy = w;
        this.worldAgeStrategy = o;
        this.layoutStrategy = l;
        this.attackStrategy = a;
        this.unitStatStrategy = us;
    }

    public void setUnitActionStrategy(UnitActionStrategy unitActionStrategy) {this.unitActionStrategy = unitActionStrategy;}

    public void setUnitStatStrategy(UnitStatStrategy unitStatStrategy) {this.unitStatStrategy = unitStatStrategy;}

    public void setLayoutStrategy(LayoutStrategy layoutStrategy) {
        this.layoutStrategy = layoutStrategy;
    }

    public void setWinningStrategy(WinningStrategy winningStrategy) {
        this.winningStrategy = winningStrategy;
    }

    public void setWorldAgeStrategy(WorldAgeStrategy worldAgeStrategy) {
        this.worldAgeStrategy = worldAgeStrategy;
    }

    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public LayoutStrategy getLayoutStrategy() {
        if (layoutStrategy == null) System.out.println("Warning: layoutStrategy object not found!\n");
        return layoutStrategy;
    }

    public UnitActionStrategy getUnitActionStrategy() {
        if (unitActionStrategy == null) System.out.println("Warning: unitActionStrategy object not found!\n");
        return unitActionStrategy;
    }

    public WinningStrategy getWinningStrategy() {
        if (winningStrategy == null) System.out.println("Warning: winningStrategy object not found!\n");
        return winningStrategy;
    }

    public WorldAgeStrategy getWorldAgeStrategy() {
        if (worldAgeStrategy == null) System.out.println("Warning: worldAgeStrategy object not found!\n");
        return worldAgeStrategy;
    }

    public AttackStrategy getAttackStrategy(){
        if (attackStrategy == null) System.out.println("Warning: attackStrategy object not found!\n");
        return attackStrategy;
    }

    public UnitStatStrategy getUnitStatStrategy() {
        if (unitStatStrategy == null) System.out.println("Warning: unitStatStrategy object not found!\n");
        return unitStatStrategy;
    }
}
