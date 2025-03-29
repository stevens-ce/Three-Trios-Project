package threetrios.model;

/**
 * A value associated with a direction.
 */
public class CardinalAttackValue {

  /**
   * The direction the CAV is situated in.
   */
  public final Direction direction;

  /**
   * The value of the CAV.
   */
  public final AttackValue attackValue;

  /**
   * Sets the direction and value to given Direction and AttackValue.
   *
   * @param direction the CAV is facing
   * @param attackValue of the CAV
   */
  public CardinalAttackValue(Direction direction, AttackValue attackValue) {
    if (direction == null || attackValue == null) {
      throw new IllegalArgumentException("null direction or attackValue.");
    }
    this.direction = direction;
    this.attackValue = attackValue;
  }

  /**
   * Returns the Direction opposite to the CAV's direction, as determined by Direction's getOpposite
   * method. cardinal directions.
   * Aka, North and South are opposites, as are East and West.
   *
   * @return direction opposite to this.direction
   */
  public Direction getOppositeDirection() {
    return direction.getOpposite();
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof CardinalAttackValue) {
      CardinalAttackValue that = (CardinalAttackValue) other;
      if (this.hashCode() == that.hashCode()) {
        return this.direction == that.direction;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return attackValue.getValue();
  }

}
