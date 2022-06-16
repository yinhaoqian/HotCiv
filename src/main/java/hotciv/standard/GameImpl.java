//Release 5.0

package hotciv.standard;

import hotciv.framework.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GameImpl implements Game {

    //Private member variables://-----------------------------------------------------------------------------
    private Unit[][] units;
    private City[][] cities;
    private Tile[][] tiles;

    private int worldAge;
    private int turns;
    private Player player;
    private FactoryImpl strategy;

    private HashMap<Player, Integer> battleWinsPerPlayer;
    private HashMap<Player, Integer> battleWinsPerPlayerUpTo20;

    private List<GameObserver> observers = new ArrayList<GameObserver>();


    //Constructor://-----------------------------------------------------------------------------
    public GameImpl(FactoryImpl strategy) {

        this.strategy = strategy;

        //Initialize battle wins array
        battleWinsPerPlayer = new HashMap<>();
        battleWinsPerPlayer.put(Player.RED, 0);
        battleWinsPerPlayer.put(Player.BLUE, 0);
        battleWinsPerPlayer.put(Player.YELLOW, 0);
        battleWinsPerPlayer.put(Player.GREEN, 0);
        battleWinsPerPlayerUpTo20 = new HashMap<>(battleWinsPerPlayer);

        //Initializations:
        initializeCities();
        initializeTiles();
        initializeUnits();
        initializeOthers();

    }


    //Private member functions://-----------------------------------------------------------------------------
    private void updateTreasury() {
        for (int i = 0; i < cities.length; i++) {
            for (int j = 0; j < cities[0].length; j++) {
                City thisCity = getCityAt(new Position(i, j));
                if (Objects.nonNull(thisCity)) {
                    int treasury_temp = thisCity.getTreasury();
                    ((CityImpl) (this.cities[i][j])).setTreasury(treasury_temp + 6);//add 6 productions for every city every turn
                    treasury_temp = this.cities[i][j].getTreasury();//update so that added treasury is counted!

                    int cost = ((UnitImpl) strategy.getUnitStatStrategy().getGameUnits().get(thisCity.getProduction())).getCost();

                    if (treasury_temp >= cost) {
                        this.produceUnit(new Position(i, j), thisCity, cost, thisCity.getProduction());
                    }
                }
            }
        }
    }

    private void initializeUnits() {
        //Units
        this.units = new UnitImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        createUnit(2, 0, Player.RED, GameConstants.ARCHER);
        createUnit(3, 2, Player.BLUE, GameConstants.LEGION);
        createUnit(4, 3, Player.RED, GameConstants.SETTLER);
        createUnit(4, 4, Player.BLUE, GameConstants.LEGION);
    }

    private void initializeCities() {
        //Cities
        this.cities = new CityImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
    }

    private void initializeTiles() {
        //Tiles
        String[] layoutMap;
        this.tiles = new TileImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        layoutMap = strategy.getLayoutStrategy().generateLayout().clone();
        //o - OCEAN UNOCCUPIED       i - OCEAN W/CITY FROM RED PLAYER        I - OCEAN W/CITY FROM BLUE PLAYER
        //f - FORESTS UNOCCUPIED     d - FORESTS W/CITY FROM RED PLAYER      D - FORESTS W/CITY FROM BLUE PLAYER
        //h - HILLS UNOCCUPIED       g - HILLS W/CITY FROM RED PLAYER        G - HILLS W/CITY FROM BLUE PLAYER
        //m - MOUNTAINS UNOCCUPIED   n - MOUNTAINS W/CITY FROM RED PLAYER    N - MOUNTAINS W/CITY FROM BLUE PLAYER
        //. - PLAINS UNOCCUPIED      , - PLAINS W/CITY FROM RED PLAYER       < - PLAINS W/CITY FROM BLUE PLAYER
        for (int i = 0; i < GameConstants.WORLDSIZE; i++) {
            String row_layout_iter = layoutMap[i];
            for (int j = 0; j < GameConstants.WORLDSIZE; j++) {
                char tile_layout_iter = row_layout_iter.charAt(j);
                switch (tile_layout_iter) {
                    case 'o'://o - OCEAN UNOCCUPIED
                        createTile(i, j, GameConstants.OCEANS);
                        break;
                    case 'i'://i - OCEAN W/CITY FROM RED PLAYER
                        createTile(i, j, GameConstants.OCEANS);
                        createCity(i, j, Player.RED, 1);
                        break;
                    case 'I'://I - OCEAN W/CITY FROM BLUE PLAYER
                        createTile(i, j, GameConstants.OCEANS);
                        createCity(i, j, Player.BLUE, 1);
                        break;
                    case 'f'://f - FORESTS UNOCCUPIED
                        createTile(i, j, GameConstants.FOREST);
                        break;
                    case 'd'://d - FORESTS W/CITY FROM RED PLAYER
                        createTile(i, j, GameConstants.FOREST);
                        createCity(i, j, Player.RED, 1);
                        break;
                    case 'D'://D - FORESTS W/CITY FROM BLUE PLAYER
                        createTile(i, j, GameConstants.FOREST);
                        createCity(i, j, Player.BLUE, 1);
                        break;
                    case 'h'://h - HILLS UNOCCUPIED
                        createTile(i, j, GameConstants.HILLS);
                        break;
                    case 'g'://g - HILLS W/CITY FROM RED PLAYER
                        createTile(i, j, GameConstants.HILLS);
                        createCity(i, j, Player.RED, 1);
                        break;
                    case 'G'://G - HILLS W/CITY FROM BLUE PLAYER
                        createTile(i, j, GameConstants.HILLS);
                        createCity(i, j, Player.BLUE, 1);
                        break;
                    case 'm'://m - MOUNTAINS UNOCCUPIED
                        createTile(i, j, GameConstants.MOUNTAINS);
                        break;
                    case 'n'://n - MOUNTAINS W/CITY FROM RED PLAYER
                        createTile(i, j, GameConstants.MOUNTAINS);
                        createCity(i, j, Player.RED, 1);
                        break;
                    case 'N'://N - MOUNTAINS W/CITY FROM BLUE PLAYER
                        createTile(i, j, GameConstants.MOUNTAINS);
                        createCity(i, j, Player.BLUE, 1);
                        break;
                    case '.'://. - PLAINS UNOCCUPIED
                        createTile(i, j, GameConstants.PLAINS);
                        break;
                    case ','://, - PLAINS W/CITY FROM RED PLAYER
                        createTile(i, j, GameConstants.PLAINS);
                        createCity(i, j, Player.RED, 1);
                        break;
                    case '<'://< - PLAINS W/CITY FROM BLUE PLAYER
                        createTile(i, j, GameConstants.PLAINS);
                        createCity(i, j, Player.BLUE, 1);
                        break;
                    default:
                        throw new ArithmeticException();
                }
            }
        }
    }

    private void initializeOthers() {
        //Miscellaneous
        this.worldAge = -4000;
        this.player = Player.RED;
    }

    private boolean[] getTilesAroundUnitsAvailability(Position p, String unit) {
        boolean[] tileValid = new boolean[9];
        int r = p.getRow();
        int c = p.getColumn();
        boolean canPassUnpassable = ((UnitImpl) strategy.getUnitStatStrategy().getGameUnits().get(unit)).canHover();
        tileValid[0] = true;
        tileValid[1] = r != 0 && (((TileImpl) getTileAt(new Position(r - 1, c))).isPassable() || canPassUnpassable);
        tileValid[2] = r != 0 && c != 15 && (((TileImpl) getTileAt(new Position(r - 1, c + 1))).isPassable() || canPassUnpassable);
        tileValid[3] = c != 15 && (((TileImpl) getTileAt(new Position(r, c + 1))).isPassable() || canPassUnpassable);
        tileValid[4] = c != 15 && r != 15 && (((TileImpl) getTileAt(new Position(r + 1, c + 1))).isPassable() || canPassUnpassable);
        tileValid[5] = r != 15 && (((TileImpl) getTileAt(new Position(r + 1, c))).isPassable() || canPassUnpassable);
        tileValid[6] = c != 0 && r != 15 && (((TileImpl) getTileAt(new Position(r + 1, c - 1))).isPassable() || canPassUnpassable);
        tileValid[7] = c != 0 && (((TileImpl) getTileAt(new Position(r, c - 1))).isPassable() || canPassUnpassable);
        tileValid[8] = c != 0 && r != 0 && (((TileImpl) getTileAt(new Position(r - 1, c - 1))).isPassable() || canPassUnpassable);
        return tileValid;
    }

    private boolean placeUnits(Position p, boolean[] tileValid, City cty, int cost) {
        int r = p.getRow();
        int c = p.getColumn();
        Position[] possiblePositions = new Position[9];
        possiblePositions[0] = new Position(r, c);//city-center
        possiblePositions[1] = new Position(r - 1, c);//north
        possiblePositions[2] = new Position(r - 1, c + 1);//north-east
        possiblePositions[3] = new Position(r, c + 1);//east
        possiblePositions[4] = new Position(r + 1, c + 1);//south-east
        possiblePositions[5] = new Position(r + 1, c);//south
        possiblePositions[6] = new Position(r + 1, c - 1);//south-west
        possiblePositions[7] = new Position(r, c - 1);//west
        possiblePositions[8] = new Position(r - 1, c - 1);//north-west
        for (int i = 0; i < 9; i++) {
            if (tileValid[i]) {
                if (Objects.isNull(getUnitAt(possiblePositions[i]))) {
                    createUnit(possiblePositions[i].getRow(), possiblePositions[i].getColumn(), cty.getOwner(), cty.getProduction());
                    ((CityImpl) cty).setTreasury(((cty.getTreasury()) - cost));
                    return true;
                }
            }
        }
        return false;
    }

    private boolean produceUnit(Position p, City c, int cost, String unit) {
        boolean[] tileValid = getTilesAroundUnitsAvailability(p, unit);
        return placeUnits(p, tileValid, c, cost);
    }

    private void incrementBattleWinsForPlayer(Player p) {
        int wins = battleWinsPerPlayer.get(p);
        wins++;
        battleWinsPerPlayer.put(p, wins);
    }


    //Public member functions://-----------------------------------------------------------------------------
    public Tile getTileAt(Position p) {
        return this.tiles[p.getRow()][p.getColumn()];
    }

    public Unit getUnitAt(Position p) {
        return this.units[p.getRow()][p.getColumn()];
    }

    public City getCityAt(Position p) {
        return this.cities[p.getRow()][p.getColumn()];
    }

    public Player getPlayerInTurn() {
        return this.player;
    }

    public Player getWinner() {
        return strategy.getWinningStrategy().getWinner(this);
    }

    public int getAge() {
        return this.worldAge;
    }

    public int getTurns() {
        return this.turns;
    }

    public boolean moveUnit(Position from, Position to) {

        //The unit we are trying to move doesn't exist or is not owned by current player
        if (units[from.getRow()][from.getColumn()] == null) {
            return false;
        }

        //Check if the unit can go on the tile type
        boolean canPassUnpassable = ((UnitImpl) getUnitAt(from)).canHover();
        if (!(((TileImpl) getTileAt(to)).isPassable() || canPassUnpassable)) {
            return false;
        }

        //Check that position is valid within game boundaries
        if (to.getColumn() < 0 || to.getColumn() >= GameConstants.WORLDSIZE || from.getColumn() < 0 || from.getColumn() >= GameConstants.WORLDSIZE || to.getRow() < 0 || to.getRow() >= GameConstants.WORLDSIZE || from.getRow() < 0 || from.getRow() >= GameConstants.WORLDSIZE) {
            return false;
        }

        //Check that from -> to is not more (or less) than 1 tile away
        if (from.getColumn() - to.getColumn() != 1 && from.getColumn() - to.getColumn() != -1) {//Columns haven't changed by 1
            if (from.getRow() - to.getRow() != 1 && from.getRow() - to.getRow() != -1) {//Rows haven't changed by 1
                return false;
            }
            else if (from.getColumn() - to.getColumn() != 0) {//Rows changed > 1
                return false;
            }
        }
        else {//Columns have changed by 1
            if (from.getRow() - to.getRow() != 1 && from.getRow() - to.getRow() != -1 && from.getRow() - to.getRow() != 0) {//Rows haven't changed by 1 or 0
                return false;
            }
        }

        //Check that unit at From is owned by current player
        if (units[from.getRow()][from.getColumn()].getOwner() != this.player) {
            return false;
        }

        //Check if unit has any moves left
        if (!((UnitImpl) units[from.getRow()][from.getColumn()]).move(this.turns)) {
            return false;
        }

        //There is another unit in the space we are moving to
        if (units[to.getRow()][to.getColumn()] != null) {
            //If the other unit is from the same team
            if (units[from.getRow()][from.getColumn()].getOwner() == units[to.getRow()][to.getColumn()].getOwner()) {
                return false;
                //If the other unit is from an opponent's team
            } else {
                //Invoke the unitActionStrategy for whatever has to happen when a unit moves
                strategy.getUnitActionStrategy().onMoveUnit(from, this);
                attack(from, to);
            }
        }
        else {
            //Invoke the unitActionStrategy for whatever has to happen when a unit moves
            strategy.getUnitActionStrategy().onMoveUnit(from, this);
        }
        if (cities[to.getRow()][to.getColumn()] != null && !((UnitImpl) units[from.getRow()][from.getColumn()]).canHover()) {
            cities[to.getRow()][to.getColumn()] = new CityImpl(units[from.getRow()][from.getColumn()].getOwner());
        }
        //Move the unit into the space
        units[to.getRow()][to.getColumn()] = units[from.getRow()][from.getColumn()];
        removeUnit(from);
        updateObservers(to);
        return true;
    }

    private boolean attack(Position from, Position to) {
        //Return true if attacker wins, false otherwise
        Player winner = strategy.getAttackStrategy().getAttackWinner(from, to, this);
        if (units[from.getRow()][from.getColumn()].getOwner() == winner) {
            //Attacker gets a win
            incrementBattleWinsForPlayer(winner);
            //If the attacker is moving onto an enemy city
            if (cities[to.getRow()][to.getColumn()] != null) {
                cities[to.getRow()][to.getColumn()] = new CityImpl(winner);
            }
            return true;
        }
        //Defender wins, kill the attacker and return false
        removeUnit(from);
        return false;
    }

    public void endOfTurn() {
        if (this.player == Player.RED) {
            this.player = Player.BLUE;
        } else {
            this.player = Player.RED;
        }
        this.worldAge = strategy.getWorldAgeStrategy().calculateWorldAge(this.worldAge);
        this.turns += 1;
        updateTreasury();
        if (this.turns == 21) {
            this.battleWinsPerPlayerUpTo20 = this.battleWinsPerPlayer;
        }
        this.updateObservers(null);
    }

    public void changeWorkForceFocusInCityAt(Position p, String balance) {
    }

    public void changeProductionInCityAt(Position p, String unitType) {
        ((CityImpl) (this.cities[p.getRow()][p.getColumn()])).setProduction(unitType);
    }

    public void performUnitActionAt(Position p) {
        strategy.getUnitActionStrategy().performAction(p, this);
    }

    public void removeUnit(Position p) {
        units[p.getRow()][p.getColumn()] = null;
        this.updateObservers(p);
    }

    public void createTile(int r, int c, String type) {
        tiles[r][c] = new TileImpl(type);
        this.updateObservers(new Position(r, c));
    }

    public void createUnit(int r, int c, Player player, String type) {
        UnitImpl stats = ((UnitImpl) strategy.getUnitStatStrategy().getGameUnits().get(type));
        units[r][c] = new UnitImpl(player, type, stats.getAttackingStrength(), stats.getDefensiveStrength(), stats.getMoves(), stats.getCost(), stats.canHover());
        this.updateObservers(new Position(r, c));
    }

    public void createCity(int r, int c, Player player, int population) {
        if (population == 0) {
            cities[r][c] = null;
        } else {
            cities[r][c] = new CityImpl(player, population);
        }
        this.updateObservers(new Position(r, c));
    }

    public int getBattleWinsPerPlayer(Player player) {
        return this.battleWinsPerPlayer.get(player);
    }

    public int getBattleWinsPerPlayerUpTo20(Player player) {
        return this.battleWinsPerPlayerUpTo20.get(player);
    }



    //Observer Functions://-----------------------------------------------------------------------------

    public GameObserver getObserver(int index) {
        return this.observers.get(index);
    }

    private void updateObservers(Position p){
        if (p == null) {
            for (int i = 0; i < this.observers.size(); i++) {
                this.observers.get(i).turnEnds(this.player, this.worldAge);
            }
        }
        else{
            for (int i = 0; i < this.observers.size(); i++) {
                this.observers.get(i).worldChangedAt(p);
            }
        }
    }

    public void setTileFocus(Position p){
        for (int i = 0; i < this.observers.size(); i++) {
            this.observers.get(i).tileFocusChangedAt(p);
        }
    }

    public void addObserver(GameObserver observer){
        this.observers.add(observer);
    }
}