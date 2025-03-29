package threetrios.model;

/**
 * implementation of Card that uses CardinalValues to store AttackValues.
 */
public class CardinalCard implements Card {

  private final CardinalValues values;

  private final String identifier;


  /**
   * Constructor for card with four specific values and a unique identifier.
   * These are set to the final state of the card's CardinalValues and identifier.
   * CardinalValues represents the four numbers at each point of the card, NSEW.
   *
   * @param values     four CardAttackValues
   * @param identifier a unique String
   */
  public CardinalCard(CardinalValues values, String identifier) {
    if (values == null) {
      throw new IllegalArgumentException("values should not be null.");
    }
    this.values = values;
    if (identifier == null) {
      throw new IllegalArgumentException("identifier should not be null.");
    }
    this.identifier = identifier;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof CardinalCard) {
      CardinalCard that = (CardinalCard) other;
      if (that.hashCode() == this.hashCode()) {
        return this.getIdentifier().equals(that.getIdentifier());
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return identifier.length();
  }

  @Override
  public String getIdentifier() {
    return this.identifier;
  }

  @Override
  public CardinalAttackValue getCardinalValue(Direction dir) {
    if (dir == null) {
      throw new IllegalArgumentException("Direction cannot be null.");
    }
    switch (dir) {
      case NORTH:
        return values.getNorth();
      case SOUTH:
        return values.getSouth();
      case EAST:
        return values.getEast();
      case WEST:
        return values.getWest();
      default:
        throw new IllegalArgumentException("Not a direction.");
    }
  }
}