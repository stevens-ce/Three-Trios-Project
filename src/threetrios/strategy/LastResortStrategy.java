package threetrios.strategy;

import threetrios.model.ReadOnlyThreeTriosModel;
import threetrios.model.Color;
import threetrios.model.Card;

import java.util.List;

/**
 * Strategy that returns a CardMove with the upperleft-most position
 * on the grid and the 0th card in hand.
 */
public class LastResortStrategy implements TTStrategy {
  @Override
  public CardMove chooseMove(ReadOnlyThreeTriosModel model, Color player) {
    if (model.isGameOver()) {
      throw new IllegalStateException("no moves when game is over.");
    }
    List<Card> hand = model.getPlayerHand(player);
    Card lRCard = hand.get(0);
    // traverses grid in an expanding triangle from the upperleftmost corner,
    // searching in top->bot order, i.e
    /*
    1__         X1_         XX1
    ___    ->   2__   ->    X2_
    ___         ___         3__
     */
    for (int col = 0; col < model.getGridWidth() * 2 - 1; col++) {
      int currRow = 0;
      int currCol = col;
      for (int factor = 0; factor <= col; factor++) {
        currRow += factor;
        currCol = col - factor;
        if (model.isMoveLegal(currRow, currCol)) {
          return new CardMove(lRCard, new Move(currRow, currCol));
        }
        currRow = 0;
        currCol = col;
      }
    }

    throw new IllegalStateException("there must be at least one legal move in model.");
  }
}
