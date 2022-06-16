package hotciv.standard;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Unit;

public class UnitImpl implements Unit {

    private String type;
    private Player owner;
    private boolean fortified;
    private int attackingStrength;
    private int defensiveStrength;
    private int moveCount;
    private int moves;
    private int lastMoveTurn;
    private int cost;
    private boolean canHover;

    public UnitImpl(Player owner, String type, int attackingStrength, int defensiveStrength, int moves, int cost, boolean canHover) {
        this.type = type;
        this.owner = owner;
        this.moveCount = 0;
        this.lastMoveTurn = 0;
        this.attackingStrength = attackingStrength;
        this.defensiveStrength = defensiveStrength;
        this.moves = moves;
        this.cost = cost;
        this.canHover = canHover;
    }

    public String getTypeString() {
        return this.type;
    }

    public Player getOwner() {
        return this.owner;
    }

    public void setOwner(Player newOwner) {
        this.owner = newOwner;
    }

    public boolean canHover() {
        return this.canHover;
    }

    public int getMoves() { return this.moves; }

    public int getCost() { return this.cost; }

    public int getMoveCount() { return this.moves - this.moveCount; }

    public boolean move(int turn) {
        if (turn == this.lastMoveTurn) {
            if (moveCount < this.moves) {
                this.moveCount++;
                return true;
            }
            return false;
        }
        this.moveCount = 1;
        this.lastMoveTurn = turn;
        return true;
    }

    public int getDefensiveStrength() { return this.defensiveStrength; }
    public void setDefensiveStrength(int strength){this.defensiveStrength = strength;}

    public int getAttackingStrength() { return this.attackingStrength; }

    public boolean getFortified(){return this.fortified;}
    public void setFortified(boolean f){this.fortified = f;}

}
