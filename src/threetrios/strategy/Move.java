package threetrios.strategy;

/**
 * stores a move on the grid as a row and a col value.
 * non-package specific version of Position.
 */
public class Move {
  public final int row;
  public final int col;

  /**
   * sets the row and col of the Move.
   *
   * @param row int
   * @param col int
   */
  public Move(int row, int col) {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("negative row/ col.");
    }
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Move) {
      Move that = (Move) other;
      if (that.hashCode() == this.hashCode()) {
        return that.row == this.row;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return row * 10 + col;
  }
}
