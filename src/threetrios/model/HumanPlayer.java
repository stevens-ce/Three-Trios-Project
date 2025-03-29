package threetrios.model;

import java.util.List;
import java.util.ArrayList;

/**
 * a human player with a color, a hand, and a turn.
 */
public class HumanPlayer<C extends Card> implements Player<C> {
  private List<C> hand;
  private final Color color;
  private boolean turn;

  /**
   * initializes human to a color and an empty hand.
   *
   * @param color player's associated color
   */
  public HumanPlayer(Color color) {
    hand = new ArrayList<C>(); // initializing this as an array list bc it makes sense for it to
    // start as empty w/in HumanPlayer and we need to pick a specific List implementation for that
    this.color = color;
    turn = false;
  }

  @Override
  public void addToHand(C card) {
    if (hand.contains(card)) {
      throw new IllegalArgumentException("This card is already in hand.");
    }
    hand.add(card);
  }

  @Override
  public void removeFromHand(C card) {
    if (!hand.contains(card)) {
      throw new IllegalArgumentException("This card is not in hand.");
    }
    hand.remove(card);
  }

  @Override
  public void nextTurn() {
    turn = !turn;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public List<C> getHand() {
    List<C> cloneHand = new ArrayList<C>();
    cloneHand.addAll(this.hand);
    return cloneHand;
  }

  @Override
  public String toString() {
    return color + " Player";
  }

  @Override
  public boolean isTurn() {
    return this.turn;
  }

}