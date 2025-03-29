package threetrios.model;

/**
 * representation of the cardinal directions, NSEW.
 */
public enum Direction {
  NORTH, SOUTH, EAST, WEST;

  /**
   * Returns the Direction opposite to this, as determined by cardinal directions.
   * Aka, North and South are opposites, as are East and West.
   *
   * @return direction opposite to this
   */
  public Direction getOpposite() {
    switch (this) {
      case NORTH:
        return Direction.SOUTH;
      case SOUTH:
        return Direction.NORTH;
      case EAST:
        return Direction.WEST;
      case WEST:
        return Direction.EAST;
      default:
        throw new IllegalArgumentException("Not a direction");
    }
  }
}
