package hotciv.strategies.layoutStrategies;

public class AlphaLayout implements LayoutStrategy {
    public String[] generateLayout() {
        String layout[] = {
                //o - OCEAN UNOCCUPIED       i - OCEAN W/CITY FROM RED PLAYER        I - OCEAN W/CITY FROM BLUE PLAYER
                //f - FORESTS UNOCCUPIED     d - FORESTS W/CITY FROM RED PLAYER      D - FORESTS W/CITY FROM BLUE PLAYER
                //h - HILLS UNOCCUPIED       g - HILLS W/CITY FROM RED PLAYER        G - HILLS W/CITY FROM BLUE PLAYER
                //m - MOUNTAINS UNOCCUPIED   n - MOUNTAINS W/CITY FROM RED PLAYER    N - MOUNTAINS W/CITY FROM BLUE PLAYER
                //. - PLAINS UNOCCUPIED      , - PLAINS W/CITY FROM RED PLAYER       < - PLAINS W/CITY FROM BLUE PLAYER
                ".h..............",
                "o,..............",
                "..m.............",
                "................",
                ".<..............",
                "................",
                "................",
                "................",
                "................",
                "................",
                "................",
                "................",
                "................",
                "................",
                "................",
                "................",
        };
     return layout;
    }


}
