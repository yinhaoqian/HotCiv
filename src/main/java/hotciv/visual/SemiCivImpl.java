package hotciv.visual;
import hotciv.standard.FactoryImpl;
import hotciv.standard.GameImpl;
import hotciv.strategies.attackStrategies.*;
import hotciv.strategies.layoutStrategies.*;
import hotciv.strategies.unitActionStrategies.*;
import hotciv.strategies.unitStatStrategies.*;
import hotciv.strategies.winningStrategies.*;
import hotciv.strategies.worldAgeStrategies.*;
import hotciv.framework.*;
import minidraw.framework.DrawingEditor;
import minidraw.standard.MiniDrawApplication;

public class SemiCivImpl {

    public static void main(String[] args) {
        FactoryImpl semiFactory = new FactoryImpl(
                new GammaMoveStrategy(),
                new WinThreeBattlesWinningStrategy(),
                new DynamicWorldAgeStrategy(),
                new DeltaLayout(),
                new CombinedStrengthAttackStrategy(),
                new Original3UnitStatStrategy()
        );
        Game game = new GameImpl(semiFactory);

        DrawingEditor editor =
                new MiniDrawApplication( "HotCiv Variant: SemiCiv",
                        new HotCivFactory4(game) );
        editor.open();
        editor.showStatus("Let's Start Playing SemiCiv!");

        editor.setTool( new CompositionTool(editor, game) );
    }
}
