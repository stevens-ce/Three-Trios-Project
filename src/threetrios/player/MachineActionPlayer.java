package threetrios.player;

import java.util.List;

import threetrios.model.Card;
import threetrios.model.ReadOnlyThreeTriosModel;
import threetrios.model.Color;

import threetrios.strategy.CardMove;
import threetrios.strategy.TTStrategy;

import threetrios.controller.Feature;

/**
 * Impl of ActionPlayer for when player is machine, i.e. generates all moves using given strategy.
 */
public class MachineActionPlayer implements ActionPlayer {
  private final ReadOnlyThreeTriosModel observableModel;
  private final TTStrategy strategy;
  private Feature features;
  private final Color color;

  /**
   * Sets the machine player to given Strategy.
   *
   * @param observableModel with only observations.
   * @param strategy        that decides moves.
   * @param color           of player.
   * @throws IllegalArgumentException if either param is null
   */
  public MachineActionPlayer(ReadOnlyThreeTriosModel observableModel,
                             TTStrategy strategy, Color color) throws IllegalArgumentException {
    if (strategy != null && observableModel != null) {
      this.strategy = strategy;
      this.observableModel = observableModel;
      this.color = color;
    } else {
      throw new IllegalArgumentException("null param MAP.");
    }
  }

  @Override
  public void addFeature(Feature features) {
    this.features = features;
  }

  @Override
  public void makeMove() {
    try {
      CardMove stratMove = strategy.chooseMove(observableModel, color);
      List<Card> hand = observableModel.getPlayerHand(color);
      features.cardSelected(hand.indexOf(stratMove.card), color);
      features.cellSelected(stratMove.move.row, stratMove.move.col);
    } catch (NullPointerException ex) {
      // putting this here because if features has not been instantiated properly, game cannot run.
      throw new IllegalStateException("Feature not instantiated properly.");
    }
  }

  @Override
  public void playerMessenger(String message) {
    // does nothing; does not interact w view.
  }
}