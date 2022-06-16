package hotciv.strategies.worldAgeStrategies;

public class Add100WorldAgeStrategy implements WorldAgeStrategy {

    public int calculateWorldAge(int currentAge) {
        return currentAge + 100;
    }
}
