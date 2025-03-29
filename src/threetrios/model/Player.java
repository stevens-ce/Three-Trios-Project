package threetrios.model;

import java.util.List;

/**
 * interface for a Player object, to be used in the model only.
 */
public interface Player<C extends Card> {
  /**
   * adds a blank card to the Player's hand, dyeing it the Player's color.
   *
   * @param card to be added
   */
  void addToHand(C card);

  /**
   * removes given card from hand.
   *
   * @param card to be removed from hand
   * @throws IllegalArgumentException if card does not exist in hand
   */
  void removeFromHand(C card);

  /**
   * flips the turn boolean, i.e. if is turn (true), now is not turn (false).
   */
  void nextTurn();

  // observations

  /**
   * Returns the color associated with the Player.
   *
   * @return color of player
   */
  Color getColor();

  /**
   * returns a copy of the hand.
   *
   * @return hand
   */
  List<C> getHand();

  /**
   * returns true if it is the player's turn, and false if not.
   *
   * @return boolean representing turn state
   */
  boolean isTurn();
}