package threetrios;

import java.util.ArrayList;
import java.util.List;

import threetrios.model.CardinalCard;
import threetrios.model.CardinalValues;
import threetrios.model.Color;
import threetrios.model.GridCell;
import threetrios.model.ReadOnlyThreeTriosModel;

/**
 * This represents a mock non-trivial state model for the TheeTrios game. It implements the
 * read-only model interface and gives a non-trivial state of the game where there are cards on
 * the grid and the number of cards in the player's hands has decreased.
 */
public class MockNonTrivialStateModel implements ReadOnlyThreeTriosModel<CardinalCard> {

  private final List<CardinalCard> redPlayerHand;
  private final List<CardinalCard> bluePlayerHand;
  private final GridCell[][] grid;
  private final Color currentPlayer;

  /**
   * This is a constructor for the mock non-trivial state model. It initializes the player's
   * hand and the crids with cards on it.
   */
  public MockNonTrivialStateModel() {
    this.grid = initializeMockGrid();
    this.redPlayerHand = initializeRedHand();
    this.bluePlayerHand = initializeBlueHand();

    this.currentPlayer = Color.RED;
  }

  /**
   * This creates a grid with cards on some of the cells.
   *
   * @return a 4x4 grid.
   */
  private GridCell[][] initializeMockGrid() {
    int rows = 4;
    int cols = 4;
    GridCell[][] grid = new GridCell[rows][cols];

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        boolean isPlaceable = (row + col) % 2 == 0;
        grid[row][col] = new GridCell(isPlaceable);

        if (isPlaceable && (row + col) % 3 == 0) {
          CardinalCard card = new CardinalCard(
                  new CardinalValues(2, 3, 4, 5),
                  "card" + row + col);
          grid[row][col].placeCard(card, row % 2 == 0 ? Color.RED : Color.BLUE);
        }
      }
    }
    return grid;
  }

  /**
   * This initializes player red's hand.
   *
   * @return a list of player red's hand.
   */
  private List<CardinalCard> initializeRedHand() {
    List<CardinalCard> hand = new ArrayList<>();
    hand.add(new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "R1"));
    hand.add(new CardinalCard(new CardinalValues(2, 3, 4, 5),
            "R2"));
    hand.add(new CardinalCard(new CardinalValues(10, 4, 10, 2),
            "R3"));
    return hand;
  }

  /**
   * This initializes player blue's hand.
   *
   * @return a list of player blue's hand.
   */
  private List<CardinalCard> initializeBlueHand() {
    List<CardinalCard> hand = new ArrayList<>();
    hand.add(new CardinalCard(new CardinalValues(3, 4, 5, 6),
            "B1"));
    hand.add(new CardinalCard(new CardinalValues(4, 5, 6, 7),
            "B2"));
    hand.add(new CardinalCard(new CardinalValues(1, 1, 7, 3),
            "B3"));
    return hand;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Color winner() {
    return null;
  }

  @Override
  public GridCell[][] getGrid() {
    return grid;
  }

  @Override
  public Color getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public CardinalCard getCardFromHand(int cardIdx, Color player) {
    return null;
  }

  @Override
  public List<CardinalCard> getPlayerHand(Color player) {
    return player == Color.RED ? new ArrayList<>(redPlayerHand) : new ArrayList<>(bluePlayerHand);
  }

  @Override
  public List<Color> getPlayers() {
    return List.of(Color.RED, Color.BLUE);
  }

  @Override
  public int getGridLength() {
    return grid.length;
  }

  @Override
  public int getGridWidth() {
    return grid[0].length;
  }

  @Override
  public GridCell getCell(int row, int col) {
    return grid[row][col];
  }

  @Override
  public Color getOwnerOfCell(GridCell cell) {
    return cell.getColor();
  }

  @Override
  public boolean isMoveLegal(int row, int col) {
    return grid[row][col].isPlaceable() && !grid[row][col].hasCard();
  }

  @Override
  public int numCardsFlipped(CardinalCard card, Color player, int row, int col) {
    return 1;
  }

  @Override
  public int currentScore(Color player) {
    //random irrelevant score
    return player == Color.RED ? 5 : 4;
  }
}