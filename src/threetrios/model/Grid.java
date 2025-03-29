package threetrios.model;

import java.util.Map;
import java.util.HashMap;

/**
 * representation of a grid containing card cells and holes, of customizable dimensions.
 * 0-indexed, with the top of the visual grid corresponding to its zeroth row,
 * and the leftmost col corresponding to the zeroth col
 */
public class Grid {
  // grid[0] is the top row of GridCells and grid[0][0] is the top-leftmost GridCell
  private GridCell[][] grid;

  private final int rows;
  private final int cols;

  private final int cardCellCount;

  private final String[] gridFile;

  /**
   * initializes grid using given string array, assuming the format:
   * {"ROWS COLS", "ROW_0", "ROW_1", ...}.
   *
   * @param gridFile String[] describing filepath
   * @throws IllegalArgumentException if gridFile does not contain at least two lines,
   *                                  if gridFile doesn't have consistent dimensions,
   *                                  or contains unrecognized character
   *                                  (besides first line, X or C).
   */
  public Grid(String[] gridFile) {
    if (gridFile.length <= 1) {
      throw new IllegalArgumentException("gridFile does not contain valid number of lines.");
    }
    this.gridFile = gridFile;
    String dimensions = gridFile[0];
    String[] dimSplit = dimensions.split(" ");
    if (dimSplit.length != 2) {
      throw new IllegalArgumentException("first line of gridFile were not format \"ROW COL\".");
    }
    try {
      this.rows = Integer.parseInt(dimSplit[0]);
      this.cols = Integer.parseInt(dimSplit[1]);
      grid = new GridCell[this.rows][this.cols];
      cardCellCount = configureGrid(gridFile);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("first line of gridFile were not dimensions.");
    }
  }

  /**
   * places card in grid pos given.
   *
   * @param card  Card being placed
   * @param pos   Position representing a GridCell in field grid
   * @param color Color of the player placing card
   * @throws IllegalArgumentException if card or pos is null,
   *                                  pos in grid is not a card cell,
   *                                  or pos in grid is occupied
   */
  public void place(Card card, Position pos, Color color) {
    if (card == null || pos == null) {
      throw new IllegalArgumentException("null arg.");
    }
    if (!isPositionValid(pos)) {
      throw new IllegalArgumentException("Position is out of grid bounds.");
    }
    GridCell cell = grid[pos.getRow()][pos.getCol()];
    cell.placeCard(card, color);
  }

  /**
   * Engages cells in each cardinal direction from given card.
   * If cell is not a card cell or is not occupied, does not battle.
   * If battle is won, process is repeated on engaged card.
   *
   * @param card attacker
   * @throws IllegalArgumentException if card is null, or if card is not in grid
   */
  public void engageAdj(Card card) {
    if (card == null) {
      throw new IllegalArgumentException("null card arg.");
    }
    Position pos = find(card);
    for (Direction dir : Direction.values()) {
      checkAdjCell(card.getCardinalValue(dir), pos);
    }
  }

  // observations

