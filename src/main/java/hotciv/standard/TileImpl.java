package hotciv.standard;

import hotciv.framework.GameConstants;
import hotciv.framework.Tile;

public class TileImpl implements Tile {

    private boolean isPassable;
    private String type;

    public TileImpl(String type) {
        this.type = type;
        setPassable();
    }

    public String getTypeString() {
        return type;
    }

    private void setPassable() {
        this.isPassable = true;
        if (this.type == GameConstants.MOUNTAINS || this.type == GameConstants.OCEANS) {
            this.isPassable = false;
        }
    }

    public boolean isPassable() {
        return this.isPassable;
    }
}
