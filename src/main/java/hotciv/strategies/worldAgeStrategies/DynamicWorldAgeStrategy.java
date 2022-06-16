package hotciv.strategies.worldAgeStrategies;

public class DynamicWorldAgeStrategy implements WorldAgeStrategy{

    public int calculateWorldAge(int currentAge) {
        if (currentAge >= -4000 && currentAge <= -200) {
            return currentAge + 100;
        }
        else if (currentAge >= 50 && currentAge <=1700) {
            return currentAge + 50;
        }
        else if (currentAge >= 1750 && currentAge <=1875) {
            return currentAge + 25;
        }
        else if (currentAge >= 1900 && currentAge <=1965) {
            return currentAge + 5;
        }
        else if (currentAge >= 1970) {
            return currentAge + 1;
        }
        else if (currentAge == -100) {
            return -1;
        }
        else if (currentAge == -1) {
            return 1;
        }
        else if (currentAge == 1) {
            return 50;
        }
        return -2222;
    }
}
