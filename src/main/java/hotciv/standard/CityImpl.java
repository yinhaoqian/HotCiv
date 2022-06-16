package hotciv.standard;

import hotciv.framework.City;
import hotciv.framework.Game;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;

public class CityImpl implements City {

    private Player owner;
    private String workforceFocus;
    private String production; //The type of unit being produced
    private int cityPopulation;
    private int cityProduction;

    public CityImpl(Player owner) {
        this.owner = owner;
        this.cityPopulation = 1;
        this.production = GameConstants.ARCHER;
        this.cityProduction = 0;
        this.workforceFocus = "hammer";
    }

    public CityImpl(Player owner, int population) {
        this.owner = owner;
        this.cityPopulation = population;
        this.production = GameConstants.LEGION;
        this.cityProduction = 0;
        this.workforceFocus = "apple";
    }

    public Player getOwner() {
        return this.owner;
    }

    public void setOwner(Player ply) {
        this.owner = ply;
    }

    public void setWorkforceFocus(String workforceFocus) {
        this.workforceFocus = workforceFocus;
    }

    public int getSize() {
        return this.cityPopulation;
    }

    public void changeSize(int delta) {
        this.cityPopulation += delta;
    }

    public int getTreasury() {return this.cityProduction;}

    public void setProduction(String production) {
        this.production = production;
    }

    public String getProduction() {
        return this.production;
    }

    public String getWorkforceFocus() {
        return this.workforceFocus;
    }

    public void setTreasury(int newCityProduction) {
        this.cityProduction = newCityProduction;
    }
}
