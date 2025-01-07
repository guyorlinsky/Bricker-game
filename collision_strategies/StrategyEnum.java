package src.collision_strategies;
import java.util.Random;


public enum StrategyEnum {
    REGULAR, EXTRA_BALLS, EXTRA_PADDLE, CAMERA_CHANGE, EXTRA_LIFE, DOUBLE;

    private static final Random random = new Random();

    /**
     * generate Random StrategyNum
     * @return a random strategy
     */
    public static StrategyEnum generateRandomStrategy() {
        StrategyEnum[] strategies = values();
        return strategies[random.nextInt(strategies.length)];
    }

    /**
     * generates Random StrategyNum not including the Double strategy
     * @return StrategyNum not including the Double strategy
     */
    public static StrategyEnum generateRandomStrategyWithOutDouble() {
        StrategyEnum[] strategies = values();
        return strategies[random.nextInt(strategies.length - 1)];
    }
}
