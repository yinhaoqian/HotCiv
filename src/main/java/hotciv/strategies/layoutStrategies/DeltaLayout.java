package hotciv.strategies.layoutStrategies;

public class DeltaLayout implements LayoutStrategy {


    public String[] generateLayout() {
        String[] layout = new String[]{
                //o - OCEAN UNOCCUPIED       i - OCEAN W/CITY FROM RED PLAYER        I - OCEAN W/CITY FROM BLUE PLAYER
                //f - FORESTS UNOCCUPIED     d - FORESTS W/CITY FROM RED PLAYER      D - FORESTS W/CITY FROM BLUE PLAYER
                //h - HILLS UNOCCUPIED       g - HILLS W/CITY FROM RED PLAYER        G - HILLS W/CITY FROM BLUE PLAYER
                //m - MOUNTAINS UNOCCUPIED   n - MOUNTAINS W/CITY FROM RED PLAYER    N - MOUNTAINS W/CITY FROM BLUE PLAYER
                //. - PLAINS UNOCCUPIED      , - PLAINS W/CITY FROM RED PLAYER       < - PLAINS W/CITY FROM BLUE PLAYER
                "ooo..m.....ooooo",
                "oo.hh....fff..oo",
                "o.....m...ooo..o",
                "o..mmm....oo....",
                "ooo.f,..hh....oo",
                "o.f..f.....hh..o",
                "ooo...oooooooooo",
                "o.....o...h..moo",
                "o.....o..h..<foo",
                ".fff....o.ff....",
                "........ooo.....",
                "o..mmm....oooooo",
                "oo......ff....oo",
                "oooo.........ooo",
                "oo...hh..ooooooo",
                "ooooo.........oo",
        };

        return layout;
    }

}
