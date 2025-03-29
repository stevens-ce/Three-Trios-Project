package threetrios.model;

/**
 * outline for a basic card.
 */
public interface Card {
  /**
   * returns the card's unique String identifier.
   *
   * @return identifier
   */
  String getIdentifier();

  /**
   * returns the cardinal attack value for the specified direction of values.
   *
   * @param dir given Direction
   * @return CardinalAttackValue with that direction
   */
  CardinalAttackValue getCardinalValue(Direction dir);
}
