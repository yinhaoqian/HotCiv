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

/** Template code for exercise FRS 36.44.

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
public class ShowComposition {
  
  public static void main(String[] args) {
    Game game = new StubGame2();

    DrawingEditor editor = 
      new MiniDrawApplication( "Click and/or drag any item to see all game actions",  
                               new HotCivFactory4(game) );
    editor.open();
    editor.showStatus("Click and drag any item to see Game's proper response.");

    editor.setTool( new CompositionTool(editor, game) );
  }
}

class CompositionTool extends NullTool {
  private Game game;
  private DrawingEditor editor;
  private Position from;

  private EndOfTurnTool endTool;
  private ActionTool actionTool;
  private SetFocusTool focusTool;
  private UnitMoveTool moveTool;

  public CompositionTool(DrawingEditor editor, Game game) {
    this.editor = editor;
    this.game = game;
    this.endTool = new EndOfTurnTool(editor, game);
    this.actionTool = new ActionTool(editor, game);
    this.focusTool = new SetFocusTool(editor, game);
    this.moveTool = new UnitMoveTool(editor, game);
    this.from = null;
  }

  public void mouseDown(MouseEvent e, int x, int y) {
    actionTool.mouseDown(e, x, y);
    moveTool.mouseDown(e, x, y);
    from = GfxConstants.getPositionFromXY(x, y);
  }

  @Override
  public void mouseUp(MouseEvent e, int x, int y) {
    moveTool.mouseUp(e, x, y);
    endTool.mouseUp(e, x, y);
    if (from.equals(GfxConstants.getPositionFromXY(x, y))){
      focusTool.mouseDown(e, x, y);
    }

    from = null;
  }
}