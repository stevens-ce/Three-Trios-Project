package threetrios.strategy;

/**
 * a strategy extending TTStrategyStacks that will always return a CardMove
 * by making the second strategy LastResortStrategy.
 */
public class TTStrategyInfallibleStack extends TTStrategyStacks {

  /**
   * sets the first strategy as the client input, and the second as LastResortStrategy.
   * LastResortStrategy always returns something as long as game is not over.
   *
   * @param strategy TTStrategy
   */
  public TTStrategyInfallibleStack(TTStrategy strategy) {
    super(strategy, new LastResortStrategy());
  }
}
