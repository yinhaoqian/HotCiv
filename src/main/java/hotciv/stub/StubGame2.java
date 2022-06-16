package hotciv.stub;

import hotciv.framework.*;
import hotciv.standard.CityImpl;
import hotciv.standard.UnitImpl;
import hotciv.strategies.layoutStrategies.DeltaLayout;
import hotciv.strategies.unitActionStrategies.GammaMoveStrategy;

import java.util.*;

/** Test stub for game for visual testing of
 * minidraw based graphics.
 *
 * SWEA support code.
 *
   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Department of Computer Science
     Aarhus University
   
   Please visit http://www.baerbak.com/ for further information.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

public class StubGame2 implements Game {

  // === Unit handling ===
  private Position pos_archer_red;
  private Position pos_legion_blue;
  private Position pos_settler_red;
  private Position pos_ufo_red;

  private int age;

  private Unit red_archer;
  private Unit red_settler;
  private Unit blue_legion;
  private Unit red_ufo;

  private City red_city;
  private City blue_city;
  protected Map<Position,City> cities;

  private Player inTurn;

  protected Map<Position,Tile> world;

  protected GameObserver gameObserver;

  public Unit getUnitAt(Position p) {
    if ( p.equals(pos_archer_red) ) {
      return red_archer;
    }
    if ( p.equals(pos_settler_red) ) {
      return red_settler;
    }
    if ( p.equals(pos_legion_blue) ) {
      return blue_legion;
    }
    if ( p.equals(pos_ufo_red) ) {
      return red_ufo;
    }
    return null;
  }

  public boolean moveUnit( Position from, Position to ) {

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

    if (getUnitAt(from).getOwner() != getPlayerInTurn()){
      return false;
    }

    //Check if unit has any moves left
    if (getUnitAt(from).getMoveCount() < 1) {
      return false;
    }

    System.out.println( "-- StubGame2 / moveUnit called: "+from+"->"+to );
    if ( from.equals(pos_archer_red) ) {
      pos_archer_red = to;
    }
    else if ( from.equals(pos_settler_red) ) {
      pos_settler_red = to;
    }
    else if ( from.equals(pos_ufo_red) ) {
      pos_ufo_red = to;
    }
    else if ( from.equals(pos_legion_blue) ) {
      pos_legion_blue = to;
    }
    // notify our observer(s) about the changes on the tiles
    gameObserver.worldChangedAt(from);
    gameObserver.worldChangedAt(to);
    ((StubUnit) getUnitAt(to)).move();
    return true; 
  }

  // === Turn handling ===
  public void endOfTurn() {
    System.out.println( "-- StubGame2 / endOfTurn called." );
    inTurn = (getPlayerInTurn() == Player.RED ?
              Player.BLUE : 
              Player.RED );
    // no age increments
    age += 1000;
    gameObserver.turnEnds(inTurn, age);
  }
  public Player getPlayerInTurn() { return inTurn; }
  

  // === Observer handling ===
  // observer list is only a single one...
  public void addObserver(GameObserver observer) {
    gameObserver = observer;
  } 


  public StubGame2() { 
    defineWorld(1); 
    // AlphaCiv configuration
    pos_archer_red = new Position( 2, 0);
    pos_legion_blue = new Position( 3, 2);
    pos_settler_red = new Position( 4, 3);
    pos_ufo_red = new Position( 6, 4);

    red_archer = new StubUnit( GameConstants.ARCHER, Player.RED );
    red_settler = new StubUnit( GameConstants.SETTLER, Player.RED );
    red_ufo = new StubUnit( ThetaConstants.UFO, Player.RED );
    blue_legion = new StubUnit( GameConstants.LEGION, Player.BLUE );

    red_city = new StubCity(Player.RED, 2);
    blue_city = new StubCity(Player.BLUE, 4);

    cities.put(new Position(3, 2), red_city);
    cities.put(new Position(7, 3), blue_city);

    inTurn = Player.RED;

    age = -4000;
  }

  // A simple implementation to draw the map of DeltaCiv
  public Tile getTileAt( Position p ) { return world.get(p); }


  /** define the world.
   * @param worldType 1 gives one layout while all other
   * values provide a second world layout.
   */
  protected void defineWorld(int worldType) {
    world = new HashMap<Position,Tile>();
    cities = new HashMap<Position, City>();
    for ( int r = 0; r < GameConstants.WORLDSIZE; r++ ) {
      for ( int c = 0; c < GameConstants.WORLDSIZE; c++ ) {
        Position p = new Position(r,c);
        world.put( p, new StubTile(GameConstants.PLAINS));
      }
    }
  }

  public City getCityAt( Position p ) { return cities.get(p); }
  public Player getWinner() { return null; }
  public int getAge() { return age; }
  public void changeWorkForceFocusInCityAt( Position p, String balance ) {}
  public void changeProductionInCityAt( Position p, String unitType ) {}
  public void performUnitActionAt( Position p ) {
    if ( p.equals(pos_archer_red) && getPlayerInTurn() == Player.RED) {
      pos_archer_red = new Position(-5, -5);
    }
    else if ( p.equals(pos_settler_red)  && getPlayerInTurn() == Player.RED) {
      pos_settler_red = new Position(-4, -5);
    }
    else if ( p.equals(pos_ufo_red)  && getPlayerInTurn() == Player.RED) {
      pos_ufo_red = new Position(-3, -5);
    }
    else if ( p.equals(pos_legion_blue)  && getPlayerInTurn() == Player.BLUE) {
      pos_legion_blue = new Position(-2, -5);
    }
    gameObserver.worldChangedAt(p);
  }

  public void setTileFocus(Position position) {
    System.out.println("-- StubGame2 / setTileFocus called.");
    gameObserver.tileFocusChangedAt(position);
  }

}

class StubUnit implements  Unit {
  private String type;
  private Player owner;
  private int moves;
  public StubUnit(String type, Player owner) {
    this.type = type;
    this.owner = owner;
    this.moves = 1;
  }
  public void move(){moves -= 1;}
  public String getTypeString() { return type; }
  public Player getOwner() { return owner; }
  public int getMoveCount() { return moves; }
  public int getDefensiveStrength() { return 0; }
  public int getAttackingStrength() { return 0; }
}

class StubCity implements City {
  private Player owner;
  private int size;
  private int treasury;
  private String unitProduction;
  private String focus;
  public StubCity(Player owner, int size) {
    this.size = size;
    this.owner = owner;
    this.treasury = 0;
    this.unitProduction = GameConstants.LEGION;
    this.focus = "apple";
  }
  public Player getOwner() { return owner; }
  public int getSize() { return size;}
  public int getTreasury() {return treasury;}
  public String getProduction() {return unitProduction;}
  public String getWorkforceFocus() {return focus;}
}