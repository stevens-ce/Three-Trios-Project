package threetrios.model;

import java.util.Objects;

/**
 * represents a cell on the grid, which is either a card cell or a hole.
 */
public class GridCell {
  private final boolean placeable;
  private Card card;
  private Color color;

  /**
   * initializes GridCell to either a card cell (placeable) or a hole (!placeable).
   *
   * @param placeable representing whether or not a card can be placed in the cell or not
   */
  public GridCell(boolean placeable) {
    this.placeable = placeable;
  }

  /**
   * places a Card in a cell if it's a card cell and empty.
   *
   * @param card  Card to be placed
   * @param color Color of player that played card
   * @throws NullPointerException  if given card is null
   * @throws IllegalStateException if this cell is a hole or if it's already full
   */
  public void placeCard(Card card, Color color) {
    try {
      Objects.requireNonNull(card);
      Objects.requireNonNull(color);
      validToPlace();
      this.card = card;
      this.color = color;
    } catch (NullPointerException ex) {
      throw new IllegalArgumentException("placeCard param null.");
    }
  }

  /**
   * swaps the color of the cell from either red -> blue or blue -> red.
   *
   * @throws IllegalStateException if cell currently has no color
   */
  public void flip() {
    if (color == null) {
      throw new IllegalStateException("No color to swap.");
    }
    if (color == Color.RED) {
      color = Color.BLUE;
    } else {
      color = Color.RED;
    }
  }

  /**
   * returns boolean representing whether this cell can have a card.
   *
   * @return placeable field
   */
  public boolean isPlaceable() {
    return this.placeable;
  }

  /**
   * returns the Card occupying the cell and null if cell is empty/ hole.
   *
   * @return card or null
   */
  public Card getCard() {
    return this.card;
  }

  /**
   * returns whether or not the gridcell contains a card.
   * @return true if there is a card and false if the cell is empty.
   */
  public boolean hasCard() {
    return card != null;
  }

  /**
   * returns the color of the card.
   *
   * @return color
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * returns whether or not it's valid to place a card on this cell.
   * @return boolean valid
   */
  public boolean isValid() {
    try {
      validToPlace();
      return true;
    } catch (IllegalStateException ex) {
      return false;
    }
  }

  // helpers

  private void validToPlace() {
    if (!placeable) {
      throw new IllegalStateException("This cell cannot contain a card");
    }
    if (hasCard()) {
      throw new IllegalStateException("This cell is full");
    }
  }
}