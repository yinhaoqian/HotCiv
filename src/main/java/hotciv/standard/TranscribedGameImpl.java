package hotciv.standard;

import hotciv.framework.*;

public class TranscribedGameImpl implements Game {

    public GameImpl game;

    private String getPlayerName(Player pl){
        switch(pl){
            case RED: return "RED";
            case BLUE: return "BLUE";
            case GREEN: return "GREEN";
            case YELLOW: return "YELLOW";
            default: return "UNKNOWN";
        }
    }

    private String pos2Str(Position p){
        return "(" + p.getRow() + "," + p.getColumn() + ")";
    }

    public TranscribedGameImpl(FactoryImpl strategy){
        System.out.println("Game begins");
        game = new GameImpl(strategy);
    }

    public Tile getTileAt(Position p) {
        return game.getTileAt(p);
    }

    public Unit getUnitAt(Position p) {
        return game.getUnitAt(p);
    }

    public City getCityAt(Position p) {
        return game.getCityAt(p);
    }

    public Player getPlayerInTurn() {
        return game.getPlayerInTurn();
    }

    public Player getWinner() {
        Player winner = game.getWinner();
        System.out.println(getPlayerName(winner) + " wins the game!");
        return winner;
    }

    public int getAge() {
        return game.getAge();
    }

    public boolean moveUnit(Position from, Position to) {
        Unit u = getUnitAt(from);
        System.out.println(getPlayerName(u.getOwner()) + " moves " + u.getTypeString() + " from " + pos2Str(from) + " to " + pos2Str(to) + ".");
        return game.moveUnit(from, to);
    }

    public void endOfTurn() {
        System.out.println(getPlayerName(game.getPlayerInTurn()) + " ends turn.");
        game.endOfTurn();
    }

    public void changeWorkForceFocusInCityAt(Position p, String balance) {
        System.out.println(getPlayerName(game.getPlayerInTurn()) + " changes work force focus in city at " + pos2Str(p) + " to " + balance);
        game.changeWorkForceFocusInCityAt(p, balance);
    }

    public void changeProductionInCityAt(Position p, String unitType) {
        System.out.println(getPlayerName(game.getPlayerInTurn()) + " changes production in city at " + pos2Str(p) + " to " + unitType);
        game.changeProductionInCityAt(p, unitType);
    }

    public void performUnitActionAt(Position p) {
        System.out.println(getPlayerName(game.getPlayerInTurn()) + " performs unit action of " + game.getUnitAt(p).getTypeString() + " at " + pos2Str(p));
        game.performUnitActionAt(p);
    }

    public void setTileFocus(Position position){
        game.setTileFocus(position);
    }

    public void addObserver(GameObserver observer){
        game.addObserver(observer);
    }
}
