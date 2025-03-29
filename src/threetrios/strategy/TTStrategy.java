package threetrios.strategy;

import threetrios.model.ReadOnlyThreeTriosModel;
import threetrios.model.Color;

/**
 * Behavior for a strategy which finds the optimal play in a given game state,
 * criteria dependent on implementation. Assumed game has started.
 */
public interface TTStrategy {
  /**
   * Chooses the "best" move via a strategy to be implemented.
   * While the definition of best move is variable, by default the best card is the one with the
   * highest average attack values.
   *
   * @param model  of current game state
   * @param player strategy is being decided for
   * @return "best" CardMove
   * @throws IllegalStateException if game is already over
   */
  CardMove chooseMove(ReadOnlyThreeTriosModel model, Color player);
}
