package threetrios.strategy;

import threetrios.model.ReadOnlyThreeTriosModel;
import threetrios.model.Color;
import threetrios.model.Card;

import java.util.Map;
import java.util.List;

/**
 * defines how to break ties between many competing CardMoves.
 */
abstract class TieBreakerStrategy implements TTStrategy {


  /**
   * When strategy is stuck between a set of moves, tiebreaker will decide between them.
   * Tiebreaker chooses the provided move with the uppermost-leftmost position on the grid, and
   * the card by which is the closest to 0 in the player's hand.
   *
   * @param model ReadOnlyThreeTriosModel
   * @param player Color
   * @param tiedMoves Map of all tied moves and the cards possible for those moves
   * @return best CardMove as specified or null if none applicable
   */
  protected CardMove tieBreakerMove(ReadOnlyThreeTriosModel model,
                                    Color player, Map<Move, List<Card>> tiedMoves) {
    Move upperLeft = getMove(tiedMoves);

    List<Card> potentialCards = tiedMoves.get(upperLeft);
    List<Card> hand = model.getPlayerHand(player);

    for (int idx = 0; idx < hand.size(); idx++) {
      if (potentialCards.contains(hand.get(idx))) {
        return new CardMove(hand.get(idx), upperLeft);
      }
    }

    throw new IllegalArgumentException("potential cards weren't from player's hand.");
  }

  private static Move getMove(Map<Move, List<Card>> tiedMoves) {
    Move upperLeft = null;
    for (Move move : tiedMoves.keySet()) {
      if (upperLeft == null) {
        upperLeft = move;
      } else if (move.row < upperLeft.row) {
        upperLeft = (move.col < upperLeft.row) ? move : upperLeft;
      } else if (move.col < upperLeft.col) {
        upperLeft = (move.row < upperLeft.col) ? move : upperLeft;
      }
    }
    return upperLeft;
  }

}