  /**
   * counts how many cells there are of a given color in grid.
   *
   * @param color Color of cards being counted
   * @return int amt of cards of that color
   */
  public int countCellsOfColor(Color color) {
    int numColor = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (grid[row][col].isPlaceable()) {
          if (grid[row][col].getColor() != null) {
            if (grid[row][col].getColor().equals(color)) {
              numColor++;
            }
          }
        }
      }
    }
    return numColor;
  }

  /**
   * Checks if given position is within the grid.
   *
   * @param pos position to check
   * @return true if position is within grid, false otherwise.
   */
  public boolean isPositionValid(Position pos) {
    return pos.getRow() >= 0 && pos.getRow() < rows
            && pos.getCol() >= 0 && pos.getCol() < cols;
  }

  /**
   * Checks if all card cells are occupied.
   *
   * @return true if all are occupied, and false if not.
   */
  public boolean areAllCellsOccupied() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        GridCell cell = grid[row][col];
        if (cell.isPlaceable() && !cell.hasCard()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * returns the number of card cells in grid, aka how many placeable cells there are.
   *
   * @return int number of card cells.
   */
  public int getCardCellCount() {
    return cardCellCount;
  }

  /**
   * returns a deep copy of the Grid's grid field.
   * just as Grid, the topmost row is the 0th row and the leftmost col is the 0th col,
   * i.e. gridCopy[0] is the top row of GridCells and gridCopy[0][0] is the top-leftmost GridCell
   *
   * @return a 2d array of the grid
   */
  public GridCell[][] deepCopy() {
    GridCell[][] gridCopy = new GridCell[rows][cols];
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        GridCell gridCell = grid[row][col];
        gridCopy[row][col] = new GridCell(gridCell.isPlaceable());
        Color cellColor = gridCell.getColor();
        if (gridCell.hasCard()) {
          gridCopy[row][col].placeCard(gridCell.getCard(), cellColor);
        }
      }
    }
    return gridCopy;
  }

  /**
   * returns length of grid, aka number of rows.
   *
   * @return rows
   */
  public int getLength() {
    return rows;
  }

  /**
   * returns width of grid, aka number of cols.
   *
   * @return cols
   */
  public int getWidth() {
    return cols;
  }

  /**
   * returns the cell at a given Position.
   *
   * @param pos giving row and col
   * @return GridCell at that pos on grid
   */
  public GridCell cellAt(Position pos) {
    if (pos.getRow() >= rows || pos.getRow() < 0 || pos.getCol() >= cols || pos.getCol() < 0) {
      throw new IllegalArgumentException("out of bounds position.");
    }
    return grid[pos.getRow()][pos.getCol()];
  }

  /**
   * returns the configurating String array for this grid.
   *
   * @return string array representing the base grid.
   */
  public String[] gridToConfigArray() {
    return gridFile;
  }

  /**
   * returns a map of all the gridcells containing cards with their positions in the current grid.
   *
   * @return map of all gridcells with cards by position
   */
  public Map<Position, GridCell> allCardsOnGrid() {
    Map<Position, GridCell> cards = new HashMap<Position, GridCell>();
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (grid[row][col].hasCard()) {
          cards.put(new Position(row, col), grid[row][col]);
        }
      }
    }
    return cards;
  }

  // helpers
  // helps engageAdj

  // if card exists within grid, gives its location as Position
  private Position find(Card card) {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        GridCell currentCell = grid[row][col];
        if (currentCell.isPlaceable()) {
          if (currentCell.hasCard()) {
            Card currentCard = currentCell.getCard();
            if (currentCard.equals(card)) {
              return new Position(row, col);
            }
          }
        }
      }
    }
    throw new IllegalArgumentException("card does not exist in grid.");
  }

  // given the CAV of a card at given gridcell (derived from pos), determines whether the
  // adjacent card is to be battled
  private void checkAdjCell(CardinalAttackValue attacker, Position pos) {
    Color color = grid[pos.getRow()][pos.getCol()].getColor();
    GridCell engaged = adjacentCellInDirection(attacker.direction, pos);
    if (engaged.isPlaceable()) {
      if (engaged.hasCard()) {
        if (engaged.getColor() != color) {
          battle(attacker, engaged);
        }
      }
    }
  }

  // CAV battles opposite dir CAV of adjacent card
  // if attacker wins, opp flips and the battlePhase process begins anew
  private void battle(CardinalAttackValue attacker, GridCell opponent) {
    Direction dir = attacker.getOppositeDirection();
    Card opponentCard = opponent.getCard();
    CardinalAttackValue oppValue = opponentCard.getCardinalValue(dir);
    if (attacker.attackValue.getValue() > oppValue.attackValue.getValue()) {
      opponent.flip();
      engageAdj(opponentCard);
    }
  }

  // retruns the adj cell depending on the given position and direction its facing.
  // if adj cell doesn't exist, returns a hole.
  private GridCell adjacentCellInDirection(Direction dir, Position pos) {
    int changedAxis = -1;
    switch (dir) {
      case NORTH:
        changedAxis = pos.getRow() - 1;
        return getCellIfExists(changedAxis, pos.getCol());
      case SOUTH:
        changedAxis = pos.getRow() + 1;
        return getCellIfExists(changedAxis, pos.getCol());
      case WEST:
        changedAxis = pos.getCol() - 1;
        return getCellIfExists(pos.getRow(), changedAxis);
      case EAST:
        changedAxis = pos.getCol() + 1;
        return getCellIfExists(pos.getRow(), changedAxis);
      default:
        return getCellIfExists(changedAxis, changedAxis);
    }
  }

  /*
  if the position indicated is valid, returns that point on the grid.
  if not, returns a hole
   */
  private GridCell getCellIfExists(int row, int col) {
    Position pos = new Position(row, col);
    if (isPositionValid(pos)) {
      return grid[row][col];
    } else {
      return new GridCell(false);
    }
  }

  // helps constructors configure the grid
  // gridFile contains the dimension line, so skip that in for loop
  // returns the cardCellCount
  private int configureGrid(String[] gridFile) {
    if (gridFile.length != rows + 1) {
      throw new IllegalArgumentException("not enough rows to construct grid.");
    }
    int cardCellCounting = 0;
    for (int row = 0; row < rows; row++) {
      String rowConfig = gridFile[row + 1];
      if (rowConfig.length() != cols) {
        throw new IllegalArgumentException("not enough cols to construct grid.");
      } else {
        cardCellCounting += initializeGridCellRow(row, rowConfig);

      }
    }
    return cardCellCounting;
  }

  // configures a row of the grid
  // returns cardCellCount for the row
  private int initializeGridCellRow(int row, String rowConfig) {
    int cardCellCounting = 0;
    for (int col = 0; col < cols; col++) {
      char cell = rowConfig.charAt(col);
      switch (cell) {
        case 'C':
          cardCellCounting++;
          grid[row][col] = new GridCell(true);
          break;
        case 'X':
          grid[row][col] = new GridCell(false);
          break;
        default:
          throw new IllegalArgumentException("unrecognized symbol on grid.");
      }
    }
    return cardCellCounting;
  }
}