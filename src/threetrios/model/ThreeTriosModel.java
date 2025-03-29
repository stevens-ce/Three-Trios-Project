package threetrios.model;

/**
 * Behaviors for a game of ThreeTrios for a variable number of players.
 * The game requires:
 * <ul>
 *   <li> a deck of cards to set up the player hands, which has at least N + 1 cards </li>
 *   <li> at least two Players, each with a hand of cards </li>
 *   <li> a grid on which to place cards that has N card cells </li>
 * </ul>
 */
public interface ThreeTriosModel<C extends Card> extends ReadOnlyThreeTriosModel {

  // operations

  /**
   * Attempts to play card to grid; goes through placing phase then battle phase; then changes turn.
   * Placing phase:
   * When given card is in hand of the current turn player, place card on the given cell pos.
   * If the card is properly placed, remove it from the hand.
   * Battle phase:
   * Given card (assumed already in grid) "battles" its neighboring cards (four tangent cells).
   * Will only battle cards that are of a different color to itself.
   * The battle process goes:
   * <ul>
   *   <li> "adjacent" values are compared, higher value wins </li>
   *   <li> if attacking card wins, dyes attacked card in its color </li>
   *   <li> dyed card becomes the battler, process repeats.</li>
   * </ul>
   *
   * @param card to be played
   * @param row  to be played to
   * @param col  to be played to
   * @throws IllegalStateException    if game has not started or has ended,
   *                                  or the card belongs to non-turn player
   * @throws IllegalArgumentException if either argument is null,
   *                                  or the card does not belong to either player
   *                                  the pos given is not a card cell or is occupied,
   */
  void playCard(C card, int row, int col);

  /**
   * Creates a grid and a card deck from given String arrays.
   * Deals (N + 1) / 2 cards to each player's hand,
   * where N is the amount of card cells in grid.
   * The cards are dealt from the deck randomly,
   * and once in hand each card is dyed the player's color.
   *
   * @param gridFile String[] representing the grid
   * @param deckFile String[] representing the deck
   * @throws IllegalStateException    if game has already started
   * @throws IllegalArgumentException if deck size < N + 1
   */
  void setup(String[] gridFile, String[] deckFile);
}