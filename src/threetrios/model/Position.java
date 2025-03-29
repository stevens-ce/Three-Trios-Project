package threetrios.model;

/**
 * stores the position of a point from zero.
 * package-specific version of Move.
 */
public class Position {
  private final int row;
  private final int col;

  /**
   * creates position with specified row and col.
   *
   * @param row number
   * @param col number
   */
  public Position(int row, int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * returns the row.
   *
   * @return the row from 0
   */
  public int getRow() {
    return this.row;
  }

  /**
   * returns the column.
   *
   * @return the col from 0
   */
  public int getCol() {
    return this.col;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Position) {
      Position that = (Position) other;
      return that.getRow() == this.getRow() && that.getCol() == this.getCol();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return row * col;
  }
}
