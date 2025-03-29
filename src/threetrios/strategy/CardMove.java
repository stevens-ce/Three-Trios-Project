package threetrios.strategy;

import threetrios.model.Card;

/**
 * used to represent a play for TTTStrategy, consisting of a position on the grid and a Card.
 */
public class CardMove {
  public final Card card;
  public final Move move;

  /**
   * initalizes the card and grid position of the play.
   *
   * @param card Card
   * @param move Move
   */
  public CardMove(Card card, Move move) {
    if (card == null || move == null) {
      throw new IllegalArgumentException("null param for PlayMove.");
    }
    this.card = card;
    this.move = move;
  }
}
