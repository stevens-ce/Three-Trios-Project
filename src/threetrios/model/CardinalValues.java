package threetrios.model;

/**
 * used to make the arguments of CardinalCard constructor shorter.
 */
public class CardinalValues {
  private final CardinalAttackValue north;
  private final CardinalAttackValue south;
  private final CardinalAttackValue east;
  private final CardinalAttackValue west;

  /**
   * Initializes each cardinal direction to an attack value.
   *
   * @param north value
   * @param south value
   * @param east value
   * @param west value
   */
  public CardinalValues(AttackValue north, AttackValue south,
                        AttackValue east, AttackValue west) {
    this.north = new CardinalAttackValue(Direction.NORTH, north);
    this.south = new CardinalAttackValue(Direction.SOUTH, south);
    this.east = new CardinalAttackValue(Direction.EAST, east);
    this.west = new CardinalAttackValue(Direction.WEST, west);
  }

  /**
   * Initializes each cardinal direction to an attack value derived from an int value.
   *
   * @param north int value
   * @param south int value
   * @param east  int value
   * @param west  int value
   * @throws IllegalArgumentException if any given int is not a valid attackvalue (1-10)
   */
  public CardinalValues(int north, int south, int east, int west) throws IllegalArgumentException {
    this.north = new CardinalAttackValue(Direction.NORTH, intToAV(north));
    this.south = new CardinalAttackValue(Direction.SOUTH, intToAV(south));
    this.east = new CardinalAttackValue(Direction.EAST, intToAV(east));
    this.west = new CardinalAttackValue(Direction.WEST, intToAV(west));
  }

  private AttackValue intToAV(int value) throws IllegalArgumentException {
    switch (value) {
      case 1:
        return AttackValue.ONE;
      case 2:
        return AttackValue.TWO;
      case 3:
        return AttackValue.THREE;
      case 4:
        return AttackValue.FOUR;
      case 5:
        return AttackValue.FIVE;
      case 6:
        return AttackValue.SIX;
      case 7:
        return AttackValue.SEVEN;
      case 8:
        return AttackValue.EIGHT;
      case 9:
        return AttackValue.NINE;
      case 10:
        return AttackValue.A;
      default:
        throw new IllegalArgumentException("Not a valid AttackValue,");
    }
  }

  /**
   * returns the CardinalAttackValue with the NORTH direction.
   *
   * @return north CardinalAttackValue
   */
  public CardinalAttackValue getNorth() {
    return this.north;
  }

  /**
   * returns the CardinalAttackValue with the SOUTH direction.
   *
   * @return south CardinalAttackValue
   */
  public CardinalAttackValue getSouth() {
    return this.south;
  }

  /**
   * returns the CardinalAttackValue with the EAST direction.
   *
   * @return east CardinalAttackValue
   */
  public CardinalAttackValue getEast() {
    return this.east;
  }

  /**
   * returns the CardinalAttackValue with the WEST direction.
   *
   * @return west CardinalAttackValue
   */
  public CardinalAttackValue getWest() {
    return this.west;
  }
}