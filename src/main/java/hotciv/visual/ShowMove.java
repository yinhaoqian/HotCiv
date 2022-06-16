package hotciv.visual;

import minidraw.standard.*;
import minidraw.framework.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import hotciv.framework.*;
import hotciv.view.*;
import hotciv.stub.*;
import minidraw.standard.handlers.DragTracker;

/** Template code for exercise FRS 36.39.

   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Computer Science Department
     Aarhus University
   
   This source code is provided WITHOUT ANY WARRANTY either 
   expressed or implied. You may study, use, modify, and 
   distribute it for non-commercial purposes. For any 
   commercial use, see http://www.baerbak.com/
 */
public class ShowMove {
  
  public static void main(String[] args) {
    Game game = new StubGame2();

    DrawingEditor editor = 
      new MiniDrawApplication( "Move any unit using the mouse",  
                               new HotCivFactory4(game) );
    editor.open();
    editor.showStatus("Move units to see Game's moveUnit method being called.");

    editor.setTool( new UnitMoveTool(editor, game) );
  }
}

class UnitMoveTool extends AbstractTool implements Tool {
  private Game game;
  private DrawingEditor editor;
  private NullTool nulltool;
  private Figure draggedFigure;
  private Tool dragTracker;
  private Position from;
  public UnitMoveTool(DrawingEditor editor, Game game) {
    super(editor);
    this.editor = editor;
    this.game = game;
    this.nulltool = new NullTool();
    this.draggedFigure = null;
    this.dragTracker = new NullTool();
    this.from = null;
  }
  public void mouseDown(MouseEvent e, int x, int y) {
    Drawing model = editor().drawing();
    model.lock();
    draggedFigure = model.findFigure(e.getX(), e.getY());
    Position temp_from = GfxConstants.getPositionFromXY(x, y);
    if (temp_from.getColumn() < GameConstants.WORLDSIZE && temp_from.getRow() < GameConstants.WORLDSIZE) {
      if (draggedFigure != null && game.getUnitAt(temp_from) != null) {
        dragTracker = new DragTracker(editor(), draggedFigure);
        from = temp_from;
      }
    }
    dragTracker.mouseDown(e, x, y);
  }

  @Override
  public void mouseDrag(MouseEvent e, int x, int y) {
    dragTracker.mouseDrag(e, x, y);
  }

  @Override
  public void mouseUp(MouseEvent e, int x, int y) {
    editor().drawing().unlock();
    Position to = GfxConstants.getPositionFromXY(x, y);
    if (draggedFigure != null && draggedFigure.getClass() == UnitFigure.class && from != null && !from.equals(to)) {
      dragTracker.mouseUp(e, x, y);
      boolean success = game.moveUnit(from, to);
      if (success) {
        editor.showStatus("Successfully Moved A Unit!");
        game.setTileFocus(to);
      } else {
        editor.showStatus("Failed To Moved A Unit!");
        System.out.println( "-- StubGame2 / moveUnit called: "+from+"->"+to );
        game.setTileFocus(from);
      }
    }
    dragTracker = nulltool;
    draggedFigure = null;
  }

  @Override
  public void mouseMove(MouseEvent e, int x, int y) {
    dragTracker.mouseMove(e, x, y);
  }

}
