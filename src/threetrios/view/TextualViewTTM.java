package threetrios.view;

import threetrios.model.Card;
import threetrios.model.Color;
import threetrios.model.Direction;
import threetrios.model.GridCell;
import threetrios.model.ReadOnlyThreeTriosModel;

import java.util.List;

/**
 * Textual view of ThreeTrios.
 */
public class TextualViewTTM {

  private final ReadOnlyThreeTriosModel model;
  private StringBuilder appendable;

  /**
   * initializes model to a given implementation of TTM.
   *
   * @param model Model to be displayed
   */
  public TextualViewTTM(ReadOnlyThreeTriosModel model, StringBuilder appendable) {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("model or appendable is null");
    }
    this.model = model;
    this.appendable = appendable;
  }

  /**
   * Renders the current state of the game.
   * Of form:
   * Player: C
   * [grid]
   * Hand:
   * ID N S E W
   * ...
   */
  public void render() {
    appendLine("Player: " + getCurrentPlayerSymbol());
    renderGrid();
    if (model.isGameOver()) {
      appendLine(model.winner() + "Wins!");
    } else {
      renderHand();
    }
  }

  private void renderGrid() {
    GridCell[][] grid = model.getGrid();
    for (GridCell[] gridCells : grid) {
      for (int col = 0; col < gridCells.length; col++) {
        GridCell cell = gridCells[col];
        appendable.append(getCellSymbol(cell));
      }
      appendLine("");
    }
  }

  private void renderHand() {
    appendLine("Hand:");
    List<Card> hand = model.getPlayerHand(model.getCurrentPlayer());
    for (Card card : hand) {
      String cardFormat = String.format("%s %s", card.getIdentifier(), formatCardStats(card));
      appendLine(cardFormat);
    }
  }

  private String formatCardStats(Card card) {
    String cardStats = "";
    for (Direction dir : Direction.values()) {
      cardStats += card.getCardinalValue(dir).attackValue + " ";
    }
    return cardStats.substring(0, cardStats.length() - 1);
  }

  /**
   * Returns symbol for current player.
   *
   * @return the symbol for the current player.
   */
  private String getCurrentPlayerSymbol() {
    return model.getCurrentPlayer().toString();
  }

  private void appendLine(String message) {
    appendable.append(message + "\n");
  }

  /**
   * Returns the symbol for the cell.
   *
   * @param cell the cell to get the symbol for.
   * @return the symbol for the cell.
   */
  private String getCellSymbol(GridCell cell) {
    if (!cell.isPlaceable()) {
      //hole
      return " ";
    } else if (cell.getCard() == null) {
      //empty
      return "_";
    } else {
      return cell.getColor() == Color.RED ? "R" : "B";
    }
  }
}