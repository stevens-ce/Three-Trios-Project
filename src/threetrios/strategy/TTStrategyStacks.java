package threetrios.strategy;

import threetrios.model.ReadOnlyThreeTriosModel;
import threetrios.model.Color;

/**
 * allows for strategies to be stacked, thus creating a higher order strategy.
 * if the first given strategy returns null, tries again with the second strategy.
 */
public class TTStrategyStacks implements TTStrategy {
  private final TTStrategy first;
  private final TTStrategy second;

  /**
   * sets the two strategies that will be used to conclude on a CardMove.
   * strat1 will be tried first, then strat2.
   *
   * @param strat1 the first strategy to be run
   */
  public TTStrategyStacks(TTStrategy strat1, TTStrategy strat2) {
    if (strat1 == null || strat2 == null) {
      throw new IllegalArgumentException("invalid strategies.");
    }
    this.first = strat1;
    this.second = strat2;
  }

  @Override
  public CardMove chooseMove(ReadOnlyThreeTriosModel model, Color player) {
    if (model.isGameOver()) {
      throw new IllegalStateException("no moves when game is over.");
    }
    CardMove cMove = this.first.chooseMove(model, player);
    if (cMove != null) {
      return cMove;
    } else {
      return this.second.chooseMove(model, player);
    }
  }

}
