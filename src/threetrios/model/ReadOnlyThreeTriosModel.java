package threetrios.model;

import java.util.List;

/**
 * Observations needed for a game of ThreeTrios.
 * Player is represented only by Color.
 */
public interface ReadOnlyThreeTriosModel<C extends Card> {
  /**
   * returns true if all gridcells are occupied.
   *
   * @return game over state
   * @throws IllegalStateException if game has not started
   */
  boolean isGameOver();

  /**
   * returns whichever player has the most cards of their color on the grid.
   * if there is a tie returns null.
   *
   * @return winning Color or null
   * @throws IllegalStateException if game has not started or is not over
   */
  Color winner();

  /**
   * Returns a 2d array representing the grid.
   *
   * @return grid
   */
  GridCell[][] getGrid();

  /**
   * Returns the current player.
   *
   * @return the current player.
   */
  Color getCurrentPlayer();

  /**
   * gets specified card from specified player hand.
   *
   * @param cardIdx of card in hand
   * @param player  of hand
   * @return specified Card
   * @throws IllegalArgumentException if given card does not exist at cardIdx of player hand
   */
  C getCardFromHand(int cardIdx, Color player);

  /**
   * Returns a copy of the given player's hand.
   *
   * @param player Color
   * @return List of cards
   */
  List<C> getPlayerHand(Color player);

  /**
   * Returns a list of all the game's player colors.
   *
   * @return List of colors
   */
  List<Color> getPlayers();

  /**
   * gets the length (num of row) of grid.
   *
   * @return length
   */
  int getGridLength();

  /**
   * gets the width (num of cols) of grid.
   *
   * @return width
   */
  int getGridWidth();

  /**
   * returns a copy of the cell at the given row and col on the grid.
   *
   * @param row of cell
   * @param col of cell
   * @return GridCell at position
   */
  GridCell getCell(int row, int col);

  /**
   * returns the color of the player that owns the cell, returns null if none.
   *
   * @param cell GridCell
   * @return Color of owner player
   */
  Color getOwnerOfCell(GridCell cell);

  /**
   * returns whether or not a move to the given position is legal to play to.
   * move is "legal" if the cell at position can take a card, aka is a card cell and is empty.
   * move is not legal if above is not true, or if the given position is not on the grid.
   *
   * @param row in grid
   * @param col in grid
   * @return true if move is legal and false if it isn't
   */
  boolean isMoveLegal(int row, int col);

  /**
   * returns the number of cards that would be flipped if the given player played the
   * given card to the specified position on the grid.
   *
   * @param card   that would be placed
   * @param player that would play
   * @param row    specified
   * @param col    specified
   * @return int num of cards flipped wth the given move
   * @throws IllegalArgumentException if move is invalid
   */
  int numCardsFlipped(C card, Color player, int row, int col);

  /**
   * returns current score of given player.
   * Current score is the sum of # of cards in player's hand + # of player's cards on grid.
   *
   * @param player Color
   * @return current score as defined above
   */
  int currentScore(Color player);

}
