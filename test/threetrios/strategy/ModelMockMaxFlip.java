package threetrios.strategy;

import java.util.List;

import threetrios.model.Card;
import threetrios.model.CardinalCard;
import threetrios.model.CardinalValues;
import threetrios.model.Color;
import threetrios.model.GridCell;
import threetrios.model.ThreeTriosModel;

/**
 * this mock will only allow for a specific cell (bottom right) to be selected as optimal by the
 * strategy, but will track whenever MaxFlip checks.
 */
public class ModelMockMaxFlip implements ThreeTriosModel<CardinalCard> {
  private String[] gridFile;
  private int rows;
  private int cols;
  private StringBuilder transcript;

  public ModelMockMaxFlip(StringBuilder transcript) {
    this.transcript = transcript;
  }

  @Override
  public void setup(String[] gridFile, String[] unused) {
    this.gridFile = gridFile;
    String dimensions = gridFile[0];
    this.rows = Integer.parseInt(dimensions.substring(0, 1));
    this.cols = Integer.parseInt(dimensions.substring(2, 3));
  }

  @Override
  public void playCard(CardinalCard card, int row, int col) {
    String line = gridFile[row];
    // F for full
    gridFile[row + 1] = String.format("%s%c%s", line.substring(0, col), 'F',
            line.substring(col + 1, line.length()));
  }

  //used
  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Color winner() {
    // never called
    return null;
  }

  @Override
  public GridCell[][] getGrid() {
    // never called
    return null;
  }

  @Override
  public Color getCurrentPlayer() {
    // never called
    return null;
  }

  @Override
  public CardinalCard getCardFromHand(int cardIdx, Color player) {
    return null;
  }

  // the cards do not matter for this test, so only return one card of max values
  @Override
  public List<CardinalCard> getPlayerHand(Color player) {
    CardinalValues values = new CardinalValues(10, 10, 10, 10);
    return List.of(new CardinalCard(values, "lucky draw"));
  }

  @Override
  public List<Color> getPlayers() {
    // never called
    return List.of();
  }

  @Override
  public int getGridLength() {
    return rows;
  }

  @Override
  public int getGridWidth() {
    return cols;
  }

  @Override
  public GridCell getCell(int row, int col) {
    // never called
    return null;
  }

  @Override
  public Color getOwnerOfCell(GridCell cell) {
    // never called
    return null;
  }

  @Override
  public boolean isMoveLegal(int row, int col) {
    return gridFile[row + 1].charAt(col) == 'C' || (row == rows - 1 && col == cols - 1);
  }

  @Override
  public int numCardsFlipped(Card card, Color player, int row, int col) {
    addToTranscript(formatForTranscript("numCardsFlipped", row, col));
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IllegalArgumentException();
    } else if (row == rows - 1 && col == cols - 1) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public int currentScore(Color player) {
    // never called
    return 0;
  }

  private String formatForTranscript(String methodName, int row, int col) {
    return methodName + " " + row + " " + col;
  }

  private void addToTranscript(String record) {
    transcript.append(record);
    transcript.append("\n");
  }

  // some functionality to make a strategy transcript of every method called
}
